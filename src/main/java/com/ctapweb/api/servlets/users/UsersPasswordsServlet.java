package com.ctapweb.api.servlets.users;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import javax.mail.MessagingException;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class UsersPasswordsServlet
 */
@WebServlet("/users/passwords/")
public class UsersPasswordsServlet extends HttpServlet {
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
    public UsersPasswordsServlet() throws ClassNotFoundException, IOException, SQLException {
        super();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(DataSourceManager.getDataSource());
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException ,IOException {
    	UserAccount userAccount = gson.fromJson(
				new InputStreamReader(req.getInputStream()), UserAccount.class);
    	String userEmail = userAccount.getEmail();

		try {
			//check if user exist
			if(!userTableOperations.isAccountExist(userEmail)) {
				resp.sendError(resp.SC_NOT_FOUND, "User does not exist.");
				return;
			}
			
			String newPasswd = userTableOperations.resetPasswd(userEmail);
			userAccount = userTableOperations.getEntry(userEmail);

			String subject = "Reset password for your CTAP account";
			String message = new StringBuilder()
					.append("Dear ")
					.append(userAccount.getFirstName() + " " + userAccount.getLastName() + ",\n")
					.append("The following is your new password:\n\n")
					.append(newPasswd + "\n\n")
					.append("Please log in with the new password and change it accordingly.\n")
					.append("Best, \n")
					.append("The CTAP Team")
					.toString();
			SendMail sendmail = new SendMail();
			sendmail.sendMail(userEmail, subject, message);

		} catch (SQLException | MessagingException e) {
			throw logger.throwing(new ServletException(e));
		} 
    	
    };

	

}
