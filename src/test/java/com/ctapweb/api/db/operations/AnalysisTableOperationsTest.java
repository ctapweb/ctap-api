package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.data_generators.TestAnalyses;
import com.ctapweb.api.db.data_generators.TestCorpora;
import com.ctapweb.api.db.data_generators.TestFeatureSets;
import com.ctapweb.api.db.data_generators.TestUserAccounts;
import com.ctapweb.api.db.pojos.Analysis;
import com.ctapweb.api.db.pojos.Corpus;
import com.ctapweb.api.db.pojos.FeatureSet;
import com.ctapweb.api.db.pojos.UserAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AnalysisTableOperationsTest {
	UserTableOperations userTableOperations;
	CorpusTableOperations corpusTableOperations;
	FeatureSetTableOperations featureSetTableOperations;
	AnalysisTableOperations analysisTableOperations;

	TestUserAccounts testUserAccounts;
	TestCorpora testCorpora;
	TestFeatureSets testFeatureSets;
	TestAnalyses testAnalyses;

	Logger logger = LogManager.getLogger();

	public AnalysisTableOperationsTest() throws ClassNotFoundException, IOException, SQLException {
		DataSource dataSource = DataSourceManager.getTestDataSource();

		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);
		featureSetTableOperations = new FeatureSetTableOperations(dataSource);
		analysisTableOperations = new AnalysisTableOperations(dataSource);

		testUserAccounts = new TestUserAccounts();
		testCorpora = new TestCorpora();
		testFeatureSets = new TestFeatureSets();
		testAnalyses = new TestAnalyses();
	}

	@Test
	public void testTableOperations() throws ClassNotFoundException, IOException, SQLException {
		logger.info("Testing AnalysisTableOperations:");

		initTestEnvironment();

		//insert one user account and one corpus of that user
		UserAccount userAccount = testUserAccounts.generateAccount();
		long insertedUserId = userTableOperations.addEntry(userAccount);
		assertTrue(userTableOperations.isAccountExist(userAccount.getEmail()));

		Corpus corpus = testCorpora.generateCorpus(insertedUserId);
		long insertedCorpusId = corpusTableOperations.addEntry(corpus);
		assertEquals(insertedCorpusId, corpusTableOperations.getEntry(insertedCorpusId).getId());
		assertEquals(corpus.getOwnerId(), corpusTableOperations.getEntry(insertedCorpusId).getOwnerId());
		assertEquals(corpus.getName(), corpusTableOperations.getEntry(insertedCorpusId).getName());
		assertEquals(corpus.getDescription(), corpusTableOperations.getEntry(insertedCorpusId).getDescription());
		assertNotNull(corpusTableOperations.getEntry(insertedCorpusId).getCreateDate());

		//insert one feature set
		FeatureSet featureSet = testFeatureSets.generateFeatureSet(insertedUserId);
		long insertedFeatureSetId = featureSetTableOperations.addEntry(featureSet);
		
		//insert an analysis
		Analysis analysis = testAnalyses.generateAnalysis(insertedCorpusId, insertedFeatureSetId);
		long insertedAnalysisId = analysisTableOperations.addEntry(analysis);
		
		assertEquals(1, analysisTableOperations.getAllEntries().size());
		assertEquals(1, analysisTableOperations.getNumEntries());
		assertEquals(1, analysisTableOperations.getAllEntriesByOwner(insertedUserId).size());
		
		logger.trace("Testing if user owner...");
		assertTrue(analysisTableOperations.isUserOwner(insertedUserId, insertedAnalysisId));
		
		//change one entry: update
		logger.trace("Testing updating single entry...");
		String newName = "New Analysis Name";
		analysisTableOperations.updateName(insertedAnalysisId, newName);
		assertEquals(newName, analysisTableOperations.getEntry(insertedAnalysisId).getName());

		String newDescription = "New Analysis Description";
		analysisTableOperations.updateDescription(insertedAnalysisId, newDescription);
		assertEquals(newDescription, analysisTableOperations.getEntry(insertedAnalysisId).getDescription());

		//delete one entry
		logger.trace("Testing deleting single entry...");
		analysisTableOperations.deleteEntry(insertedAnalysisId);
		assertEquals(0, analysisTableOperations.getNumEntries());
		assertNull(analysisTableOperations.getEntry(insertedAnalysisId));
		assertNull(analysisTableOperations.getAllEntries());
		assertNull(analysisTableOperations.getAllEntriesByOwner(insertedUserId));


		//drop the table
		logger.trace("Testing dropping the table...");
		analysisTableOperations.dropTable();
		assertFalse(analysisTableOperations.isTableExist());

		//finishing the test, cleaning the db
		cleanTestEnvironment();
	}

	
	//initialize dependent tables and recreate the test table
	private void initTestEnvironment() throws ClassNotFoundException, SQLException, IOException {
		logger.trace("Initializing test environment...");

		//drop the tables, be careful of dependencies
		analysisTableOperations.dropTable();
		corpusTableOperations.dropTable();
		featureSetTableOperations.dropTable();
		userTableOperations.dropTable();
		assertFalse(analysisTableOperations.isTableExist());
		assertFalse(corpusTableOperations.isTableExist());
		assertFalse(featureSetTableOperations.isTableExist());
		assertFalse(userTableOperations.isTableExist());


		//recreate the tables
		userTableOperations.createTable();
		corpusTableOperations.createTable();
		featureSetTableOperations.createTable();
		analysisTableOperations.createTable();
		assertTrue(userTableOperations.isTableExist());
		assertTrue(corpusTableOperations.isTableExist());
		assertTrue(analysisTableOperations.isTableExist());
		assertTrue(featureSetTableOperations.isTableExist());
	}

	//droping all the test tables
	//be careful with table dependencies, drop tables that are not depended by others first.
	private void cleanTestEnvironment() throws ClassNotFoundException, SQLException, IOException {
		logger.trace("Cleaning test environment...");
		analysisTableOperations.dropTable();
		corpusTableOperations.dropTable();
		featureSetTableOperations.dropTable();
		userTableOperations.dropTable();
		assertFalse(analysisTableOperations.isTableExist());
		assertFalse(corpusTableOperations.isTableExist());
		assertFalse(featureSetTableOperations.isTableExist());
		assertFalse(userTableOperations.isTableExist());

	}

}
