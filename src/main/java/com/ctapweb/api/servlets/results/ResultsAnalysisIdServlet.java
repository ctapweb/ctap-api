package com.ctapweb.api.servlets.results;

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
import org.apache.shiro.SecurityUtils;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.operations.AnalysisTableOperations;
import com.ctapweb.api.db.operations.ResultTableOperations;
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.db.pojos.Result;
import com.ctapweb.api.servlets.exceptions.CTAPException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class ResultsAnalysisIdServlet
 */
@WebServlet(description = "for getting results of an anlysis", urlPatterns = { "/results/analysis/*" })
public class ResultsAnalysisIdServlet extends HttpServlet {
	private static final long serialVersionUID = -8767496940326628018L;

	private Logger logger = LogManager.getLogger();
	private Gson gson;
	private UserTableOperations userTableOperations;
	private AnalysisTableOperations analysisTableOperations;  
	private ResultTableOperations resultTableOperations;
    /**
     * @throws SQLException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public ResultsAnalysisIdServlet() throws ClassNotFoundException, IOException, SQLException {
        super();
        DataSource dataSource = DataSourceManager.getDataSource();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(dataSource);
		analysisTableOperations = new AnalysisTableOperations(dataSource);
		resultTableOperations = new ResultTableOperations(dataSource);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting GET service from /results/analysis/*");

		//gets the requested analysis id 
		long analysisId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of resource 
			long userId = getCurrentUserId();
			if(!analysisTableOperations.isUserOwner(userId, analysisId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of analysis " + analysisId + ".")));
			}

			//get analysis info
			logger.trace("Getting results of analysis {} for user {}:{}...", analysisId, userId, getCurrentUserEmail());
			List<Result> resultList = resultTableOperations.getAllEntriesByAnalysis(analysisId);

			response.getWriter().append(gson.toJson(resultList));

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	private String getCurrentUserEmail() {
		return SecurityUtils.getSubject().getPrincipal().toString();
	}

	private long getCurrentUserId() throws SQLException {
		return userTableOperations.getEntry(getCurrentUserEmail()).getId();
	}

}
