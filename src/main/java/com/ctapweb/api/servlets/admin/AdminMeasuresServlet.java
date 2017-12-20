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
 * Servlet implementation class AdminMeasuresServlet
 */
@WebServlet(description = "for importing and cleaning measures table in db", urlPatterns = { "/admin/measures/" })
public class AdminMeasuresServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Logger logger = LogManager.getLogger();
	private AdminOperations adminOperations;
	private Gson gson;

    /**
     * @throws SQLException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public AdminMeasuresServlet() throws ClassNotFoundException, IOException, SQLException {
        super();
        adminOperations = new AdminOperations();
		gson = new GsonBuilder().setPrettyPrinting().create();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("User calling doPost() in AdminMeasuresServlet...");
		
		try {
			adminOperations.importMeasures();
		} catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("User calling doPut() in AdminMeasuresServlet...");
		//cleans the measures table to make it match the annotations in measure extractor classes.
		try {
			adminOperations.cleanMeasures();
		} catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
			throw logger.throwing(new ServletException(e));
		} 
	}

}
