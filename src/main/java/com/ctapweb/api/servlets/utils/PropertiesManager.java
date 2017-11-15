/**
 * 
 */
package com.ctapweb.api.servlets.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A singleton for loading properties.
 * 
 * @author xiaobin
 *
 */
public class PropertiesManager {

	private static final String propsFileName = "config.properties";
	private static Properties props = null;
	private static Logger logger = LogManager.getLogger();

	public static Properties getProperties() throws IOException {
		if(props == null) {
			logger.trace("Loading system properties from {}.", propsFileName);

			//get files from the resources folder
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			props = new Properties();

			//read the properties configuration file to get db confidentials
			InputStream ins = classLoader.getResourceAsStream(propsFileName);
			props.load(ins);
			
			if(ins != null) {
				ins.close();
			}
			
//			props.list(System.out);
			
		}

		return props;
	}

}
