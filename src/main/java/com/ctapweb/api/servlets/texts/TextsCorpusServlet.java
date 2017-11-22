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
import com.ctapweb.api.servlets.ServletUtils;
import com.ctapweb.api.servlets.exceptions.CTAPException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class TextsCorpusServlet
 */
@WebServlet(description = "manipulating texts of a corpus", urlPatterns = { "/texts/corpus/*" })
public class TextsCorpusServlet extends HttpServlet {
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
	public TextsCorpusServlet() throws ClassNotFoundException, IOException, SQLException {
		super();
		DataSource dataSource = DataSourceManager.getDataSource();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);
		tagTableOperations = new TagTableOperations(dataSource);
		textTableOperations = new TextTableOperations(dataSource);

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting GET service from /texts/corpus/*");
		long corpusId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of the corpus
			long userId = getCurrentUserId();

			if(!corpusTableOperations.isUserOwner(userId, corpusId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of corpus " + corpusId + ".")));
			}

			//get texts
			logger.trace("Getting texts from corpus {} for user {}:{}...", corpusId, userId, getCurrentUserEmail());
			List<Text> textList = textTableOperations.getAllEntriesByCorpus(corpusId);
			response.getWriter().append(gson.toJson(textList));

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting POST service from /texts/corpus/*");
		long corpusId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of the corpus
			long userId = getCurrentUserId();

			if(!corpusTableOperations.isUserOwner(userId, corpusId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of corpus " + corpusId + ".")));
			}

			//get text passed in
			Text text = gson.fromJson(request.getReader(), Text.class);

			logger.trace("Getting texts from corpus {} for user {}:{}...", corpusId, userId, getCurrentUserEmail());
			long insertedTextId = textTableOperations.addEntry(text);

			response.setHeader("Link:", ServletUtils.createLinkHeader("/texts/corpus/" + insertedTextId, "self"));
			response.setStatus(response.SC_CREATED);

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
