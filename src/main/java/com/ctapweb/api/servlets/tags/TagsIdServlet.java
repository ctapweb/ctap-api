package com.ctapweb.api.servlets.tags;

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
import com.ctapweb.api.db.operations.CorpusTableOperations;
import com.ctapweb.api.db.operations.TagTableOperations;
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.db.pojos.Tag;
import com.ctapweb.api.servlets.exceptions.CTAPException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class TagsIdServlet
 */
@WebServlet("/tags/id/*")
public class TagsIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger();
	private Gson gson;
	private CorpusTableOperations corpusTableOperations;
	private UserTableOperations userTableOperations;
	private TagTableOperations tagTableOperations;
 
    /**
     * @throws SQLException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public TagsIdServlet() throws ClassNotFoundException, IOException, SQLException {
        super();
        DataSource dataSource = DataSourceManager.getDataSource();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);
		tagTableOperations = new TagTableOperations(dataSource);
    }

	/**
	 * Gets a tag's name.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting GET service from /tags/id/*");
		long tagId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of the tag
			long userId = getCurrentUserId();
			if(!tagTableOperations.isUserOwner(userId, tagId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of tag " + tagId + ".")));
			}
			
			//get tag info
			logger.trace("Getting info of tag {} for user {}:{}...", tagId, userId, getCurrentUserEmail());
			Tag tag = tagTableOperations.getEntry(tagId);
			response.getWriter().append(gson.toJson(tag.getName()));

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}	
		
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting DELETE service from /tags/id/*");
		long tagId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of the tag
			long userId = getCurrentUserId();
			if(!tagTableOperations.isUserOwner(userId, tagId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of tag " + tagId + ".")));
			}
			
			//delete the tag
			logger.trace("Deleting tag {} for user {}:{}...", tagId, userId, getCurrentUserEmail());
			tagTableOperations.deleteEntry(tagId);

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting GET service from /tags/id/*");
		long tagId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of the tag
			long userId = getCurrentUserId();
			if(!tagTableOperations.isUserOwner(userId, tagId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of tag " + tagId + ".")));
			}
			
			//get the info passed in
			Tag tag = gson.fromJson(request.getReader(), Tag.class);
			tag.setId(tagId);

			//change the tag name
			logger.trace("Changing info of tag {} for user {}:{}...", tagId, userId, getCurrentUserEmail());
			tagTableOperations.updateEntry(tag);

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}
	

	private long getCurrentUserId() throws SQLException {
		return userTableOperations.getEntry(getCurrentUserEmail()).getId();
	}

	private String getCurrentUserEmail() {
		return SecurityUtils.getSubject().getPrincipal().toString();
	}
}
