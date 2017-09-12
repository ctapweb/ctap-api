package com.ctapweb.api.servlets.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.ctapweb.api.servlets.collection.TextTemplate;

import net.hamnaberg.json.Collection;

public class CollectionUtilsTest {
	private final Logger logger = LogManager.getLogger();
	private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	private InputStream ins;

	@Test
	public void testParseRequestCollection() {
		Collection collection;
		try {
			ins = classLoader.getResourceAsStream("collection/no_template.json");
			collection = 
					CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			Assert.assertTrue(collection.hasError());

			ins = classLoader.getResourceAsStream("collection/text_template_plain_text.json");
			collection = 
					CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			Assert.assertFalse(collection.hasError());

			ins = classLoader.getResourceAsStream("collection/text_template_pdf.json");
			collection = 
					CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			Assert.assertFalse(collection.hasError());

			ins = classLoader.getResourceAsStream("collection/text_template_word.json");
			collection = 
					CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			Assert.assertFalse(collection.hasError());

		} catch (IOException | SAXException | TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testIsTemplateContentPlainText() {
		Collection collection;
		TextTemplate textTemplate;
		try {
			ins = classLoader.getResourceAsStream("collection/text_template_plain_text.json");
			collection = CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			textTemplate = new TextTemplate(collection.getTemplate().get());
			Assert.assertTrue(CollectionUtils.isTemplateContentPlainText(textTemplate));

			ins = classLoader.getResourceAsStream("collection/text_template_pdf.json");
			collection = CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			textTemplate = new TextTemplate(collection.getTemplate().get());
			Assert.assertFalse(CollectionUtils.isTemplateContentPlainText(textTemplate));

			ins = classLoader.getResourceAsStream("collection/text_template_word.json");
			collection = CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			textTemplate = new TextTemplate(collection.getTemplate().get());
			Assert.assertFalse(CollectionUtils.isTemplateContentPlainText(textTemplate));

			ins = classLoader.getResourceAsStream("collection/text_template_plain_text_zip.json");
			collection = CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			textTemplate = new TextTemplate(collection.getTemplate().get());
			Assert.assertFalse(CollectionUtils.isTemplateContentPlainText(textTemplate));
		} catch (IOException | SAXException | TikaException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetTexts() {
		Collection collection;
		try {
			ins = classLoader.getResourceAsStream("language_detector/plain_text_en.json");
			collection = CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			Assert.assertEquals(1, CollectionUtils.getTexts(collection).size());
			
			ins = classLoader.getResourceAsStream("language_detector/pdf_en.json");
			collection = CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			Assert.assertEquals(1, CollectionUtils.getTexts(collection).size());

			ins = classLoader.getResourceAsStream("language_detector/msword_en.json");
			collection = CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			Assert.assertEquals(1, CollectionUtils.getTexts(collection).size());

			ins = classLoader.getResourceAsStream("language_detector/tar_plain_texts.json");
			collection = CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			Assert.assertEquals(4, CollectionUtils.getTexts(collection).size());

			ins = classLoader.getResourceAsStream("language_detector/tar_mix_file_types.json");
			collection = CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			Assert.assertEquals(12, CollectionUtils.getTexts(collection).size());

			ins = classLoader.getResourceAsStream("language_detector/zip_plain_texts.json");
			collection = CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			Assert.assertEquals(4, CollectionUtils.getTexts(collection).size());

			ins = classLoader.getResourceAsStream("language_detector/zip_mix_file_types.json");
			collection = CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			Assert.assertEquals(12, CollectionUtils.getTexts(collection).size());
//			
			ins = classLoader.getResourceAsStream("language_detector/gzip_plain_texts.json");
			collection = CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			Assert.assertEquals(4, CollectionUtils.getTexts(collection).size());

			ins = classLoader.getResourceAsStream("language_detector/gzip_mix_file_types.json");
			collection = CollectionUtils.parseRequestCollection(ins, URI.create("http://example.com/test"));
			Assert.assertEquals(12, CollectionUtils.getTexts(collection).size());
			
		} catch (IOException | SAXException | TikaException e) {
			e.printStackTrace();
		}

	}
}
