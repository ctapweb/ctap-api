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
import com.ctapweb.api.db.data_generators.TestCorpora;
import com.ctapweb.api.db.data_generators.TestUserAccounts;
import com.ctapweb.api.db.pojos.Corpus;
import com.ctapweb.api.db.pojos.UserAccount;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CorpusTableOperationsTest {
	UserTableOperations userTableOperations;
	CorpusTableOperations corpusTableOperations;
	TestUserAccounts testUserAccounts;
	TestCorpora testCorpora;
	Logger logger = LogManager.getLogger();

	public CorpusTableOperationsTest() throws ClassNotFoundException, IOException, SQLException {
		DataSource dataSource = DataSourceManager.getTestDataSource();

		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);

		testUserAccounts = new TestUserAccounts();
		testCorpora = new TestCorpora();
	}

	@Test
	public void testTableOperations() throws ClassNotFoundException, IOException, SQLException {
		logger.info("Testing CorpusTableOperations:");

		initTestEnvironment();

		//insert one user account
		UserAccount userAccount = testUserAccounts.generateAccount();
		long insertedUserId = userTableOperations.addEntry(userAccount);
		assertTrue(userTableOperations.isAccountExist(userAccount.getEmail()));

		//insert one entry
		logger.trace("Testing inserting single entry...");
		Corpus corpus = testCorpora.generateCorpus(insertedUserId);
		long insertedCorpusId = corpusTableOperations.addEntry(corpus);

		assertEquals(insertedCorpusId, corpusTableOperations.getEntry(insertedCorpusId).getId());
		assertEquals(corpus.getOwnerId(), corpusTableOperations.getEntry(insertedCorpusId).getOwnerId());
		assertEquals(corpus.getName(), corpusTableOperations.getEntry(insertedCorpusId).getName());
		assertEquals(corpus.getDescription(), corpusTableOperations.getEntry(insertedCorpusId).getDescription());
		assertNotNull(corpusTableOperations.getEntry(insertedCorpusId).getCreateDate());

		//test if user owner
		logger.trace("Testing if user owner...");
		assertTrue(corpusTableOperations.isUserOwner(insertedUserId, insertedCorpusId));

		//change one entry: update
		logger.trace("Testing updating single entry...");
		String newCorpusName = "New Corpus Name";
		corpusTableOperations.updateCorpusName(insertedCorpusId, newCorpusName);
		assertEquals(newCorpusName, corpusTableOperations.getEntry(insertedCorpusId).getName());

		String newCorpusDescription = "New Corpus Description";
		corpusTableOperations.updateCorpusDescription(insertedCorpusId, newCorpusDescription);
		assertEquals(newCorpusDescription, corpusTableOperations.getEntry(insertedCorpusId).getDescription());

		corpus.setName("updated name");
		corpus.setDescription("updated description");
		corpusTableOperations.updateEntry(corpus);
		assertEquals(corpus.getName(), corpusTableOperations.getEntry(insertedCorpusId).getName());
		assertEquals(corpus.getDescription(), corpusTableOperations.getEntry(insertedCorpusId).getDescription());

		//delete one entry
		logger.trace("Testing deleting single entry...");
		corpusTableOperations.deleteEntry(insertedCorpusId);
		assertEquals(0, corpusTableOperations.getNumEntries());
		assertNull(corpusTableOperations.getEntry(insertedCorpusId));

		//insert multiple entries
		logger.trace("Testing inserting multiple entries...");
		int nEntries = 10;
		List<Corpus> corpusList = testCorpora.generateCorpora(userAccount.getId(), nEntries);
		long[] insertedCorpusIds = corpusTableOperations.addEntries(corpusList);

		assertEquals(nEntries, corpusTableOperations.getAllEntries().size());
		assertEquals(nEntries, corpusTableOperations.getNumEntries());

		//delete multiple entries: half of the inserted entries
		logger.trace("Testing deleting multiple entries...");
		int nEntriesToDelete = nEntries / 2;
		int nEntriesLeft = nEntries - nEntriesToDelete;
		long[] corpusIdsToDelete = new long[nEntriesToDelete];
		for(int i = 0; i < nEntriesToDelete; i ++) {
			corpusIdsToDelete[i] = insertedCorpusIds[i];
		}
		corpusTableOperations.deleteEntries(corpusIdsToDelete);
		assertEquals(nEntriesLeft, corpusTableOperations.getNumEntries());

		//delete all entries
		logger.trace("Testing deleting all entries...");
		corpusTableOperations.deleteAllEntries();
		assertNull(corpusTableOperations.getAllEntries());
		assertEquals(0, corpusTableOperations.getNumEntries());

		//drop the table
		logger.trace("Testing dropping the table...");
		corpusTableOperations.dropTable();
		assertFalse(corpusTableOperations.isTableExist());

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

		//for each user, insert 10 corpora
		int numCorporaEachUser = 10;
		for(long userId: userIds) {
			List<Corpus> corpusList = testCorpora.generateCorpora(userId, numCorporaEachUser);
			//insert the corpora
			corpusTableOperations.addEntries(corpusList);
		}

		//for each user, get their corpora from db
		for(long userId: userIds) {
			List<Corpus> corporaOfUser = corpusTableOperations.getAllEntriesByOwner(userId);
			assertEquals(numCorporaEachUser, corporaOfUser.size());
			assertEquals(numCorporaEachUser, corpusTableOperations.getNumEntriesByOwner(userId));

			//check each corpus has the same userId
			for(Corpus c: corporaOfUser) {
				assertEquals(userId, c.getOwnerId());
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
		corpusTableOperations.dropTable();
		userTableOperations.dropTable();
		assertFalse(corpusTableOperations.isTableExist());
		assertFalse(userTableOperations.isTableExist());

		//recreate the tables
		userTableOperations.createTable();
		corpusTableOperations.createTable();
		assertTrue(userTableOperations.isTableExist());
		assertTrue(corpusTableOperations.isTableExist());
	}

	//droping all the test tables
	//be careful with table dependencies, drop tables that are not depended by others first.
	private void cleanTestEnvironment() throws ClassNotFoundException, SQLException, IOException {
		logger.trace("Cleaning test environment...");
		corpusTableOperations.dropTable();
		assertFalse(corpusTableOperations.isTableExist());

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

		List<Corpus> corpora = testCorpora.generateCorpora(insertedUserId, numEntries);
		long[] insertedCorpusIds = corpusTableOperations.addEntries(corpora);
		assertEquals(numEntries, corpusTableOperations.getNumEntries());

		//get paged results
		Arrays.sort(insertedCorpusIds);
		for(int i = 0, offset = 0; i < numPages; i++, offset += limit) {
			//ids of inserted entries
			long[] idsOnPage = Arrays.copyOfRange(insertedCorpusIds, offset, 
					Math.min(insertedCorpusIds.length, offset + limit));

			logger.trace("IDs should be on page {}: {}", i, idsOnPage);

			//ids of retrieved entries of the page
			List<Corpus> retrievedEntriesForPage = 
					corpusTableOperations.getEntriesByOwner(insertedUserId, corpusTableOperations.COLUMN_ID, limit, offset);
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
