package com.ctapweb.api.lexical.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class LexicalPOS {

	public static List<String> getTokensAdjectives(Annotation annotation, boolean lemma) {
		List<String> adjectiveTokens = new ArrayList<>();
		adjectiveTokens.addAll(LexicalUtils.getPOSTokens(annotation, "JJ", lemma));
		adjectiveTokens.addAll(LexicalUtils.getPOSTokens(annotation, "JJR", lemma));
		adjectiveTokens.addAll(LexicalUtils.getPOSTokens(annotation, "JJS", lemma));
		return adjectiveTokens;
	}

	public static List<String> getTokensAdverbs(Annotation annotation, boolean lemma) {
		List<String> adverbTokens = new ArrayList<>();
		adverbTokens.addAll(LexicalUtils.getPOSTokens(annotation, "RB", lemma));
		adverbTokens.addAll(LexicalUtils.getPOSTokens(annotation, "RBR", lemma));
		adverbTokens.addAll(LexicalUtils.getPOSTokens(annotation, "RBS", lemma));
		return adverbTokens;
	}

	public static List<String> getTokensLexicals(Annotation annotation, boolean lemma) {
		List<String> lexicalTokens = new ArrayList<>();
		lexicalTokens.addAll(getTokensNouns(annotation, lemma));
		lexicalTokens.addAll(getTokensVerbs(annotation, lemma));
		lexicalTokens.addAll(getTokensAdjectives(annotation, lemma));
		lexicalTokens.addAll(getTokensAdjectives(annotation, lemma));
		
		return lexicalTokens;
	}

	public static int countNTokensAdjectives(Annotation annotation, boolean lemma) {
		return getTokensAdjectives(annotation, lemma).size();
	}

	public static int countNTypesAdjectives(Annotation annotation, boolean lemma) {
		return (new HashSet<>(getTokensAdjectives(annotation, lemma))).size();
	}

	public static int countNTokensAdverbs(Annotation annotation, boolean lemma) {
		return getTokensAdverbs(annotation, lemma).size();
	}

	public static int countNTypesAdverbs(Annotation annotation, boolean lemma) {
		return (new HashSet<>(getTokensAdverbs(annotation, lemma))).size();
	}

	/**
	 * Gets number of lexical tokens.
	 * @param annotation
	 * @return
	 */
	public static int countNTokensLexicals(Annotation annotation, boolean lemma) {
		return countNTokensNouns(annotation, lemma) +
				countNTokensVerbs(annotation, lemma) +
				countNTokensAdjectives(annotation, lemma) +
				countNTokensAdverbs(annotation, lemma);
	}

	/**
	 * Gets number of lexical types.
	 * @param annotation
	 * @return
	 */
	public static int countNTypesLexicals(Annotation annotation, boolean lemma) {
		return (new HashSet<>(getTokensLexicals(annotation, lemma))).size();
	}

	public static int countNTokensNouns(Annotation annotation, boolean lemma) {
		return getTokensNouns(annotation, lemma).size();
	}

	public static int countNTypesNouns(Annotation annotation, boolean lemma) {
		return (new HashSet<>(getTokensNouns(annotation, lemma))).size();
	}

	public static List<String> getTokensNouns(Annotation annotation, boolean lemma) {
		List<String> nounTokens = new ArrayList<>();
		nounTokens.addAll(LexicalUtils.getPOSTokens(annotation, "NN", lemma));
		nounTokens.addAll(LexicalUtils.getPOSTokens(annotation, "NNP", lemma));
		nounTokens.addAll(LexicalUtils.getPOSTokens(annotation, "NNPS", lemma));
		nounTokens.addAll(LexicalUtils.getPOSTokens(annotation, "NNS", lemma));
		return nounTokens;
	}

	public static List<String> getTokensNouns(CoreMap sentence, boolean lemma) {
		List<String> nounTokens = new ArrayList<>();
		nounTokens.addAll(LexicalUtils.getPOSTokens(sentence, "NN", lemma));
		nounTokens.addAll(LexicalUtils.getPOSTokens(sentence, "NNP", lemma));
		nounTokens.addAll(LexicalUtils.getPOSTokens(sentence, "NNPS", lemma));
		nounTokens.addAll(LexicalUtils.getPOSTokens(sentence, "NNS", lemma));
		return nounTokens;
	}
	
	public static List<String> getTokensPronouns(CoreMap sentence, boolean lemma) {
		List<String> nounTokens = new ArrayList<>();
		nounTokens.addAll(LexicalUtils.getPOSTokens(sentence, "PRP", lemma));
		nounTokens.addAll(LexicalUtils.getPOSTokens(sentence, "PRP$", lemma));
		nounTokens.addAll(LexicalUtils.getPOSTokens(sentence, "WP", lemma));
		nounTokens.addAll(LexicalUtils.getPOSTokens(sentence, "WP$", lemma));
		return nounTokens;
	}
	
	public static int countNTokensVerbs(Annotation annotation, boolean lemma) {
		return getTokensVerbs(annotation, lemma).size();
	}

	public static int countNTypesVerbs(Annotation annotation, boolean lemma) {
		return (new HashSet<>(getTokensNouns(annotation, lemma))).size();
	}

	public static List<String> getTokensVerbs(Annotation annotation, boolean lemma) {
		List<String> verbTokens = new ArrayList<>();
		verbTokens.addAll(LexicalUtils.getPOSTokens(annotation, "VB", lemma));
		verbTokens.addAll(LexicalUtils.getPOSTokens(annotation, "VBD", lemma));
		verbTokens.addAll(LexicalUtils.getPOSTokens(annotation, "VBG", lemma));
		verbTokens.addAll(LexicalUtils.getPOSTokens(annotation, "VBN", lemma));
		verbTokens.addAll(LexicalUtils.getPOSTokens(annotation, "VBP", lemma));
		verbTokens.addAll(LexicalUtils.getPOSTokens(annotation, "VBZ", lemma));
		return verbTokens;
	}
	
	public static List<String> getTokensVerbs(CoreMap sentence, boolean lemma) {
		List<String> verbTokens = new ArrayList<>();
		verbTokens.addAll(LexicalUtils.getPOSTokens(sentence, "VB", lemma));
		verbTokens.addAll(LexicalUtils.getPOSTokens(sentence, "VBD", lemma));
		verbTokens.addAll(LexicalUtils.getPOSTokens(sentence, "VBG", lemma));
		verbTokens.addAll(LexicalUtils.getPOSTokens(sentence, "VBN", lemma));
		verbTokens.addAll(LexicalUtils.getPOSTokens(sentence, "VBP", lemma));
		verbTokens.addAll(LexicalUtils.getPOSTokens(sentence, "VBZ", lemma));
		return verbTokens;
	}

	public static List<String> getTokensAdjectives(CoreMap sentence, boolean lemma) {
		List<String> adjectiveTokens = new ArrayList<>();
		adjectiveTokens.addAll(LexicalUtils.getPOSTokens(sentence, "JJ", lemma));
		adjectiveTokens.addAll(LexicalUtils.getPOSTokens(sentence, "JJR", lemma));
		adjectiveTokens.addAll(LexicalUtils.getPOSTokens(sentence, "JJS", lemma));
		return adjectiveTokens;
	}

	public static List<String> getTokensAdverbs(CoreMap sentence, boolean lemma) {
		List<String> adverbTokens = new ArrayList<>();
		adverbTokens.addAll(LexicalUtils.getPOSTokens(sentence, "RB", lemma));
		adverbTokens.addAll(LexicalUtils.getPOSTokens(sentence, "RBR", lemma));
		adverbTokens.addAll(LexicalUtils.getPOSTokens(sentence, "RBS", lemma));
		return adverbTokens;
	}

	public static List<String> getTokensLexicals(CoreMap sentence, boolean lemma) {
		List<String> lexicalTokens = new ArrayList<>();
		lexicalTokens.addAll(getTokensNouns(sentence, lemma));
		lexicalTokens.addAll(getTokensVerbs(sentence, lemma));
		lexicalTokens.addAll(getTokensAdjectives(sentence, lemma));
		lexicalTokens.addAll(getTokensAdjectives(sentence, lemma));
		return lexicalTokens;
	}

}
