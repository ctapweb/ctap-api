package com.ctapweb.api.servlets.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TikaUtilsTest {
	private Logger logger = LogManager.getLogger();
	private final String folder = "file_types/";

	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	InputStream singleFilePlainText = 
			classLoader.getResourceAsStream(folder + "plain_text.txt");
	InputStream singleFileWord = 
			classLoader.getResourceAsStream(folder + "msword.doc");
	InputStream singleFilePDF = 
			classLoader.getResourceAsStream(folder + "doc.pdf");
	InputStream compressedPlainTextsTAR = 
			classLoader.getResourceAsStream(folder + "compressed_plain_texts.tar");
	InputStream compressedPlainTextsGZ = 
			classLoader.getResourceAsStream(folder + "compressed_plain_texts.tar.gz");
	InputStream compressedPlainTextsZIP = 
			classLoader.getResourceAsStream(folder + "compressed_plain_texts.zip");
	InputStream compressedMixedGZ = 
			classLoader.getResourceAsStream(folder + "compressed_mix_types.tar.gz");
	InputStream compressedMixedZIP = 
			classLoader.getResourceAsStream(folder + "compressed_mix_types.zip");
	
	Metadata metaData = new Metadata();
	
	@Test
	public void testDetectMediaType() {
		
		try {
			assertEquals("text/plain",
			TikaUtils.detectMediaType(singleFilePlainText, metaData).getBaseType().toString());

			assertEquals("application/msword",
			TikaUtils.detectMediaType(singleFileWord, metaData).getBaseType().toString());

			assertEquals("application/pdf",
			TikaUtils.detectMediaType(singleFilePDF, metaData).getBaseType().toString());

			assertEquals("application/x-tar",
			TikaUtils.detectMediaType(compressedPlainTextsTAR, metaData).getBaseType().toString());

			assertEquals("application/gzip",
			TikaUtils.detectMediaType(compressedMixedGZ, metaData).getBaseType().toString());

			assertEquals("application/zip",
			TikaUtils.detectMediaType(compressedMixedZIP, metaData).getBaseType().toString());

			assertEquals("application/gzip",
			TikaUtils.detectMediaType(compressedPlainTextsGZ, metaData).getBaseType().toString());

			assertEquals("application/zip",
			TikaUtils.detectMediaType(compressedPlainTextsZIP, metaData).getBaseType().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testIsCompressed() {
		try {
			assertTrue(TikaUtils.isCompressedFile(compressedMixedGZ, metaData));
			assertTrue(TikaUtils.isCompressedFile(compressedMixedZIP, metaData));
			assertTrue(TikaUtils.isCompressedFile(compressedPlainTextsGZ, metaData));
			assertTrue(TikaUtils.isCompressedFile(compressedPlainTextsTAR, metaData));
			assertTrue(TikaUtils.isCompressedFile(compressedPlainTextsZIP, metaData));
			assertFalse(TikaUtils.isCompressedFile(singleFilePDF, metaData));
			assertFalse(TikaUtils.isCompressedFile(singleFilePlainText, metaData));
			assertFalse(TikaUtils.isCompressedFile(singleFileWord, metaData));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testParseSingleFile() {
		try {
			assertNotNull(TikaUtils.parseSingleFile(singleFilePlainText, metaData));
			assertNotNull(TikaUtils.parseSingleFile(singleFileWord, metaData));
			assertNotNull(TikaUtils.parseSingleFile(singleFilePDF, metaData));
		} catch (IOException | SAXException | TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
