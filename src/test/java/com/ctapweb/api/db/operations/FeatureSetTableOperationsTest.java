package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.data_generators.TestFeatureSets;
import com.ctapweb.api.db.data_generators.TestUserAccounts;
import com.ctapweb.api.db.pojos.FeatureSet;
import com.ctapweb.api.db.pojos.UserAccount;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FeatureSetTableOperationsTest {
	UserTableOperations userTableOperations;
	FeatureSetTableOperations featureSetTableOperations;
	TestUserAccounts testUserAccounts;
	TestFeatureSets testFeatureSets;
	Logger logger = LogManager.getLogger();

	public FeatureSetTableOperationsTest() throws ClassNotFoundException, IOException, SQLException {
		DataSource dataSource = DataSourceManager.getTestDataSource();

		userTableOperations = new UserTableOperations(dataSource);
		featureSetTableOperations = new FeatureSetTableOperations(dataSource);

		testUserAccounts = new TestUserAccounts();
		testFeatureSets = new TestFeatureSets();
	}

	@Test
	public void testTableOperations() throws ClassNotFoundException, IOException, SQLException {
		logger.info("Testing FeatureSetTableOperations:");

		initTestEnvironment();

		//insert one user account
		UserAccount userAccount = testUserAccounts.generateAccount();
		long insertedUserId = userTableOperations.addEntry(userAccount);
		assertTrue(userTableOperations.isAccountExist(userAccount.getEmail()));

		//insert one entry
		logger.trace("Testing inserting single entry...");
		FeatureSet featureSet = testFeatureSets.generateFeatureSet(insertedUserId);
		long insertedFeatureSetId = featureSetTableOperations.addEntry(featureSet);

		assertEquals(insertedFeatureSetId, featureSetTableOperations.getEntry(insertedFeatureSetId).getId());
		assertEquals(featureSet.getOwnerId(), featureSetTableOperations.getEntry(insertedFeatureSetId).getOwnerId());
		assertEquals(featureSet.getName(), featureSetTableOperations.getEntry(insertedFeatureSetId).getName());
		assertEquals(featureSet.getDescription(), featureSetTableOperations.getEntry(insertedFeatureSetId).getDescription());
		assertNotNull(featureSetTableOperations.getEntry(insertedFeatureSetId).getCreateDate());

		//test if user owner
		logger.trace("Testing if user owner...");
		assertTrue(featureSetTableOperations.isUserOwner(insertedUserId, insertedFeatureSetId));

		//change one entry: update
		logger.trace("Testing updating single entry...");
		String newName = "New Feature Set Name";
		featureSetTableOperations.updateName(insertedFeatureSetId, newName);
		assertEquals(newName, featureSetTableOperations.getEntry(insertedFeatureSetId).getName());

		String newDescription = "New Feature Set Description";
		featureSetTableOperations.updateDescription(insertedFeatureSetId, newDescription);
		assertEquals(newDescription, featureSetTableOperations.getEntry(insertedFeatureSetId).getDescription());

		featureSet.setName("updated name");
		featureSet.setDescription("updated description");
		featureSetTableOperations.updateEntry(featureSet);
		assertEquals(featureSet.getName(), featureSetTableOperations.getEntry(insertedFeatureSetId).getName());
		assertEquals(featureSet.getDescription(), featureSetTableOperations.getEntry(insertedFeatureSetId).getDescription());

		//delete one entry
		logger.trace("Testing deleting single entry...");
		featureSetTableOperations.deleteEntry(insertedFeatureSetId);
		assertEquals(0, featureSetTableOperations.getNumEntries());
		assertNull(featureSetTableOperations.getEntry(insertedFeatureSetId));

		//insert multiple entries
		logger.trace("Testing inserting multiple entries...");
		int nEntries = 10;
		List<FeatureSet> featureSetList = testFeatureSets.generateFeatureSets(userAccount.getId(), nEntries);
		long[] insertedFeatureSetIds = featureSetTableOperations.addEntries(featureSetList);

		assertEquals(nEntries, featureSetTableOperations.getAllEntries().size());
		assertEquals(nEntries, featureSetTableOperations.getNumEntries());

		//delete multiple entries: half of the inserted entries
		logger.trace("Testing deleting multiple entries...");
		int nEntriesToDelete = nEntries / 2;
		int nEntriesLeft = nEntries - nEntriesToDelete;
		long[] corpusIdsToDelete = new long[nEntriesToDelete];
		for(int i = 0; i < nEntriesToDelete; i ++) {
			corpusIdsToDelete[i] = insertedFeatureSetIds[i];
		}
		featureSetTableOperations.deleteEntries(corpusIdsToDelete);
		assertEquals(nEntriesLeft, featureSetTableOperations.getNumEntries());

		//delete all entries
		logger.trace("Testing deleting all entries...");
		featureSetTableOperations.deleteAllEntries();
		assertNull(featureSetTableOperations.getAllEntries());
		assertEquals(0, featureSetTableOperations.getNumEntries());

		//drop the table
		logger.trace("Testing dropping the table...");
		featureSetTableOperations.dropTable();
		assertFalse(featureSetTableOperations.isTableExist());

		//finishing the test, cleaning the db
		cleanTestEnvironment();
	}

	@Test
	public void testGetEntriesByOwner() throws ClassNotFoundException, SQLException, IOException {
		logger.info("Testing get entries by owner:");
		initTestEnvironment(); 

		//insert five users
		int numUsers = 5;
		List<UserAccount>  userList = testUserAccounts.generateAccounts(numUsers);
		long[] userIds = userTableOperations.addEntries(userList);

		//for each user, insert 10 feature sets
		int numFeatureSetsEachUser = 10;
		for(long userId: userIds) {
			List<FeatureSet> corpusList = testFeatureSets.generateFeatureSets(userId, numFeatureSetsEachUser);
			//insert the feature set
			featureSetTableOperations.addEntries(corpusList);
		}

		//for each user, get their feature set from db
		for(long userId: userIds) {
			List<FeatureSet> featureSetsOfUser = featureSetTableOperations.getAllEntriesByOwner(userId);
			assertEquals(numFeatureSetsEachUser, featureSetsOfUser.size());
			assertEquals(numFeatureSetsEachUser, featureSetTableOperations.getNumEntriesByOwner(userId));

			//check each feature set has the same userId
			for(FeatureSet f: featureSetsOfUser) {
				assertEquals(userId, f.getOwnerId());
			}
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
		featureSetTableOperations.dropTable();
		userTableOperations.dropTable();
		assertFalse(featureSetTableOperations.isTableExist());
		assertFalse(userTableOperations.isTableExist());

		//recreate the tables
		userTableOperations.createTable();
		featureSetTableOperations.createTable();
		assertTrue(userTableOperations.isTableExist());
		assertTrue(featureSetTableOperations.isTableExist());
	}

	//droping all the test tables
	//be careful with table dependencies, drop tables that are not depended by others first.
	private void cleanTestEnvironment() throws ClassNotFoundException, SQLException, IOException {
		logger.trace("Cleaning test environment...");
		featureSetTableOperations.dropTable();
		assertFalse(featureSetTableOperations.isTableExist());

		userTableOperations.dropTable();
		assertFalse(userTableOperations.isTableExist());

	}

	/**
	 * 
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
