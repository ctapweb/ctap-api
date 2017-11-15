/**
 * 
 */
package com.ctapweb.api.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctapweb.api.servlets.utils.PropertiesManager;
import com.ctapweb.api.servlets.utils.PropertyKeys;


/**
 * @author xiaobin
 * Manages database connections.
 * 
 * Before being able to connect to the database, create the user and grant it the rights to use the database.
 * 	  CREATE ROLE ctapapi WITH PASSWORD 'ctapapi' LOGIN;
 *    CREATE DATABASE ctapapi WITH OWNER ctapapi;
 */
public class DBConnectionManager {

	private static final Logger logger = LogManager.getLogger();  

	private static BasicDataSource basicDataSource = null; // for production
	private static BasicDataSource testDataSource = null; // for testing
	private static Properties props;

	public static DataSource getDataSource() 
			throws IOException, ClassNotFoundException, SQLException {

		if(basicDataSource == null) {
			props = PropertiesManager.getProperties();

			String dbHost = props.getProperty(PropertyKeys.DB_HOST);
			String dbName = props.getProperty(PropertyKeys.DB_NAME);
			String dbUser = props.getProperty(PropertyKeys.DB_USER);
			String dbPasswd = props.getProperty(PropertyKeys.DB_PASSWD);

			basicDataSource = createDataSource(dbHost, dbName, dbUser, dbPasswd);
		}
		
		return basicDataSource;

	}




	/**
	 * For doing unit testing, which uses a different database.
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static DataSource getTestDataSource() 
			throws IOException, ClassNotFoundException, SQLException {
		if(testDataSource == null) {
			props = PropertiesManager.getProperties();

			String dbHost = props.getProperty(PropertyKeys.DB_HOST_TEST);
			String dbName = props.getProperty(PropertyKeys.DB_NAME_TEST);
			String dbUser = props.getProperty(PropertyKeys.DB_USER_TEST);
			String dbPasswd = props.getProperty(PropertyKeys.DB_PASSWD_TEST);

			testDataSource = createDataSource(dbHost, dbName, dbUser, dbPasswd);
		}
		
		return testDataSource;
	}

	//create the connection
	private static BasicDataSource createDataSource(String dbHost, String dbName, String dbUser, String dbPasswd) 
			throws ClassNotFoundException, SQLException, IOException {
		BasicDataSource dataSource = new BasicDataSource();

		dataSource.setDriverClassName("org.postgresql.Driver");
		String url = "jdbc:postgresql://" + dbHost + "/" + dbName;
		dataSource.setUrl(url);
		dataSource.setUsername(dbUser);
		dataSource.setPassword(dbPasswd);
		
		return dataSource;
	}

}
