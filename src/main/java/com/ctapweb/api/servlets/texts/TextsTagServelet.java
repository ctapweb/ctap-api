package com.ctapweb.api.servlets.texts;

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
import com.ctapweb.api.db.operations.CorpusTableOperations;
import com.ctapweb.api.db.operations.TagTableOperations;
import com.ctapweb.api.db.operations.TextTableOperations;
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.db.pojos.Text;
import com.ctapweb.api.servlets.exceptions.CTAPException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class TextsTagServelet
 */
@WebServlet(description = "for manipulting text tags", urlPatterns = { "/texts/tag/*" })
public class TextsTagServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger();
	private Gson gson;
	private CorpusTableOperations corpusTableOperations;
	private UserTableOperations userTableOperations;
	private TagTableOperations tagTableOperations;
	private TextTableOperations textTableOperations;

	/**
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @see HttpServlet#HttpServlet()
	 */
	public TextsTagServelet() throws ClassNotFoundException, IOException, SQLException {
		super();
		DataSource dataSource = DataSourceManager.getDataSource();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);
		tagTableOperations = new TagTableOperations(dataSource);
		textTableOperations = new TextTableOperations(dataSource);

	}

	/**
	 * Gets a list of texts with a tag.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting GET service from /texts/tag/*");
		long tagId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of the tag
			long userId = getCurrentUserId();

			if(!tagTableOperations.isUserOwner(userId, tagId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of tag " + tagId + ".")));
			}

			//get texts
			logger.trace("Getting texts with tag {} for user {}:{}...", tagId, userId, getCurrentUserEmail());
			List<Text> textList = textTableOperations.getAllEntriesByTag(tagId);

			response.getWriter().append(gson.toJson(textList));

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}	
	}

	/**
	 * Tags the posted texts with a tag.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting POST service from /texts/tag/*");
		long tagId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of the tag
			long userId = getCurrentUserId();

			if(!tagTableOperations.isUserOwner(userId, tagId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of tag " + tagId + ".")));
			}

			//get texts passed in
			Text[] textArray = gson.fromJson(request.getReader(), Text[].class);
			
			for(Text text: textArray) {
				long textId = text.getId();
				logger.trace("Updating text {} with tag {} for user {}:{}...", textId, tagId, userId, getCurrentUserEmail());
				textTableOperations.updateTag(textId, tagId);
			}
					
		} catch (SQLException | ClassNotFoundException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting DELETE service from /texts/tag/*");
		long tagId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of the tag
			long userId = getCurrentUserId();

			if(!tagTableOperations.isUserOwner(userId, tagId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of tag " + tagId + ".")));
			}

			//get texts passed in
			Text[] textArray = gson.fromJson(request.getReader(), Text[].class);
			
			for(Text text: textArray) {
				long textId = text.getId();

				logger.trace("Removing tag for text {} of user {}:{}...", textId, userId, getCurrentUserEmail());
				textTableOperations.removeTag(textId);
			}
					
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
