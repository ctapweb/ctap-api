package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.ctapweb.api.db.DBConnectionManager;
import com.ctapweb.api.db.data_generators.TestCategories;
import com.ctapweb.api.db.data_generators.TestMeasures;
import com.ctapweb.api.db.pojos.Measure;
import com.ctapweb.api.db.pojos.MeasureCategory;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MeasureTableOperationsTest {
	MeasureCategoryTableOperations categoryTableOperations;
	MeasureTableOperations measureTableOperations;

	TestCategories testCategories;
	TestMeasures testMeasures;

	Logger logger = LogManager.getLogger();

	public MeasureTableOperationsTest() throws ClassNotFoundException, IOException, SQLException {
		DataSource dataSource = DBConnectionManager.getTestDataSource();

		categoryTableOperations = new MeasureCategoryTableOperations(dataSource);
		measureTableOperations = new MeasureTableOperations(dataSource);

		testCategories = new TestCategories();
		testMeasures = new TestMeasures();
	}

	@Test
	public void testTableOperations() throws ClassNotFoundException, IOException, SQLException {
		logger.info("Testing MeasureTableOperations:");

		initTestEnvironment();

		//insert one measure category
		MeasureCategory category = testCategories.generateCategory();
		long insertedCategoryId = categoryTableOperations.addEntry(category);
		assertTrue(categoryTableOperations.isCategoryExist(category.getName()));

		//insert one measure
		logger.trace("Testing inserting single entry...");
		Measure measure = testMeasures.generateMeasure(insertedCategoryId, Measure.Languages.English);
		long insertedMeasureId = measureTableOperations.addEntry(measure);

		assertEquals(insertedMeasureId, measureTableOperations.getEntry(insertedMeasureId).getId());
		assertEquals(insertedCategoryId, measureTableOperations.getEntry(insertedMeasureId).getCategoryId());
		assertEquals(measure.getName(), measureTableOperations.getEntry(insertedMeasureId).getName());
		assertEquals(measure.getDescription(), measureTableOperations.getEntry(insertedMeasureId).getDescription());

		//change one entry: update
		logger.trace("Testing updating single entry...");
		String newLanguage = Measure.Languages.French;
		measureTableOperations.updateLanguage(insertedMeasureId, newLanguage);
		assertEquals(newLanguage, measureTableOperations.getEntry(insertedMeasureId).getLanguage());

		String newMeasureName = "New measure name";
		measureTableOperations.updateName(insertedMeasureId, newMeasureName);
		assertEquals(newMeasureName, measureTableOperations.getEntry(insertedMeasureId).getName());

		String newCorpusDescription = "New measure description";
		measureTableOperations.updateDescription(insertedMeasureId, newCorpusDescription);
		assertEquals(newCorpusDescription, measureTableOperations.getEntry(insertedMeasureId).getDescription());

		measure.setLanguage(Measure.Languages.German);
		measure.setName("updated name");
		measure.setDescription("updated description");
		measureTableOperations.updateEntry(measure);
		assertEquals(measure.getLanguage(), measureTableOperations.getEntry(insertedMeasureId).getLanguage());
		assertEquals(measure.getName(), measureTableOperations.getEntry(insertedMeasureId).getName());
		assertEquals(measure.getDescription(), measureTableOperations.getEntry(insertedMeasureId).getDescription());

		//delete one entry
		logger.trace("Testing deleting single entry...");
		measureTableOperations.deleteEntry(insertedMeasureId);
		assertEquals(0, measureTableOperations.getNumEntries());
		assertNull(measureTableOperations.getEntry(insertedMeasureId));

		//insert multiple entries
		logger.trace("Testing inserting multiple entries...");
		int nEnEntries = 10;
		int nDeEntries = 20;
		int nFrEntries = 30;
		int numAllEntries = nEnEntries + nDeEntries + nFrEntries;
		List<Measure> enMeasureList = 
				testMeasures.generateMeasures(insertedCategoryId, Measure.Languages.English, nEnEntries);
		List<Measure> deMeasureList = 
				testMeasures.generateMeasures(insertedCategoryId, Measure.Languages.German, nDeEntries);
		List<Measure> frMeasureList = 
				testMeasures.generateMeasures(insertedCategoryId, Measure.Languages.French, nFrEntries);

		long[] insertedEnMeasureIds = measureTableOperations.addEntries(enMeasureList);
		long[] insertedDeMeasureIds = measureTableOperations.addEntries(deMeasureList);
		long[] insertedFrMeasureIds = measureTableOperations.addEntries(frMeasureList);

		assertEquals(numAllEntries, measureTableOperations.getAllEntries().size());
		assertEquals(numAllEntries, measureTableOperations.getNumEntries());
		assertEquals(numAllEntries, measureTableOperations.getNumEntriesByCategory(insertedCategoryId));

		assertEquals(nEnEntries, measureTableOperations.getNumEntriesByLanguage(Measure.Languages.English));
		assertEquals(nEnEntries, measureTableOperations.
				getNumEntriesByLanguageAndCategory(Measure.Languages.English, insertedCategoryId));
		assertEquals(nDeEntries, measureTableOperations.getNumEntriesByLanguage(Measure.Languages.German));
		assertEquals(nDeEntries, measureTableOperations.
				getNumEntriesByLanguageAndCategory(Measure.Languages.German, insertedCategoryId));
		assertEquals(nFrEntries, measureTableOperations.getNumEntriesByLanguage(Measure.Languages.French));
		assertEquals(nFrEntries, measureTableOperations.
				getNumEntriesByLanguageAndCategory(Measure.Languages.French, insertedCategoryId));

		//delete multiple entries: half of the inserted entries
		logger.trace("Testing deleting multiple entries...");
		int nEnEntriesToDelete = nEnEntries / 2;
		int nEnEntriesLeft = nEnEntries - nEnEntriesToDelete;
		long[] enMeasureIdsToDelete = new long[nEnEntriesToDelete];
		for(int i = 0; i < nEnEntriesToDelete; i ++) {
			enMeasureIdsToDelete[i] = insertedEnMeasureIds[i];
		}
		measureTableOperations.deleteEntries(enMeasureIdsToDelete);
		assertEquals(nEnEntriesLeft, measureTableOperations.getNumEntriesByLanguage(Measure.Languages.English));

		//delete all entries
		logger.trace("Testing deleting all entries...");
		measureTableOperations.deleteAllEntries();
		assertNull(measureTableOperations.getAllEntries());
		assertNull(measureTableOperations.getAllEntriesByCategory(insertedCategoryId));
		assertNull(measureTableOperations.getAllEntriesByLanguage(Measure.Languages.English));
		assertNull(measureTableOperations.getAllEntriesByLanguage(Measure.Languages.French));
		assertNull(measureTableOperations.getAllEntriesByLanguage(Measure.Languages.German));
		assertNull(measureTableOperations.getAllEntriesByLanguageAndCategory(Measure.Languages.English, 
				insertedCategoryId));
		assertNull(measureTableOperations.getAllEntriesByLanguageAndCategory(Measure.Languages.French, 
				insertedCategoryId));
		assertNull(measureTableOperations.getAllEntriesByLanguageAndCategory(Measure.Languages.German, 
				insertedCategoryId));

		assertEquals(0, measureTableOperations.getNumEntries());
		assertEquals(0, measureTableOperations.getNumEntriesByCategory(insertedCategoryId));
		assertEquals(0, measureTableOperations.getNumEntriesByLanguage(Measure.Languages.English));
		assertEquals(0, measureTableOperations.getNumEntriesByLanguage(Measure.Languages.French));
		assertEquals(0, measureTableOperations.getNumEntriesByLanguage(Measure.Languages.German));

		//drop the table
		logger.trace("Testing dropping the table...");
		measureTableOperations.dropTable();
		assertFalse(measureTableOperations.isTableExist());

		//finishing the test, cleaning the db
		cleanTestEnvironment();
	}

	@Test
	public void testGetEntriesByCategoriesAndLanguage() throws ClassNotFoundException, SQLException, IOException {
		logger.info("Testing get entries by categories and language:");
		initTestEnvironment(); 

		int numCategories = 5;
		int numLanguages = 3;
		int numMeasuresPerLangPerCat = 10;
		int numMeasuresPerCat = numLanguages * numMeasuresPerLangPerCat;
		int numMeasuresPerLang = numCategories * numMeasuresPerLangPerCat;
		String[] languages = {Measure.Languages.English, Measure.Languages.French, Measure.Languages.German};

		//insert measure categories
		List<MeasureCategory>  categoryList = testCategories.generateCategories(numCategories);
		long[] categoryIds = categoryTableOperations.addEntries(categoryList);

		//for each category, insert measures in 3 different languages
		for(long categoryId: categoryIds) {
			for(String lang: languages) {
				List<Measure> measureList = testMeasures.generateMeasures(categoryId, 
						lang, numMeasuresPerLangPerCat);
				measureTableOperations.addEntries(measureList);
			}
		}

		//for each category, get their measures from db
		for(long categoryId: categoryIds) {
			assertEquals(numMeasuresPerCat, measureTableOperations.getAllEntriesByCategory(categoryId).size());
			assertEquals(numMeasuresPerCat, measureTableOperations.getNumEntriesByCategory(categoryId));

			//check each measure has the same category
			for(Measure m: measureTableOperations.getAllEntriesByCategory(categoryId)) {
				assertEquals(categoryId, m.getCategoryId());
			}
		}

		//for each language, get their measures from db
		for(String lang: languages) {
			List<Measure> measures = 
					measureTableOperations.getAllEntriesByLanguage(lang);
			assertEquals(numMeasuresPerLang, measures.size());
			assertEquals(numMeasuresPerLang, measureTableOperations.getNumEntriesByLanguage(lang));

			//check each measure has the same lang
			for(Measure m: measures) {
				assertEquals(lang, m.getLanguage());
			}
		}
		
		for(String language: languages) {
			for(long categoryId: categoryIds) {
				List<Measure> measures =
						measureTableOperations.getAllEntriesByLanguageAndCategory(language, categoryId);
				assertEquals(numMeasuresPerLangPerCat, measures.size());
				assertEquals(numMeasuresPerLangPerCat, 
						measureTableOperations.getNumEntriesByLanguageAndCategory(language, categoryId));
				
				for(Measure measure: measures) {
					assertEquals(categoryId, measure.getCategoryId());
					assertEquals(language, measure.getLanguage());
				}
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
		measureTableOperations.dropTable();
		categoryTableOperations.dropTable();
		assertFalse(measureTableOperations.isTableExist());
		assertFalse(categoryTableOperations.isTableExist());

		//recreate the tables
		categoryTableOperations.createTable();
		measureTableOperations.createTable();
		assertTrue(categoryTableOperations.isTableExist());
		assertTrue(measureTableOperations.isTableExist());
	}

	//droping all the test tables
	//be careful with table dependencies, drop tables that are not depended by others first.
	private void cleanTestEnvironment() throws ClassNotFoundException, SQLException, IOException {
		logger.trace("Cleaning test environment...");
		measureTableOperations.dropTable();
		assertFalse(measureTableOperations.isTableExist());

		categoryTableOperations.dropTable();
		assertFalse(categoryTableOperations.isTableExist());

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

		//generate a measure category
		MeasureCategory measureCategory = testCategories.generateCategory();
		long insertedCategoryId = categoryTableOperations.addEntry(measureCategory);

		List<Measure> measures = testMeasures.generateMeasures(insertedCategoryId, Measure.Languages.English, numEntries);
		long[] insertedMeasureIds = measureTableOperations.addEntries(measures);
		assertEquals(numEntries, measureTableOperations.getNumEntries());

		//get paged results
		Arrays.sort(insertedMeasureIds);
		for(int i = 0, offset = 0; i < numPages; i++, offset += limit) {
			//ids of inserted entries
			long[] idsOnPage = Arrays.copyOfRange(insertedMeasureIds, offset, 
					Math.min(insertedMeasureIds.length, offset + limit));

			logger.trace("IDs should be on page {}: {}", i, idsOnPage);

			//ids of retrieved entries of the page
			List<Measure> retrievedEntriesForPage = 
					measureTableOperations.getEntriesByCategory(insertedCategoryId, measureTableOperations.COLUMN_ID, limit, offset);
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
