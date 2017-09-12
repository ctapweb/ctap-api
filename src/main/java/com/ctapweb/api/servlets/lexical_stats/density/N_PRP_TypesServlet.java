package com.ctapweb.api.servlets.lexical_stats.density;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.eclipse.jetty.util.URIUtil;
import org.xml.sax.SAXException;

import com.ctapweb.api.lexical.external.LexicalSophisticationBNC;
import com.ctapweb.api.servlets.collection.ResultItem;
import com.ctapweb.api.servlets.collection.TextTemplate;
import com.ctapweb.api.servlets.utils.CollectionUtils;
import com.ctapweb.api.utils.NLPPipeLinesManager;
import com.ctapweb.api.utils.PropertiesManager;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import net.hamnaberg.json.Collection;
import net.hamnaberg.json.Collection.Builder;
import net.hamnaberg.json.Item;
import net.hamnaberg.json.Property;
import net.hamnaberg.json.Template;
import net.hamnaberg.json.ValueFactory;

@WebServlet(description = "Service for counting the personal pronoun types",
urlPatterns = { N_PRP_TypesServlet.URL_PATTERN })
public class N_PRP_TypesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String POS_TAG = "PRP";
	public static final String URL_PATTERN = "/lexical_stats/density/n_" + POS_TAG + "_types/*";
	public static final String STAT_NAME = "n_" + POS_TAG + "_types";

	private final String PROP_KEY_CONTENT_TYPE = "api.content_type";
	private URI servletURI;

	private Logger logger = LogManager.getLogger();
	private String contentType;
	private StanfordCoreNLP posTagger;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public N_PRP_TypesServlet() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		logger.info("Initializing servlet " + URL_PATTERN);

		//load properties
		Properties props;
		try {
			props = PropertiesManager.getProperties();
			contentType = props.getProperty(PROP_KEY_CONTENT_TYPE);
		} catch (IOException e) {
			throw new ServletException(logger.throwing(Level.FATAL, e));
		}

		//gets a tagger
		posTagger = NLPPipeLinesManager.getPosTagger();

	}
	/**
	 * Return a collection representation.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		logger.info("Client " + req.getRemoteAddr() + " doing GET...");
		servletURI = URI.create(req.getRequestURL().toString());
		Template template = TextTemplate.generateTemplate();
		Collection collection = Collection.builder(servletURI)
				.withTemplate(template)
				.withHref(servletURI)
				.build();

		resp.setHeader("Content-Type", contentType);
		PrintWriter printWriter = resp.getWriter();
		printWriter.write(collection.toString());
		printWriter.close();
		logger.info("Server returned collection template.");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("Client " + request.getRemoteAddr() + " doing POST...");
		//Content type contains encoding setup. 
		//The header should be set before getting the writer.
		response.setHeader("Content-Type", contentType);
		servletURI = URI.create(request.getRequestURL().toString());
		
		Builder responseCollectionBuilder = new Builder(servletURI);
		PrintWriter responseWriter = response.getWriter();

		try {
			//Parse the request body. 
			logger.info("Parsing request body to get text for analysis...");
			Collection collection = 
					CollectionUtils.parseRequestCollection(request.getInputStream(), servletURI);
			//If not successful, return collection with error and template.
			if(collection.hasError()) {
				logger.error("Collection parse error. Can't get data from client.");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				responseWriter.write(collection.toString());
				responseWriter.close();
				return;
			}

			//If parsed successfully, NLP pre-process the texts and get the required stat
			logger.info("Request body parsed successfully, analyzing texts...");
			List<TextTemplate> texts = CollectionUtils.getTexts(collection);
			for(TextTemplate text: texts) {
				Item resultItem = runAnalysisAndGetResult(text);
				responseCollectionBuilder.addItem(resultItem);
			}
			
			
			response.setStatus(HttpServletResponse.SC_OK);
			responseWriter.write(responseCollectionBuilder.build().toString());
			responseWriter.close();

		} catch (SAXException | TikaException e) {
			throw logger.throwing(new ServletException(e));
		}

	}

	/**
	 * @see HttpServlet#doOptions(HttpServletRequest, HttpServletResponse)
	 */
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		response.setHeader("Allowed", "GET POST");
	}


	/**
	 * Run NLP preprocess on a text and return the result stat.
	 * @param text
	 * @return
	 */
	private Item runAnalysisAndGetResult(TextTemplate text) {
		URI itemURI = URI.create(servletURI.toString() + URIUtil.encodePath(text.getTextTitle()));

		Item resultItem = null;
		String textContent = text.getTextContent();

		Annotation annotation = posTagger.process(textContent);
		double result = LexicalSophisticationBNC.getN_POS_Types(annotation, POS_TAG);

		//create a result item
		logger.trace("Analyzed text \"{}\", with result {}.", 
				textContent.length() > 50 ? textContent.substring(0, 50) + "..." : textContent, result);
		resultItem = Item.builder()
				.withHref(itemURI)
				.addProperty(Property.value(ResultItem.KEY_TEXT_TITLE, ValueFactory.createValue(text.getTextTitle())))
				.addProperty(Property.value(ResultItem.KEY_STAT_NAME, ValueFactory.createValue(STAT_NAME)))
				.addProperty(Property.value(ResultItem.KEY_RESULT_VALUE, ValueFactory.createValue(result)))
				.build();

		return resultItem;
	}
	
}
