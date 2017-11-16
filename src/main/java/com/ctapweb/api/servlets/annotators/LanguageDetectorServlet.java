package com.ctapweb.api.servlets.annotators;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * Servlet implementation class LanguageDetectorServlet
 */
@WebServlet(description = "service for detecting the natural language in which a document is written", 
urlPatterns = { LanguageDetectorServlet.URL_PATTERN })
public class LanguageDetectorServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
	public static final String URL_PATTERN = "/annotators/language_detector/*";
//
//	private final String PROP_KEY_CONTENT_TYPE = "api.content_type";
//	private final String AE_DESCRIPTOR_PATH = "descriptors/annotators/LanguageDetector.xml";
//	private URI servletURI;
//	AnalysisEngine langDetectorAE;
//
//
//	private Logger logger = LogManager.getLogger();
//	private String contentType;
//
//	/**
//	 * @see HttpServlet#HttpServlet()
//	 */
//	public LanguageDetectorServlet() {
//		super();
//	}
//
//	@Override
//	public void init() throws ServletException {
//		super.init();
//
//		//load properties
//		Properties props;
//		try {
//			props = PropertiesManager.getProperties();
//			contentType = props.getProperty(PROP_KEY_CONTENT_TYPE);
//		} catch (IOException e) {
//			throw new ServletException(logger.throwing(Level.FATAL, e));
//		}
//
//		//get AE descriptor URL
//		URL aeDescriptorUrl= Thread.currentThread()
//				.getContextClassLoader().getResource(AE_DESCRIPTOR_PATH);
//		try {
//			langDetectorAE = 
//					AnalysisEngineFactory.createEngineFromPath(aeDescriptorUrl.getPath());
//		} catch (InvalidXMLException | ResourceInitializationException | IOException e) {
//			throw logger.throwing(new ServletException(e));
//		}
//	}
//	/**
//	 * Return a collection representation.
//	 */
//	@Override
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
//			throws ServletException, IOException {
//		servletURI = URI.create(req.getRequestURL().toString());
//		Template template = TextTemplate.generateTemplate();
//		Collection collection = Collection.builder(servletURI)
//				.withTemplate(template)
//				.withHref(servletURI)
//				.build();
//
//		resp.setHeader("Content-Type", contentType);
//		PrintWriter printWriter = resp.getWriter();
//		printWriter.write(collection.toString());
//		printWriter.close();
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		//Content type contains encoding setup. 
//		//The header should be set before getting the writer.
//		response.setHeader("Content-Type", contentType);
//		servletURI = URI.create(request.getRequestURL().toString());
//		
//		Builder responseCollectionBuilder = new Builder(servletURI);
//		PrintWriter responseWriter = response.getWriter();
//
//		try {
//			//Parse the request body. 
//			logger.info("Parsing request body to get text for analysis...");
//			Collection collection = 
//					CollectionUtils.parseRequestCollection(request.getInputStream(), servletURI);
//			//If not successful, return collection with error and template.
//			if(collection.hasError()) {
//				logger.error("Collection parse error. Can't get data from client.");
//				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//				responseWriter.write(collection.toString());
//				responseWriter.close();
//				return;
//			}
//
//			//If parsed successfully, run the AE on the parsed collection object
//			//which contains the data to be analyzed.
////			collection = ServletUtils.runAnalysisOnCollection(collection, langDetectorAE, servletURI).build();
//			
//			List<TextTemplate> texts = CollectionUtils.getTexts(collection);
//			for(TextTemplate text: texts) {
//				Item resultItem = runAEOnText(text);
//				responseCollectionBuilder.addItem(resultItem);
//			}
//			
//			
//			response.setStatus(HttpServletResponse.SC_OK);
//			responseWriter.write(responseCollectionBuilder.build().toString());
//			responseWriter.close();
//
//		} catch (SAXException | TikaException | UIMAException e) {
//			throw logger.throwing(new ServletException(e));
//		}
//
//	}
//
//	/**
//	 * @see HttpServlet#doOptions(HttpServletRequest, HttpServletResponse)
//	 */
//	protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
//			throws ServletException, IOException {
//		response.setHeader("Allowed", "GET POST");
//	}
//
//
//	/**
//	 *  Runs an analysis engine, return a result item of collection item type 
//	 * @param text
//	 * @param ae the Analysis Engine to run
//	 * @param servletURI
//	 * @return a collection item
//	 * @throws UIMAException
//	 * @throws IOException
//	 * @throws SAXException
//	 * @throws TikaException
//	 */
//	private Item runAEOnText(TextTemplate text) 
//			throws UIMAException, IOException, SAXException, TikaException {
//		String aeName = langDetectorAE.getMetaData().getName();
//		URI itemURI = URI.create(servletURI.toString() + URIUtil.encodePath(text.getTextTitle()));
//
//		Item resultItem = null;
//		String textContent = text.getTextContent();
//
//		//create the language detector AE
//		JCas jCas = JCasFactory.createJCas();
//		jCas.setDocumentText(textContent);
//
//		SimplePipeline.runPipeline(jCas, langDetectorAE);
//		String documentLanguage = jCas.getDocumentLanguage();
//
//		//create a result item
//		resultItem = Item.builder()
//				.withHref(itemURI)
//				.addProperty(Property.value(ResultItem.KEY_TEXT_TITLE, ValueFactory.createValue(text.getTextTitle())))
//				.addProperty(Property.value(ResultItem.KEY_AE_NAME, ValueFactory.createValue(aeName)))
//				.addProperty(Property.value(ResultItem.KEY_RESULT_VALUE, ValueFactory.createValue(documentLanguage)))
//				.build();
//
//		return resultItem;
//	}
	
}
