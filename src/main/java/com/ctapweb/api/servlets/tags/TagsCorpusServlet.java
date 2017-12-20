package com.ctapweb.api.servlets.tags;

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
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.db.pojos.Tag;
import com.ctapweb.api.servlets.exceptions.CTAPException;
import com.ctapweb.api.servlets.utils.ServletUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class TagsCorpusServlet
 */
@WebServlet(description = "tags of corpus", urlPatterns = { "/tags/corpus/*" })
public class TagsCorpusServlet extends HttpServlet {
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
    public TagsCorpusServlet() throws ClassNotFoundException, IOException, SQLException {
        super();
        DataSource dataSource = DataSourceManager.getDataSource();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);
		tagTableOperations = new TagTableOperations(dataSource);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting GET service from /tags/corpus/*");
		long corpusId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of the corpus
			long userId = getCurrentUserId();
			
			if(!corpusTableOperations.isUserOwner(userId, corpusId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of corpus " + corpusId + ".")));
			}
			
			//get tag list of the corpus
			logger.trace("Getting tags of corpus {} for user {}:{}...", corpusId, userId, getCurrentUserEmail());
			List<Tag> tagList = tagTableOperations.getAllEntriesByCorpus(corpusId);
			
			response.getWriter().append(gson.toJson(tagList));

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting POST service from /tags/corpus/*");
		long corpusId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of the corpus
			long userId = getCurrentUserId();
			
			if(!corpusTableOperations.isUserOwner(userId, corpusId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of corpus " + corpusId + ".")));
			}
			
			//insert the tag
			logger.trace("Adding new tag to corpus {} for user {}:{}...", corpusId, userId, getCurrentUserEmail());
			Tag tag = gson.fromJson(request.getReader(), Tag.class);
			tag.setCorpusId(corpusId);
			long insertedTagId = tagTableOperations.addEntry(tag);
			
			response.setHeader("Link", ServletUtils.createLinkHeader("/tags/" + insertedTagId, "self"));
			response.setStatus(response.SC_CREATED);

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
