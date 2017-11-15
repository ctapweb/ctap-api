package com.ctapweb.api.servlets.admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctapweb.api.db.DBConnectionManager;
import com.ctapweb.api.servlets.utils.PropertiesManager;

/**
 * Only authorized admin has access to the service. Admin credential can be set
 * in src/main/resouces/config.properties.
 * 
 * Requests should be sent with "Authorization: Basic xxxxxxxxx" header (RFC 2617).
 * According the the standard, the credential string is a base64 encoded string of 
 * "username:passwd". The servlet returns 401 Unauthorized if authorization fails.
 * 
 */
@WebServlet(description = "Admin of users: listing, deleting, changing user accounts.", urlPatterns = { "/admin/users" })
public class AdminUsers extends HttpServlet {
       
	private static final long serialVersionUID = -1112059001176672168L;

	private final String KEY_HEADER_AUTHORIZATION = "Authorization";
	private Logger logger = LogManager.getLogger();
	private Connection conn; 
	private String userTableName;
	private Properties props;


	/**
     * @see HttpServlet#HttpServlet()
     */
    public AdminUsers() {
        super();
    }

    @Override
    public void init() throws ServletException {
    	super.init();

    	try {
			conn = DBConnectionManager.getDbConnection();
			props = PropertiesManager.getProperties();
			userTableName = props.getProperty("db.tname.user");
		} catch (ClassNotFoundException | IOException | SQLException e) {
			throw new ServletException(logger.throwing(Level.FATAL, e));
		}
    }
	/**
	 * Gets a list of all users.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * Create the user table.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//checks admin credentials
		AdminUtils.checkAdminCredential(request.getHeader("Authorization"));

	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doOptions(HttpServletRequest, HttpServletResponse)
	 */
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	
}
