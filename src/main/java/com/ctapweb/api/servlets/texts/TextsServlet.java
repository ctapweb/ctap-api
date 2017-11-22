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
 * Servlet implementation class TextsServlet
 */
@WebServlet(description = "for manipulating multiple texts", urlPatterns = { "/texts/*" })
public class TextsServlet extends HttpServlet {
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
	public TextsServlet() throws ClassNotFoundException, IOException, SQLException {
		super();
		DataSource dataSource = DataSourceManager.getDataSource();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);
		tagTableOperations = new TagTableOperations(dataSource);
		textTableOperations = new TextTableOperations(dataSource);

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting GET service from /texts/");
		
		if(isForward(request)) {
			logger.trace("Forwarding GET service to /texts/id/*");
			request.getRequestDispatcher("/texts/id" + request.getPathInfo()).forward(request, response);
			return;
		}
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting PUT service from /texts/");
		
		if(isForward(request)) {
			logger.trace("Forwarding PUT service to /texts/id/*");
			request.getRequestDispatcher("/texts/id" + request.getPathInfo()).forward(request, response);
			return;
		}

	}

	/**
	 * deletes multiple texts
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting DELETE service from /texts/");
		
		if(isForward(request)) {
			logger.trace("Forwarding DELETE service to /texts/id/*");
			request.getRequestDispatcher("/texts/id" + request.getPathInfo()).forward(request, response);
			return;
		}
		
		Text[] textArray = gson.fromJson(request.getReader(), Text[].class);

		try {
			long userId = getCurrentUserId();
			for(Text text: textArray) {
				long textId = text.getId();
				//check if current user owner of the text
				if(!textTableOperations.isUserOwner(userId, textId)) {
					throw logger.throwing(new ServletException(
							new CTAPException("User " + userId + " not owner of text " + textId + ".")));
				}

				//delete the text
				logger.trace("Deleting text {} for user {}:{}...", textId, userId, getCurrentUserEmail());
				textTableOperations.deleteEntry(textId);
			}
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
	
	//decides whether forward is necessary
	private boolean isForward(HttpServletRequest request) {
		String requestPath = request.getPathInfo();
		return !requestPath.isEmpty() && !requestPath.equals("/");
	}
}
