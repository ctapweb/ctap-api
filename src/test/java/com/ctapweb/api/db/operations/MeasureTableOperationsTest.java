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
import com.ctapweb.api.db.data_generators.TestCategories;
import com.ctapweb.api.db.data_generators.TestMeasures;
import com.ctapweb.api.db.pojos.Measure;
import com.ctapweb.api.db.pojos.MeasureCategory;
import com.ctapweb.api.measures.annotations.MeasureCategory.Languages;
import com.ctapweb.api.measures.annotations.MeasureCategory.Pipelines;

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
		DataSource dataSource = DataSourceManager.getTestDataSource();

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
		assertTrue(categoryTableOperations.isCategoryExist(category.getName(), category.getLanguage()));

		//insert one measure
		logger.trace("Testing inserting single entry...");
		Measure measure = testMeasures.generateMeasure(insertedCategoryId);
		long insertedMeasureId = measureTableOperations.addEntry(measure);

		assertEquals(insertedMeasureId, measureTableOperations.getEntry(insertedMeasureId).getId());
		assertEquals(insertedCategoryId, measureTableOperations.getEntry(insertedMeasureId).getCategoryId());
		assertEquals(measure.getName(), measureTableOperations.getEntry(insertedMeasureId).getName());
		assertEquals(measure.getDescription(), measureTableOperations.getEntry(insertedMeasureId).getDescription());

		//change one entry: update
		logger.trace("Testing updating single entry...");
		String newMeasureName = "New measure name";
		measureTableOperations.updateName(insertedMeasureId, newMeasureName);
		assertEquals(newMeasureName, measureTableOperations.getEntry(insertedMeasureId).getName());

		String newMeasureDescription = "New measure description";
		measureTableOperations.updateDescription(insertedMeasureId, newMeasureDescription);
		assertEquals(newMeasureDescription, measureTableOperations.getEntry(insertedMeasureId).getDescription());

		category.setLanguage(Languages.GERMAN);
		insertedCategoryId = categoryTableOperations.addEntry(category);
		measureTableOperations.updateCategoryId(insertedMeasureId, insertedCategoryId);
		assertEquals(insertedCategoryId, measureTableOperations.getEntry(insertedMeasureId).getCategoryId());
		
		//update info of the whole entry
		measure.setCategoryId(insertedCategoryId);
		measure.setName("updated name");
		measure.setDescription("updated description");
		measureTableOperations.updateEntry(measure);
		assertEquals(measure.getCategoryId(), measureTableOperations.getEntry(insertedMeasureId).getCategoryId());
		assertEquals(measure.getName(), measureTableOperations.getEntry(insertedMeasureId).getName());
		assertEquals(measure.getDescription(), measureTableOperations.getEntry(insertedMeasureId).getDescription());

		//delete one entry
		logger.trace("Testing deleting single entry...");
		measureTableOperations.deleteEntry(insertedMeasureId);
		assertEquals(0, measureTableOperations.getNumEntries());
		assertNull(measureTableOperations.getEntry(insertedMeasureId));
		
		categoryTableOperations.deleteAllEntries();

		//insert multiple entries
		logger.trace("Testing inserting multiple entries...");
		int nEnEntries = 10;
		int nDeEntries = 20;
		int nFrEntries = 30;
		int numAllEntries = nEnEntries + nDeEntries + nFrEntries;
		
		//insert 3 categories for three languages
		MeasureCategory enCategory = testCategories.generateCategory();
		enCategory.setLanguage(Languages.ENGLISH);
		long insertedEnCategoryId = categoryTableOperations.addEntry(enCategory);

		MeasureCategory deCategory = testCategories.generateCategory();
		deCategory.setLanguage(Languages.GERMAN);
		long insertedDeCategoryId = categoryTableOperations.addEntry(deCategory);

		MeasureCategory frCategory = testCategories.generateCategory();
		frCategory.setLanguage(Languages.FRENCH);
		long insertedFrCategoryId = categoryTableOperations.addEntry(frCategory);

		List<Measure> enMeasureList = 
				testMeasures.generateMeasures(insertedEnCategoryId, nEnEntries);
		List<Measure> deMeasureList = 
				testMeasures.generateMeasures(insertedDeCategoryId, nDeEntries);
		List<Measure> frMeasureList = 
				testMeasures.generateMeasures(insertedFrCategoryId, nFrEntries);

		long[] insertedEnMeasureIds = measureTableOperations.addEntries(enMeasureList);
		long[] insertedDeMeasureIds = measureTableOperations.addEntries(deMeasureList);
		long[] insertedFrMeasureIds = measureTableOperations.addEntries(frMeasureList);

		assertEquals(numAllEntries, measureTableOperations.getAllEntries().size());
		assertEquals(numAllEntries, measureTableOperations.getNumEntries());

		assertEquals(nEnEntries, measureTableOperations.getNumEntriesByLanguage(Languages.ENGLISH));
		assertEquals(nDeEntries, measureTableOperations.getNumEntriesByLanguage(Languages.GERMAN));
		assertEquals(nFrEntries, measureTableOperations.getNumEntriesByLanguage(Languages.FRENCH));

		assertEquals(nEnEntries, measureTableOperations.
				getNumEntriesByLanguageAndCategory(Languages.ENGLISH, insertedEnCategoryId));
		assertEquals(nDeEntries, measureTableOperations.
				getNumEntriesByLanguageAndCategory(Languages.GERMAN, insertedDeCategoryId));
		assertEquals(nFrEntries, measureTableOperations.
				getNumEntriesByLanguageAndCategory(Languages.FRENCH, insertedFrCategoryId));

		//delete multiple entries: half of the inserted entries
		logger.trace("Testing deleting multiple entries...");
		int nEnEntriesToDelete = nEnEntries / 2;
		int nEnEntriesLeft = nEnEntries - nEnEntriesToDelete;
		long[] enMeasureIdsToDelete = new long[nEnEntriesToDelete];
		for(int i = 0; i < nEnEntriesToDelete; i ++) {
			enMeasureIdsToDelete[i] = insertedEnMeasureIds[i];
		}
		measureTableOperations.deleteEntries(enMeasureIdsToDelete);
		assertEquals(nEnEntriesLeft, measureTableOperations.getNumEntriesByLanguage(Languages.ENGLISH));
		assertEquals(nEnEntriesLeft, measureTableOperations.getAllEntriesByLanguage(Languages.ENGLISH).size());
		assertEquals(nEnEntriesLeft, measureTableOperations.getAllEntriesByLanguageAndCategory(Languages.ENGLISH, insertedEnCategoryId).size());

		int nDeEntriesToDelete = nDeEntries / 2;
		int nDeEntriesLeft = nDeEntries - nDeEntriesToDelete;
		long[] deMeasureIdsToDelete = new long[nDeEntriesToDelete];
		for(int i = 0; i < nDeEntriesToDelete; i ++) {
			deMeasureIdsToDelete[i] = insertedDeMeasureIds[i];
		}
		measureTableOperations.deleteEntries(deMeasureIdsToDelete);
		assertEquals(nDeEntriesLeft, measureTableOperations.getNumEntriesByLanguage(Languages.GERMAN));
		assertEquals(nDeEntriesLeft, measureTableOperations.getAllEntriesByLanguage(Languages.GERMAN).size());
		assertEquals(nDeEntriesLeft, measureTableOperations.getAllEntriesByLanguageAndCategory(Languages.GERMAN, insertedDeCategoryId).size());

		int nFrEntriesToDelete = nFrEntries / 2;
		int nFrEntriesLeft = nFrEntries - nFrEntriesToDelete;
		long[] frMeasureIdsToDelete = new long[nFrEntriesToDelete];
		for(int i = 0; i < nFrEntriesToDelete; i ++) {
			frMeasureIdsToDelete[i] = insertedFrMeasureIds[i];
		}
		measureTableOperations.deleteEntries(frMeasureIdsToDelete);
		assertEquals(nFrEntriesLeft, measureTableOperations.getNumEntriesByLanguage(Languages.FRENCH));
		assertEquals(nFrEntriesLeft, measureTableOperations.getAllEntriesByLanguage(Languages.FRENCH).size());
		assertEquals(nFrEntriesLeft, measureTableOperations.getAllEntriesByLanguageAndCategory(Languages.FRENCH, insertedFrCategoryId).size());

		//delete all entries
		logger.trace("Testing deleting all entries...");
		measureTableOperations.deleteAllEntries();
		assertNull(measureTableOperations.getAllEntries());
		assertNull(measureTableOperations.getAllEntriesByCategory(insertedEnCategoryId));
		assertNull(measureTableOperations.getAllEntriesByCategory(insertedDeCategoryId));
		assertNull(measureTableOperations.getAllEntriesByCategory(insertedFrCategoryId));
		assertNull(measureTableOperations.getAllEntriesByLanguage(Languages.ENGLISH));
		assertNull(measureTableOperations.getAllEntriesByLanguage(Languages.FRENCH));
		assertNull(measureTableOperations.getAllEntriesByLanguage(Languages.GERMAN));
		assertNull(measureTableOperations.getAllEntriesByLanguageAndCategory(Languages.ENGLISH, 
				insertedCategoryId));
		assertNull(measureTableOperations.getAllEntriesByLanguageAndCategory(Languages.FRENCH, 
				insertedCategoryId));
		assertNull(measureTableOperations.getAllEntriesByLanguageAndCategory(Languages.GERMAN, 
				insertedCategoryId));

		assertEquals(0, measureTableOperations.getNumEntries());
		assertEquals(0, measureTableOperations.getNumEntriesByCategory(insertedCategoryId));
		assertEquals(0, measureTableOperations.getNumEntriesByLanguage(Languages.ENGLISH));
		assertEquals(0, measureTableOperations.getNumEntriesByLanguage(Languages.FRENCH));
		assertEquals(0, measureTableOperations.getNumEntriesByLanguage(Languages.GERMAN));
		assertEquals(0, measureTableOperations.getNumEntriesByLanguageAndCategory(Languages.ENGLISH, insertedCategoryId));
		assertEquals(0, measureTableOperations.getNumEntriesByLanguageAndCategory(Languages.GERMAN, insertedCategoryId));
		assertEquals(0, measureTableOperations.getNumEntriesByLanguageAndCategory(Languages.FRENCH, insertedCategoryId));

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
		int numTotalMeasures = numCategories * numLanguages * numMeasuresPerLangPerCat;
		String[] languages = {Languages.ENGLISH, Languages.FRENCH, Languages.GERMAN};

		//for each language, insert the categories
		for(String language: languages) {
			List<MeasureCategory>  categoryList = 
					testCategories.generateCategories(language, Pipelines.COMPLETE_PIPELINE, numCategories);
			categoryTableOperations.addEntries(categoryList);
			assertEquals(numCategories, categoryTableOperations.getNumEntriesByLanguage(language));
			assertEquals(numCategories, categoryTableOperations.getAllEntriesByLanguage(language).size());
		}

		//for each language,  insert some measures for each category
		for(String language: languages) {
			for(MeasureCategory cat: categoryTableOperations.getAllEntriesByLanguage(language)) {
				List<Measure> measureList = testMeasures.generateMeasures(cat.getId(), numMeasuresPerLangPerCat);
				measureTableOperations.addEntries(measureList);
			}
		}
		assertEquals(numTotalMeasures, measureTableOperations.getNumEntries());
		assertEquals(numTotalMeasures, measureTableOperations.getAllEntries().size());

		
		//for each language, test num measures
		for(String language: languages) {
			assertEquals(numMeasuresPerLang, measureTableOperations.getNumEntriesByLanguage(language));
			for(MeasureCategory cat: categoryTableOperations.getAllEntriesByLanguage(language)) {
				long catId = cat.getId();
				assertEquals(numMeasuresPerLangPerCat, 
						measureTableOperations.getNumEntriesByLanguageAndCategory(language, catId));
				assertEquals(numMeasuresPerLangPerCat, 
						measureTableOperations.getAllEntriesByLanguageAndCategory(language, catId).size());
				assertEquals(numMeasuresPerLangPerCat, 
						measureTableOperations.getNumEntriesByCategory(catId));
				assertEquals(numMeasuresPerLangPerCat, 
						measureTableOperations.getAllEntriesByCategory(catId).size());
				assertEquals(language, cat.getLanguage());

				//for specific measure entry
				for(Measure m: measureTableOperations.getAllEntriesByLanguageAndCategory(language, catId)) {
					assertEquals(catId, m.getCategoryId());
				}
				
			}
			assertEquals(numMeasuresPerLang, 
					measureTableOperations.getNumEntriesByLanguage(language));
			assertEquals(numMeasuresPerLang, 
					measureTableOperations.getAllEntriesByLanguage(language).size());
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

		List<Measure> measures = testMeasures.generateMeasures(insertedCategoryId, numEntries);
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
