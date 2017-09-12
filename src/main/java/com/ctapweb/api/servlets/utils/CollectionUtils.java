package com.ctapweb.api.servlets.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.xml.sax.SAXException;

import com.ctapweb.api.servlets.collection.TextTemplate;

import net.hamnaberg.json.Collection;
import net.hamnaberg.json.Error;
import net.hamnaberg.json.parser.CollectionParser;

public class CollectionUtils {
	private static final Logger logger = LogManager.getLogger();

	/**
	 * 	For parsing a collection input stream that posts a text template for analysis.
	 * 
	 * @param requestInputStream
	 * @param servletURI
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws TikaException
	 */
	public static Collection parseRequestCollection(InputStream requestInputStream, URI servletURI) 
			throws IOException, SAXException, TikaException {
		Collection collection = null;
		CollectionParser collectionParser = new CollectionParser();
		collection = collectionParser.parse(requestInputStream);

		//no template
		if(!collection.hasTemplate()) {
			//return collection object with template
			collection = new Collection.Builder(servletURI)
					.withError(Error.create("No template", "400", "The collection object does not contain a template."))
					.withTemplate(TextTemplate.generateTemplate())
					.build();
		} 	

		return collection;
	}

	private static List<TextTemplate> uncompressTarStream(byte[] decodedBytes) throws IOException, SAXException, TikaException {
		List<TextTemplate> uncompressedTexts = new ArrayList<>();
		//extract tar entries and do the analysis
		TarArchiveInputStream tarInputStream = new TarArchiveInputStream(new ByteArrayInputStream(decodedBytes));

		TarArchiveEntry tarEntry;
		while((tarEntry = tarInputStream.getNextTarEntry()) != null) {
			TextTemplate text = new TextTemplate();
			byte[] entryContentBytes = new byte[(int) tarEntry.getSize()];
			tarInputStream.read(entryContentBytes);

			InputStream entryInputStream = new ByteArrayInputStream(entryContentBytes);

			String bodyContent;
			bodyContent = TikaUtils.parseSingleFile(entryInputStream, new Metadata());
			text.setTextContent(bodyContent);
			text.setTextTitle(tarEntry.getName());
			logger.trace("Obtained tar archive entry '{}' with content '{}...'.", 
					text.getTextTitle(), 
					bodyContent.length() <= 40 ? bodyContent : bodyContent.substring(0, 40));
			uncompressedTexts.add(text);
		}
		return uncompressedTexts;
	}

	private static List<TextTemplate> uncompressGzipStream(byte[] decodedBytes) throws IOException, SAXException, TikaException {
		List<TextTemplate> uncompressedTexts = new ArrayList<>();

		GzipCompressorInputStream gzInputStream = new GzipCompressorInputStream(new ByteArrayInputStream(decodedBytes));
		TarArchiveInputStream tarInputStream = new TarArchiveInputStream(gzInputStream);

		TarArchiveEntry tarEntry;
		while((tarEntry = tarInputStream.getNextTarEntry()) != null) {
			TextTemplate text = new TextTemplate();
			byte[] entryContentBytes = new byte[(int) tarEntry.getSize()];
			tarInputStream.read(entryContentBytes);

			InputStream entryInputStream = new ByteArrayInputStream(entryContentBytes);

			String bodyContent;
			bodyContent = TikaUtils.parseSingleFile(entryInputStream, new Metadata());
			text.setTextContent(bodyContent);
			text.setTextTitle(tarEntry.getName());
			logger.trace("Obtained tar archive entry '{}' with content '{}...'.", 
					text.getTextTitle(), 
					bodyContent.length() <= 40 ? bodyContent : bodyContent.substring(0, 40));
			uncompressedTexts.add(text);
		}

		return uncompressedTexts;
	}

	private static List<TextTemplate> uncompressZipStream(byte[] decodedBytes) 
			throws IOException, SAXException, TikaException {
		List<TextTemplate> uncompressedTexts = new ArrayList<>();
		SeekableInMemoryByteChannel inMemoryByteChannel = new SeekableInMemoryByteChannel(decodedBytes);
		ZipFile zipFile = new ZipFile(inMemoryByteChannel);
		Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();

		while(entries.hasMoreElements()) {
			TextTemplate text = new TextTemplate();
			ZipArchiveEntry archiveEntry = entries.nextElement();
			InputStream entryInputStream = zipFile.getInputStream(archiveEntry);

			String bodyContent;
			bodyContent = TikaUtils.parseSingleFile(entryInputStream, new Metadata());
			text.setTextContent(bodyContent);
			text.setTextTitle(archiveEntry.getName());
			logger.trace("Obtained zip archive entry '{}' with content '{}...'.", 
					text.getTextTitle(), 
					bodyContent.length() <= 40 ? bodyContent : bodyContent.substring(0, 40));
			uncompressedTexts.add(text);

			//			Item resultItem = ServletUtils.runAE(textTemplate, analysisEngine, servletURI);
			//			responseCollectionBuilder.addItem(resultItem);
			//			logger.trace("Analysis result added to collection.");
		}
		zipFile.close();
		return uncompressedTexts;
	}




	/**
	 * Gets a list of texts from a collection.
	 * @param collection
	 * @return a list of texts, null if file format unknown
	 * @throws IOException 
	 * @throws TikaException 
	 * @throws SAXException 
	 */
	public static List<TextTemplate> getTexts(Collection collection) 
			throws IOException, SAXException, TikaException {
		List<TextTemplate> texts = new ArrayList<>();
		TextTemplate textTemplate = new TextTemplate(collection.getTemplate().get());

		if(isTemplateContentPlainText(textTemplate)) {
			logger.info("File is plain text."); 
			texts.add(textTemplate);
		} else {
			//not plain text, decode the content
			logger.info("File is not plain text, decoding base64...");
			byte[] decodedBytes = Base64.decodeBase64(textTemplate.getTextContent());	

			//get media type of the file
			Metadata metaData = new Metadata();
			metaData.set(Metadata.CONTENT_TYPE, textTemplate.getFileType());

			String mediaType = TikaUtils.detectMediaType(new ByteArrayInputStream(decodedBytes), metaData).toString();

			//deal with different media types 
			if(mediaType.equals(TikaUtils.MEDIA_TYPE_ZIP)) {
				//extract zip entries 
				texts.addAll(uncompressZipStream(decodedBytes));
			} else if (mediaType.equals(TikaUtils.MEDIA_TYPE_TAR)) {
				//extract tar entries 
				texts.addAll(uncompressTarStream(decodedBytes));
			} else if (mediaType.equals(TikaUtils.MEDIA_TYPE_GZIP)) {
				//extract gzip entries
				texts.addAll(uncompressGzipStream(decodedBytes));
			} else if (mediaType.equals(TikaUtils.MEDIA_TYPE_TEXT) || mediaType.equals(TikaUtils.MEDIA_TYPE_PDF) || 
					mediaType.equals(TikaUtils.MEDIA_TYPE_MSWORD)){
				//single file, do tika parse to get body content from files of various formats
				logger.info("Decoded single file. Tika-Parsing content body...");
				String bodyContent = TikaUtils.parseSingleFile(new ByteArrayInputStream(decodedBytes), metaData);
				textTemplate.setTextContent(bodyContent);
				logger.trace("Obtained parsed body content: {}.", 
						bodyContent.length() <= 40 ? bodyContent : bodyContent.substring(0, 40));
				texts.add(textTemplate);
			} else {
				//unknown file format
				return null;
			}
		}

		return texts;
	}

	/**
	 * Check if the content of the template in a collection is plain text.
	 * @param collection
	 * @return
	 */
	public static boolean isTemplateContentPlainText(TextTemplate textTemplate) {
		boolean isPlainText = false;
		String textContent = textTemplate.getTextContent();
		String fileType = textTemplate.getFileType();

		if(!Base64.isBase64(textContent) || fileType.equals("text/plain")) {
			isPlainText = true;
		}

		return isPlainText;
	}



}
