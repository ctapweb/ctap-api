package com.ctapweb.api.lexical.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class LexicalUtils {

	/**
	 * get all word tokens, excluding puncts and numbers
	 * @param annotation
	 * @param lemma
	 * @return
	 */
	public static List<String> getWordTokens(Annotation annotation, boolean lemma) {
		List<String> tokensList = new ArrayList<>();
		List<CoreLabel> tokens = annotation.get(TokensAnnotation.class);
		for(CoreLabel token: tokens) {
			String tokenStr = lemma ? token.get(LemmaAnnotation.class) : token.get(TextAnnotation.class);
			if(tokenStr.matches("\\p{Digit}+") || tokenStr.matches("\\p{Punct}+")) {
				continue;
			}
			tokensList.add(tokenStr.toLowerCase());
		}
		return tokensList;
	}

	/**Get a certain number of word tokens from the beginning of text.
	 * 
	 * @param annotation
	 * @param limit number of word tokens to get. if limit is longer than text length, get the whole text.
	 * @return
	 */
	public static List<String> getWordTokens(Annotation annotation, int limit) {
		List<String> wordTokenList = getWordTokens(annotation, false);
		int nTokens = wordTokenList.size();
		if(limit >= nTokens) {
			return wordTokenList;
		} else {
			return wordTokenList.subList(0, limit);
		}
	}

	/**
	 * get word types as a set
	 * @param annotation
	 * @return
	 */
	public static Set<String> getWordTypes(Annotation annotation, boolean lemma) {
		Set<String> wordTypes = new HashSet<>(getWordTokens(annotation, lemma));
		return wordTypes;
	}

	/**
	 * Gets a set of word types from the beginning, limiting number of word tokens.
	 * @param annotation
	 * @param limit the number of tokens to get
	 * @return
	 */
	public static Set<String> getWordTypes(Annotation annotation, int limit) {
		return new HashSet<>(getWordTokens(annotation, limit));
	}

	/**
	 * Gets the number of word tokens of a certain pos.
	 * @param annotation
	 * @param posTag
	 * @return
	 */
	public static int getN_POS_Tokens(Annotation annotation, String posTag) {
		return getPOSTokens(annotation, posTag, false).size();
	}

	/**
	 * Gets the number of word types of a certain pos.
	 * @param annotation
	 * @param posTag
	 * @return
	 */
	public static int getN_POS_Types(Annotation annotation, String posTag) {
		Set<String> posTypes = new HashSet<>(getPOSTokens(annotation, posTag, false));
		return posTypes.size();
	}

	//get a list of tokens of designated pos 
	// if lemma == true, gets the lemma instead of the original form
	public static List<String> getPOSTokens(Annotation annotation, String posTag, boolean lemma ) {
		List<String> posTokens = new ArrayList<>();
		for(CoreLabel token: annotation.get(TokensAnnotation.class)) {
			String tokenStr = lemma? token.get(LemmaAnnotation.class) : token.get(TextAnnotation.class);
			String posStr = token.get(PartOfSpeechAnnotation.class);
			if(posTag.equals(posStr)) {
				posTokens.add(tokenStr);
			}
		}
		return posTokens;
	}
	
	//from a sentence, instead of annotation object
	public static List<String> getPOSTokens(CoreMap sentence, String posTag, boolean lemma ) {
		List<String> posTokens = new ArrayList<>();
		for(CoreLabel token: sentence.get(TokensAnnotation.class)) {
			String tokenStr = lemma? token.get(LemmaAnnotation.class) : token.get(TextAnnotation.class);
			String posStr = token.get(PartOfSpeechAnnotation.class);
			if(posTag.equals(posStr)) {
				posTokens.add(tokenStr);
			}
		}
		return posTokens;
	}
	

}
