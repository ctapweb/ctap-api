package com.ctapweb.api.utils;

import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * Singletons for getting CoreNLP pipeline.
 * @author xiaobin
 *
 */
public class NLPPipeLinesManager {
	private static StanfordCoreNLP tokenizer = null;
	private static StanfordCoreNLP posTagger = null;
	private static StanfordCoreNLP lemmatizer = null;
	private static StanfordCoreNLP parser = null;
	private static StanfordCoreNLP completePipe = null;
	
	private NLPPipeLinesManager() {}
	
	public static StanfordCoreNLP getPosTagger() {
		//init NLP pipeline
		//NOTE: for jetty to be able to see the pos models, one would need to set jetty 
		// dependency in Eclipse jetty plugin to look into packages with "no scope"
		if(posTagger == null) {
			Properties props = new Properties();
			props.setProperty("annotators", "tokenize, ssplit, pos");
			posTagger = new StanfordCoreNLP(props);
		}
		return posTagger;
	}

	public static StanfordCoreNLP getLemmatizer() {
		//init NLP pipeline
		//NOTE: for jetty to be able to see the pos models, one would need to set jetty 
		// dependency in Eclipse jetty plugin to look into packages with "no scope"
		if(lemmatizer == null) {
			Properties props = new Properties();
			props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
			lemmatizer = new StanfordCoreNLP(props);
		}
		return lemmatizer;
	}
	
	public static StanfordCoreNLP getTokenizer() {
		if(tokenizer == null) {
			Properties props = new Properties();
			props.setProperty("annotators", "tokenize");
			tokenizer = new StanfordCoreNLP(props);
		}
		return tokenizer;
	}
	
	public static StanfordCoreNLP getParser() {
		//init NLP pipeline
		//NOTE: for jetty to be able to see the pos models, one would need to set jetty 
		// dependency in Eclipse jetty plugin to look into packages with "no scope"
		if(parser == null) {
			Properties props = new Properties();
			props.setProperty("annotators", "tokenize, ssplit, parse");
			parser = new StanfordCoreNLP(props);
		}
		return parser;
	}
	
	public static StanfordCoreNLP getCompletePipe() {
		//init NLP pipeline
		//NOTE: for jetty to be able to see the pos models, one would need to set jetty 
		// dependency in Eclipse jetty plugin to look into packages with "no scope"
		if(completePipe == null) {
			Properties props = new Properties();
			props.setProperty("annotators", "tokenize, ssplit, pos, lemma, pos, parse");
			completePipe = new StanfordCoreNLP(props);
		}
		return completePipe;
	}


}
