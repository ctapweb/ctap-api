package com.ctapweb.api.servlets.security;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.servlets.utils.PropertiesManager;
import com.ctapweb.api.servlets.utils.PropertyKeys;

public class CTAPRealm extends AuthenticatingRealm {
	public static final String realmName = "CTAPRealm";
	DataSource dataSource;
	private String adminEmail;
	private String adminPasswd;
	private Logger logger = LogManager.getLogger();

	public CTAPRealm() throws ClassNotFoundException, IOException, SQLException {
		dataSource = DataSourceManager.getDataSource();
		Properties prop = PropertiesManager.getProperties();
		adminEmail = prop.getProperty(PropertyKeys.ADMIN_USERNAME);
		adminPasswd = prop.getProperty(PropertyKeys.ADMIN_PASSWD);

	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		logger.trace("Doing get authentication info...");
		
		UsernamePasswordToken passedInToken = (UsernamePasswordToken) token;

		AuthenticationInfo authenticationInfo = null;
		String userEmail = passedInToken.getPrincipal().toString();
		String userPasswd = new String(passedInToken.getPassword());

//		logger.trace("adminEmail:{}, user passed in username:{}, user passed-in passwd: {}", adminEmail, userEmail, userPasswd);

		//check if user admin
		if(userEmail.equals(adminEmail)) {
			this.setCredentialsMatcher(new SimpleCredentialsMatcher());
			return new SimpleAuthenticationInfo(userEmail, adminPasswd, realmName);
		}
		
		//normal user
		this.setCredentialsMatcher(new CTAPCredentialsMatcher());
		try {
			UserTableOperations userTableOperations = new UserTableOperations(dataSource);
			String hashedPasswd = userTableOperations.getPasswdByEmail(userEmail);

			authenticationInfo = new SimpleAuthenticationInfo(userEmail, hashedPasswd, realmName);
		} catch (ClassNotFoundException | IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return authenticationInfo;
	}


}
