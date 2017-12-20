package com.ctapweb.api.servlets.exceptions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class ErrorHandlerServlet
 */
@WebServlet(description = "for handling server exceptions thrown from other servlet", urlPatterns = { "/error_handler/" })
public class ErrorHandlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger logger = LogManager.getLogger();
	private Gson gson;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ErrorHandlerServlet() {
		super();
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//get the error information
		Throwable throwable = (Throwable)
				request.getAttribute("javax.servlet.error.exception");
		String errorMessage = throwable.getMessage();

		response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
		response.getWriter().append(gson.toJson(errorMessage));
	}

}
