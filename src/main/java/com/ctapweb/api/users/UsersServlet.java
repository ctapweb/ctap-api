package com.ctapweb.api.users;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

import com.ctapweb.api.utils.PropertiesManager;
import com.ctapweb.api.utils.unused.DBConnectionManager;

/**
 * Servlet implementation class UsersServlet
 */
@WebServlet(description = "for creating new user, list all users for admin", urlPatterns = { "/users" })
public class UsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LogManager.getLogger();
	private Connection conn; 
	private String userTableName;
	private Properties props;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UsersServlet() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();

		try {
			conn = DBConnectionManager.getDbConnection();
			props = PropertiesManager.getProperties();
			userTableName = props.getProperty("PropKeys.USER_TABLE");
		} catch (ClassNotFoundException | IOException | SQLException e) {
			throw new ServletException(logger.throwing(Level.FATAL, e));
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("A list of users.").append(request.getContextPath());
	}

	/**
	 * Create users by posting to the servlet. The servlet first checks if the database contains a user table. 
	 * If not, it creates it. 
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		//check post data to verify that account info is correctly provided
////		String templateStr = IOUtils.toString(request.getReader());
//		
//		//parse the json string
//		Gson gson = new Gson();
//		TemplateWrapper userTemplate = gson.fromJson(request.getReader(), TemplateWrapper.class);
////		Template userTemplate = gson.fromJson(templateStr, Template.class);
//		User user = new User(userTemplate.getTemplate());
//
//		logger.info("Client passed in user info. firstName: {}, lastName: {}, email: {}, institution: {}, encrypted passwd: {}", 
//				user.getFirstName(), user.getLastName(), user.getEmail(), user.getInstitution(), user.getPasswd());
//		
//		int insertedUserId;
//		//check if user table already exists, if not, create it
//		try {
//			if(!isUserTableExist()) {
//				logger.info("User account table does not exist, creating user account table...");
//				//create user table
//				createUserTable();
//			} 
//			//create user
//			logger.info("Inserting new user info into user account table...");
//			insertedUserId = createUser(user);
//		} catch (SQLException e) {
//			throw new ServletException(logger.throwing(Level.FATAL, e));
//		}
//		
//		//write results to response
//		response.setContentType(props.getProperty(PropKeys.CONTENT_TYPE));
//		
//		//create the url for the inserted user
//		sendCreated(response, insertedUserId);
//		

	}

	/**
	 * @see HttpServlet#doOptions(HttpServletRequest, HttpServletResponse)
	 */
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Allowed actions: OPTIONS, POST, GET");
	}

	private boolean isUserTableExist() throws SQLException {
		boolean isExist = false;

		String queryStr = "SELECT table_name FROM information_schema.tables WHERE  table_name = ?";
		PreparedStatement ps;
		ps = conn.prepareStatement(queryStr);
		ps.setString(1, userTableName);
		ResultSet rs = ps.executeQuery();
		if(rs.isBeforeFirst()) {
			isExist = true;
		}

		return isExist;
	}


	//insert the new user and return the inserted user id
//	private int createUser(User user) throws SQLException {
//		int insertedId = 0;
//
//		String insertStr = "INSERT INTO " + userTableName 
//				+ "(first_name, last_name, institution, email, passwd, create_date) "
//				+ "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING id";
//		PreparedStatement ps = conn.prepareStatement(insertStr);
//		ps.setString(1, user.getFirstName());
//		ps.setString(2, user.getLastName());
//		ps.setString(3, user.getInstitution());
//		ps.setString(4, user.getEmail());
//		ps.setString(5, user.getPasswd());
//
//		ResultSet rs = ps.executeQuery();
//		if(rs.isBeforeFirst()) {
//			rs.next();
//			insertedId = rs.getInt(1);
//		}
//
//		logger.trace("Inserted id: {}", insertedId);
//		return insertedId;
//	}

	//create user account by adding new entry to user account table
	private void createUserTable() throws SQLException {
		String createTableStr = ""
				+ "CREATE TABLE IF NOT EXISTS " + userTableName  
				+ "(id SERIAL UNIQUE PRIMARY KEY,"
				+ "first_name VARCHAR(30),"
				+ "last_name VARCHAR(30),"
				+ "institution VARCHAR(30),"
				+ "email VARCHAR(254) NOT NULL UNIQUE, "
				+ "passwd CHAR(60) NOT NULL, "
				+ "create_date TIMESTAMP, "
				+ "last_login TIMESTAMP "
				+ ");"
				+ "";
		PreparedStatement ps = conn.prepareStatement(createTableStr);
		ps.execute();
	}
	
	//send created (201) response to client
	private void sendCreated(HttpServletResponse response, int insertedUserId) throws IOException {
		response.setStatus(response.SC_CREATED);
//		Collection collection = Collection.builder().withHref(URI.create("http://example.com/users/123")).build();
		
//		response.getWriter().write(collection.toString());
	}
}
