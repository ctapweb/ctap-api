package com.ctapweb.api.measures.utils;

import java.io.IOException;
import java.util.Properties;

import com.ctapweb.api.measures.annotations.MeasureCategory.Pipelines;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoNLLUOutputter;
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
	
	public static void main(String[] args) throws IOException {
		StanfordCoreNLP pipeline = getParser();
		String text = "After the turbulent elections in Honduras, "
				+ "what happens next may be unclear, but the renewed "
				+ "triumph of President Juan Orlando Hernandez can certainly "
				+ "not be seen as a legitimate victory. He may have won the "
				+ "majority of the votes but there is still great doubt "
				+ "about the official ballot count. The country did not "
				+ "suddenly move away from democracy when the state of "
				+ "emergency was announced last Friday. According to local reports, "
				+ "state security forces have committed numerous human rights violations "
				+ "since then, and people have even been killed.";
		
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);
		
		System.out.println("Conllu output: ");
		CoNLLUOutputter.conllUPrint(annotation, System.out);

	}
	
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
			props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse");
			completePipe = new StanfordCoreNLP(props);
		}
		return completePipe;
	}

	public static StanfordCoreNLP getPipeline(String pipelineName) {
		switch (pipelineName) {
		case Pipelines.TOKENIZER_PIPELINE:
			return getTokenizer();
		case Pipelines.POSTAGGER_PIPELINE:
			return getPosTagger();
		case Pipelines.PARSER_PIPELINE:
			return getParser();
		case Pipelines.LEMMATIZER_PIPELINE:
			return getLemmatizer();
		case Pipelines.COMPLETE_PIPELINE:
			return getCompletePipe();
		default:
			return null;
		}
	}

}
