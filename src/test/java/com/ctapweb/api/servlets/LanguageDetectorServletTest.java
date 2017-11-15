package com.ctapweb.api.servlets;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Assert;
import org.junit.Test;

import com.ctapweb.api.measure.servlets.collection.TextTemplate;
import com.ctapweb.api.servlets.annotators.LanguageDetectorServlet;
import com.ctapweb.api.servlets.utils.PropKeys;
import com.ctapweb.api.servlets.utils.PropertiesManager;

import net.hamnaberg.json.Collection;
import net.hamnaberg.json.parser.CollectionParser;

public class LanguageDetectorServletTest {
	private Server server = null;
	private HttpClient httpClient;
	String host, contextPath, urlPattern;
	int port;
	Properties props;

	public static void main(String[] args) {

	}

	//Initialize a server instance and a client
	public LanguageDetectorServletTest() throws Exception {
		props = PropertiesManager.getProperties();
		host = props.getProperty(PropKeys.API_HOST);
		port = Integer.parseInt(props.getProperty(PropKeys.API_PORT));
		contextPath = props.getProperty(PropKeys.API_CONTEXT_PATH);
		urlPattern = LanguageDetectorServlet.URL_PATTERN;

		server = new Server(port);
		ServletContextHandler servletContext = 
				new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContext.setContextPath(contextPath);
		servletContext.addServlet(LanguageDetectorServlet.class, urlPattern);
		server.setHandler(servletContext);

		//		server.join();
		httpClient = ServletTestUtils.getHttpClient();
	}

	@Test
	public void testDoGet() {
		try {
			server.start();
			ContentResponse response =
					httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.GET)
					.send();
			URI requestURI = response.getRequest().getURI();
			Collection collection = Collection.builder(requestURI)
					.withTemplate(TextTemplate.generateTemplate())
					.build();

			Assert.assertEquals(props.getProperty(PropKeys.CONTENT_TYPE), 
					response.getHeaders().getField(HttpHeader.CONTENT_TYPE).getValue());
			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertEquals(collection.toString(), response.getContentAsString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testPostNoTemplate() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection without template
			postFilePath = Paths.get(
					classLoader.getResource("collection/no_template.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
			Assert.assertTrue(responseCollection.hasError());
			Assert.assertTrue(responseCollection.hasTemplate());
			Assert.assertEquals(TextTemplate.generateTemplate().toString(), 
					responseCollection.getTemplate().get().toString());

			//			//test posting a collection with plain text
			//			postFilePath = Paths.get(
			//					classLoader.getResource("collection/text_template_plain_text.json").toURI());
			//			response = request.file(postFilePath).send();
			//			responseCollection = collectionParser.parse(response.getContentAsString());
			//
			//			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			//			Assert.assertFalse(responseCollection.hasError());
			//			Assert.assertFalse(responseCollection.hasTemplate());
			//			Assert.assertFalse(responseCollection.getItems().isEmpty());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testPostPlainText() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);


			//			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("collection/text_template_plain_text.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testPostPlainTextEN() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/plain_text_en.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(1, responseCollection.getItems().size());
			Assert.assertEquals("en", 
					responseCollection.getFirstItem().get().getDataAsMap().get("result-value").getValue().get().asString());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testPostPlainTextDE() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/plain_text_de.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(1, responseCollection.getItems().size());
			Assert.assertEquals("de", 
					responseCollection.getFirstItem().get().getDataAsMap().get("result-value").getValue().get().asString());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testPostPlainTextES() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/plain_text_es.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(1, responseCollection.getItems().size());
			Assert.assertEquals("es", 
					responseCollection.getFirstItem().get().getDataAsMap().get("result-value").getValue().get().asString());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testPostPlainTextFR() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/plain_text_fr.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(1, responseCollection.getItems().size());
			Assert.assertEquals("fr", 
					responseCollection.getFirstItem().get().getDataAsMap().get("result-value").getValue().get().asString());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}

	@Test
	public void testPostWordEN() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/msword_en.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(1, responseCollection.getItems().size());
			Assert.assertEquals("en", 
					responseCollection.getFirstItem().get().getDataAsMap().get("result-value").getValue().get().asString());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	@Test
	public void testPostWordDE() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/msword_de.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(1, responseCollection.getItems().size());
			Assert.assertEquals("de", 
					responseCollection.getFirstItem().get().getDataAsMap().get("result-value").getValue().get().asString());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Test
	public void testPostWordES() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/msword_es.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(1, responseCollection.getItems().size());
			Assert.assertEquals("es", 
					responseCollection.getFirstItem().get().getDataAsMap().get("result-value").getValue().get().asString());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Test
	public void testPostWordFR() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/msword_fr.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(1, responseCollection.getItems().size());
			Assert.assertEquals("fr", 
					responseCollection.getFirstItem().get().getDataAsMap().get("result-value").getValue().get().asString());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testPostPdfEN() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/pdf_en.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(1, responseCollection.getItems().size());
			Assert.assertEquals("en", 
					responseCollection.getFirstItem().get().getDataAsMap().get("result-value").getValue().get().asString());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Test
	public void testPostPdfDE() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/pdf_de.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(1, responseCollection.getItems().size());
			Assert.assertEquals("de", 
					responseCollection.getFirstItem().get().getDataAsMap().get("result-value").getValue().get().asString());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	@Test
	public void testPostPdfES() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/pdf_es.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(1, responseCollection.getItems().size());
			Assert.assertEquals("es", 
					responseCollection.getFirstItem().get().getDataAsMap().get("result-value").getValue().get().asString());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Test
	public void testPostPdfFR() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/pdf_fr.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(1, responseCollection.getItems().size());
			Assert.assertEquals("fr", 
					responseCollection.getFirstItem().get().getDataAsMap().get("result-value").getValue().get().asString());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testPostTarPlainText() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/tar_plain_texts.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(4, responseCollection.getItems().size());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testPostZipPlainText() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/zip_plain_texts.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(4, responseCollection.getItems().size());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Test
	public void testPostZipMixed() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/zip_mix_file_types.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(12, responseCollection.getItems().size());
			System.out.println(responseCollection.toString());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Test
	public void testPostGzPlainText() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;

			server.start();

			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);

			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/gzip_plain_texts.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());

			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(4, responseCollection.getItems().size());

		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	@Test
	public void testPostGzMixed() {}

	@Test
	public void testPostTarMixed() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			ContentResponse response;
			Path postFilePath;
			CollectionParser collectionParser = new CollectionParser();
			Collection responseCollection;
	
			server.start();
	
			Request request = httpClient.newRequest(host, port)
					.path(urlPattern)
					.method(HttpMethod.POST);
	
			//test posting a collection with plain text
			postFilePath = Paths.get(
					classLoader.getResource("language_detector/tar_mix_file_types.json").toURI());
			response = request.file(postFilePath).send();
			responseCollection = collectionParser.parse(response.getContentAsString());
	
			Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
			Assert.assertFalse(responseCollection.hasError());
			Assert.assertFalse(responseCollection.hasTemplate());
			Assert.assertFalse(responseCollection.getItems().isEmpty());
			Assert.assertEquals(12, responseCollection.getItems().size());
	
		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				server.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
