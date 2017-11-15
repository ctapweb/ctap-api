package com.ctapweb.api.servlets.admin;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.ctapweb.api.db.DBConnectionManager;
import com.ctapweb.api.db.TableNames;

public class AdminOperationsTest {
	
	private TableNames tableNames ;
	private Connection conn;
	private AdminOperations adminOperations;

	public AdminOperationsTest() throws IOException, ClassNotFoundException, SQLException {
		tableNames = new TableNames();
		conn = DBConnectionManager.getDbConnectionForTest();
		adminOperations = new AdminOperations(conn);
	}
	
	
	@Test
	public void testInitDBandDropTables() throws SQLException, ClassNotFoundException, IOException {
		//create tables
		adminOperations.initDB();

		//check if tables exist
		assertTrue(isTableExist(tableNames.getUserTableName()));
		assertTrue(isTableExist(tableNames.getCorpusTableName()));
		assertTrue(isTableExist(tableNames.getTextTableName()));
		assertTrue(isTableExist(tableNames.getTagTableName()));
		assertTrue(isTableExist(tableNames.getTaTeTableName()));
		assertTrue(isTableExist(tableNames.getResultTableName()));
		
		//drop tables
		adminOperations.dropTables();

		//check if tables exist
		assertFalse(isTableExist(tableNames.getUserTableName()));
		assertFalse(isTableExist(tableNames.getCorpusTableName()));
		assertFalse(isTableExist(tableNames.getTextTableName()));
		assertFalse(isTableExist(tableNames.getTagTableName()));
		assertFalse(isTableExist(tableNames.getTaTeTableName()));
		assertFalse(isTableExist(tableNames.getResultTableName()));

	}
	
	
	@Test
	public void testReInitDB() throws SQLException, ClassNotFoundException, IOException {
		//create tables
		adminOperations.reInitDB();

		//check if tables exist
		assertTrue(isTableExist(tableNames.getUserTableName()));
		assertTrue(isTableExist(tableNames.getCorpusTableName()));
		assertTrue(isTableExist(tableNames.getTextTableName()));
		assertTrue(isTableExist(tableNames.getTagTableName()));
		assertTrue(isTableExist(tableNames.getTaTeTableName()));
		assertTrue(isTableExist(tableNames.getResultTableName()));
		
		//drop tables
		adminOperations.dropTables();

	}

	private boolean isTableExist(String tableName) throws SQLException {
		boolean isExist = false;
		String queryStr = "SELECT EXISTS(SELECT 1 FROM pg_tables WHERE schemaname='public' and tablename=?)";
		PreparedStatement ps = conn.prepareStatement(queryStr);
		
		ps.setString(1, tableName);
		ResultSet rs = ps.executeQuery();
		if(rs.isBeforeFirst()) {
			rs.next();
			isExist = rs.getBoolean(1);
		}
		
		return isExist;
	}
}
