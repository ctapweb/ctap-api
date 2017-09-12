package com.ctapweb.api.servlets.utils;

import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.hamnaberg.json.Collection;
import net.hamnaberg.json.Collection.Builder;

public class ServletUtils {
	private final Logger logger = LogManager.getLogger();
	Builder responseCollectionBuilder;
	URI servletURI;
	
	public ServletUtils(URI servletURI) {
		this.servletURI = servletURI;
		responseCollectionBuilder = Collection.builder(servletURI);
	}

	/**
	 *  Runs an analysis engine, return a result item of collection item type 
	 * @param text
	 * @param ae the Analysis Engine to run
	 * @param servletURI
	 * @return a collection item
	 * @throws UIMAException
	 * @throws IOException
	 * @throws SAXException
	 * @throws TikaException
	 */
//	public Item runAE(TextTemplate text, AnalysisEngine ae) 
//			throws UIMAException, IOException, SAXException, TikaException {
//		String aeName = ae.getMetaData().getName();
//		URI itemURI = URI.create(servletURI.toString() + URIUtil.encodePath(text.getTextTitle()));
//
//		Item resultItem = null;
//		//		Collection resultCollection = null;
//		String textContent = text.getTextContent();
//
//		//create the language detector AE
//		JCas jCas = JCasFactory.createJCas();
//		jCas.setDocumentText(textContent);
//
//		SimplePipeline.runPipeline(jCas, ae);
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
//	

	
	/**
	 * Check if the content of the template in a collection is plain text.
	 * @param collection
	 * @return
	 */
//	public static boolean isTemplateContentPlainText(Collection collection) {
//		boolean isPlainText = false;
//		TextTemplate textTemplate = new TextTemplate(collection.getTemplate().get());
//		String textContent = textTemplate.getTextContent();
//		String fileType = textTemplate.getFileType();
//		
//		if(!Base64.isBase64(textContent) || fileType.equals("text/plain")) {
//			isPlainText = true;
//		}
//		
//		return isPlainText;
//	}
	
	
	/**
	 * Run an analysis engine on the parsed collection passed in from the client.
	 * 
	 * It first check the type of data the client passes in. 
	 * 		If it is "plain/text", it creates a simple pipeline to run the AE on
	 * 		the text content and returns a result collection.
	 * 
	 * 		If it is NOT "plain/text", it base64-decodes the data, then checks
	 * 		if the data is compressed.
	 * 
	 * 			If the data is not compressed, it is a single file which can be
	 * 			in any non-plain-text format (e.g. msword, pdf, rtf, etc.). The
	 * 			function would first extracts the body content from the file
	 * 			with Apache Tika. Then the AE would be run on the body content.
	 * 
	 * 			If the data is compressed, then the file may contain multiple
	 * 			files. The function would first extract all these files before
	 * 			further extracting the body content from each file. Then the AE 
	 * 			would be run on the body content.
	 * 
	 * 
	 * @param collection The collection that contains the data to be analyzed.
	 * @param analysisEngine The analysis engine to run. Usually created in the Servlet init() method.
	 * @param servletURI The uri of the servlet.
	 * @return a collection builder
	 * @throws IOException
	 * @throws UIMAException
	 * @throws SAXException
	 * @throws TikaException
	 * @throws ArchiveException 
	 */
//	public Builder runAnalysisOnCollection(Collection collection, AnalysisEngine analysisEngine) 
//			throws IOException, UIMAException, SAXException, TikaException, ArchiveException {
////		responseCollectionBuilder = Collection.builder(servletURI);
//
//		TextTemplate textTemplate = new TextTemplate(collection.getTemplate().get());
//		String textTitle = textTemplate.getTextTitle();
//		String fileType = textTemplate.getFileType();
//		String textContent = textTemplate.getTextContent();
//		logger.info("Running analysis on text '{}' of type '{}'.", textTitle, fileType);
//
//		//If the file is not BASE64 encoded, it must be plain text. Run the analysis directly.
//		if(!Base64.isBase64(textContent) || fileType.equals("text/plain")) {
//			logger.info("File is plain text, running the analysis...");
//			Item resultItem = runAE(textTemplate, analysisEngine);
//			//			responseCollectionBuilder = Collection.builder(servletURI)
//			responseCollectionBuilder.addItem(resultItem);
////			logger.info("Analysis results returned successfully!");
////			return responseCollectionBuilder;
//		} else {
//			//if file_type not plain text (base64 encoded), decode the file content
//			logger.info("File is not plain text, decoding base64...");
//			byte[] decodedBytes = Base64.decodeBase64(textContent);
//
//			//get media type of the file
//			Metadata metaData = new Metadata();
//			metaData.set(Metadata.CONTENT_TYPE, fileType);
//			String mediaType = TikaUtils.detectMediaType(new ByteArrayInputStream(decodedBytes), metaData).toString();
//			
//
//			//check if text content compressed file. if not, Tika-parse it for content body and run analysis
//			if(!TikaUtils.isCompressedFile(new ByteArrayInputStream(decodedBytes), metaData)) {
//				logger.info("Decoded single file. Tika-Parsing content body...");
//				//single file, do tika parse to get body content from files of
//				//various formats
//				String bodyContent;
//				bodyContent = TikaUtils.parseSingleFile(new ByteArrayInputStream(decodedBytes), metaData);
//				textTemplate.setTextContent(bodyContent);
//				logger.trace("Obtained parsed body content: {}. Running analysis...", 
//						bodyContent.length() <= 40 ? bodyContent : bodyContent.substring(0, 40));
//
//				Item resultItem = ServletUtils.runAE(textTemplate, analysisEngine, servletURI);
//				responseCollectionBuilder = Collection.builder(servletURI)
//						.addItem(resultItem);
//				//				responseWriter.write(responseCollection.toString());
//				//				responseWriter.close();
//				logger.info("Analysis results returned successfully!");
//				return responseCollectionBuilder;
//
//			} 
//
//			//text content compressed file, handle compressed files
//			logger.info("Decoded compressed file. Decompressing file...");
//			//get media type of content
//			String mediaType = TikaUtils.detectMediaType(new ByteArrayInputStream(decodedBytes), metaData).toString();
//
//			if(mediaType.equals(TikaUtils.MEDIA_TYPE_ZIP)) {
//				//extract zip entries and do the analysis on each entry
//				SeekableInMemoryByteChannel inMemoryByteChannel = new SeekableInMemoryByteChannel(decodedBytes);
//				ZipFile zipFile = new ZipFile(inMemoryByteChannel);
//				Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
//				responseCollectionBuilder = Collection.builder(servletURI);
//				while(entries.hasMoreElements()) {
//					ZipArchiveEntry archiveEntry = entries.nextElement();
//					InputStream entryInputStream = zipFile.getInputStream(archiveEntry);
//
//					String bodyContent;
//					bodyContent = TikaUtils.parseSingleFile(entryInputStream, new Metadata());
//					textTemplate.setTextContent(bodyContent);
//					textTemplate.setTextTitle(archiveEntry.getName());
//					logger.trace("Obtained zip archive entry '{}' with content '{}...'. Running analysis...", 
//							textTemplate.getTextTitle(), 
//							bodyContent.length() <= 40 ? bodyContent : bodyContent.substring(0, 40));
//
//					Item resultItem = ServletUtils.runAE(textTemplate, analysisEngine, servletURI);
//					responseCollectionBuilder.addItem(resultItem);
//					logger.trace("Analysis result added to collection.");
//				}
//				zipFile.close();
//			} else if(mediaType.equals(TikaUtils.MEDIA_TYPE_TAR)) {
//				//extract tar entries and do the analysis
//				TarArchiveInputStream tarInputStream = new TarArchiveInputStream(new ByteArrayInputStream(decodedBytes));
//
//				TarArchiveEntry tarEntry;
//				while((tarEntry = tarInputStream.getNextTarEntry()) != null) {
//					byte[] entryContentBytes = new byte[(int) tarEntry.getSize()];
//					tarInputStream.read(entryContentBytes);
//
//					InputStream entryInputStream = new ByteArrayInputStream(entryContentBytes);
//
//					String bodyContent;
//					bodyContent = TikaUtils.parseSingleFile(entryInputStream, new Metadata());
//					textTemplate.setTextContent(bodyContent);
//					textTemplate.setTextTitle(tarEntry.getName());
//					logger.trace("Obtained tar archive entry '{}' with content '{}...'. Running analysis...", 
//							textTemplate.getTextTitle(), 
//							bodyContent.length() <= 40 ? bodyContent : bodyContent.substring(0, 40));
//
//					Item resultItem = ServletUtils.runAE(textTemplate, analysisEngine, servletURI);
//					responseCollectionBuilder.addItem(resultItem);
//					logger.trace("Analysis result added to collection.");
//				}
//
//			} else if(mediaType.equals(TikaUtils.MEDIA_TYPE_GZIP)) {
//
//
//			} else {
//				//unsupported compressed file
//			}
//		}
//		logger.info("Analysis results returned successfully!");
//		return responseCollectionBuilder;
//	}
}
