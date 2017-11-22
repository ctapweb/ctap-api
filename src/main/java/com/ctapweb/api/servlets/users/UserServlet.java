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
import com.ctapweb.api.db.pojos.UserAccount;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet(description = "for dealing with specific user", urlPatterns = { "/user/" })
public class UserServlet extends HttpServlet {
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
	public UserServlet() throws ClassNotFoundException, IOException, SQLException {
		super();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(DataSourceManager.getDataSource());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("Getting user information...");

		//get current user's email
		String userEmail = SecurityUtils.getSubject().getPrincipal().toString();

		try {
			UserAccount userAccount = userTableOperations.getEntry(userEmail);

			//hide the passwd
			userAccount.setPasswd(null);
			response.getWriter().append(gson.toJson(userAccount));

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}


	/**
	 * Update user account.
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//get current user's email
		String userEmail = SecurityUtils.getSubject().getPrincipal().toString();

		//get new user info
		UserAccount userAccount = gson.fromJson(
				new InputStreamReader(request.getInputStream()), UserAccount.class);

		//update user info
		try {
			long userId = userTableOperations.getEntry(userEmail).getId();

			userTableOperations.updateFirstName(userId, userAccount.getFirstName());
			userTableOperations.updateLastName(userId, userAccount.getLastName());
			userTableOperations.updateInstitution(userId, userAccount.getInstitution());
		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

}
