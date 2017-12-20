package com.ctapweb.api.servlets.analyses;

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
 * Servlet implementation class AnalysesIdServlet
 */
@WebServlet(description = "for manipulating sepcific analysis", urlPatterns = { "/analyses/id/*" })
public class AnalysesIdServlet extends HttpServlet {
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
	public AnalysesIdServlet() throws ClassNotFoundException, IOException, SQLException {
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
		logger.trace("Requesting GET service from /analyses/id/*");

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
			logger.trace("Getting info of analysis {} for user {}:{}...", analysisId, userId, getCurrentUserEmail());
			Analysis analysis = analysisTableOperations.getEntry(analysisId);
			response.getWriter().append(gson.toJson(analysis));

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting PUT service from /analyses/id/*");

		//gets the requested analysis id
		long analysisId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			long userId = getCurrentUserId();

			//check if current user owner of the corpus
			if(!analysisTableOperations.isUserOwner(userId, analysisId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of analysis " + analysisId + ".")));
			}

			//get the passed in analysis info
			Analysis analysis = gson.fromJson(request.getReader(), Analysis.class);
			analysis.setId(analysisId);

			//update the analysis
			logger.trace("Updating analysis {} for user {}: {}...", analysisId, userId, getCurrentUserEmail());
			analysisTableOperations.updateEntry(analysis);

			//return the link of the updated analysis 
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
		logger.trace("Requesting DELETE service from /feature_sets/id/*");
		//gets the requested analysis id
		long analysisId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			long userId = getCurrentUserId();

			//check if current user owner of the analysis
			if(!analysisTableOperations.isUserOwner(userId, analysisId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of analysis " + analysisId + ".")));
			}

			//delete the analysis
			logger.trace("Deleting analysis {} for user {}: {}...", analysisId, userId, getCurrentUserEmail());
			analysisTableOperations.deleteEntry(analysisId);

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

}
