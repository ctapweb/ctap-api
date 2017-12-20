package com.ctapweb.api.servlets.texts;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.operations.CorpusTableOperations;
import com.ctapweb.api.db.operations.TagTableOperations;
import com.ctapweb.api.db.operations.TextTableOperations;
import com.ctapweb.api.db.operations.UserTableOperations;
import com.ctapweb.api.db.pojos.Text;
import com.ctapweb.api.servlets.exceptions.CTAPException;
import com.ctapweb.api.servlets.utils.ServletUtils;
import com.ctapweb.api.servlets.utils.TikaUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class TextsImportServlet
 */
@WebServlet(description = "for uploading texts to corpus", urlPatterns = { "/texts/import/*" })
@MultipartConfig
public class TextsImportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger();
	private Gson gson;
	private CorpusTableOperations corpusTableOperations;
	private UserTableOperations userTableOperations;
	private TagTableOperations tagTableOperations;
	private TextTableOperations textTableOperations;

	private String uploadedFileName; 
	private long corpusId; 

	/**
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @see HttpServlet#HttpServlet()
	 */
	public TextsImportServlet() throws ClassNotFoundException, IOException, SQLException {
		super();
		DataSource dataSource = DataSourceManager.getDataSource();
		gson = new GsonBuilder().setPrettyPrinting().create();
		userTableOperations = new UserTableOperations(dataSource);
		corpusTableOperations = new CorpusTableOperations(dataSource);
		tagTableOperations = new TagTableOperations(dataSource);
		textTableOperations = new TextTableOperations(dataSource);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("Requesting POST service from /texts/import/*");
		corpusId = Long.parseLong(request.getPathInfo().substring(1));

		Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
		InputStream uploadInputStream = filePart.getInputStream();
		uploadedFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.

		try {
			//check if current user owner of the corpus
			long userId = getCurrentUserId();
			if(!corpusTableOperations.isUserOwner(userId, corpusId)) {
				throw logger.throwing(new ServletException(
						new CTAPException("User " + userId + " not owner of corpus " + corpusId + ".")));
			}

			logger.trace("Uploading file {} for corpus {}...", uploadedFileName, corpusId);
			//checks if uploaded file archive format, e.g. zip, gzip, tar...
			if(TikaUtils.isCompressedFile(uploadInputStream)) {
				processCompressedFile(uploadInputStream);
			} else {
				processUncompressedFile(uploadInputStream);
			}
		} catch (Exception e) {
			throw logger.throwing(new ServletException(e));
		}
		
		//returns a link to the corpus
		String corpusUrl = request.getContextPath() + "/corpora/" + corpusId;
		response.addHeader("Link", ServletUtils.createLinkHeader(corpusUrl, "self"));
		response.setStatus(response.SC_CREATED);
	}

	private void processUncompressedFile(InputStream inputStream) 
			throws IOException, SAXException, TikaException, SQLException {
		logger.trace("Processing uncompressed file...");

		String fileContent = TikaUtils.parseSingleFileForContent(inputStream);

		Text text = new Text();
		text.setTitle(uploadedFileName);
		text.setContent(fileContent);
		text.setCorpusId(corpusId);

		textTableOperations.addEntry(text);

		logger.trace("Obtained uncompressed file '{}' with content '{}...'",
				uploadedFileName, fileContent.length() <= 40 ? fileContent : fileContent.substring(0, 40));
	}

	private void processCompressedFile(InputStream inputStream) throws IOException, CTAPException, SAXException, TikaException, SQLException {
		//get media type of content
		String mediaType = TikaUtils.detectMediaType(inputStream);
		logger.trace("Detected compressed media type {}. Processing file...", mediaType);

		if(mediaType.equals(TikaUtils.MEDIA_TYPE_ZIP)) {
			processZip(inputStream);
		} else if(mediaType.equals(TikaUtils.MEDIA_TYPE_TAR)) {
			processTar(inputStream);
		} else if(mediaType.equals(TikaUtils.MEDIA_TYPE_GZIP)) {
			processGzipTar(inputStream);
		} else {
			throw new CTAPException("Unsupported compress file.");
		}

	}

	private void processZip(InputStream inputStream) throws IOException, SAXException, TikaException, SQLException {
		logger.trace("Processing zip file...");
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);

		//When it goes to the next entry, the pointer of the input  stream is
		//automatically placed at that entry. So there is no need to use another
		//inputstream to extract the entry content. One can directly use the
		//original zip inputstream.
		ZipEntry zipEntry;
		while((zipEntry = zipInputStream.getNextEntry()) != null) {
			String entryName = zipEntry.getName();
			String entryContent = TikaUtils.parseSingleFileForContent(zipInputStream);

			Text text = new Text();
			text.setTitle(entryName);
			text.setContent(entryContent);
			text.setCorpusId(corpusId);

			textTableOperations.addEntry(text);

			logger.trace("Obtained zip archive entry '{}' with content '{}...'", 
					entryName, entryContent.length() <= 40 ? entryContent : entryContent.substring(0, 40));
		}

			
	}

	private void processTar(InputStream inputStream) throws IOException, SAXException, TikaException, SQLException {
		logger.trace("Processing tar file...");

		TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
		TarArchiveEntry tarEntry;
		while((tarEntry = tarInputStream.getNextTarEntry()) != null) {
			String entryName = tarEntry.getName();
			String entryContent = TikaUtils.parseSingleFileForContent(tarInputStream);
			
			Text text = new Text();
			text.setTitle(entryName);
			text.setContent(entryContent);
			text.setCorpusId(corpusId);

			textTableOperations.addEntry(text);

			logger.trace("Obtained tar archive entry '{}' with content '{}...'", 
					entryName, entryContent.length() <= 40 ? entryContent : entryContent.substring(0, 40));
		}
		
	}

	//assumes all gzip files are gzip compressed tar balls
	private void processGzipTar(InputStream inputStream) throws IOException, SAXException, TikaException, SQLException {
		logger.trace("Processing gzip tar file...");

		GzipCompressorInputStream gzipInputStream = new GzipCompressorInputStream(inputStream);
		
		processTar(gzipInputStream);
	}

	private String getCurrentUserEmail() {
		return SecurityUtils.getSubject().getPrincipal().toString();
	}

	private long getCurrentUserId() throws SQLException {
		return userTableOperations.getEntry(getCurrentUserEmail()).getId();
	}
}
