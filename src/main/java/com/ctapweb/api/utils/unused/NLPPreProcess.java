package com.ctapweb.api.utils.unused;

import java.util.Properties;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class NLPPreProcess {

	StanfordCoreNLP pipeline;
	
	/**
	 * Initialize the pipeline with annotators. Annotators should follow the coreNLP 
	 * annotator names on <a href="https://stanfordnlp.github.io/CoreNLP/annotators.html">this page</a>.
	 * 
	 * @param annotators
	 */
	public NLPPreProcess(String annotators) {
		
		Properties props = new Properties();
		props.setProperty("annotators", annotators);
		
		pipeline = new StanfordCoreNLP(props);
		
	}
	
	/**
	 * Process document with pipeline;
	 * @param document
	 * @return
	 */
	public Annotation process(String document) {
		Annotation annotation = new Annotation(document);
		pipeline.annotate(annotation);
		
		return annotation;
	}
	
	
}
