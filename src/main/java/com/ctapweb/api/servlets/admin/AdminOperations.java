/**
 * 
 */
package com.ctapweb.api.servlets.admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctapweb.api.db.DBConnectionManager;
import com.ctapweb.api.db.TableNames;

/**
 * Administrative operations: initialize database, etc.
 * @author xiaobin
 *
 */
public class AdminOperations {
	
	private String userTableName;
	private String corpusTableName;
	private String textTableName;
	private String tagTableName;
	private String taTeTableName;
	private String resultTableName;

	private Connection conn;
	private Logger logger = LogManager.getLogger();
	
	public AdminOperations() throws IOException, ClassNotFoundException, SQLException {
		initFields();
		conn = DBConnectionManager.getDbConnection();
	}

	/**
	 * Constructor for testing purposes.
	 * @param testConnection
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 */
	public AdminOperations(Connection testConnection) throws ClassNotFoundException, IOException, SQLException {
		initFields();
		conn= testConnection;
	}

	private void initFields() throws IOException {
		//get table names
		TableNames tableNames = new TableNames();
		userTableName = tableNames.getUserTableName();
		corpusTableName = tableNames.getCorpusTableName();
		textTableName = tableNames.getTextTableName();
		tagTableName = tableNames.getTagTableName();
		taTeTableName = tableNames.getTaTeTableName();
		resultTableName = tableNames.getResultTableName();
	}
	
	
	

	/**
	 * For initializing database. Create all the tables necessary for the system.
	 * @throws SQLException 
	 */
	public void initDB() throws SQLException {
		createUserAccountTable();
		createCorpusTable();
		createTextTable();
		createTagTable();
		createTa_TeTable();
		createResultTable();
	}
	
	/**
	 * Reinitialize database, dropping tables that are already created.
	 * Be careful with the operation because it will delete all the data in the tables.
	 * @throws SQLException
	 */
	public void reInitDB() throws SQLException {

		//drop tables
		dropTables();
		
		//create the tables again
		initDB();
		
	}
	
	/**
	 * Drop all the database tables. Used mainly when testing the system. Normally do need this function in production.
	 * Be cautious: could delete all data and non-recoverable.
	 * @throws SQLException
	 */
	public void dropTables() throws SQLException {
		//drop the original tables. Tables should be dropped in order because of dependencies.
		dropTable(taTeTableName);
		dropTable(tagTableName);
		dropTable(resultTableName);
		dropTable(textTableName);
		dropTable(corpusTableName);
		dropTable(userTableName);
	}
	
	
	//create the table for user account info
	private void createUserAccountTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + userTableName + "("
				+ "id BIGSERIAL PRIMARY KEY NOT NULL,"
				+ "first_name VARCHAR(30),"
				+ "last_name VARCHAR(30),"
				+ "institution VARCHAR(100),"
				+ "email VARCHAR(254) NOT NULL UNIQUE, "
				+ "passwd TEXT NOT NULL, "
				+ "create_date TIMESTAMP NOT NULL, "
				+ "last_login TIMESTAMP "
				+ ");"
				+ "";

		logger.trace("Creating table {}...", userTableName);

		PreparedStatement ps = conn.prepareStatement(createTableStr);
		ps.execute();

		logger.info("Created table {}.", userTableName);
	}
	
	private void createCorpusTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + corpusTableName + "("
				+ "id BIGSERIAL PRIMARY KEY NOT NULL ,"
				+ "owner_id BIGINT NOT NULL REFERENCES " + userTableName + "(id) ON DELETE CASCADE,"
				+ "name TEXT NOT NULL,"
				+ "description TEXT,"
				+ "create_timestamp TIMESTAMP NOT NULL" + ");"
				+ "";

		logger.trace("Creating table {}...", corpusTableName);

		PreparedStatement ps = conn.prepareStatement(createTableStr);
		ps.executeUpdate();

		logger.info("Created table {}.", corpusTableName);

	}
	
	private void createTextTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + textTableName + "("
				+ "id BIGSERIAL PRIMARY KEY NOT NULL ,"
				+ "corpus_id BIGINT NOT NULL REFERENCES " + corpusTableName + "(id) ON DELETE CASCADE,"
				+ "title TEXT,"
				+ "content TEXT,"
				+ "status VARCHAR(20),"
				+ "update_timestamp TIMESTAMP NOT NULL" + ");"
				+ "";

		logger.trace("Creating table {}...", textTableName);

		PreparedStatement ps = conn.prepareStatement(createTableStr);
		ps.executeUpdate();

		logger.info("Created table {}.", textTableName);
	}
	
	private void createResultTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + resultTableName + "("
				+ "id BIGSERIAL PRIMARY KEY NOT NULL ,"
				+ "text_id BIGINT NOT NULL REFERENCES " + textTableName + "(id) ON DELETE CASCADE,"
				+ "measure TEXT NOT NULL,"
				+ "value NUMERIC NOT NULL,"
				+ "UNIQUE(text_id, feature)"
				+ ")";

		logger.trace("Creating table {}...", resultTableName);

		PreparedStatement ps = conn.prepareStatement(createTableStr);
		ps.executeUpdate();

		logger.info("Created table {}.", resultTableName);
	}
	
	private void createTagTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + tagTableName + "("
				+ "id BIGSERIAL PRIMARY KEY NOT NULL ,"
				+ "owner_id BIGINT NOT NULL REFERENCES " + userTableName + "(id) ON DELETE CASCADE,"
				+ "name TEXT NOT NULL,"
				+ "create_timestamp TIMESTAMP NOT NULL,"
				+ "UNIQUE(owner_id, name)" 
				+ ")";

		logger.trace("Creating table {}...", tagTableName);

		PreparedStatement ps = conn.prepareStatement(createTableStr);
		ps.executeUpdate();

		logger.info("Created table {}.", tagTableName);
	}

	private void createTa_TeTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + taTeTableName + "("
				+ "ta_id BIGINT NOT NULL REFERENCES " + tagTableName + "(id) ON DELETE CASCADE,"
				+ "te_id BIGINT NOT NULL REFERENCES " + textTableName + "(id) ON DELETE CASCADE,"
				+ "UNIQUE(ta_id, te_id)"
				+ ")"
				+ "";

		logger.trace("Creating table {}...", taTeTableName);

		PreparedStatement ps = conn.prepareStatement(createTableStr);
		ps.executeUpdate();

		logger.info("Created table {}.", taTeTableName);
	}

	private void dropTable(String tableName) throws SQLException {
		String dropTableStr = ""
				+ "DROP TABLE IF EXISTS " + tableName;
	
		logger.trace("Dropping table {}...", tableName);
	
		PreparedStatement ps = conn.prepareStatement(dropTableStr);
		ps.executeUpdate();
	
		logger.info("Dropped table {}.", tableName);
	
	}
	
	

}






