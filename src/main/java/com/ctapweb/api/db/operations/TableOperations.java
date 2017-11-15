/**
 * 
 */
package com.ctapweb.api.db.operations;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctapweb.api.db.TableNames;
import com.ctapweb.api.db.pojos.TablePojo;

/**
 * An abstract class for table operations. Concrete tables should extend this class and implement 
 * the abstract operation functions.
 * @author xiaobin
 *
 */
public abstract class TableOperations {
	public static final String COLUMN_ID = "id";

	protected String tableName;
	protected TableNames tableNames;
	protected DataSource dataSource;
	protected Logger logger = LogManager.getLogger();

	/**
	 * 
	 * @param conn the db connection the operations are going to be carried out
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public TableOperations(DataSource dataSource) throws IOException, ClassNotFoundException, SQLException {
		//load the all user table names
		tableNames = new TableNames();
		this.dataSource = dataSource;
	}

	public String getTableName() {
		return tableName;
	}

	public abstract <T extends TablePojo> T getEntry(long entryId) throws SQLException;

	public abstract List<? extends TablePojo> getAllEntries() throws SQLException;

	/**
	 * @param entry
	 * @return the added entry's id
	 * @throws SQLException
	 */
	public abstract <T extends TablePojo> long addEntry(T entry) throws SQLException;

	public abstract long[] addEntries(List<? extends TablePojo> entries) throws SQLException;

	public abstract <T extends TablePojo> void updateEntry(T entry) throws SQLException;

	public void deleteEntry(long entryId) throws SQLException {
		String deleteStr = "DELETE FROM " + tableName 
				+ " WHERE " + COLUMN_ID + " = ?";
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(deleteStr);
				){
			ps.setLong(1, entryId);
			ps.executeUpdate();
		}


	}
	
	public long getNumEntries() throws SQLException {
		String queryStr = "SELECT COUNT(*) FROM " + tableName;
		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				ResultSet rs = ps.executeQuery();
				) {

			if(rs.isBeforeFirst()) {
				rs.next();
				return rs.getLong(1);
			}
			return 0;
		}
	}

	public void deleteEntries(long[] entryIds) throws SQLException {
		for(long entryId: entryIds) {
			deleteEntry(entryId);
		}
	}

	/**
	 * Get paged entries.
	 * @param orderByColumn
	 * @param limit
	 * @param offset
	 * @return
	 */
	public abstract List<? extends TablePojo> getEntries(String orderByColumn, long limit, long offset) throws SQLException;

	/**
	 * Empty a table
	 * @throws SQLException
	 */
	public void deleteAllEntries() throws SQLException {
		String deleteStr = "DELETE from " + tableName;

		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(deleteStr);
				) {

			ps.executeUpdate();
		} 

	}

	public abstract void createTable() throws SQLException;

	public void dropTable() throws ClassNotFoundException, SQLException, IOException {
		if(tableName == null) {
			throw logger.throwing(new NullPointerException("Can't drop table null."));
		}

		String dropTableStr = ""
				+ "DROP TABLE IF EXISTS " + tableName;

		try( 
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(dropTableStr);
				) {
			ps.executeUpdate();
		}
	}

	
	/**
	 * Check if talbe exists in DB. 
	 * @return
	 * @throws SQLException
	 */
	protected boolean isTableExist() throws SQLException {
		String queryStr = "SELECT EXISTS(SELECT 1 FROM pg_tables WHERE schemaname='public' and tablename=?)";

		try(
				Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(queryStr);
				) {
			ps.setString(1, tableName);

			try(
					ResultSet rs = ps.executeQuery();
					) {
				if(rs.isBeforeFirst()) {
					rs.next();
					return rs.getBoolean(1);
				}
				return false;
			}

		} 

	}
	
}
