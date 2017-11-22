package com.ctapweb.api.servlets.admin;

import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctapweb.api.servlets.utils.PropertiesManager;

public class AdminUtils {
	private static final Logger logger = LogManager.getLogger();

	private static Properties properties; 

	/**
	 * Check if the authorization string contains correct authorization credentials.
	 * 
	 * @param authorizationHeaderStr extracted from the "Authorization" header in a format similar to:
	 * "Basic YWRtaW5AY3RhcHdlYi5jb206YWRtaW4K"
	 * @return
	 * @throws IOException 
	 */
	public static boolean checkAdminCredential(String authorizationHeaderStr) throws IOException {
		boolean isAuthorized = false;

		//get admin credentials from config file
		properties = PropertiesManager.getProperties();
		String adminUsername = properties.getProperty("admin.username");
		String adminPasswd = properties.getProperty("admin.passwd");
		
		String[] credentials = extractCredentials(authorizationHeaderStr);
		
		if(adminUsername.equals(credentials[0]) && adminPasswd.equals(credentials[1])) {
			isAuthorized = true;
		}
		
		return isAuthorized;
	}
	
	//Extracts credentials from authorization header str. 
	//Returns a String array of length 2: index 0 is the user name and index 1 the passwd
	private static String[] extractCredentials(String authorizationHeaderStr) {
		String[] credentials = new String[2];
		
		//check if "Basic" authorization protocol is used
		if(!authorizationHeaderStr.startsWith("Basic")) {
			logger.trace("Authorization protocol not 'Basic'.");
			return null;
		}
		
		//extract the credential string
		String[] splittedStr = authorizationHeaderStr.split("\\p{Blank}", 2);
		if(splittedStr.length != 2) {
			logger.trace("Authorization string not in correct format. "
					+ "Should be 'Basic YWRtaW5AY3RhcHdlYi5jb206YWRtaW4K'.");
			return null;
		}

		//decode base64
		String base64Credentials = splittedStr[1];
		String decodedStr = new String(Base64.getDecoder().decode(base64Credentials));
		
		//test the decoded string format, making sure it is in the format "username:passwd"
		String[] splittedCredentials = decodedStr.split(":", 2);
		if(splittedCredentials.length != 2) {
			logger.trace("Incorrected credential format. Should be 'username:passwd'.");
			return null;
		}
		
		credentials[0] = splittedCredentials[0];
		credentials[1] = splittedCredentials[1];
		
		return credentials;
	}
}
