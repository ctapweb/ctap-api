package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.data_generators.TestCategories;
import com.ctapweb.api.db.data_generators.TestCorpora;
import com.ctapweb.api.db.data_generators.TestMeasures;
import com.ctapweb.api.db.data_generators.TestResults;
import com.ctapweb.api.db.data_generators.TestTags;
import com.ctapweb.api.db.data_generators.TestTexts;
import com.ctapweb.api.db.data_generators.TestUserAccounts;
import com.ctapweb.api.db.pojos.Corpus;
import com.ctapweb.api.db.pojos.Measure;
import com.ctapweb.api.db.pojos.MeasureCategory;
import com.ctapweb.api.db.pojos.Result;
import com.ctapweb.api.db.pojos.Tag;
import com.ctapweb.api.db.pojos.Text;
import com.ctapweb.api.db.pojos.UserAccount;

import static org.junit.Assert.assertArrayEquals;
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
	ResultTableOperations resultTableOperations;

	TestUserAccounts testUserAccounts;
	TestCorpora testCorpora;
	TestTags testTags;
	TestTexts testTexts;
	TestCategories testCategories;
	TestMeasures testMeasures;
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
		resultTableOperations = new ResultTableOperations(dataSource);


		testUserAccounts = new TestUserAccounts();
		testCorpora = new TestCorpora();
		testTags = new TestTags();
		testTexts = new TestTexts();
		testCategories = new TestCategories();
		testMeasures = new TestMeasures();
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
		assertEquals(text.getStatus(), textTableOperations.getEntry(insertedTextId).getStatus());
		assertNotNull(textTableOperations.getEntry(insertedTextId).getUpdateDate());
		
		//insert one category
		MeasureCategory category = testCategories.generateCategory();
		long insertedCategoryId = categoryTableOperations.addEntry(category);
		assertEquals(insertedCategoryId, categoryTableOperations.getEntry(insertedCategoryId).getId());
		assertEquals(category.getName(), categoryTableOperations.getEntry(insertedCategoryId).getName());
		assertEquals(category.getDescription(), categoryTableOperations.getEntry(insertedCategoryId).getDescription());
		
		//insert one measure of that category
		Measure measure = testMeasures.generateMeasure(insertedCategoryId, Measure.Languages.English);
		long insertedMeasureId = measureTableOperations.addEntry(measure);
		assertEquals(insertedMeasureId, measureTableOperations.getEntry(insertedMeasureId).getId());
		assertEquals(insertedCategoryId, measureTableOperations.getEntry(insertedMeasureId).getCategoryId());
		assertEquals(Measure.Languages.English, measureTableOperations.getEntry(insertedMeasureId).getLanguage());
		assertEquals(measure.getName(), measureTableOperations.getEntry(insertedMeasureId).getName());
		assertEquals(measure.getDescription(), measureTableOperations.getEntry(insertedMeasureId).getDescription());

		//insert result entry
		logger.trace("Testing inserting single entry...");
		Result result = testResults.generateResult(insertedTextId, insertedMeasureId);
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
			Result r = testResults.generateResult(insertedTextIds[i], insertedMeasureId);
			insertedResultIds[i] = resultTableOperations.addEntry(r);
		}
		
		assertEquals(nEntries, resultTableOperations.getAllEntries().size());
		assertEquals(nEntries, resultTableOperations.getNumEntries());
		assertEquals(nEntries, resultTableOperations.getNumEntriesByCorpus(insertedCorpusId));
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
		assertEquals(nEntriesLeft, resultTableOperations.getNumEntriesByCorpus(insertedCorpusId));
		assertEquals(nEntriesLeft, resultTableOperations.getNumEntriesByTag(insertedTagId));
		assertEquals(nEntriesLeft, resultTableOperations.getAllEntries().size());
		assertEquals(nEntriesLeft, resultTableOperations.getAllEntriesByCorpus(insertedCorpusId).size());

		//delete all entries
		logger.trace("Testing deleting all entries...");
		resultTableOperations.deleteAllEntries();
		assertNull(resultTableOperations.getAllEntries());
		assertEquals(0, resultTableOperations.getNumEntries());
		assertEquals(0, resultTableOperations.getNumEntriesByCorpus(insertedCorpusId));

		//drop the table
		logger.trace("Testing dropping the table...");
		resultTableOperations.dropTable();
		assertFalse(resultTableOperations.isTableExist());

		//finishing the test, cleaning the db
		cleanTestEnvironment();
	}

	@Test
	public void testGetEntriesBy() throws ClassNotFoundException, SQLException, IOException {
		logger.info("Testing get entries by:");
		initTestEnvironment(); 

		int numUsers = 2;
		int numCorporaEachUser = 3;
		int numTextsPerCorpus = 10;
		int numCategories = 2;
		int numMeasuresPerCategory = 4;
		int numMeasures = numCategories * numMeasuresPerCategory;

		//insert users
		List<UserAccount>  userList = testUserAccounts.generateAccounts(numUsers);
		long[] userIds = userTableOperations.addEntries(userList);

		//for each user, insert some corpora
		for(long userId: userIds) {
			List<Corpus> corpusList = testCorpora.generateCorpora(userId, numCorporaEachUser);
			//insert the corpora
			long[] corpusIds = corpusTableOperations.addEntries(corpusList);

			//for each corpus, insert some texts
			for(long corpusId: corpusIds) {
				List<Text> textList = testTexts.generateTexts(corpusId, null, numTextsPerCorpus);
				//insert the texts
				textTableOperations.addEntries(textList);
			}
		}
		
		//insert measure categories
		List<MeasureCategory> categoryList = testCategories.generateCategories(numCategories);
		long[] categoryIds = categoryTableOperations.addEntries(categoryList);
		for(long cid: categoryIds) {
			//insert some measures for each category
			List<Measure> measureList = testMeasures.generateMeasures(cid, Measure.Languages.English, numMeasuresPerCategory);
			measureTableOperations.addEntries(measureList);
		}
		
		//for each text insert a result for each measure
		Random rand = new Random();
		for(Text t: textTableOperations.getAllEntries()) {
			for(Measure m: measureTableOperations.getAllEntries()) {
				long tid = t.getId();
				long mid = m.getId();
				Result result = new Result();
				result.setTextId(tid);
				result.setMeasureId(mid);
				result.setValue(rand.nextDouble());
				
				resultTableOperations.addEntry(result);
			}
			
		}

		//test getEntries by corpus, by text, and by corpus and measure
		logger.trace("Testing get entries by corpus, text, and corpus and measure...");
		for(Corpus c: corpusTableOperations.getAllEntries()) {
			long cid = c.getId();
			assertEquals(numTextsPerCorpus, textTableOperations.getNumEntriesByCorpus(cid));
			assertEquals(numTextsPerCorpus * numMeasures, resultTableOperations.getNumEntriesByCorpus(cid));
			assertEquals(numTextsPerCorpus * numMeasures, resultTableOperations.getAllEntriesByCorpus(cid).size());
			
			//get all texts in each corpus
			for(Text t: textTableOperations.getAllEntriesByCorpus(cid)) {
				long tid = t.getId();
				assertEquals(numMeasures, resultTableOperations.getNumEntriesByText(tid));
				assertEquals(numMeasures, resultTableOperations.getAllEntriesByText(tid).size());
			}
			
			//by corpus and measure
			for(Measure m: measureTableOperations.getAllEntries()) {
				long mid = m.getId();
				assertEquals(numTextsPerCorpus, resultTableOperations.getNumEntriesByCorpusAndMeasure(cid, mid));
				assertEquals(numTextsPerCorpus, resultTableOperations.getAllEntriesByCorpusAndMeasure(cid, mid).size());
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
		resultTableOperations.dropTable();
		measureTableOperations.dropTable();
		categoryTableOperations.dropTable();
		textTableOperations.dropTable();
		tagTableOperations.dropTable();
		corpusTableOperations.dropTable();
		userTableOperations.dropTable();
		assertFalse(categoryTableOperations.isTableExist());
		assertFalse(measureTableOperations.isTableExist());
		assertFalse(resultTableOperations.isTableExist());
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
		resultTableOperations.createTable();
		assertTrue(userTableOperations.isTableExist());
		assertTrue(corpusTableOperations.isTableExist());
		assertTrue(tagTableOperations.isTableExist());
		assertTrue(textTableOperations.isTableExist());
		assertTrue(categoryTableOperations.isTableExist());
		assertTrue(measureTableOperations.isTableExist());
		assertTrue(resultTableOperations.isTableExist());
	}

	//droping all the test tables
	//be careful with table dependencies, drop tables that are not depended by others first.
	private void cleanTestEnvironment() throws ClassNotFoundException, SQLException, IOException {
		logger.trace("Cleaning test environment...");

		resultTableOperations.dropTable();
		assertFalse(resultTableOperations.isTableExist());

		measureTableOperations.dropTable();
		assertFalse(measureTableOperations.isTableExist());

		categoryTableOperations.dropTable();
		assertFalse(categoryTableOperations.isTableExist());
		
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
		
		MeasureCategory category = testCategories.generateCategory();
		long insertedCategoryId = categoryTableOperations.addEntry(category);
		assertEquals(insertedCategoryId, categoryTableOperations.getEntry(insertedCategoryId).getId());

		Measure measure = testMeasures.generateMeasure(insertedCategoryId, Measure.Languages.English);
		long insertedMeasureId = measureTableOperations.addEntry(measure);
		assertEquals(insertedMeasureId, measureTableOperations.getEntry(insertedMeasureId).getId());


		//for each corpus, generate a number of texts
		for(long corpusId: insertedCorpusIds) {
			List<Text> texts = testTexts.generateTexts(corpusId, null, numEntries);
			long[] insertedTextIds = textTableOperations.addEntries(texts);

			assertEquals(numEntries, textTableOperations.getNumEntriesByCorpus(corpusId));
			assertEquals(numEntries, textTableOperations.getAllEntriesByCorpus(corpusId).size());
			
			//for each text, generate a result
			for(long tid: insertedTextIds) {
				Result result = testResults.generateResult(tid, insertedMeasureId);
				long insertedResultId = resultTableOperations.addEntry(result);
				assertEquals(insertedResultId, resultTableOperations.getEntry(insertedResultId).getId());
			}
			
			assertEquals(numEntries, resultTableOperations.getAllEntriesByCorpus(corpusId).size());
			assertEquals(numEntries, resultTableOperations.getNumEntriesByCorpus(corpusId));

			
			//get all result entry ids
			long[] resultIds = new long[numEntries];
			List<Result> resultList = resultTableOperations.getAllEntriesByCorpus(corpusId);
			for(int i = 0; i < resultList.size(); i++) {
				resultIds[i] = resultList.get(i).getId();
			}
			
			
			//get paged results
			Arrays.sort(resultIds);
			for(int i = 0, offset = 0; i < numPages; i++, offset += limit) {
				//ids of inserted entries
				long[] idsOnPage = Arrays.copyOfRange(resultIds, offset, 
						Math.min(resultIds.length, offset + limit));

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
