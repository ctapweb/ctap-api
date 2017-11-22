package com.ctapweb.api.servlets.corpora;

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
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.db.pojos.Corpus;
import com.ctapweb.api.servlets.ServletUtils;
import com.ctapweb.api.servlets.exceptions.CTAPException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class CorporaServlet
 */
@WebServlet(description = "for getting and creating corpora", urlPatterns = { "/corpora/*" })
public class CorporaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LogManager.getLogger();
	private Gson gson;
	private CorpusTableOperations corpusTableOperations;
	private UserTableOperations userTableOperations;

    /**
     * @throws SQLException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public CorporaServlet() throws ClassNotFoundException, IOException, SQLException {
        super();
        DataSource dataSource = DataSourceManager.getDataSource();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);
    }

	/**
	 * lists all corpora belonging to the user
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting GET service for /corpora/*");
		
		//decides whether to dispatch. If request for specific corpus info, dispatch.
		if(isForward(request)) {
			logger.trace("Forwarding GET service to /corpora/id/*");
			request.getRequestDispatcher("/corpora/id" + request.getPathInfo()).forward(request, response);
			return;
		}
		
		try {
			//get user id
			long userId = getCurrentUserId();
			
			logger.trace("Getting corpus list for user {}: {}...", userId,  getCurrentUserEmail());
			//get corpora
			List<Corpus> corpusList = corpusTableOperations.getAllEntriesByOwner(userId);
			
			//return the data
			response.getWriter().append(gson.toJson(corpusList));
			
		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	/**
	 * creates a new corpus
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting POST service for /corpora/*");

		//decides whether to dispatch. If request for specific corpus info, dispatch.
		if(isForward(request)) {
			logger.trace("Forwarding POST service to /corpora/id/*");
			request.getRequestDispatcher("/corpora/id" + request.getPathInfo()).forward(request, response);
			return;
		}
		
			logger.trace("still running doPost in /corpora/*");
		try {
			long userId = getCurrentUserId();

			//get the passed in corpus info
			Corpus corpus = gson.fromJson(request.getReader(), Corpus.class);
			corpus.setOwnerId(userId);
			
			//insert the corpus
			logger.trace("Creating a new corpus for user {}: {}...", userId, getCurrentUserEmail());
			long insertedCorpusId = corpusTableOperations.addEntry(corpus);
			
			//return the link of the inserted corpus
			response.setHeader("Link:", ServletUtils.createLinkHeader("/corpora/" + insertedCorpusId, "self"));
			response.setStatus(response.SC_CREATED);

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting DELETE service for /corpora/*");
		
		//decides whether to dispatch. If request for specific corpus info, dispatch.
		if(isForward(request)) {
			logger.trace("Forwarding DELETE service to /corpora/id/*");
			request.getRequestDispatcher("/corpora/id" + request.getPathInfo()).forward(request, response);
			return;
		}
		
		try {
			//get the passed in corpus info
			Corpus corpus = gson.fromJson(request.getReader(), Corpus.class);
			long userId = getCurrentUserId();
			long corpusId = corpus.getId();
			
			//check if current user owner of the corpus
			if(!corpusTableOperations.isUserOwner(userId, corpusId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of corpus " + corpusId + ".")));
			}
			
			//delete the corpus
			logger.trace("Deleting corpus {} for user {}: {}...", corpusId, userId, getCurrentUserEmail());
			corpusTableOperations.deleteEntry(corpusId);
			
			response.setStatus(response.SC_OK);

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
		
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting PUT service for /corpora/*");
		
		//decides whether to dispatch. If request for specific corpus info, dispatch.
		if(isForward(request)) {
			logger.trace("Forwarding PUT service to /corpora/id/*");
			request.getRequestDispatcher("/corpora/id" + request.getPathInfo()).forward(request, response);
			return;
		}
		
		try {
			//get the passed in corpus info
			Corpus corpus = gson.fromJson(request.getReader(), Corpus.class);
			long userId = getCurrentUserId();
			long corpusId = corpus.getId();
			
			//check if current user owner of the corpus
			if(!corpusTableOperations.isUserOwner(userId, corpusId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of corpus " + corpusId + ".")));
			}
			
			//update the corpus
			logger.trace("Updating corpus {} for user {}: {}...", corpusId, userId, getCurrentUserEmail());
			corpusTableOperations.updateEntry(corpus);
			
			//return the link of the inserted corpus
			response.setHeader("Link:", ServletUtils.createLinkHeader("/corpora/" + corpusId, "self"));
			response.setStatus(response.SC_OK);

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	//decides whether forward is necessary
	private boolean isForward(HttpServletRequest request) {
		String requestPath = request.getPathInfo();
		return !requestPath.isEmpty() && !requestPath.equals("/");
	}

	private long getCurrentUserId() throws SQLException {
		return userTableOperations.getEntry(getCurrentUserEmail()).getId();
	}
	
	private String getCurrentUserEmail() {
		return SecurityUtils.getSubject().getPrincipal().toString();
	}
}
