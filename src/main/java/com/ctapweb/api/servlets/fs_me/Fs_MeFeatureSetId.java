package com.ctapweb.api.servlets.fs_me;

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
import com.ctapweb.api.db.operations.FS_METableOperations;
import com.ctapweb.api.db.operations.FeatureSetTableOperations;
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.db.pojos.Fs_Me;
import com.ctapweb.api.db.pojos.Measure;
import com.ctapweb.api.servlets.exceptions.CTAPException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class Fs_MeFeatureSetId
 */
@WebServlet(description = "for manipulating measures in feature set", urlPatterns = { "/fs_me/feature_set/*" })
public class Fs_MeFeatureSetId extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger();
	private Gson gson;
	private FeatureSetTableOperations featureSetTableOperations;
	private FS_METableOperations fs_METableOperations;
	private UserTableOperations userTableOperations; 

	/**
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @see HttpServlet#HttpServlet()
	 */
	public Fs_MeFeatureSetId() throws ClassNotFoundException, IOException, SQLException {
		super();
		DataSource dataSource = DataSourceManager.getDataSource();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(dataSource);
		featureSetTableOperations = new FeatureSetTableOperations(dataSource);
		fs_METableOperations = new FS_METableOperations(dataSource);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting GET service from /fs_me/feature_set/*");

		//gets the requested feature set id
		long featureSetId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of resource 
			long userId = getCurrentUserId();
			if(!featureSetTableOperations.isUserOwner(userId, featureSetId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of feature set " + featureSetId + ".")));
			}

			//get measure list in feature set 
			logger.trace("Getting measure list in feature set {} for user {}:{}...", featureSetId, userId, getCurrentUserEmail());
			response.getWriter().append(
					gson.toJson(fs_METableOperations.getAllEntriesByFeatureSet(featureSetId)));

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting POST service from /fs_me/feature_set/*");

		//gets the requested feature set id
		long featureSetId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of resource 
			long userId = getCurrentUserId();
			if(!featureSetTableOperations.isUserOwner(userId, featureSetId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of feature set " + featureSetId + ".")));
			}

			//add measures to feature set. Should pass in an array of measures.
			logger.trace("Adding measure list to feature set {} for user {}:{}...", featureSetId, userId, getCurrentUserEmail());
			Measure[] measuresToAdd = gson.fromJson(request.getReader(), Measure[].class);
			for(Measure measure: measuresToAdd) {
				long measureId = measure.getId();
				Fs_Me fs_meEntry = new Fs_Me(1, featureSetId, measureId);
				//check if measure already in feature set
				if(!fs_METableOperations.isMeasureExistInFeatureSet(featureSetId, measureId)) {
					fs_METableOperations.addEntry(fs_meEntry);
				}
			}

			response.setStatus(response.SC_CREATED);
			//return a list of measures in that feature set
			doGet(request, response);

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting DELETE service from /fs_me/feature_set/*");

		//gets the requested feature set id
		long featureSetId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of resource 
			long userId = getCurrentUserId();
			if(!featureSetTableOperations.isUserOwner(userId, featureSetId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of feature set " + featureSetId + ".")));
			}

			//delete measures from feature set 
			logger.trace("Deleting measures in feature set {} for user {}:{}...", featureSetId, userId, getCurrentUserEmail());

			Measure[] measuresToDelete = gson.fromJson(request.getReader(), Measure[].class);
			for(Measure measure: measuresToDelete) {
				fs_METableOperations.deleteEntry(featureSetId, measure.getId());
			}
			
			//return the new list of measures
			doGet(request, response);

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
