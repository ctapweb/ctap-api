package com.ctapweb.api.servlets.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;


public class TikaUtils {
	
	private static Logger logger = LogManager.getLogger();
	
	public static String MEDIA_TYPE_ZIP = "application/zip";
	public static String MEDIA_TYPE_GZIP = "application/gzip";
	public static String MEDIA_TYPE_TAR = "application/x-tar";
	public static String MEDIA_TYPE_MSWORD = "application/msword";
	public static String MEDIA_TYPE_PDF = "application/pdf";
	public static String MEDIA_TYPE_TEXT = "text/plain";

	public static boolean isCompressedFile(InputStream ins) 
			throws IOException {
		String mediaType = detectMediaType(ins);

//		logger.trace("Detected media type: {}.", mediaType);

		return mediaType.equals(MEDIA_TYPE_ZIP) || 
				mediaType.equals(MEDIA_TYPE_GZIP) || 
				mediaType.equals(MEDIA_TYPE_TAR);
		
	}
	
	/**
	 * Detects the media type of the inputstream using Tika.
	 * @param inputStream
	 * @param metaData
	 * @return
	 * @throws IOException
	 */
	public static String detectMediaType(InputStream inputStream) 
			throws IOException {
		
		DefaultDetector detector = new DefaultDetector();
//		TikaInputStream tikaInputStream = TikaInputStream.get(inputStream);
//		MediaType mediaType = detector.detect(tikaInputStream, new Metadata());
		MediaType mediaType = detector.detect(inputStream, new Metadata());
		
		//TODO be careful with this
//		tikaInputStream.reset();
		
		return mediaType.toString();
	}
	
	/**
	 * Parse a single file to get the body content. A file can be in any format Tika recognizes.
	 * e.g. pdf, msword, plain texts...
	 * @param inputStream
	 * @param metaData
	 * @return
	 * @throws TikaException 
	 * @throws SAXException 
	 * @throws IOException 
	 */
	public static String parseSingleFileForContent(InputStream inputStream) 
			throws IOException, SAXException, TikaException {
		AutoDetectParser tikaParser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler(-1);
		tikaParser.parse(inputStream, handler, new Metadata());
		
		return handler.toString();	
		
	}
	
}
