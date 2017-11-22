package com.ctapweb.api.servlets.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.mindrot.jbcrypt.BCrypt;

public class CTAPCredentialsMatcher implements CredentialsMatcher {
	private Logger logger = LogManager.getLogger();

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		//need to cast token type because the getCredentials() function returns a char array.
		UsernamePasswordToken passedInToken = (UsernamePasswordToken) token;
		String userPasswd = new String(passedInToken.getPassword());

//		logger.trace("userPasswd: {}, infoPasswd: {}", userPasswd, info.getCredentials());
		return BCrypt.checkpw(userPasswd, info.getCredentials().toString());
	}

}
