package com.ctapweb.api.servlets.measures;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.operations.MeasureTableOperations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class MeasuresLanguage
 */
@WebServlet(description = "for getting measures of certain language", urlPatterns = { "/measures/language/*" })
public class MeasuresLanguage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger();
	private MeasureTableOperations measureTableOperations;
	private Gson gson;   
    /**
     * @throws SQLException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public MeasuresLanguage() throws ClassNotFoundException, IOException, SQLException {
        super();
    	DataSource dataSource = DataSourceManager.getDataSource();
		gson = new GsonBuilder().setPrettyPrinting().create();
		measureTableOperations = new MeasureTableOperations(dataSource);

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("User calling doGet() in MeasuresLanguageServlet...");
		String language = request.getPathInfo().substring(1);

		try {
			response.getWriter().append(gson.toJson(measureTableOperations.getAllEntriesByLanguage(language)));
		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

}
