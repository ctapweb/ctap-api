package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.data_generators.TestCategories;
import com.ctapweb.api.db.pojos.MeasureCategory;
import com.ctapweb.api.measures.annotations.MeasureCategory.Languages;
import com.ctapweb.api.measures.annotations.MeasureCategory.Pipelines;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MeasureCategoryTableOperationsTest {
	MeasureCategoryTableOperations measureCategoryTableOperations;
	TestCategories testCategories;
	Logger logger = LogManager.getLogger();
	

	public MeasureCategoryTableOperationsTest() throws ClassNotFoundException, IOException, SQLException {
		measureCategoryTableOperations = new MeasureCategoryTableOperations(DataSourceManager.getTestDataSource());
		testCategories = new TestCategories();
	}

	@Test
	public void testTableOperations() throws ClassNotFoundException, IOException, SQLException {
		logger.info("Testing MeasureCategoryTableOperations:");
		//clean up. drop the table if exists
		recreateMeasureCategoryTable();
		
		//insert one entry
		logger.trace("Testing inserting single entry...");
		MeasureCategory category = testCategories.generateCategory();
		long insertedCategoryId = measureCategoryTableOperations.addEntry(category);
		
		assertEquals(insertedCategoryId, measureCategoryTableOperations.getEntry(insertedCategoryId).getId());
		assertTrue(measureCategoryTableOperations.isCategoryExist(category.getName(), category.getLanguage()));
		assertEquals(category.getName(), measureCategoryTableOperations.getEntry(insertedCategoryId).getName());
		assertEquals(category.getDescription(), measureCategoryTableOperations.getEntry(insertedCategoryId).getDescription());
		assertEquals(category.getLanguage(), measureCategoryTableOperations.getEntry(insertedCategoryId).getLanguage());
		assertEquals(category.getRequiredPipeline(), measureCategoryTableOperations.getEntry(insertedCategoryId).getRequiredPipeline());
		assertEquals(category.getClassName(), measureCategoryTableOperations.getEntry(insertedCategoryId).getClassName());

		//change one entry: update
		logger.trace("Testing updating single entry...");
		String newCategoryName = "New category name";
		measureCategoryTableOperations.updateName(insertedCategoryId, newCategoryName);
		assertEquals(newCategoryName, measureCategoryTableOperations.getEntry(insertedCategoryId).getName());
		
		String newDescription = "New category description";
		measureCategoryTableOperations.updateDescription(insertedCategoryId, newDescription);
		assertEquals(newDescription, measureCategoryTableOperations.getEntry(insertedCategoryId).getDescription());

		String newLanguage = Languages.GERMAN;
		measureCategoryTableOperations.updateLanguage(insertedCategoryId, newLanguage);
		assertEquals(newLanguage, measureCategoryTableOperations.getEntry(insertedCategoryId).getLanguage());

		String newPipeline = Pipelines.LEMMATIZER_PIPELINE;
		measureCategoryTableOperations.updateRequiredPipeline(insertedCategoryId, newPipeline);
		assertEquals(newPipeline, measureCategoryTableOperations.getEntry(insertedCategoryId).getRequiredPipeline());

		String newClassName = "new class name";
		measureCategoryTableOperations.updateClassName(insertedCategoryId, newClassName);
		assertEquals(newClassName, measureCategoryTableOperations.getEntry(insertedCategoryId).getClassName());

		//delete one entry
		logger.trace("Testing deleting single entry...");
		measureCategoryTableOperations.deleteEntry(insertedCategoryId);
		assertFalse(measureCategoryTableOperations.isCategoryExist(category.getName(), newLanguage));
		assertEquals(0, measureCategoryTableOperations.getNumEntries());

		//insert multiple entries
		logger.trace("Testing inserting multiple entries...");
		int enEntries = 10;
		int deEntries = 20;
		int frEntries = 30;
		int totalEntries = enEntries + deEntries + frEntries;

		List<MeasureCategory> enCategories = testCategories.generateCategories(Languages.ENGLISH, Pipelines.TOKENIZER_PIPELINE, enEntries);
		List<MeasureCategory> deCategories = testCategories.generateCategories(Languages.GERMAN, Pipelines.LEMMATIZER_PIPELINE, deEntries);
		List<MeasureCategory> frCategories = testCategories.generateCategories(Languages.FRENCH, Pipelines.PARSER_PIPELINE, frEntries);

		long[] insertedEnCategoryIds = measureCategoryTableOperations.addEntries(enCategories);
		long[] insertedDeCategoryIds = measureCategoryTableOperations.addEntries(deCategories);
		long[] insertedFrCategoryIds = measureCategoryTableOperations.addEntries(frCategories);
		
		assertEquals(totalEntries, measureCategoryTableOperations.getAllEntries().size());

		for(MeasureCategory cat: enCategories) {
			assertTrue(measureCategoryTableOperations.isCategoryExist(cat.getName(), Languages.ENGLISH));
		}

		for(MeasureCategory cat: deCategories) {
			assertTrue(measureCategoryTableOperations.isCategoryExist(cat.getName(), Languages.GERMAN));
		}

		for(MeasureCategory cat: frCategories) {
			assertTrue(measureCategoryTableOperations.isCategoryExist(cat.getName(), Languages.FRENCH));
		}
		
		//delete multiple entries: half of the inserted entries
		logger.trace("Testing deleting multiple entries...");
		int nEnEntriesToDelete = enEntries / 2;
		int nEnEntriesLeft = enEntries - nEnEntriesToDelete;
		long[] enCategoryIdsToDelete = new long[nEnEntriesToDelete];
		for(int i = 0; i < nEnEntriesToDelete; i ++) {
			enCategoryIdsToDelete[i] = insertedEnCategoryIds[i];
		}
		measureCategoryTableOperations.deleteEntries(enCategoryIdsToDelete);
		assertEquals(nEnEntriesLeft, measureCategoryTableOperations.getNumEntriesByLanguage(Languages.ENGLISH));
		
		int nDeEntriesToDelete = deEntries / 2;
		int nDeEntriesLeft = deEntries - nDeEntriesToDelete;
		long[] deCategoryIdsToDelete = new long[nDeEntriesToDelete];
		for(int i = 0; i < nDeEntriesToDelete; i ++) {
			deCategoryIdsToDelete[i] = insertedDeCategoryIds[i];
		}
		measureCategoryTableOperations.deleteEntries(deCategoryIdsToDelete);
		assertEquals(nDeEntriesLeft, measureCategoryTableOperations.getNumEntriesByLanguage(Languages.GERMAN));

		int nFrEntriesToDelete = frEntries / 2;
		int nFrEntriesLeft = frEntries - nFrEntriesToDelete;
		long[] frCategoryIdsToDelete = new long[nFrEntriesToDelete];
		for(int i = 0; i < nFrEntriesToDelete; i ++) {
			frCategoryIdsToDelete[i] = insertedFrCategoryIds[i];
		}
		measureCategoryTableOperations.deleteEntries(frCategoryIdsToDelete);
		assertEquals(nFrEntriesLeft, measureCategoryTableOperations.getNumEntriesByLanguage(Languages.FRENCH));

		//delete all entries
		logger.trace("Testing deleting all entries...");
		measureCategoryTableOperations.deleteAllEntries();
		assertNull(measureCategoryTableOperations.getAllEntries());
		assertEquals(0, measureCategoryTableOperations.getNumEntriesByLanguage(Languages.ENGLISH));
		assertEquals(0, measureCategoryTableOperations.getNumEntriesByLanguage(Languages.GERMAN));
		assertEquals(0, measureCategoryTableOperations.getNumEntriesByLanguage(Languages.FRENCH));
		assertNull(measureCategoryTableOperations.getAllEntriesByLanguage(Languages.ENGLISH));
		assertNull(measureCategoryTableOperations.getAllEntriesByLanguage(Languages.GERMAN));
		assertNull(measureCategoryTableOperations.getAllEntriesByLanguage(Languages.FRENCH));
		assertEquals(0, measureCategoryTableOperations.getNumEntries());
		
		//drop the table
		logger.trace("Testing dropping the table...");
		measureCategoryTableOperations.dropTable();
		assertFalse(measureCategoryTableOperations.isTableExist());

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
	
	private void recreateMeasureCategoryTable() throws ClassNotFoundException, SQLException, IOException {
		//clean up. drop the table if exists and recreate it.
		measureCategoryTableOperations.dropTable();
		assertFalse(measureCategoryTableOperations.isTableExist());
		
		measureCategoryTableOperations.createTable();
		assertTrue(measureCategoryTableOperations.isTableExist());
		
	}
	
	private void runPagedQuery(int numEntries, int limit) throws SQLException, ClassNotFoundException, IOException {
		recreateMeasureCategoryTable();

		int numPages = (int) Math.ceil((double)numEntries / limit);

		logger.trace("numEntries: {}, limit: {}, numPages:{}", numEntries, limit, numPages);
		
		List<MeasureCategory> categoryList = 
				testCategories.generateCategories(Languages.ENGLISH, Pipelines.COMPLETE_PIPELINE, numEntries);
		long[] insertedIds = measureCategoryTableOperations.addEntries(categoryList);
		assertEquals(numEntries, measureCategoryTableOperations.getNumEntries());
		assertEquals(numEntries, measureCategoryTableOperations.getNumEntriesByLanguage(Languages.ENGLISH));

		//get paged results
		Arrays.sort(insertedIds);
		for(int i = 0, offset = 0; i < numPages; i++, offset += limit) {
			//ids of inserted entries
			long[] idsOnPage = Arrays.copyOfRange(insertedIds, offset, 
					Math.min(insertedIds.length, offset + limit));
			
			logger.trace("IDs should be on page {}: {}", i, idsOnPage);

			//ids of retrieved entries of the page
			List<MeasureCategory> retrievedEntriesForPage = 
					measureCategoryTableOperations.getEntries(measureCategoryTableOperations.COLUMN_ID, limit, offset);
			int numEntriesRetrieved = retrievedEntriesForPage.size();
			long[] retrievedEntryIds = new long[numEntriesRetrieved];
			for(int j = 0; j < numEntriesRetrieved; j++) {
				retrievedEntryIds[j] = retrievedEntriesForPage.get(j).getId();
			}
			
			logger.trace("Retrieved IDs on page {}: {}", i, retrievedEntryIds);

			assertArrayEquals(idsOnPage, retrievedEntryIds);
		}
		
		//drop the table
		measureCategoryTableOperations.dropTable();
		assertFalse(measureCategoryTableOperations.isTableExist());		
	}
	

}
