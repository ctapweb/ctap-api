package com.ctapweb.api.servlets.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;


/**
 * for supporting CORS. Not needed at the moment. Disable preflight in browser for development.
 * @author xiaobin
 *
 */
public class CORSBasicHttpAuthenticationFilter extends BasicHttpAuthenticationFilter {
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		HttpServletResponse httpResponse = WebUtils.toHttp(response);
		
	    String httpMethod = httpRequest.getMethod();
	    if ("OPTIONS".equalsIgnoreCase(httpMethod)) {
//	    	httpResponse.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
//	    	httpResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
//	    	httpResponse.addHeader("Access-Control-Allow-Headers", "Authorization");
//	    	httpResponse.addHeader("Access-Control-Allow-Credentials", "true");
//	    	httpResponse.addHeader("Access-Control-Max-Age", "10000");
	      return true;
	    } else {
	      return super.isAccessAllowed(request, response, mappedValue);
	    }
	}
}
