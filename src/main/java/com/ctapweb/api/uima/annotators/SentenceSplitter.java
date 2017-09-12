package com.ctapweb.api.uima.annotators;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

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
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator;


/**
 * For splitting tokenized sentences.
 * @author xiaobin
 *
 */
public class SentenceSplitter extends JCasAnnotator_ImplBase {


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

		//read coreNLP annotation object from CAS
		Annotation coreNLPAnnotation = null;
		Iterator<CoreNLPAnnotationObject> it = aJCas.getAllIndexedFS(CoreNLPAnnotationObject.class);
		if(it.hasNext()) {
			CoreNLPAnnotationObject annotationObj = it.next();
			byte[] objBytes = annotationObj.getObjectBytes().toArray();
			try {
				ObjectInputStream objInputStream = new ObjectInputStream(new ByteArrayInputStream(objBytes));
				coreNLPAnnotation = (Annotation) objInputStream.readObject();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//create a coreNLP sentence splitter
		WordsToSentencesAnnotator sentenceSplitter = new WordsToSentencesAnnotator();
		sentenceSplitter.annotate(coreNLPAnnotation);
		
        //store the coreNLP annotation object for future use to avoid repeating annotation
		aJCas.removeAllIncludingSubtypes(CoreNLPAnnotationObject.type);
        try {
        	ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bout);
			oos.writeObject(coreNLPAnnotation);
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
