package com.ctapweb.api.servlets.feature_sets;

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
import com.ctapweb.api.db.operations.FeatureSetTableOperations;
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.db.pojos.FeatureSet;
import com.ctapweb.api.servlets.exceptions.CTAPException;
import com.ctapweb.api.servlets.utils.ServletUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class FeatureSetsServlet
 */
@WebServlet(description = "for manipulating a user's feature sets", urlPatterns = { "/feature_sets/*" })
public class FeatureSetsServlet extends HttpServlet {
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
	public FeatureSetsServlet() throws ClassNotFoundException, IOException, SQLException {
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
		logger.trace("Requesting GET service for /feature_sets/*");
		
		//decides whether to dispatch. If request for specific corpus info, dispatch.
		if(isForward(request)) {
			logger.trace("Forwarding GET service to /feature_sets/id/*");
			request.getRequestDispatcher("/feature_sets/id" + request.getPathInfo()).forward(request, response);
			return;
		}
		
		try {
			//get user id
			long userId = getCurrentUserId();
			
			logger.trace("Getting feature set list for user {}: {}...", userId,  getCurrentUserEmail());
			//get feature sets
			List<FeatureSet> featureSetList = featureSetTableOperations.getAllEntriesByOwner(userId);
			
			//return the data
			response.getWriter().append(gson.toJson(featureSetList));
			
		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting POST service for /feature_sets/*");

		//decides whether to dispatch. If request for specific corpus info, dispatch.
		if(isForward(request)) {
			logger.trace("Forwarding POST service to /feature_sets/id/*");
			request.getRequestDispatcher("/feature_sets/id" + request.getPathInfo()).forward(request, response);
			return;
		}
		
			logger.trace("still running doPost in /feature_sets/*");
		try {
			long userId = getCurrentUserId();

			//get the passed in feature set info
			FeatureSet featureSet = gson.fromJson(request.getReader(), FeatureSet.class);
			featureSet.setOwnerId(userId);
			
			//insert the feature set
			logger.trace("Creating a new feature set for user {}: {}...", userId, getCurrentUserEmail());
			long insertedFeatureSetId = featureSetTableOperations.addEntry(featureSet);
			
			//return the link of the inserted corpus
			response.setHeader("Link", ServletUtils.createLinkHeader("/feature_sets/" + insertedFeatureSetId, "self"));
			response.setStatus(response.SC_CREATED);

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	logger.trace("Requesting PUT service for /feature_sets/*");
		
		//decides whether to dispatch. If request for specific corpus info, dispatch.
		if(isForward(request)) {
			logger.trace("Forwarding PUT service to /feature_sets/id/*");
			request.getRequestDispatcher("/feature_sets/id" + request.getPathInfo()).forward(request, response);
			return;
		}
		
		try {
			//get the passed in feature set info
			FeatureSet featureSet = gson.fromJson(request.getReader(), FeatureSet.class);
			long userId = getCurrentUserId();
			long featureSetId = featureSet.getId();
			
			//check if current user owner of the feature set
			if(!featureSetTableOperations.isUserOwner(userId, featureSetId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of feature set " + featureSetId + ".")));
			}
			
			//update the feature set
			logger.trace("Updating feature set {} for user {}: {}...", featureSetId, userId, getCurrentUserEmail());
			featureSetTableOperations.updateEntry(featureSet);
			
			//return the link of the inserted corpus
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
		logger.trace("Requesting DELETE service for /feature_sets/*");
		
		//decides whether to dispatch. If request for specific info, dispatch.
		if(isForward(request)) {
			logger.trace("Forwarding DELETE service to /feature_sets/id/*");
			request.getRequestDispatcher("/feature_sets/id" + request.getPathInfo()).forward(request, response);
			return;
		}
		
		try {
			//get the passed in feature set info
			FeatureSet[] featureSets = gson.fromJson(request.getReader(), FeatureSet[].class);
			long userId = getCurrentUserId();
			
			for(FeatureSet featureSet: featureSets) {
				long featureSetId = featureSet.getId();
				
				//check if current user owner of the resource
				if(!featureSetTableOperations.isUserOwner(userId, featureSetId)) {
					throw logger.throwing(new ServletException(
							new CTAPException("User " + userId + " not owner of feature set " + featureSetId + ".")));
				}
				
				//delete the feature set
				logger.trace("Deleting feature set {} for user {}: {}...", featureSetId, userId, getCurrentUserEmail());
				featureSetTableOperations.deleteEntry(featureSetId);
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
