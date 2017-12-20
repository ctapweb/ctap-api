package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.data_generators.TestCorpora;
import com.ctapweb.api.db.data_generators.TestTags;
import com.ctapweb.api.db.data_generators.TestTexts;
import com.ctapweb.api.db.data_generators.TestUserAccounts;
import com.ctapweb.api.db.pojos.Corpus;
import com.ctapweb.api.db.pojos.Tag;
import com.ctapweb.api.db.pojos.Text;
import com.ctapweb.api.db.pojos.UserAccount;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TextTableOperationsTest {
	UserTableOperations userTableOperations;
	CorpusTableOperations corpusTableOperations;
	TagTableOperations tagTableOperations;
	TextTableOperations textTableOperations;

	TestUserAccounts testUserAccounts;
	TestCorpora testCorpora;
	TestTags testTags;
	TestTexts testTexts;

	Logger logger = LogManager.getLogger();

	public TextTableOperationsTest() throws ClassNotFoundException, IOException, SQLException {
		DataSource dataSource = DataSourceManager.getTestDataSource();

		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);
		tagTableOperations = new TagTableOperations(dataSource);
		textTableOperations = new TextTableOperations(dataSource);

		testUserAccounts = new TestUserAccounts();
		testCorpora = new TestCorpora();
		testTags = new TestTags();
		testTexts = new TestTexts();
	}

	@Test
	public void testTableOperations() throws ClassNotFoundException, IOException, SQLException {
		logger.info("Testing TextTableOperations:");

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
		logger.trace("Testing inserting single entry...");
		Text text = testTexts.generateText(insertedCorpusId, insertedTagId);
		long insertedTextId = textTableOperations.addEntry(text);
		assertEquals(insertedTextId, textTableOperations.getEntry(insertedTextId).getId());
		assertEquals(insertedCorpusId, textTableOperations.getEntry(insertedTextId).getCorpusId());
		assertEquals(text.getTitle(), textTableOperations.getEntry(insertedTextId).getTitle());
		assertEquals(text.getContent(), textTableOperations.getEntry(insertedTextId).getContent());
		assertEquals(text.getTagId(), textTableOperations.getEntry(insertedTextId).getTagId());
		assertNotNull(textTableOperations.getEntry(insertedTextId).getUpdateDate());

		//test if user owner
		logger.trace("Testing if user owner...");
		assertTrue(textTableOperations.isUserOwner(insertedUserId,insertedTextId));

		//change one entry: update
		logger.trace("Testing updating single entry...");
		String newTextTitle = "New Text Title";
		textTableOperations.updateTitle(insertedTextId, newTextTitle);
		assertEquals(newTextTitle, textTableOperations.getEntry(insertedTextId).getTitle());

		String newTextContent = "New Text Content";
		Date oldUpdateDate = textTableOperations.getEntry(insertedTextId).getUpdateDate();
		textTableOperations.updateContent(insertedTextId, newTextContent);
		Date newUpdateDate = textTableOperations.getEntry(insertedTextId).getUpdateDate();
		assertEquals(newTextContent, textTableOperations.getEntry(insertedTextId).getContent());
		assertFalse(oldUpdateDate.equals(newUpdateDate));

		//delete one entry
		logger.trace("Testing deleting single entry...");
		textTableOperations.deleteEntry(insertedTextId);
		assertEquals(0, textTableOperations.getNumEntries());
		assertNull(textTableOperations.getEntry(insertedTextId));

		//test text entry without tags
		text.setTagId(null);
		insertedTextId = textTableOperations.addEntry(text);
		assertEquals(insertedTextId, textTableOperations.getEntry(insertedTextId).getId());
		assertEquals(insertedCorpusId, textTableOperations.getEntry(insertedTextId).getCorpusId());
		assertEquals(text.getTitle(), textTableOperations.getEntry(insertedTextId).getTitle());
		assertEquals(text.getContent(), textTableOperations.getEntry(insertedTextId).getContent());
		assertEquals(0, textTableOperations.getEntry(insertedTextId).getTagId().longValue());
		assertNotNull(textTableOperations.getEntry(insertedTextId).getUpdateDate());

		textTableOperations.deleteEntry(insertedTextId);
		assertEquals(0, textTableOperations.getNumEntries());
		assertNull(textTableOperations.getEntry(insertedTextId));

		//insert multiple entries
		logger.trace("Testing inserting multiple entries...");
		int nEntries = 10;
		List<Text> textList = testTexts.generateTexts(insertedCorpusId, insertedTagId, nEntries);
		long[] insertedTextIds = textTableOperations.addEntries(textList);

		assertEquals(nEntries, textTableOperations.getAllEntries().size());
		assertEquals(nEntries, textTableOperations.getNumEntries());

		//delete multiple entries: half of the inserted entries
		logger.trace("Testing deleting multiple entries...");
		int nEntriesToDelete = nEntries / 2;
		int nEntriesLeft = nEntries - nEntriesToDelete;
		long[] textIdsToDelete = new long[nEntriesToDelete];
		for(int i = 0; i < nEntriesToDelete; i ++) {
			textIdsToDelete[i] = insertedTextIds[i];
		}
		textTableOperations.deleteEntries(textIdsToDelete);
		assertEquals(nEntriesLeft, textTableOperations.getNumEntries());
		assertEquals(nEntriesLeft, textTableOperations.getNumEntriesByCorpus(insertedCorpusId));
		assertEquals(nEntriesLeft, textTableOperations.getAllEntries().size());
		assertEquals(nEntriesLeft, textTableOperations.getAllEntriesByCorpus(insertedCorpusId).size());

		//delete all entries
		logger.trace("Testing deleting all entries...");
		textTableOperations.deleteAllEntries();
		assertNull(textTableOperations.getAllEntries());
		assertEquals(0, textTableOperations.getNumEntries());
		assertEquals(0, textTableOperations.getNumEntriesByCorpus(insertedCorpusId));

		//drop the table
		logger.trace("Testing dropping the table...");
		textTableOperations.dropTable();
		assertFalse(textTableOperations.isTableExist());

		//finishing the test, cleaning the db
		cleanTestEnvironment();
	}

	@Test
	public void testGetEntriesByCorpus() throws ClassNotFoundException, SQLException, IOException {
		logger.info("Testing get entries by corpus:");
		initTestEnvironment(); 

		//insert five users
		int numUsers = 5;
		List<UserAccount>  userList = testUserAccounts.generateAccounts(numUsers);
		long[] userIds = userTableOperations.addEntries(userList);

		//for each user, insert 10 corpora
		int numCorporaEachUser = 10;
		int numTextEachCorpus = 100;
		for(long userId: userIds) {
			List<Corpus> corpusList = testCorpora.generateCorpora(userId, numCorporaEachUser);
			//insert the corpora
			long[] corpusIds = corpusTableOperations.addEntries(corpusList);

			//for each corpus, insert 100 texts
			for(long corpusId: corpusIds) {
				List<Text> textList = testTexts.generateTexts(corpusId, null, numTextEachCorpus);
				//insert the texts
				textTableOperations.addEntries(textList);
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

				assertEquals(numTextEachCorpus, textTableOperations.getNumEntriesByCorpus(corpusId));
				assertEquals(numTextEachCorpus, textTableOperations.getAllEntriesByCorpus(corpusId).size());
			}

		}

		//drop the tables
		cleanTestEnvironment();

	}

	@Test
	public void testGetEntriesByTag() throws ClassNotFoundException, SQLException, IOException {
		logger.info("Testing get entries by tag:");
		initTestEnvironment(); 

		//insert five users
		int numUsers = 5;
		List<UserAccount>  userList = testUserAccounts.generateAccounts(numUsers);
		long[] userIds = userTableOperations.addEntries(userList);

		//for each user, insert 10 corpora
		int numCorporaEachUser = 10;
		int numTextsEachCorpus = 100;
		int numTextsToTag = 20;
		for(long userId: userIds) {
			List<Corpus> corpusList = testCorpora.generateCorpora(userId, numCorporaEachUser);
			//insert the corpora
			long[] corpusIds = corpusTableOperations.addEntries(corpusList);
			
			//for each corpus, insert 100 texts
			for(long corpusId: corpusIds) {
				//insert texts
				List<Text> textList = testTexts.generateTexts(corpusId, null, numTextsEachCorpus);
				textTableOperations.addEntries(textList);
				
				//update text tags
				Tag tag = testTags.generateTag(corpusId);
				long insertedTagId = tagTableOperations.addEntry(tag);
				
				List<Text> texts = textTableOperations.getAllEntriesByCorpus(corpusId).subList(0, numTextsToTag);
				for(Text text: texts) {
					textTableOperations.updateTag(text.getId(), insertedTagId);
					assertEquals(tagTableOperations.getEntry(insertedTagId).getId(), 
							textTableOperations.getEntry(text.getId()).getTagId().longValue());
				}
				assertEquals(numTextsToTag, textTableOperations.getNumEntriesByTag(insertedTagId));
				assertEquals(numTextsToTag, textTableOperations.getAllEntriesByTag(insertedTagId).size());
				
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

				assertEquals(numTextsEachCorpus, textTableOperations.getNumEntriesByCorpus(corpusId));
				assertEquals(numTextsEachCorpus, textTableOperations.getAllEntriesByCorpus(corpusId).size());
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
		textTableOperations.dropTable();
		tagTableOperations.dropTable();
		corpusTableOperations.dropTable();
		userTableOperations.dropTable();
		assertFalse(textTableOperations.isTableExist());
		assertFalse(tagTableOperations.isTableExist());
		assertFalse(corpusTableOperations.isTableExist());
		assertFalse(userTableOperations.isTableExist());

		//recreate the tables
		userTableOperations.createTable();
		corpusTableOperations.createTable();
		tagTableOperations.createTable();
		textTableOperations.createTable();
		assertTrue(userTableOperations.isTableExist());
		assertTrue(corpusTableOperations.isTableExist());
		assertTrue(tagTableOperations.isTableExist());
		assertTrue(textTableOperations.isTableExist());
	}

	//droping all the test tables
	//be careful with table dependencies, drop tables that are not depended by others first.
	private void cleanTestEnvironment() throws ClassNotFoundException, SQLException, IOException {
		logger.trace("Cleaning test environment...");
		textTableOperations.dropTable();
		assertFalse(textTableOperations.isTableExist());

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

		//generate a user, 5 corpora, and a number of text entries
		int numCorpora = 5;
		UserAccount userAccount = testUserAccounts.generateAccount();
		long insertedUserId = userTableOperations.addEntry(userAccount);

		List<Corpus> corpora = testCorpora.generateCorpora(insertedUserId, numCorpora);
		long[] insertedCorpusIds = corpusTableOperations.addEntries(corpora);
		assertEquals(numCorpora, corpusTableOperations.getNumEntries());
		assertEquals(numCorpora, corpusTableOperations.getNumEntriesByOwner(insertedUserId));

		//for each corpus, generate a number of texts
		for(long corpusId: insertedCorpusIds) {
			List<Text> texts = testTexts.generateTexts(corpusId, null, numEntries);
			long[] insertedTextIds = textTableOperations.addEntries(texts);

			assertEquals(numEntries, textTableOperations.getNumEntriesByCorpus(corpusId));
			assertEquals(numEntries, textTableOperations.getAllEntriesByCorpus(corpusId).size());

			//get paged results
			Arrays.sort(insertedTextIds);
			for(int i = 0, offset = 0; i < numPages; i++, offset += limit) {
				//ids of inserted entries
				long[] idsOnPage = Arrays.copyOfRange(insertedTextIds, offset, 
						Math.min(insertedTextIds.length, offset + limit));

				logger.trace("IDs should be on page {}: {}", i, idsOnPage);

				//ids of retrieved entries of the page
				List<Text> retrievedEntriesForPage = 
						textTableOperations.getEntriesByCorpus(corpusId, textTableOperations.COLUMN_ID, limit, offset);
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
