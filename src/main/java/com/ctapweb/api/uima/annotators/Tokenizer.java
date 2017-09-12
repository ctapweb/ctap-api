package com.ctapweb.api.uima.annotators;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.ByteArray;
import org.apache.uima.resource.ResourceInitializationException;

import com.ctapweb.api.uima.types.CoreNLPAnnotationObject;
import com.ctapweb.api.utils.PropertiesManager;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;


/**
 * For tokeninzing the input text which has been language detected.
 * @author xiaobin
 *
 */
public class Tokenizer extends JCasAnnotator_ImplBase {


	private Logger logger = LogManager.getLogger();
	private String exceptionMessagesBundle; 


	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		try {
			exceptionMessagesBundle = 
					PropertiesManager.getProperties().getProperty("exception.messages.bundle");
		} catch (IOException e) {
			throw logger.throwing(new ResourceInitializationException(e));
		}
		
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		//get document language. if not set, throw exception
		String docLang = aJCas.getDocumentLanguage();
		if(docLang == null || docLang.isEmpty()) {
			throw logger.throwing(new AnalysisEngineProcessException(exceptionMessagesBundle, "doc_lang_not_set", new Object[]{""}));
		}

		//create a coreNLP annotation object
		Annotation document = new Annotation(aJCas.getDocumentText());
		
		//create a coreNLP tokenizer
        TokenizerAnnotator tokenizer = new TokenizerAnnotator(false, TokenizerAnnotator.TokenizerType.English);
        tokenizer.annotate(document);
        
        //get annotated results and populate the cas
//        List<CoreLabel> tokens = document.get(TokensAnnotation.class);
//        for(CoreLabel token: tokens) {
//        	Token t = new Token(aJCas);
//        	t.setBegin(token.beginPosition());
//        	t.setEnd(token.endPosition());
//        	t.addToIndexes();
//        }
        
        //store the coreNLP annotation object for future use to avoid repeating annotation
        try {
        	ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bout);
			oos.writeObject(document);
			oos.flush();
			
			byte[] objectBytes = bout.toByteArray();
			CoreNLPAnnotationObject annotationObject = new CoreNLPAnnotationObject(aJCas);
			ByteArray barray = new ByteArray(aJCas, objectBytes.length);
			barray.copyFromArray(objectBytes, 0, 0, objectBytes.length);
			annotationObject.setObjectBytes(barray);
			annotationObject.addToIndexes();
			
		} catch (IOException e) {
			throw logger.throwing(new AnalysisEngineProcessException(e));
		}
	}

}
