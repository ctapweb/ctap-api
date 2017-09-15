package com.ctapweb.api.cohesion;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.ctapweb.api.lexical.internal.LexicalPOS;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class ReferentialCohesion {
	private static double calcContentWordOverlap(CoreMap sentence1, CoreMap sentence2) {
		double percentOverlap = 0;

		//get lexical lemmas from both sentences
		List<String> sent1Lexicals = new ArrayList<>();
		sent1Lexicals.addAll(LexicalPOS.getTokensLexicals(sentence1, true));

		List<String> sent2Lexicals = new ArrayList<>();
		sent2Lexicals.addAll(LexicalPOS.getTokensLexicals(sentence2, true));

		int nSent1Lexicals = sent1Lexicals.size();
		int nSent2Lexicals = sent2Lexicals.size();
		if(nSent1Lexicals == 0 && nSent2Lexicals == 0) {
			return 0;
		}

		//count number of overlapped lemmas
		int nOverlaps = 0;
		for(String lexicalLemma: sent1Lexicals) {
			if(sent2Lexicals.contains(lexicalLemma)) {
				nOverlaps += 2;
				break;
			}
		}
		
		percentOverlap = (double) nOverlaps / (nSent1Lexicals + nSent2Lexicals);

		return percentOverlap;
	}

	public static double calculateGlobalArgumentOverlap(Annotation annotation) {
		//get all sentences
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		int nSentences = sentences.size();
		if(nSentences == 0) {
			return 0;
		}

		//loop through all sentences to check overlap
		int nOverlaps = 0;
		for(int i = 0; i < nSentences; i++) {
			for(int j = i + 1; j < nSentences; j++ ) {
				if(isArgumentOverlap(sentences.get(i), sentences.get(j))) {
					nOverlaps++;
				}
			}
		}

		return (double) nOverlaps / nSentences;
	}

	public static double calculateGlobalLexicalOverlapMean(Annotation annotation) {
		//get all sentences
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		int nSentences = sentences.size();
		if(nSentences == 0) {
			return 0;
		}

		//loop through all sentences to get overlap percentage
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for(int i = 0; i < nSentences; i++) {
			for(int j = i + 1; j < nSentences; j++ ) {
				stats.addValue(calcContentWordOverlap(sentences.get(i), sentences.get(j)));

			}
		}

		return stats.getMean();
	}

	public static double calculateGlobalLexicalOverlapSD(Annotation annotation) {
		//get all sentences
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		int nSentences = sentences.size();
		if(nSentences == 0) {
			return 0;
		}

		//loop through all sentences to get overlap percentage
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for(int i = 0; i < nSentences; i++) {
			for(int j = i + 1; j < nSentences; j++ ) {
				stats.addValue(calcContentWordOverlap(sentences.get(i), sentences.get(j)));

			}
		}

		return stats.getStandardDeviation();
	}

	public static double calculateGlobalNounOverlap(Annotation annotation) {
		//get all sentences
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		int nSentences = sentences.size();
		if(nSentences == 0) {
			return 0;
		}

		//loop through all sentences to check overlap
		int nOverlaps = 0;
		for(int i = 0; i < nSentences; i++) {
			for(int j = i + 1; j < nSentences; j++ ) {
				if(isNounOverlap(sentences.get(i), sentences.get(j))) {
					nOverlaps++;
				}
			}
		}

		return (double) nOverlaps / nSentences;
	}

	public static double calculateGlobalStemOverlap(Annotation annotation) {
		//get all sentences
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		int nSentences = sentences.size();
		if(nSentences == 0) {
			return 0;
		}

		//loop through all sentences to check overlap
		int nOverlaps = 0;
		for(int i = 0; i < nSentences; i++) {
			for(int j = i + 1; j < nSentences; j++ ) {
				if(isStemOverlap(sentences.get(i), sentences.get(j))) {
					nOverlaps++;
				}
			}
		}

		return (double) nOverlaps / nSentences;
	}
	public static double calculateLocalArgumentOverlap(Annotation annotation) {
		//get all sentences
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		int nSentences = sentences.size();
		if(nSentences == 0) {
			return 0;
		}
	
		//loop through all sentences to check overlap
		int nOverlaps = 0;
		for(int i = 0; i < nSentences; i++) {
			if(isArgumentOverlap(sentences.get(i +1), sentences.get(i))) {
				nOverlaps++;
			}
		}
	
		return (double) nOverlaps / nSentences;
	}

	public static double calculateLocalLexicalOverlapMean(Annotation annotation) {
		//get all sentences
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		int nSentences = sentences.size();
		if(nSentences == 0) {
			return 0;
		}
	
		//loop through all sentences to get overlap percentage
		int nOverlaps = 0;
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for(int i = 0; i < nSentences; i++) {
			stats.addValue(calcContentWordOverlap(sentences.get(i +1), sentences.get(i)));
		}
	
		return stats.getMean();
	}

	public static double calculateLocalLexicalOverlapSD(Annotation annotation) {
		//get all sentences
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		int nSentences = sentences.size();
		if(nSentences == 0) {
			return 0;
		}
	
		//loop through all sentences to get overlap percentage
		int nOverlaps = 0;
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for(int i = 0; i < nSentences; i++) {
			stats.addValue(calcContentWordOverlap(sentences.get(i +1), sentences.get(i)));
		}
	
		return stats.getStandardDeviation();
	}

	public static double calculateLocalNounOverlap(Annotation annotation) {

		//get all sentences
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		int nSentences = sentences.size();
		if(nSentences == 0) {
			return 0;
		}

		//loop through all sentences to check overlap
		int nOverlaps = 0;
		for(int i = 0; i < nSentences; i++) {
			if(isNounOverlap(sentences.get(i +1), sentences.get(i))) {
				nOverlaps++;
			}
		}

		return (double) nOverlaps / nSentences;
	}

	public static double calculateLocalStemOverlap(Annotation annotation) {
		//get all sentences
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		int nSentences = sentences.size();
		if(nSentences == 0) {
			return 0;
		}
	
		//loop through all sentences to check overlap
		int nOverlaps = 0;
		for(int i = 0; i < nSentences; i++) {
			if(isStemOverlap(sentences.get(i +1), sentences.get(i))) {
				nOverlaps++;
			}
		}
	
		return (double) nOverlaps / nSentences;
	}

	//Arguments: nouns + pronoun lemmas
	private static boolean isArgumentOverlap(CoreMap sentence1, CoreMap sentence2) {
		boolean isOverlap = false;

		//get argument tokens for each sentence
		List<String> sent1Arguments = new ArrayList<>();
		sent1Arguments.addAll(LexicalPOS.getTokensNouns(sentence1, true));
		sent1Arguments.addAll(LexicalPOS.getTokensPronouns(sentence1, true));

		List<String> sent2Arguments = new ArrayList<>();
		sent2Arguments.addAll(LexicalPOS.getTokensNouns(sentence2, true));
		sent2Arguments.addAll(LexicalPOS.getTokensPronouns(sentence2, true));

		for(String argument: sent1Arguments) {
			if(sent2Arguments.contains(argument)) {
				isOverlap = true;
				break;
			}
		}

		return isOverlap;
	}

	private static boolean isNounOverlap(CoreMap sentence1, CoreMap sentence2) {
		boolean isOverlap = false;

		//get noun tokens for each sentence
		List<String> sent1Nouns = LexicalPOS.getTokensNouns(sentence1, false);
		List<String> sent2Nouns = LexicalPOS.getTokensNouns(sentence2, false);

		for(String noun: sent1Nouns) {
			if(sent2Nouns.contains(noun)) {
				isOverlap = true;
				break;
			}
		}

		return isOverlap;
	}

	//overlap between noun lemmas in sentence1 and lexical lemmas in sentence2
	private static boolean isStemOverlap(CoreMap sentence1, CoreMap sentence2) {
		boolean isOverlap = false;

		//get noun lemmas from sentence1 
		List<String> sent1Nouns = new ArrayList<>();
		sent1Nouns.addAll(LexicalPOS.getTokensNouns(sentence1, true));

		//get lexical lemmas from sentence 2
		List<String> sent2Stems = new ArrayList<>();
		sent2Stems.addAll(LexicalPOS.getTokensLexicals(sentence2, true));

		for(String argument: sent1Nouns) {
			if(sent2Stems.contains(argument)) {
				isOverlap = true;
				break;
			}
		}

		return isOverlap;
	}
}






