package com.ctapweb.api.servlets.categories;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.operations.MeasureCategoryTableOperations;
import com.ctapweb.api.db.pojos.MeasureCategory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class CategoriesLanguageServlet
 */
@WebServlet(description = "for listing measure categories of a certain language", urlPatterns = { "/categories/language/*" })
public class CategoriesLanguageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger();
	private MeasureCategoryTableOperations categoryTableOperations;
	private Gson gson;  
    /**
     * @throws SQLException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public CategoriesLanguageServlet() throws ClassNotFoundException, IOException, SQLException {
        super();
        DataSource dataSource = DataSourceManager.getDataSource();
		categoryTableOperations = new MeasureCategoryTableOperations(dataSource);
		gson = new GsonBuilder().setPrettyPrinting().create();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("User calling doGet() in CategoriesLanguageServlet...");
		String language = request.getPathInfo().substring(1);

		try {
			List<MeasureCategory> categoryList = categoryTableOperations.getAllEntriesByLanguage(language);
			if(categoryList == null) return;
			for(MeasureCategory cat: categoryList) {
				cat.setRequiredPipeline(null);
				cat.setClassName(null);
			}
			response.getWriter().append(gson.toJson(categoryList));
		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

}
