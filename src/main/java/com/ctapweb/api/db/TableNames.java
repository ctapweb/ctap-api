package com.ctapweb.api.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctapweb.api.servlets.utils.PropertiesManager;
import com.ctapweb.api.servlets.utils.PropertyKeys;

/**
 * A class for loading table names from properties configuration.
 * @author xiaobin
 *
 */
public class TableNames {

	private String userTableName;
	private String corpusTableName;
	private String textTableName;
	private String tagTableName;
	private String measureCategoryTableName;
	private String measureTableName;
	private String resultTableName;

	private Properties properties;
	private Logger logger = LogManager.getLogger();

	public TableNames() throws IOException {
		properties = PropertiesManager.getProperties();

		//get the table names from configuration file
		if((userTableName = properties.getProperty(PropertyKeys.DB_TNAME_USER)) == null) {
			throw logger.throwing(new NullPointerException("Null table name for table 'user account'."));
		}

		if((corpusTableName = properties.getProperty(PropertyKeys.DB_TNAME_CORPUS)) == null ) {
			throw logger.throwing(new NullPointerException("Null table name for table 'corpus'."));
		}

		if((textTableName = properties.getProperty(PropertyKeys.DB_TNAME_TEXT)) == null) {
			throw logger.throwing(new NullPointerException("Null table name for table 'text'."));
		}

		if((tagTableName = properties.getProperty(PropertyKeys.DB_TNAME_TAG)) == null) {
			throw logger.throwing(new NullPointerException("Null table name for table 'tag'."));
		}

		if((measureCategoryTableName = properties.getProperty(PropertyKeys.DB_TNAME_MEASURE_CATEGORY)) == null) {
			throw logger.throwing(new NullPointerException("Null table name for table 'measure_category'."));
		}

		if((measureTableName = properties.getProperty(PropertyKeys.DB_TNAME_MEASURE)) == null) {
			throw logger.throwing(new NullPointerException("Null table name for table 'measure'."));
		}

		if((resultTableName = properties.getProperty(PropertyKeys.DB_TNAME_RESULT)) == null) {
			throw logger.throwing(new NullPointerException("Null table name for table 'result'."));
		}
	}

	public String getUserTableName() {
		return userTableName;
	}

	public String getCorpusTableName() {
		return corpusTableName;
	}

	public String getTextTableName() {
		return textTableName;
	}

	public String getTagTableName() {
		return tagTableName;
	}

	public String getMeasureCategoryTableName() {
		return measureCategoryTableName;
	}

	public String getMeasureTableName() {
		return measureTableName;
	}

	public String getResultTableName() {
		return resultTableName;
	}
	
	public List<String> getAllTableNames() {
		List<String> allNames = new ArrayList<>();
		allNames.add(getUserTableName());
		allNames.add(getCorpusTableName());
		allNames.add(getTagTableName());
		allNames.add(getTextTableName());
		allNames.add(getMeasureCategoryTableName());
		allNames.add(getMeasureTableName());
		allNames.add(getResultTableName());
		return allNames;
	}
	
}
