package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.ctapweb.api.db.DBConnectionManager;
import com.ctapweb.api.db.data_generators.TestCategories;
import com.ctapweb.api.db.pojos.MeasureCategory;

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
		measureCategoryTableOperations = new MeasureCategoryTableOperations(DBConnectionManager.getTestDataSource());
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
		assertTrue(measureCategoryTableOperations.isCategoryExist(category.getName()));
		assertEquals(category.getName(), measureCategoryTableOperations.getEntry(insertedCategoryId).getName());
		assertEquals(category.getDescription(), measureCategoryTableOperations.getEntry(insertedCategoryId).getDescription());

		//change one entry: update
		logger.trace("Testing updating single entry...");
		String newCategoryName = "New category name";
		measureCategoryTableOperations.updateName(insertedCategoryId, newCategoryName);
		assertEquals(newCategoryName, measureCategoryTableOperations.getEntry(insertedCategoryId).getName());
		
		String newDescription = "New category description";
		measureCategoryTableOperations.updateDescription(insertedCategoryId, newDescription);
		assertEquals(newDescription, measureCategoryTableOperations.getEntry(insertedCategoryId).getDescription());

		//delete one entry
		logger.trace("Testing deleting single entry...");
		measureCategoryTableOperations.deleteEntry(insertedCategoryId);
		assertFalse(measureCategoryTableOperations.isCategoryExist(category.getName()));
		assertEquals(0, measureCategoryTableOperations.getNumEntries());

		//insert multiple entries
		logger.trace("Testing inserting multiple entries...");
		int nEntries = 10;
		List<MeasureCategory> categories = testCategories.generateCategories(nEntries);
		long[] insertedCategoryIds = measureCategoryTableOperations.addEntries(categories);
		
		assertEquals(nEntries, measureCategoryTableOperations.getAllEntries().size());
		for(MeasureCategory cat: categories) {
			assertTrue(measureCategoryTableOperations.isCategoryExist(cat.getName()));
		}
		
		//delete multiple entries: half of the inserted entries
		logger.trace("Testing deleting multiple entries...");
		int nEntriesToDelete = nEntries / 2;
		int nEntriesLeft = nEntries - nEntriesToDelete;
		long[] categoryIdsToDelete = new long[nEntriesToDelete];
		for(int i = 0; i < nEntriesToDelete; i ++) {
			categoryIdsToDelete[i] = insertedCategoryIds[i];
		}
		measureCategoryTableOperations.deleteEntries(categoryIdsToDelete);
		assertEquals(nEntriesLeft, measureCategoryTableOperations.getNumEntries());
		
		//delete all entries
		logger.trace("Testing deleting all entries...");
		measureCategoryTableOperations.deleteAllEntries();
		assertNull(measureCategoryTableOperations.getAllEntries());
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
		
		List<MeasureCategory> userAccounts = testCategories.generateCategories(numEntries);
		long[] insertedIds = measureCategoryTableOperations.addEntries(userAccounts);
		assertEquals(numEntries, measureCategoryTableOperations.getNumEntries());

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
