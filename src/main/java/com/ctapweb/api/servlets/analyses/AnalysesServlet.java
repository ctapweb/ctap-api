package com.ctapweb.api.servlets.analyses;

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
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.db.pojos.Analysis;
import com.ctapweb.api.servlets.exceptions.CTAPException;
import com.ctapweb.api.servlets.utils.ServletUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class AnalysesServlet
 */
@WebServlet(description = "for manipulating analyses of a user", urlPatterns = { "/analyses/*" })
public class AnalysesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LogManager.getLogger();
	private Gson gson;
	private UserTableOperations userTableOperations;
	private AnalysisTableOperations analysisTableOperations;
	/**
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @see HttpServlet#HttpServlet()
	 */
	public AnalysesServlet() throws ClassNotFoundException, IOException, SQLException {
		super();
		DataSource dataSource = DataSourceManager.getDataSource();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(dataSource);
		analysisTableOperations = new AnalysisTableOperations(dataSource);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting GET service for /analyses/*");

		//decides whether to dispatch. If request for specific analysis info, dispatch.
		if(isForward(request)) {
			logger.trace("Forwarding GET service to /analyses/id/*");
			request.getRequestDispatcher("/analyses/id" + request.getPathInfo()).forward(request, response);
			return;
		}

		try {
			//get user id
			long userId = getCurrentUserId();

			logger.trace("Getting analysis list for user {}: {}...", userId,  getCurrentUserEmail());
			//get analyses
			List<Analysis> analysisList = analysisTableOperations.getAllEntriesByOwner(userId);

			//return the data
			response.getWriter().append(gson.toJson(analysisList));

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting POST service for /analyses/*");

		//decides whether to dispatch. If request for specific analysis info, dispatch.
		if(isForward(request)) {
			logger.trace("Forwarding POST service to /analyses/id/*");
			request.getRequestDispatcher("/analyses/id" + request.getPathInfo()).forward(request, response);
			return;
		}

		logger.trace("still running doPost in /analyses/*");
		try {
			long userId = getCurrentUserId();

			//get the passed in analysis info
			Analysis analysis = gson.fromJson(request.getReader(), Analysis.class);

			//insert the feature set
			logger.trace("Creating a new analysis for user {}: {}...", userId, getCurrentUserEmail());
			long insertedAnalysisId = analysisTableOperations.addEntry(analysis);

			//return the link of the inserted corpus
			response.setHeader("Link", ServletUtils.createLinkHeader("/analyses/" + insertedAnalysisId, "self"));
			response.setStatus(response.SC_CREATED);

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting PUT service for /analyses/*");

		//decides whether to dispatch. If request for specific analysis info, dispatch.
		if(isForward(request)) {
			logger.trace("Forwarding PUT service to /analyses/id/*");
			request.getRequestDispatcher("/analyses/id" + request.getPathInfo()).forward(request, response);
			return;
		}

		try {
			//get the passed in analysis info
			Analysis analysis = gson.fromJson(request.getReader(), Analysis.class);
			long userId = getCurrentUserId();
			long analysisId = analysis.getId();

			//check if current user owner of the analysis
			if(!analysisTableOperations.isUserOwner(userId, analysisId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of analysis " + analysisId + ".")));
			}

			//update the analysis
			logger.trace("Updating analysis {} for user {}: {}...", analysisId, userId, getCurrentUserEmail());
			analysisTableOperations.updateEntry(analysis);

			//return the link of the inserted corpus
			response.setHeader("Link", ServletUtils.createLinkHeader("/analyses/" + analysisId, "self"));
			response.setStatus(response.SC_OK);

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting DELETE service for /analyses/*");

		//decides whether to dispatch. If request for specific info, dispatch.
		if(isForward(request)) {
			logger.trace("Forwarding DELETE service to /analyses/id/*");
			request.getRequestDispatcher("/analyses/id" + request.getPathInfo()).forward(request, response);
			return;
		}

		try {
			//get the passed in analyses info
			Analysis[] analyses = gson.fromJson(request.getReader(), Analysis[].class);
			long userId = getCurrentUserId();

			for(Analysis analysis: analyses) {
				long analysisId = analysis.getId();

				//check if current user owner of the resource
				if(!analysisTableOperations.isUserOwner(userId, analysisId)) {
					throw logger.throwing(new ServletException(
							new CTAPException("User " + userId + " not owner of analysis " + analysisId + ".")));
				}

				//delete the resource
				logger.trace("Deleting analysis {} for user {}: {}...", analysisId, userId, getCurrentUserEmail());
				analysisTableOperations.deleteEntry(analysisId);
			}

			response.setStatus(response.SC_OK);

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

	//decides whether forward is necessary
	private boolean isForward(HttpServletRequest request) {
		String requestPath = request.getPathInfo();
		return !requestPath.isEmpty() && !requestPath.equals("/");
	}

}
