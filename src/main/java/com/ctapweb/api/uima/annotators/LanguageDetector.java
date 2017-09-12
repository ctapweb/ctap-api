package com.ctapweb.api.uima.annotators;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageResult;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;


/**
 * For detecting the language in which a document is written.
 * @author xiaobin
 *
 */
public class LanguageDetector extends JCasAnnotator_ImplBase {


	private Logger logger = LogManager.getLogger();
//	private com.optimaize.langdetect.LanguageDetector languageDetector;
	private org.apache.tika.language.detect.LanguageDetector languageDetector;

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);

		//init language detector
		languageDetector = new OptimaizeLangDetector();
		try {
			languageDetector.loadModels();
		} catch (IOException e) {
			throw logger.throwing(new ResourceInitializationException("could_not_access_data", 
					new Object[] {"languages profiles folder for language detector"}, e));
		}
		
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		String docText = aJCas.getDocumentText();

		LanguageResult result = languageDetector.detect(docText);
		String lang = result.getLanguage();
		logger.trace("Detected language: {} from text {{}...}.", lang, 
				docText.length() <= 40 ? docText : 
				docText.substring(0, 40));

		aJCas.setDocumentLanguage(lang);

	}

}
