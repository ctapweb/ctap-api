package com.ctapweb.api.servlets.corpora;

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
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.db.pojos.Corpus;
import com.ctapweb.api.servlets.ServletUtils;
import com.ctapweb.api.servlets.exceptions.CTAPException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class CorporaIdServlet
 */
@WebServlet("/corpora/id/*")
public class CorporaIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LogManager.getLogger();
	private Gson gson;
	private UserTableOperations userTableOperations; 
	private CorpusTableOperations corpusTableOperations; 
    /**
     * @throws SQLException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public CorporaIdServlet() throws ClassNotFoundException, IOException, SQLException {
        super();
        DataSource dataSource = DataSourceManager.getDataSource();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting GET service from /corpora/id/*");

		//gets the requested corpus id
		long corpusId = Long.parseLong(request.getPathInfo().substring(1));
		
		try {
			//check if current user owner of the corpus
			long userId = getCurrentUserId();
			if(!corpusTableOperations.isUserOwner(userId, corpusId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of corpus " + corpusId + ".")));
			}
			
			//get corpus info
			logger.trace("Getting info of corpus {} for user {}:{}...", corpusId, userId, getCurrentUserEmail());
			Corpus corpus = corpusTableOperations.getEntry(corpusId);
			response.getWriter().append(gson.toJson(corpus));

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}


	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting PUT service from /corpora/id/*");
		
		//gets the requested corpus id
		long corpusId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			long userId = getCurrentUserId();
			
			//check if current user owner of the corpus
			if(!corpusTableOperations.isUserOwner(userId, corpusId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of corpus " + corpusId + ".")));
			}

			//get the passed in corpus info
			Corpus corpus = gson.fromJson(request.getReader(), Corpus.class);
			corpus.setId(corpusId);
			
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
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting DELETE service from /corpora/id/*");
		
		//gets the requested corpus id
		long corpusId = Long.parseLong(request.getPathInfo().substring(1));
		
		try {
			long userId = getCurrentUserId();
			
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
	
	private long getCurrentUserId() throws SQLException {
		return userTableOperations.getEntry(getCurrentUserEmail()).getId();
	}
	
	private String getCurrentUserEmail() {
		return SecurityUtils.getSubject().getPrincipal().toString();
	}
}
