package com.ctapweb.api.servlets.texts;

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
import com.ctapweb.api.db.operations.TextTableOperations;
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.db.pojos.Text;
import com.ctapweb.api.servlets.exceptions.CTAPException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class TextsIdServlet
 */
@WebServlet(description = "for manipulating single text", urlPatterns = { "/texts/id/*" })
public class TextsIdServlet extends HttpServlet {
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
	public TextsIdServlet() throws ClassNotFoundException, IOException, SQLException {
		super();
		DataSource dataSource = DataSourceManager.getDataSource();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);
		tagTableOperations = new TagTableOperations(dataSource);
		textTableOperations = new TextTableOperations(dataSource);
	}

	/**
	 *gets a text
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting GET service from /texts/id/*");
		long textId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of the text
			long userId = getCurrentUserId();
			if(!textTableOperations.isUserOwner(userId, textId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of text " + textId + ".")));
			}

			//get text info
			logger.trace("Getting info of text {} for user {}:{}...", textId, userId, getCurrentUserEmail());
			Text text = textTableOperations.getEntry(textId);
			response.getWriter().append(gson.toJson(text));

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}	
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting PUT service from /texts/id/*");
		long textId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of the tag
			long userId = getCurrentUserId();
			if(!textTableOperations.isUserOwner(userId, textId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of text " + textId + ".")));
			}
			
			//get the info passed in
			Text text = gson.fromJson(request.getReader(), Text.class);
			text.setId(textId);

			//change the text
			logger.trace("Changing info of text {} for user {}:{}...", textId, userId, getCurrentUserEmail());
			textTableOperations.updateEntry(text);

		} catch (SQLException e) {
			throw logger.throwing(new ServletException(e));
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting DELETE service from /texts/id/*");
		long textId = Long.parseLong(request.getPathInfo().substring(1));

		try {
			//check if current user owner of the text
			long userId = getCurrentUserId();
			if(!textTableOperations.isUserOwner(userId, textId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of text " + textId + ".")));
			}
			
			//delete the text
			logger.trace("Deleting text {} for user {}:{}...", textId, userId, getCurrentUserEmail());
			textTableOperations.deleteEntry(textId);

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
