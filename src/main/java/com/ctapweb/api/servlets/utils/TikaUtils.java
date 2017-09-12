package com.ctapweb.api.servlets.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;


public class TikaUtils {
	public static String MEDIA_TYPE_ZIP = "application/zip";
	public static String MEDIA_TYPE_GZIP = "application/gzip";
	public static String MEDIA_TYPE_TAR = "application/x-tar";
	public static String MEDIA_TYPE_MSWORD = "application/msword";
	public static String MEDIA_TYPE_PDF = "application/pdf";
	public static String MEDIA_TYPE_TEXT = "text/plain";

	public static boolean isCompressedFile(InputStream ins, Metadata metaData) 
			throws IOException {
		boolean isCompressed = false;
		
		String mediaType = detectMediaType(ins, metaData).toString();
		if(mediaType.equals(MEDIA_TYPE_ZIP) || 
				mediaType.equals(MEDIA_TYPE_GZIP) || 
				mediaType.equals(MEDIA_TYPE_TAR)) {
			isCompressed = true;
		}
		
		return isCompressed;
	}
	
	/**
	 * Detects the media type of the inputstream using Tika.
	 * @param ins
	 * @param metaData
	 * @return
	 * @throws IOException
	 */
	public static MediaType detectMediaType(InputStream ins, Metadata metaData) 
			throws IOException {
		MediaType mediaType = null;
		
		DefaultDetector detector = new DefaultDetector();
		TikaInputStream tikaInputStream = TikaInputStream.get(ins);
		mediaType = detector.detect(tikaInputStream, metaData);
		
		//TODO be careful with this
		tikaInputStream.reset();
		
		
		return mediaType;
	}
	
	/**
	 * Parse a single file to get the body content. A file can be in any format Tika recognizes.
	 * e.g. pdf, msword, plain texts...
	 * @param ins
	 * @param metaData
	 * @return
	 * @throws TikaException 
	 * @throws SAXException 
	 * @throws IOException 
	 */
	public static String parseSingleFile(InputStream ins, Metadata metaData) 
			throws IOException, SAXException, TikaException {
		AutoDetectParser tikaParser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler();
		tikaParser.parse(ins, handler, metaData);
		
		return handler.toString();	
	}
	
}
