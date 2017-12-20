package com.ctapweb.api.servlets.security;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

/**
 * This method helps prevent the Authorization window from browser.
 * Refer to http://loudvchar.blogspot.de/2010/11/avoiding-browser-popup-for-401.html for more info.
 * @author xiaobin
 *
 */
public class CTAPAuthcBasic extends BasicHttpAuthenticationFilter {

	@Override
	public String getAuthcScheme() {
		return "xBasic";
	}
	
	@Override
	protected boolean isRememberMe(ServletRequest request) {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		
		//check if rememberMe is passed in as parameter
		String[] rememberMeValues = httpRequest.getParameterValues("rememberMe");

		if(rememberMeValues == null) {
			return false;
		}
		
		return Boolean.parseBoolean(rememberMeValues[0]);
		
	}


}
