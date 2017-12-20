package com.ctapweb.api.servlets.analyses;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import com.ctapweb.api.main.RunAnalysis;
import com.ctapweb.api.servlets.exceptions.CTAPException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class AnalysesStatusId
 */
@WebServlet(description = "for updating analysis status", urlPatterns = { "/analyses/status/*" })
public class AnalysesStatusId extends HttpServlet {
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
	public AnalysesStatusId() throws ClassNotFoundException, IOException, SQLException {
		super();
		DataSource dataSource = DataSourceManager.getDataSource();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(dataSource);
		analysisTableOperations = new AnalysisTableOperations(dataSource);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting PUT service from /analyses/status/*");

		//gets the requested analysis id
		final long analysisId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			long userId = getCurrentUserId();

			//check if current user owner of the analysis
			if(!analysisTableOperations.isUserOwner(userId, analysisId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of analysis " + analysisId + ".")));
			}

			//get the current analysis status 
			//			Analysis analysis = gson.fromJson(request.getReader(), Analysis.class);
			String status = analysisTableOperations.getEntry(analysisId).getStatus();

			//switch the analysis status
			logger.trace("Updating status of analysis {} for user {}: {}...", analysisId, userId, getCurrentUserEmail());

			//stop the analysis if it is still running
			if(status.equals(Analysis.Status.RUNNING)) {
				analysisTableOperations.updateStatus(analysisId, Analysis.Status.STOPPED);
				response.getWriter().append(gson.toJson(analysisTableOperations.getEntry(analysisId)));
				return;
			}

			analysisTableOperations.updateStatus(analysisId, Analysis.Status.RUNNING);
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						RunAnalysis runAnalysis = new RunAnalysis(analysisId);
						runAnalysis.run();
					} catch (ClassNotFoundException | IOException | SQLException 
							| IllegalAccessException | IllegalArgumentException 
							| InvocationTargetException | CTAPException e) {
						logger.catching(e);
					}

				}
			}).start();

			//return accepted and point the client to where to query the status
			response.setStatus(response.SC_ACCEPTED);
			response.addHeader("Location", request.getContextPath() + "/analyses/" + analysisId);
			response.getWriter().append(gson.toJson(analysisTableOperations.getEntry(analysisId)));

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
