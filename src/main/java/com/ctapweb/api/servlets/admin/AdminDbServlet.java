package com.ctapweb.api.servlets.admin;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class AdminDbServlet
 */
@WebServlet(description = "Provides access to the database resources.", 
urlPatterns = { "/admin/db/" })
public class AdminDbServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Logger logger = LogManager.getLogger();
	private AdminOperations adminOperations;
	private Gson gson;

	/**
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminDbServlet() throws IOException, ClassNotFoundException, SQLException {
		super();
		adminOperations = new AdminOperations();
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//returns a list of tables
		response.getWriter().append(gson.toJson(adminOperations.getAllTableNames()));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("User calling doPost() in AdminDbServlet...");
		//creates all tables in db
		try {
			adminOperations.initDB();
		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("User calling doPut() in AdminDbServlet...");
		//deletes all tables in db
		try {
			adminOperations.reInitDB();
		} catch (SQLException | ClassNotFoundException e) {
			throw logger.throwing(new ServletException(e));
		} 
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("User calling doDelete() in AdminDbServlet...");
		//deletes all tables in db
		try {
			adminOperations.dropAllTables();
		} catch (SQLException | ClassNotFoundException e) {
			throw logger.throwing(new ServletException(e));
		} 
	}

}
