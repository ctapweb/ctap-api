package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.data_generators.TestAnalyses;
import com.ctapweb.api.db.data_generators.TestCategories;
import com.ctapweb.api.db.data_generators.TestCorpora;
import com.ctapweb.api.db.data_generators.TestFeatureSets;
import com.ctapweb.api.db.data_generators.TestFs_Mes;
import com.ctapweb.api.db.data_generators.TestMeasures;
import com.ctapweb.api.db.data_generators.TestResults;
import com.ctapweb.api.db.data_generators.TestTags;
import com.ctapweb.api.db.data_generators.TestTexts;
import com.ctapweb.api.db.data_generators.TestUserAccounts;
import com.ctapweb.api.db.pojos.Analysis;
import com.ctapweb.api.db.pojos.Corpus;
import com.ctapweb.api.db.pojos.FeatureSet;
import com.ctapweb.api.db.pojos.Fs_Me;
import com.ctapweb.api.db.pojos.Measure;
import com.ctapweb.api.db.pojos.MeasureCategory;
import com.ctapweb.api.db.pojos.Result;
import com.ctapweb.api.db.pojos.Tag;
import com.ctapweb.api.db.pojos.Text;
import com.ctapweb.api.db.pojos.UserAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ResultTableOperationsTest {
	UserTableOperations userTableOperations;
	CorpusTableOperations corpusTableOperations;
	TagTableOperations tagTableOperations;
	TextTableOperations textTableOperations;
	MeasureCategoryTableOperations categoryTableOperations;
	MeasureTableOperations measureTableOperations;
	FeatureSetTableOperations featureSetTableOperations;
	FS_METableOperations fs_METableOperations;
	AnalysisTableOperations analysisTableOperations;
	ResultTableOperations resultTableOperations;

	TestUserAccounts testUserAccounts;
	TestCorpora testCorpora;
	TestTags testTags;
	TestTexts testTexts;
	TestCategories testCategories;
	TestMeasures testMeasures;
	TestFeatureSets testFeatureSets;
	TestFs_Mes testFs_Mes;
	TestAnalyses testAnalyses;
	TestResults testResults;

	Logger logger = LogManager.getLogger();

	public ResultTableOperationsTest() throws ClassNotFoundException, IOException, SQLException {
		DataSource dataSource = DataSourceManager.getTestDataSource();

		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);
		tagTableOperations = new TagTableOperations(dataSource);
		textTableOperations = new TextTableOperations(dataSource);
		categoryTableOperations = new MeasureCategoryTableOperations(dataSource);
		measureTableOperations = new MeasureTableOperations(dataSource);
		featureSetTableOperations = new FeatureSetTableOperations(dataSource);
		fs_METableOperations = new FS_METableOperations(dataSource);
		analysisTableOperations = new AnalysisTableOperations(dataSource);
		resultTableOperations = new ResultTableOperations(dataSource);

		testUserAccounts = new TestUserAccounts();
		testCorpora = new TestCorpora();
		testTags = new TestTags();
		testTexts = new TestTexts();
		testCategories = new TestCategories();
		testMeasures = new TestMeasures();
		testFeatureSets = new TestFeatureSets();
		testFs_Mes = new TestFs_Mes();
		testAnalyses = new TestAnalyses();
		testResults = new TestResults();
	}

	@Test
	public void testTableOperations() throws ClassNotFoundException, IOException, SQLException {
		logger.info("Testing ResultTableOperations:");

		initTestEnvironment();

		//insert one user account, one corpus of that user and one tag for the corpus
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

		Tag tag = testTags.generateTag(insertedCorpusId);
		long insertedTagId = tagTableOperations.addEntry(tag);
		assertEquals(insertedTagId, tagTableOperations.getEntry(insertedTagId).getId());
		assertEquals(insertedCorpusId, tagTableOperations.getEntry(insertedTagId).getCorpusId());
		assertEquals(tag.getName(), tagTableOperations.getEntry(insertedTagId).getName());

		//insert one text entry
		Text text = testTexts.generateText(insertedCorpusId, insertedTagId);
		long insertedTextId = textTableOperations.addEntry(text);
		assertEquals(insertedTextId, textTableOperations.getEntry(insertedTextId).getId());
		assertEquals(insertedCorpusId, textTableOperations.getEntry(insertedTextId).getCorpusId());
		assertEquals(text.getTitle(), textTableOperations.getEntry(insertedTextId).getTitle());
		assertEquals(text.getContent(), textTableOperations.getEntry(insertedTextId).getContent());
		assertEquals(text.getTagId(), textTableOperations.getEntry(insertedTextId).getTagId());
		assertNotNull(textTableOperations.getEntry(insertedTextId).getUpdateDate());

		//insert one category
		MeasureCategory category = testCategories.generateCategory();
		long insertedCategoryId = categoryTableOperations.addEntry(category);
		assertEquals(insertedCategoryId, categoryTableOperations.getEntry(insertedCategoryId).getId());
		assertEquals(category.getName(), categoryTableOperations.getEntry(insertedCategoryId).getName());
		assertEquals(category.getDescription(), categoryTableOperations.getEntry(insertedCategoryId).getDescription());

		//insert one measure of that category
		Measure measure = testMeasures.generateMeasure(insertedCategoryId);
		long insertedMeasureId = measureTableOperations.addEntry(measure);
		assertEquals(insertedMeasureId, measureTableOperations.getEntry(insertedMeasureId).getId());
		assertEquals(insertedCategoryId, measureTableOperations.getEntry(insertedMeasureId).getCategoryId());
		assertEquals(measure.getName(), measureTableOperations.getEntry(insertedMeasureId).getName());
		assertEquals(measure.getDescription(), measureTableOperations.getEntry(insertedMeasureId).getDescription());
		
		//insert a feature set a add the measure to the feature set
		FeatureSet featureSet = testFeatureSets.generateFeatureSet(insertedUserId);
		long insertedFeatureSetId = featureSetTableOperations.addEntry(featureSet);
		Fs_Me fs_me = testFs_Mes.generateFs_Me(insertedFeatureSetId, insertedMeasureId);
		fs_METableOperations.addEntry(fs_me);
		
		//create an analysis
		Analysis analysis = testAnalyses.generateAnalysis(insertedCorpusId, insertedFeatureSetId);
		long insertedAnalysisId = analysisTableOperations.addEntry(analysis);

		//insert result entry
		logger.trace("Testing inserting single entry...");
		Result result = testResults.generateResult(insertedAnalysisId, insertedTextId, insertedMeasureId);
		long insertedResultId = resultTableOperations.addEntry(result);
		assertEquals(insertedResultId, resultTableOperations.getEntry(insertedResultId).getId());
		assertEquals(insertedTextId, resultTableOperations.getEntry(insertedResultId).getTextId());
		assertEquals(insertedMeasureId, resultTableOperations.getEntry(insertedResultId).getMeasureId());
		assertNotNull(resultTableOperations.getEntry(insertedResultId).getValue());
		assertEquals(1, resultTableOperations.getAllEntriesByText(insertedTextId).size());
		assertEquals(1, resultTableOperations.getNumEntriesByText(insertedTextId));

		//change one entry: update
		logger.trace("Testing updating single entry...");
		double oldValue = resultTableOperations.getEntry(insertedResultId).getValue();
		Random rand = new Random();
		double newValue = rand.nextDouble();
		resultTableOperations.updateValue(insertedResultId, newValue);
		assertEquals(newValue, resultTableOperations.getEntry(insertedResultId).getValue(), .0001);
		assertNotEquals(oldValue, newValue);

		result.setValue(rand.nextDouble());
		oldValue = newValue;
		resultTableOperations.updateEntry(result);
		assertEquals(result.getValue(), resultTableOperations.getEntry(insertedResultId).getValue(), .0001);
		assertNotEquals(oldValue, resultTableOperations.getEntry(insertedResultId).getValue(), .0001);

		//delete one entry
		logger.trace("Testing deleting single entry...");
		resultTableOperations.deleteEntry(insertedResultId);
		assertEquals(0, resultTableOperations.getNumEntries());
		assertNull(resultTableOperations.getEntry(insertedResultId));

		//insert multiple entries
		logger.trace("Testing inserting multiple entries...");
		int nEntries = 10;

		//insert multiple texts first
		List<Text> texts = testTexts.generateTexts(insertedCorpusId, insertedTagId, nEntries);
		long[] insertedTextIds = textTableOperations.addEntries(texts);
		long[] insertedResultIds = new long[nEntries];

		//for each text, generate a result on a measure and insert the result
		for(int i = 0; i < nEntries; i++) {
			Result r = testResults.generateResult(insertedAnalysisId, insertedTextIds[i], insertedMeasureId);
			insertedResultIds[i] = resultTableOperations.addEntry(r);
		}

		assertEquals(nEntries, resultTableOperations.getAllEntries().size());
		assertEquals(nEntries, resultTableOperations.getNumEntries());
		assertEquals(nEntries, resultTableOperations.getNumEntriesByTag(insertedTagId));

		for(long tid: insertedTextIds) {
			assertEquals(1, resultTableOperations.getNumEntriesByText(tid));
		}

		//delete multiple entries: half of the inserted entries
		logger.trace("Testing deleting multiple entries...");
		int nEntriesToDelete = nEntries / 2;
		int nEntriesLeft = nEntries - nEntriesToDelete;
		long[] resultIdsToDelete = new long[nEntriesToDelete];
		for(int i = 0; i < nEntriesToDelete; i ++) {
			resultIdsToDelete[i] = insertedResultIds[i];
		}
		resultTableOperations.deleteEntries(resultIdsToDelete);
		assertEquals(nEntriesLeft, resultTableOperations.getNumEntries());
		assertEquals(nEntriesLeft, resultTableOperations.getNumEntriesByTag(insertedTagId));
		assertEquals(nEntriesLeft, resultTableOperations.getAllEntries().size());
		assertEquals(nEntriesLeft, resultTableOperations.getAllEntriesByAnalysis(insertedAnalysisId).size());

		//delete all entries
		logger.trace("Testing deleting all entries...");
		resultTableOperations.deleteAllEntries();
		assertNull(resultTableOperations.getAllEntries());
		assertEquals(0, resultTableOperations.getNumEntries());

		//drop the table
		logger.trace("Testing dropping the table...");
		resultTableOperations.dropTable();
		assertFalse(resultTableOperations.isTableExist());

		//finishing the test, cleaning the db
		cleanTestEnvironment();
	}

	//initialize dependent tables and recreate the test table
	private void initTestEnvironment() throws ClassNotFoundException, SQLException, IOException {
		logger.trace("Initializing test environment...");

		//drop the tables, be careful of dependencies
		resultTableOperations.dropTable();
		analysisTableOperations.dropTable();
		fs_METableOperations.dropTable();
		featureSetTableOperations.dropTable();
		textTableOperations.dropTable();
		tagTableOperations.dropTable();
		corpusTableOperations.dropTable();
		measureTableOperations.dropTable();
		categoryTableOperations.dropTable();
		userTableOperations.dropTable();
		assertFalse(resultTableOperations.isTableExist());
		assertFalse(measureTableOperations.isTableExist());
		assertFalse(categoryTableOperations.isTableExist());
		assertFalse(analysisTableOperations.isTableExist());
		assertFalse(fs_METableOperations.isTableExist());
		assertFalse(featureSetTableOperations.isTableExist());
		assertFalse(textTableOperations.isTableExist());
		assertFalse(tagTableOperations.isTableExist());
		assertFalse(corpusTableOperations.isTableExist());
		assertFalse(userTableOperations.isTableExist());

		//recreate the tables
		userTableOperations.createTable();
		corpusTableOperations.createTable();
		tagTableOperations.createTable();
		textTableOperations.createTable();
		categoryTableOperations.createTable();
		measureTableOperations.createTable();
		featureSetTableOperations.createTable();
		fs_METableOperations.createTable();
		analysisTableOperations.createTable();
		resultTableOperations.createTable();
		assertTrue(userTableOperations.isTableExist());
		assertTrue(corpusTableOperations.isTableExist());
		assertTrue(tagTableOperations.isTableExist());
		assertTrue(textTableOperations.isTableExist());
		assertTrue(categoryTableOperations.isTableExist());
		assertTrue(measureTableOperations.isTableExist());
		assertTrue(featureSetTableOperations.isTableExist());
		assertTrue(fs_METableOperations.isTableExist());
		assertTrue(analysisTableOperations.isTableExist());
		assertTrue(resultTableOperations.isTableExist());
	}

	//droping all the test tables
	//be careful with table dependencies, drop tables that are not depended by others first.
	private void cleanTestEnvironment() throws ClassNotFoundException, SQLException, IOException {
		logger.trace("Cleaning test environment...");

		resultTableOperations.dropTable();
		analysisTableOperations.dropTable();
		fs_METableOperations.dropTable();
		featureSetTableOperations.dropTable();
		textTableOperations.dropTable();
		tagTableOperations.dropTable();
		corpusTableOperations.dropTable();
		measureTableOperations.dropTable();
		categoryTableOperations.dropTable();
		userTableOperations.dropTable();
		assertFalse(resultTableOperations.isTableExist());
		assertFalse(measureTableOperations.isTableExist());
		assertFalse(categoryTableOperations.isTableExist());
		assertFalse(analysisTableOperations.isTableExist());
		assertFalse(fs_METableOperations.isTableExist());
		assertFalse(featureSetTableOperations.isTableExist());
		assertFalse(textTableOperations.isTableExist());
		assertFalse(tagTableOperations.isTableExist());
		assertFalse(corpusTableOperations.isTableExist());
		assertFalse(userTableOperations.isTableExist());
	}


}
