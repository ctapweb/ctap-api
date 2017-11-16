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
import com.ctapweb.api.db.data_generators.TestTags;
import com.ctapweb.api.db.data_generators.TestUserAccounts;
import com.ctapweb.api.db.pojos.Corpus;
import com.ctapweb.api.db.pojos.Tag;
import com.ctapweb.api.db.pojos.UserAccount;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TagTableOperationsTest {
	UserTableOperations userTableOperations;
	CorpusTableOperations corpusTableOperations;
	TagTableOperations tagTableOperations;

	TestUserAccounts testUserAccounts;
	TestCorpora testCorpora;
	TestTags testTags;

	Logger logger = LogManager.getLogger();

	public TagTableOperationsTest() throws ClassNotFoundException, IOException, SQLException {
		DataSource dataSource = DataSourceManager.getTestDataSource();

		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);
		tagTableOperations = new TagTableOperations(dataSource);

		testUserAccounts = new TestUserAccounts();
		testCorpora = new TestCorpora();
		testTags = new TestTags();
	}

	@Test
	public void testTableOperations() throws ClassNotFoundException, IOException, SQLException {
		logger.info("Testing TextTableOperations:");

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

		//insert one tag entry
		logger.trace("Testing inserting single entry...");
		Tag tag = testTags.generateTag(insertedCorpusId);
		long insertedTagId = tagTableOperations.addEntry(tag);
		assertEquals(insertedTagId, tagTableOperations.getEntry(insertedTagId).getId());
		assertEquals(insertedCorpusId, tagTableOperations.getEntry(insertedTagId).getCorpusId());

		//test if user owner
		logger.trace("Testing if user owner...");
		assertTrue(tagTableOperations.isUserOwner(insertedUserId,insertedTagId));

		//change one entry: update
		logger.trace("Testing updating single entry...");
		String newTagName = "New Tag Name";
		tagTableOperations.updateName(insertedTagId, newTagName);
		assertEquals(newTagName, tagTableOperations.getEntry(insertedTagId).getName());

		//delete one entry
		logger.trace("Testing deleting single entry...");
		tagTableOperations.deleteEntry(insertedTagId);
		assertEquals(0, tagTableOperations.getNumEntries());
		assertNull(tagTableOperations.getEntry(insertedTagId));
		assertNull(tagTableOperations.getAllEntries());
		assertNull(tagTableOperations.getAllEntriesByOwner(insertedUserId));
		assertNull(tagTableOperations.getAllEntriesByCorpus(insertedCorpusId));

		//insert multiple entries
		logger.trace("Testing inserting multiple entries...");
		int nEntries = 10;
		List<Tag> tagList = testTags.generateTags(insertedCorpusId, nEntries);
		long[] insertedTagIds = tagTableOperations.addEntries(tagList);

		assertEquals(nEntries, tagTableOperations.getAllEntries().size());
		assertEquals(nEntries, tagTableOperations.getNumEntries());
		assertEquals(nEntries, tagTableOperations.getNumEntriesByCorpus(insertedCorpusId));
		assertEquals(nEntries, tagTableOperations.getNumEntriesByOwner(insertedUserId));
		
		//test if entry exists
		for(Tag t: tagList) {
			String tagName = t.getName();
			long corpusId = t.getCorpusId();
			assertTrue(tagTableOperations.isTagExist(corpusId, tagName));
		}

		//delete multiple entries: half of the inserted entries
		logger.trace("Testing deleting multiple entries...");
		int nEntriesToDelete = nEntries / 2;
		int nEntriesLeft = nEntries - nEntriesToDelete;
		long[] tagIdsToDelete = new long[nEntriesToDelete];
		for(int i = 0; i < nEntriesToDelete; i ++) {
			tagIdsToDelete[i] = insertedTagIds[i];
		}
		tagTableOperations.deleteEntries(tagIdsToDelete);
		assertEquals(nEntriesLeft, tagTableOperations.getNumEntries());
		assertEquals(nEntriesLeft, tagTableOperations.getNumEntriesByOwner(insertedUserId));
		assertEquals(nEntriesLeft, tagTableOperations.getNumEntriesByCorpus(insertedCorpusId));
		assertEquals(nEntriesLeft, tagTableOperations.getAllEntries().size());
		assertEquals(nEntriesLeft, tagTableOperations.getAllEntriesByCorpus(insertedCorpusId).size());
		assertEquals(nEntriesLeft, tagTableOperations.getAllEntriesByOwner(insertedUserId).size());

		//delete all entries
		logger.trace("Testing deleting all entries...");
		tagTableOperations.deleteAllEntries();
		assertNull(tagTableOperations.getAllEntries());
		assertEquals(0, tagTableOperations.getNumEntries());
		assertEquals(0, tagTableOperations.getNumEntriesByOwner(insertedUserId));
		assertEquals(0, tagTableOperations.getNumEntriesByCorpus(insertedCorpusId));

		//drop the table
		logger.trace("Testing dropping the table...");
		tagTableOperations.dropTable();
		assertFalse(tagTableOperations.isTableExist());

		//finishing the test, cleaning the db
		cleanTestEnvironment();
	}

	@Test
	public void testGetEntriesByOwner() throws ClassNotFoundException, SQLException, IOException {
		logger.info("Testing getting entries by owner or corpus:");
		initTestEnvironment(); 

		//insert five users
		int numUsers = 5;
		List<UserAccount>  userList = testUserAccounts.generateAccounts(numUsers);
		long[] userIds = userTableOperations.addEntries(userList);

		//for each user, insert 10 corpora
		int numCorporaEachUser = 10;
		int numTagEachCorpus = 100;
		for(long userId: userIds) {
			List<Corpus> corpusList = testCorpora.generateCorpora(userId, numCorporaEachUser);
			//insert the corpora
			long[] corpusIds = corpusTableOperations.addEntries(corpusList);

			//for each corpus, insert 100 tags
			for(long corpusId: corpusIds) {
				List<Tag> tagList = testTags.generateTags(corpusId, numTagEachCorpus);
				//insert the texts
				tagTableOperations.addEntries(tagList);
			}
		}

		//for each user, get their corpora from db
		for(long userId: userIds) {
			List<Corpus> corporaOfUser = corpusTableOperations.getAllEntriesByOwner(userId);
			assertEquals(numCorporaEachUser, corporaOfUser.size());
			assertEquals(numCorporaEachUser, corpusTableOperations.getNumEntriesByOwner(userId));

			//for each corpus belonging to the user
			for(Corpus c: corporaOfUser) {
				//check each corpus has the same userId
				assertEquals(userId, c.getOwnerId());

				long corpusId = c.getId();

				assertEquals(numTagEachCorpus * numCorporaEachUser, tagTableOperations.getNumEntriesByOwner(userId));
				assertEquals(numTagEachCorpus * numCorporaEachUser, tagTableOperations.getAllEntriesByOwner(userId).size());
				assertEquals(numTagEachCorpus, tagTableOperations.getAllEntriesByCorpus(corpusId).size());
				assertEquals(numTagEachCorpus, tagTableOperations.getNumEntriesByCorpus(corpusId));
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
		tagTableOperations.dropTable();
		corpusTableOperations.dropTable();
		userTableOperations.dropTable();
		tagTableOperations.dropTable();
		assertFalse(tagTableOperations.isTableExist());
		assertFalse(corpusTableOperations.isTableExist());
		assertFalse(userTableOperations.isTableExist());
		assertFalse(tagTableOperations.isTableExist());


		//recreate the tables
		userTableOperations.createTable();
		corpusTableOperations.createTable();
		tagTableOperations.createTable();
		tagTableOperations.createTable();
		assertTrue(userTableOperations.isTableExist());
		assertTrue(corpusTableOperations.isTableExist());
		assertTrue(tagTableOperations.isTableExist());
		assertTrue(tagTableOperations.isTableExist());
	}

	//droping all the test tables
	//be careful with table dependencies, drop tables that are not depended by others first.
	private void cleanTestEnvironment() throws ClassNotFoundException, SQLException, IOException {
		logger.trace("Cleaning test environment...");
		tagTableOperations.dropTable();
		assertFalse(tagTableOperations.isTableExist());

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

		//generate a user, 5 corpora, and a number of tag entries
		int numCorpora = 5;
		UserAccount userAccount = testUserAccounts.generateAccount();
		long insertedUserId = userTableOperations.addEntry(userAccount);

		List<Corpus> corpora = testCorpora.generateCorpora(insertedUserId, numCorpora);
		long[] insertedCorpusIds = corpusTableOperations.addEntries(corpora);
		assertEquals(numCorpora, corpusTableOperations.getNumEntries());
		assertEquals(numCorpora, corpusTableOperations.getNumEntriesByOwner(insertedUserId));

		//for each corpus, generate a number of tags
		for(long corpusId: insertedCorpusIds) {
			List<Tag> tags = testTags.generateTags(corpusId, numEntries);
			long[] insertedTagIds = tagTableOperations.addEntries(tags);

			assertEquals(numEntries, tagTableOperations.getNumEntriesByCorpus(corpusId));
			assertEquals(numEntries, tagTableOperations.getAllEntriesByCorpus(corpusId).size());

			//get paged results
			Arrays.sort(insertedTagIds);
			for(int i = 0, offset = 0; i < numPages; i++, offset += limit) {
				//ids of inserted entries
				long[] idsOnPage = Arrays.copyOfRange(insertedTagIds, offset, 
						Math.min(insertedTagIds.length, offset + limit));

				logger.trace("IDs should be on page {}: {}", i, idsOnPage);

				//ids of retrieved entries of the page
				List<Tag> retrievedEntriesForPage = 
						tagTableOperations.getEntriesByCorpus(corpusId, tagTableOperations.COLUMN_ID, limit, offset);
				int numEntriesRetrieved = retrievedEntriesForPage.size();
				long[] retrievedEntryIds = new long[numEntriesRetrieved];
				for(int j = 0; j < numEntriesRetrieved; j++) {
					retrievedEntryIds[j] = retrievedEntriesForPage.get(j).getId();
				}

				logger.trace("Retrieved IDs on page {}: {}", i, retrievedEntryIds);

				assertArrayEquals(idsOnPage, retrievedEntryIds);
			}
		}

		//drop the tables
		cleanTestEnvironment();
	}


}
