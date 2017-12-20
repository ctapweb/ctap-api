package com.ctapweb.api.servlets.feature_sets;

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
import com.ctapweb.api.db.operations.FeatureSetTableOperations;
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.db.pojos.FeatureSet;
import com.ctapweb.api.servlets.exceptions.CTAPException;
import com.ctapweb.api.servlets.utils.ServletUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class FeatureSetsIdServlet
 */
@WebServlet(description = "for manipulating specific feature set", urlPatterns = { "/feature_sets/id/*" })
public class FeatureSetsIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger();
	private Gson gson;
	private FeatureSetTableOperations featureSetTableOperations;
	private UserTableOperations userTableOperations; 
	/**
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @see HttpServlet#HttpServlet()
	 */
	public FeatureSetsIdServlet() throws ClassNotFoundException, IOException, SQLException {
		super();
		DataSource dataSource = DataSourceManager.getDataSource();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(dataSource);
		featureSetTableOperations = new FeatureSetTableOperations(dataSource);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting GET service from /feature_sets/id/*");

		//gets the requested feature set id
		long featureSetId = Long.parseLong(request.getPathInfo().substring(1));
		
		try {
			//check if current user owner of resource 
			long userId = getCurrentUserId();
			if(!featureSetTableOperations.isUserOwner(userId, featureSetId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of feature set " + featureSetId + ".")));
			}
			
			//get feature set info
			logger.trace("Getting info of feature set {} for user {}:{}...", featureSetId, userId, getCurrentUserEmail());
			FeatureSet featureSet = featureSetTableOperations.getEntry(featureSetId);
			response.getWriter().append(gson.toJson(featureSet));

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting PUT service from /feature_set/id/*");
		
		//gets the requested feature set id
		long featureSetId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			long userId = getCurrentUserId();
			
			//check if current user owner of the corpus
			if(!featureSetTableOperations.isUserOwner(userId, featureSetId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of feature set " + featureSetId + ".")));
			}

			//get the passed in feature set info
			FeatureSet featureSet = gson.fromJson(request.getReader(), FeatureSet.class);
			featureSet.setId(featureSetId);
			
			//update the feature set
			logger.trace("Updating feature set {} for user {}: {}...", featureSetId, userId, getCurrentUserEmail());
			featureSetTableOperations.updateEntry(featureSet);
			
			//return the link of the updated feature set 
			response.setHeader("Link", ServletUtils.createLinkHeader("/feature_sets/" + featureSetId, "self"));
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
		
		//gets the requested feature set id
		long featureSetId = Long.parseLong(request.getPathInfo().substring(1));
		
		try {
			long userId = getCurrentUserId();
			
			//check if current user owner of the feature set
			if(!featureSetTableOperations.isUserOwner(userId, featureSetId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of feature set " + featureSetId + ".")));
			}
			
			//delete the feature set
			logger.trace("Deleting feature set {} for user {}: {}...", featureSetId, userId, getCurrentUserEmail());
			featureSetTableOperations.deleteEntry(featureSetId);
			
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
