package com.ctapweb.api.servlets.users;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.operations.UserTableOperations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class UserPasswordServlet
 */
@WebServlet(description = "for updating user password", urlPatterns = {"/user/passwd/"})
public class UserPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LogManager.getLogger();
	private Gson gson;
	private UserTableOperations userTableOperations;
    /**
     * @throws SQLException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public UserPasswordServlet() throws ClassNotFoundException, IOException, SQLException {
        super();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(DataSourceManager.getDataSource());
    }


	/**
	 * Updates user password
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//get current user's email
		String userEmail = SecurityUtils.getSubject().getPrincipal().toString();
		
		logger.trace("Changing user password for user {}...", userEmail);

		//get old and new passwds, which are passed in as a String array
		String[] passwds = gson.fromJson(
				new InputStreamReader(request.getInputStream()), String[].class);

		String oldPasswd = passwds[0], newPasswd = passwds[1];
		
		try {
			if(!userTableOperations.checkPasswd(userEmail, oldPasswd)) {
				throw logger.throwing(new ServletException("Wrong old passwd while changing passwd."));
			}
			userTableOperations.updatePasswd(userEmail, newPasswd);
		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
		
	}
	

}






