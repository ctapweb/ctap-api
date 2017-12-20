package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.data_generators.TestCategories;
import com.ctapweb.api.db.data_generators.TestFeatureSets;
import com.ctapweb.api.db.data_generators.TestFs_Mes;
import com.ctapweb.api.db.data_generators.TestMeasures;
import com.ctapweb.api.db.data_generators.TestUserAccounts;
import com.ctapweb.api.db.pojos.FeatureSet;
import com.ctapweb.api.db.pojos.Fs_Me;
import com.ctapweb.api.db.pojos.Measure;
import com.ctapweb.api.db.pojos.MeasureCategory;
import com.ctapweb.api.db.pojos.UserAccount;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class Fs_MeTableOperationsTest {
	UserTableOperations userTableOperations;
	FeatureSetTableOperations featureSetTableOperations;
	MeasureCategoryTableOperations categoryTableOperations;
	MeasureTableOperations measureTableOperations;
	FS_METableOperations fs_meTableOperations;


	TestUserAccounts testUserAccounts;
	TestFeatureSets testFeatureSets;
	TestCategories testCategories;
	TestMeasures testMeasures;
	TestFs_Mes testFs_Mes;
	Logger logger = LogManager.getLogger();

	public Fs_MeTableOperationsTest() throws ClassNotFoundException, IOException, SQLException {
		DataSource dataSource = DataSourceManager.getTestDataSource();

		userTableOperations = new UserTableOperations(dataSource);
		featureSetTableOperations = new FeatureSetTableOperations(dataSource);
		categoryTableOperations = new MeasureCategoryTableOperations(dataSource);
		measureTableOperations = new MeasureTableOperations(dataSource);
		fs_meTableOperations = new FS_METableOperations(dataSource);

		testUserAccounts = new TestUserAccounts();
		testFeatureSets = new TestFeatureSets();
		testCategories = new TestCategories();
		testMeasures = new TestMeasures();
		testFs_Mes = new TestFs_Mes();
	}

	@Test
	public void testTableOperations() throws ClassNotFoundException, IOException, SQLException {
		logger.info("Testing Fs_MeTableOperations:");

		initTestEnvironment();

		//insert one user account
		UserAccount userAccount = testUserAccounts.generateAccount();
		long insertedUserId = userTableOperations.addEntry(userAccount);
		assertTrue(userTableOperations.isAccountExist(userAccount.getEmail()));

		//insert one feature set
		FeatureSet featureSet = testFeatureSets.generateFeatureSet(insertedUserId);
		long insertedFeatureSetId = featureSetTableOperations.addEntry(featureSet);
		assertEquals(insertedFeatureSetId, featureSetTableOperations.getEntry(insertedFeatureSetId).getId());

		//insert one category
		MeasureCategory category = testCategories.generateCategory();
		long insertedCategoryId = categoryTableOperations.addEntry(category);
		assertEquals(insertedCategoryId, featureSetTableOperations.getEntry(insertedCategoryId).getId());

		//insert one measure
		Measure measure = testMeasures.generateMeasure(insertedCategoryId);
		long insertedMeasureId = measureTableOperations.addEntry(measure);
		assertEquals(insertedMeasureId, measureTableOperations.getEntry(insertedMeasureId).getId());

		//insert one entry
		logger.trace("Testing inserting single entry...");
		Fs_Me fs_me = testFs_Mes.generateFs_Me(insertedFeatureSetId, insertedMeasureId);
		long insertedFs_MeId = fs_meTableOperations.addEntry(fs_me);
		assertEquals(insertedFs_MeId, fs_meTableOperations.getEntry(insertedFs_MeId).getId());
		assertEquals(insertedMeasureId, fs_meTableOperations.getEntry(insertedFs_MeId).getMeasure_id());
		assertEquals(insertedFeatureSetId, fs_meTableOperations.getEntry(insertedFs_MeId).getFs_id());
		assertEquals(1, fs_meTableOperations.getNumEntries());
		assertEquals(1, fs_meTableOperations.getNumEntriesByFeatureSet(insertedFeatureSetId));

		assertEquals(insertedFeatureSetId, featureSetTableOperations.getEntry(insertedFeatureSetId).getId());
		assertEquals(featureSet.getOwnerId(), featureSetTableOperations.getEntry(insertedFeatureSetId).getOwnerId());
		assertEquals(featureSet.getName(), featureSetTableOperations.getEntry(insertedFeatureSetId).getName());
		assertEquals(featureSet.getDescription(), featureSetTableOperations.getEntry(insertedFeatureSetId).getDescription());
		assertNotNull(featureSetTableOperations.getEntry(insertedFeatureSetId).getCreateDate());

		//delete one entry
		logger.trace("Testing deleting single entry...");
		fs_meTableOperations.deleteEntry(insertedFs_MeId);
		assertEquals(0, fs_meTableOperations.getNumEntries());
		assertNull(fs_meTableOperations.getEntry(insertedFs_MeId));

		//insert multiple entries
		logger.trace("Testing inserting multiple entries...");
		int nEntries = 10;
		measureTableOperations.deleteAllEntries();
		assertNull(measureTableOperations.getAllEntries());
		assertEquals(0, measureTableOperations.getNumEntries());

		//first, insert multiple measures
		List<Measure> measureList = testMeasures.generateMeasures(insertedCategoryId, nEntries);
		long[] insertedMeasureIds = measureTableOperations.addEntries(measureList);

		//add all the measures to a feature set
		for(int i = 0; i < insertedMeasureIds.length; i++ ) {
			Fs_Me entry = testFs_Mes.generateFs_Me(insertedFeatureSetId, insertedMeasureIds[i]);
			fs_meTableOperations.addEntry(entry);
		}

		assertEquals(nEntries, fs_meTableOperations.getAllEntriesByFeatureSet(insertedFeatureSetId).size());
		assertEquals(nEntries, fs_meTableOperations.getNumEntriesByFeatureSet(insertedFeatureSetId));

		//delete all entries
		logger.trace("Testing deleting all entries...");
		featureSetTableOperations.deleteAllEntries();
		assertNull(featureSetTableOperations.getAllEntries());
		assertEquals(0, featureSetTableOperations.getNumEntries());

		//drop the table
		logger.trace("Testing dropping the table...");
		fs_meTableOperations.dropTable();
		assertFalse(fs_meTableOperations.isTableExist());

		//finishing the test, cleaning the db
		cleanTestEnvironment();
	}

	@Test
	public void testGetEntriesByFeatureSet() throws ClassNotFoundException, SQLException, IOException {
		logger.info("Testing get entries by festure set:");
		initTestEnvironment(); 

		//insert a user
		UserAccount userAcocunt = testUserAccounts.generateAccount();
		long insertedUserId = userTableOperations.addEntry(userAcocunt);

		//the user creates some feature sets
		int nFeatureSets = 5;
		List<FeatureSet> twoFeatureSets = testFeatureSets.generateFeatureSets(insertedUserId, nFeatureSets);
		long[] insertedFeatureSetIds = featureSetTableOperations.addEntries(twoFeatureSets);


		//insert one category
		MeasureCategory category = testCategories.generateCategory();
		long insertedCategoryId = categoryTableOperations.addEntry(category);
		assertEquals(insertedCategoryId, featureSetTableOperations.getEntry(insertedCategoryId).getId());

		//insert multiple measures
		int nMeasures = 20;
		List<Measure> measureList = testMeasures.generateMeasures(insertedCategoryId, nMeasures);
		long[] insertedMeasureIds = measureTableOperations.addEntries(measureList);

		//for each feature set, the user selects all the measures
		List<Fs_Me> fs_MeList = new ArrayList<>();
		for(int i = 0; i < insertedFeatureSetIds.length; i++) {
			for(int j = 0; j < insertedMeasureIds.length; j++) {
				Fs_Me fs_me = testFs_Mes.generateFs_Me(insertedFeatureSetIds[i], insertedMeasureIds[j]);
				fs_meTableOperations.addEntry(fs_me);
			}
		}
		
		for(int i = 0; i < insertedFeatureSetIds.length; i++) {
			assertEquals(nMeasures, fs_meTableOperations.getAllEntriesByFeatureSet(insertedFeatureSetIds[i]).size());
			assertEquals(nMeasures, fs_meTableOperations.getNumEntriesByFeatureSet(insertedFeatureSetIds[i]));
		}

		//drop the tables
		cleanTestEnvironment();

	}

	@Test
	public void testPagedQueries() throws SQLException, ClassNotFoundException, IOException {
		logger.info("Testing paged queries:");

		//test query paged results
		runPagedQuery(100, 10);
		runPagedQuery(205, 20);
		runPagedQuery(296, 13);
		runPagedQuery(335, 50);

	}

	//initialize dependent tables and recreate the test table
	private void initTestEnvironment() throws ClassNotFoundException, SQLException, IOException {
		logger.trace("Initializing test environment...");

		//drop the tables, be careful of dependencies
		fs_meTableOperations.dropTable();
		featureSetTableOperations.dropTable();
		userTableOperations.dropTable();
		measureTableOperations.dropTable();
		categoryTableOperations.dropTable();

		assertFalse(fs_meTableOperations.isTableExist());
		assertFalse(featureSetTableOperations.isTableExist());
		assertFalse(userTableOperations.isTableExist());
		assertFalse(measureTableOperations.isTableExist());
		assertFalse(categoryTableOperations.isTableExist());

		//recreate the tables
		userTableOperations.createTable();
		featureSetTableOperations.createTable();
		categoryTableOperations.createTable();
		measureTableOperations.createTable();
		fs_meTableOperations.createTable();
		assertTrue(userTableOperations.isTableExist());
		assertTrue(featureSetTableOperations.isTableExist());
		assertTrue(categoryTableOperations.isTableExist());
		assertTrue(measureTableOperations.isTableExist());
		assertTrue(fs_meTableOperations.isTableExist());
	}

	//droping all the test tables
	//be careful with table dependencies, drop tables that are not depended by others first.
	private void cleanTestEnvironment() throws ClassNotFoundException, SQLException, IOException {
		logger.trace("Cleaning test environment...");
		//drop the tables, be careful of dependencies
		fs_meTableOperations.dropTable();
		featureSetTableOperations.dropTable();
		userTableOperations.dropTable();
		measureTableOperations.dropTable();
		categoryTableOperations.dropTable();

		assertFalse(fs_meTableOperations.isTableExist());
		assertFalse(featureSetTableOperations.isTableExist());
		assertFalse(userTableOperations.isTableExist());
		assertFalse(measureTableOperations.isTableExist());
		assertFalse(categoryTableOperations.isTableExist());

	}

	/**
	 * Did not test.
	 * @param numEntries the number of entries to generate
	 * @param limit the number of entries to show per page
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void runPagedQuery(int numEntries, int limit) throws SQLException, ClassNotFoundException, IOException {
		initTestEnvironment();
		int numPages = (int) Math.ceil((double)numEntries / limit);

		logger.trace("numEntries: {}, limit: {}, numPages:{}", numEntries, limit, numPages);

		//generate a user and some corpora
		UserAccount userAccount = testUserAccounts.generateAccount();
		long insertedUserId = userTableOperations.addEntry(userAccount);

		List<FeatureSet> featureSets = testFeatureSets.generateFeatureSets(insertedUserId, numEntries);
		long[] insertedFeatureSetIds = featureSetTableOperations.addEntries(featureSets);
		assertEquals(numEntries, featureSetTableOperations.getNumEntries());

		//get paged results
		Arrays.sort(insertedFeatureSetIds);
		for(int i = 0, offset = 0; i < numPages; i++, offset += limit) {
			//ids of inserted entries
			long[] idsOnPage = Arrays.copyOfRange(insertedFeatureSetIds, offset, 
					Math.min(insertedFeatureSetIds.length, offset + limit));

			logger.trace("IDs should be on page {}: {}", i, idsOnPage);

			//ids of retrieved entries of the page
			List<FeatureSet> retrievedEntriesForPage = 
					featureSetTableOperations.getEntriesByOwner(insertedUserId, featureSetTableOperations.COLUMN_ID, limit, offset);
			int numEntriesRetrieved = retrievedEntriesForPage.size();
			long[] retrievedEntryIds = new long[numEntriesRetrieved];
			for(int j = 0; j < numEntriesRetrieved; j++) {
				retrievedEntryIds[j] = retrievedEntriesForPage.get(j).getId();
			}

			logger.trace("Retrieved IDs on page {}: {}", i, retrievedEntryIds);

			assertArrayEquals(idsOnPage, retrievedEntryIds);
		}

		//drop the tables
		cleanTestEnvironment();
	}


}
