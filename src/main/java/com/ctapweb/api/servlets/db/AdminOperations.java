/**
 * 
 */
package com.ctapweb.api.servlets.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.operations.CorpusTableOperations;
import com.ctapweb.api.db.operations.MeasureCategoryTableOperations;
import com.ctapweb.api.db.operations.MeasureTableOperations;
import com.ctapweb.api.db.operations.ResultTableOperations;
import com.ctapweb.api.db.operations.TagTableOperations;
import com.ctapweb.api.db.operations.TextTableOperations;
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.db.pojos.UserAccount;

/**
 * Administrative operations: initialize database, get user account list, etc.
 * @author xiaobin
 *
 */
public class AdminOperations {
	
	private Logger logger = LogManager.getLogger();
	
	UserTableOperations userTableOperations;
	CorpusTableOperations corpusTableOperations;
	TagTableOperations tagTableOperations;
	TextTableOperations textTableOperations;
	MeasureCategoryTableOperations categoryTableOperations;
	MeasureTableOperations measureTableOperations;
	ResultTableOperations resultTableOperations;
	
	public AdminOperations() throws ClassNotFoundException, IOException, SQLException {
		DataSource dataSource = DataSourceManager.getDataSource();

		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);
		tagTableOperations = new TagTableOperations(dataSource);
		textTableOperations = new TextTableOperations(dataSource);
		categoryTableOperations = new MeasureCategoryTableOperations(dataSource);
		measureTableOperations = new MeasureTableOperations(dataSource);
		resultTableOperations = new ResultTableOperations(dataSource);

	}
	
	/**
	 * For initializing database. Create all the tables necessary for the system.
	 * Pay attention to table dependencies.
	 * @throws SQLException 
	 */
	public void initDB() throws SQLException {
		logger.info("Initializing database and creating tables...");
		userTableOperations.createTable();
		corpusTableOperations.createTable();
		tagTableOperations.createTable();
		textTableOperations.createTable();
		categoryTableOperations.createTable();
		measureTableOperations.createTable();
		resultTableOperations.createTable();
	}
	
	/**
	 * Reinitialize database, dropping tables that are already created.
	 * Be careful with the operation because it will delete all the data in the tables.
	 * @throws SQLException
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void reInitDB() throws SQLException, ClassNotFoundException, IOException {
		logger.warn("Reinitializing database to recreate the tables...");
		dropAllTables();
		initDB();
	}
	
	/**
	 * Drop all database tables. Be careful of dependency.
	 * @throws SQLException
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void dropAllTables() throws SQLException, ClassNotFoundException, IOException {
		logger.warn("Dropping all database tables...");
		resultTableOperations.dropTable();
		measureTableOperations.dropTable();
		categoryTableOperations.dropTable();
		textTableOperations.dropTable();
		tagTableOperations.dropTable();
		corpusTableOperations.dropTable();
		userTableOperations.dropTable();
	}
	
	/**
	 * Import measures from meausre class annotations.
	 */
	public void importMeasures() {
		//TODO
	}
	
	/**
	 * Update measure information by rereading measure class annotations.
	 */
	public void updateMeasures() {
		
	}

	/**
	 * Check that all measures in DB have corresponding annotations from measure class, 
	 * or else remove them.
	 * Remove measure categories as well.
	 */
	public void cleanMeasures() {
		//TODO
		
	}
	
	public List<UserAccount> getAllUserAccounts() throws SQLException {
		return userTableOperations.getAllEntries();
	}
	
}






