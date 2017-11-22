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

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.db.pojos.UserAccount;
import com.ctapweb.api.servlets.exceptions.CTAPException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class UsersServlet
 */
@WebServlet(description = "for users manipulation", urlPatterns = { "/users/" })
public class UsersServlet extends HttpServlet {
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
    public UsersServlet() throws ClassNotFoundException, IOException, SQLException {
        super();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(DataSourceManager.getDataSource());
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("Inserting a new user...");

		UserAccount userAccount = gson.fromJson(new InputStreamReader(request.getInputStream()), UserAccount.class);
		
		//check required fields and email format
		String email = userAccount.getEmail();
		String passwd = userAccount.getPasswd();

		// empty info exception
		if (email.isEmpty() || userAccount.getPasswd().isEmpty() 
				|| email.length() < 6 ) {
			throw new ServletException(logger.throwing(
					new CTAPException("Please provide user email, password and make sure password is longer than 6 characters.")));
		}

		// validate the email
		if (!UserUtils.isEmailValid(email)) {
			throw new ServletException(logger.throwing(new CTAPException("Invalid email format.")));
		}

		// check if email already exists
		try {
			if(userTableOperations.isAccountExist(email)) { 
				throw new ServletException(logger.throwing(new CTAPException("Email already exists.")));
			}
		} catch (SQLException e) {
			throw new ServletException(e);
		}
		
		//add the account
		try {
			userTableOperations.addEntry(userAccount);
		} catch (SQLException e) {
			throw new ServletException(e);
		}
		
		response.setStatus(response.SC_CREATED);
		logger.info("New user created.");
	}

}





