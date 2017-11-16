package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.data_generators.TestUserAccounts;
import com.ctapweb.api.db.pojos.UserAccount;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserTableOperationsTest {
	UserTableOperations userTableOperations;
	TestUserAccounts testUserAccounts;
	Logger logger = LogManager.getLogger();
	

	public UserTableOperationsTest() throws ClassNotFoundException, IOException, SQLException {
		userTableOperations = new UserTableOperations(DataSourceManager.getTestDataSource());
		testUserAccounts = new TestUserAccounts();
	}

	@Test
	public void testTableOperations() throws ClassNotFoundException, IOException, SQLException {
		logger.info("Testing UserTableOperations:");
		//clean up. drop the table if exists
		recreateUserTable();
		
		//insert one entry
		logger.trace("Testing inserting single entry...");
		UserAccount userAccount = testUserAccounts.generateAccount();
		long insertedUserId = userTableOperations.addEntry(userAccount);
		
		assertEquals(insertedUserId, userTableOperations.getEntry(insertedUserId).getId());
		assertTrue(userTableOperations.isAccountExist(userAccount.getEmail()));
		assertEquals(userAccount.getFirstName(), userTableOperations.getEntry(insertedUserId).getFirstName());
		assertEquals(userAccount.getLastName(), userTableOperations.getEntry(insertedUserId).getLastName());
		assertEquals(userAccount.getInstitution(), userTableOperations.getEntry(insertedUserId).getInstitution());
		assertEquals(userAccount.getEmail(), userTableOperations.getEntry(insertedUserId).getEmail());
		assertTrue(BCrypt.checkpw(userAccount.getPasswd(), userTableOperations.getEntry(insertedUserId).getPasswd()));
		assertNotNull(userTableOperations.getEntry(insertedUserId).getCreateDate());

		//test check passwd
		logger.trace("Testing checking passwd...");
		assertTrue(userTableOperations.checkPasswd(userAccount.getEmail(), userAccount.getPasswd()));
		assertFalse(userTableOperations.checkPasswd(userAccount.getEmail(), "Not the right passwd"));
			
		//change one entry: update
		logger.trace("Testing updating single entry...");
		String newFirstName = "NewFirstName";
		userTableOperations.updateFirstName(insertedUserId, newFirstName);
		assertEquals(newFirstName, userTableOperations.getEntry(insertedUserId).getFirstName());
		
		String newLastName = "NewLastName";
		userTableOperations.updateLastName(insertedUserId, newLastName);
		assertEquals(newLastName, userTableOperations.getEntry(insertedUserId).getLastName());
		
		String newInstitution = "NewInstitution";
		userTableOperations.updateInstitution(insertedUserId, newInstitution);
		assertEquals(newInstitution, userTableOperations.getEntry(insertedUserId).getInstitution());
		
		String newPasswd = "newPassWord";
		userTableOperations.updatePasswd(insertedUserId, newPasswd);
		assertTrue(BCrypt.checkpw(newPasswd, userTableOperations.getEntry(insertedUserId).getPasswd()));
		
		userTableOperations.updateLastLogin();
		assertNotNull(userTableOperations.getEntry(insertedUserId).getLastLogin());
		assertNotEquals(userAccount.getLastLogin(), userTableOperations.getEntry(insertedUserId).getLastLogin());
		

		
		//test reset passwd
		logger.trace("Testing reseting passwd...");
		String resetPasswd = userTableOperations.resetPasswd(insertedUserId);
		assertTrue(BCrypt.checkpw(resetPasswd, userTableOperations.getEntry(insertedUserId).getPasswd()));
		
		//delete one entry
		logger.trace("Testing deleting single entry...");
		userTableOperations.deleteEntry(insertedUserId);
		assertFalse(userTableOperations.isAccountExist(userAccount.getEmail()));
		assertEquals(0, userTableOperations.getNumEntries());

		//insert multiple entries
		logger.trace("Testing inserting multiple entries...");
		int nEntries = 10;
		List<UserAccount> userAccounts = testUserAccounts.generateAccounts(nEntries);
		long[] insertedUserIds = userTableOperations.addEntries(userAccounts);
		
		assertEquals(nEntries, userTableOperations.getAllEntries().size());
		for(UserAccount account: userAccounts) {
			assertTrue(userTableOperations.isAccountExist(account.getEmail()));
		}
		
		//delete multiple entries: half of the inserted entries
		logger.trace("Testing deleting multiple entries...");
		int nEntriesToDelete = nEntries / 2;
		int nEntriesLeft = nEntries - nEntriesToDelete;
		long[] userIdsToDelete = new long[nEntriesToDelete];
		for(int i = 0; i < nEntriesToDelete; i ++) {
			userIdsToDelete[i] = insertedUserIds[i];
		}
		userTableOperations.deleteEntries(userIdsToDelete);
		assertEquals(nEntriesLeft, userTableOperations.getNumEntries());
		
		//delete all entries
		logger.trace("Testing deleting all entries...");
		userTableOperations.deleteAllEntries();
		assertNull(userTableOperations.getAllEntries());
		assertEquals(0, userTableOperations.getNumEntries());
		
		//drop the table
		logger.trace("Testing dropping the table...");
		userTableOperations.dropTable();
		assertFalse(userTableOperations.isTableExist());

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
	
	private void recreateUserTable() throws ClassNotFoundException, SQLException, IOException {
		//clean up. drop the table if exists and recreate it.
		userTableOperations.dropTable();
		assertFalse(userTableOperations.isTableExist());
		
		userTableOperations.createTable();
		assertTrue(userTableOperations.isTableExist());
		
	}
	
	private void runPagedQuery(int numEntries, int limit) throws SQLException, ClassNotFoundException, IOException {
		recreateUserTable();

		int numPages = (int) Math.ceil((double)numEntries / limit);

		logger.trace("numEntries: {}, limit: {}, numPages:{}", numEntries, limit, numPages);
		
		List<UserAccount> userAccounts = testUserAccounts.generateAccounts(numEntries);
		long[] insertedIds = userTableOperations.addEntries(userAccounts);
		assertEquals(numEntries, userTableOperations.getNumEntries());

		//get paged results
		Arrays.sort(insertedIds);
		for(int i = 0, offset = 0; i < numPages; i++, offset += limit) {
			//ids of inserted entries
			long[] idsOnPage = Arrays.copyOfRange(insertedIds, offset, 
					Math.min(insertedIds.length, offset + limit));
			
			logger.trace("IDs should be on page {}: {}", i, idsOnPage);

			//ids of retrieved entries of the page
			List<UserAccount> retrievedEntriesForPage = 
					userTableOperations.getEntries(userTableOperations.COLUMN_ID, limit, offset);
			int numEntriesRetrieved = retrievedEntriesForPage.size();
			long[] retrievedEntryIds = new long[numEntriesRetrieved];
			for(int j = 0; j < numEntriesRetrieved; j++) {
				retrievedEntryIds[j] = retrievedEntriesForPage.get(j).getId();
			}
			
			logger.trace("Retrieved IDs on page {}: {}", i, retrievedEntryIds);

			assertArrayEquals(idsOnPage, retrievedEntryIds);
		}
		
		//drop the table
		userTableOperations.dropTable();
		assertFalse(userTableOperations.isTableExist());		
		
	}
	

}
