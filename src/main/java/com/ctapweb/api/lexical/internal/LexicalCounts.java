package com.ctapweb.api.lexical.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;

public class LexicalCounts {
	private static Logger logger = LogManager.getLogger();

	/**
	 * number of all tokens
	 * @param annotation
	 * @return
	 */
	public static int countNTokensAll(Annotation annotation) {
		return annotation.get(TokensAnnotation.class).size();
	}

	/**
	 * Number of punctuation marks.
	 */
	public static int countNTokensPuncts(Annotation annotation) {
		int nTokensPuncts = 0;

		List<CoreLabel> tokens = annotation.get(TokensAnnotation.class);
		for(CoreLabel token: tokens) {
			String tokenStr = token.get(TextAnnotation.class);
			if(tokenStr.matches("\\p{Punct}+")) {
				nTokensPuncts++;
			}
		}

		return nTokensPuncts;
	}

	/**
	 * Number of numberic tokens.
	 */
	public static int countNTokensNumbers(Annotation annotation) {
		int nTokensNumbers = 0;

		List<CoreLabel> tokens = annotation.get(TokensAnnotation.class);
		for(CoreLabel token: tokens) {
			String tokenStr = token.get(TextAnnotation.class);
			if(tokenStr.matches("\\p{Digit}+")) {
				nTokensNumbers++;
			}
		}

		return nTokensNumbers;
	}

	/**
	 * number of all word types
	 * @param annotation
	 * @return
	 */
	public static int countNTypesAll(Annotation annotation) {
		return LexicalUtils.getWordTypes(annotation, false).size();
	}

	/**
	 * number of punctuation types
	 * @param annotation
	 * @return
	 */
	public static int countNTypesPuncts(Annotation annotation) {
		int nTypesPuncts = 0;
		for(String wordType: LexicalUtils.getWordTypes(annotation, false)) {
			if(wordType.matches("\\p{Punct}+")) {
				nTypesPuncts++;
			}
		}

		return nTypesPuncts;
	}	

	/**
	 * number of numberic types
	 * @param annotation
	 * @return
	 */
	public static int countNTypesNumbers(Annotation annotation) {
		int nTypesPuncts = 0;
		for(String wordType: LexicalUtils.getWordTypes(annotation, false)) {
			if(wordType.matches("\\p{Digit}+")) {
				nTypesPuncts++;
			}
		}

		return nTypesPuncts;
	}	

	/**
	 * Get the number of words that are used only once.
	 * @param annotation
	 * @return
	 */
	public static int countNHapax(Annotation annotation) {
		int nHapax = 0;
		List<String> wordTokens = LexicalUtils.getWordTokens(annotation, false);
		Map<String, Integer> wordCounts = new HashMap<>();

		//count the occurence of tokens
		for(String token: wordTokens) {
			if(wordCounts.containsKey(token)) {
				int count = wordCounts.get(token).intValue();
				wordCounts.put(token, ++count);
			} else {
				wordCounts.put(token, 1);
			}
		}
		
		//get the hapax words
		for(String key: wordCounts.keySet()) {
			int count = wordCounts.get(key).intValue();
			if(count == 1) {
				nHapax++;
			}
		}

		return nHapax;
	}
	
	/**
	 * number of all word tokens
	 * @param annotation
	 * @return
	 */
	public static int countNTokensWords(Annotation annotation) {
		return countNTokensAll(annotation) - countNTokensNumbers(annotation) - countNTokensNumbers(annotation);
	}


	/**
	 * Number of non-numeric, non-punctuational word types.
	 * @param annotation
	 * @return
	 */
	public static int countNTypesWords(Annotation annotation) {
		return countNTypesAll(annotation) - countNTypesNumbers(annotation) - countNTypesPuncts(annotation);
	}
}











