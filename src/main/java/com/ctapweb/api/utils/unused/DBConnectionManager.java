/**
 * 
 */
package com.ctapweb.api.utils.unused;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctapweb.api.utils.PropertiesManager;


/**
 * @author xiaobin
 * Manages database connections.
 * 
 * Before being able to connect to the database, create the user and grant it the rights to use the database.
 * 	  CREATE ROLE ctapapi WITH PASSWORD 'ctapapi' LOGIN;
 *    CREATE DATABASE ctapapi;
 *    GRANT ALL PRIVILEGES ON ctapapi TO ctapapi;
 */
public class DBConnectionManager {

	private static final Logger logger = LogManager.getLogger();  

	private static Connection dbConnection= null;

	private DBConnectionManager() {
	}

	public static Connection getDbConnection() 
			throws IOException, ClassNotFoundException, SQLException {
		if(dbConnection == null) {

			Properties props = PropertiesManager.getProperties();

			String dbHost = props.getProperty("db.host");
			String dbName = props.getProperty("db.name");
			String dbUser = props.getProperty("db.user");
			String dbPasswd = props.getProperty("db.passwd");

			//create the connection
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://" + dbHost + "/" + dbName;
			logger.trace("Creating DB connection to {}.", url);
			dbConnection =DriverManager.getConnection(url, dbUser, dbPasswd); 
		}

		return dbConnection;
	}

}
