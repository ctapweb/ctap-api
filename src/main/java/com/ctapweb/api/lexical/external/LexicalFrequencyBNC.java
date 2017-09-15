package com.ctapweb.api.lexical.external;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.ctapweb.api.lexical.internal.LexicalPOS;
import com.ctapweb.api.lexical.internal.LexicalUtils;

import edu.stanford.nlp.pipeline.Annotation;

public class LexicalFrequencyBNC {

	public static double calculateTokenAdjectiveMean(Annotation annotation) throws IOException {
		return getFreqAdjectives(annotation, true).getMean();
	}

	public static double calculateTokenAdjectiveSD(Annotation annotation) throws IOException {
		return getFreqAdjectives(annotation, true).getStandardDeviation();
	}

	public static double calculateTokenAdverbMean(Annotation annotation) throws IOException {
		return getFreqAdverbs(annotation, true).getMean();
	}

	public static double calculateTokenAdverbSD(Annotation annotation) throws IOException {
		return getFreqAdverbs(annotation, true).getStandardDeviation();
	}
	public static double calculateTokenAllMean(Annotation annotation) throws IOException {
		return getFreqAllWords(annotation, true).getMean();
	}

	public static double calculateTokenAllSD(Annotation annotation) throws IOException {
		return getFreqAllWords(annotation, true).getStandardDeviation();
	}
	public static double calculateTokenLexicalMean(Annotation annotation) throws IOException {
		return getFreqLexicals(annotation, true).getMean();
	}

	public static double calculateTokenLexicalSD(Annotation annotation) throws IOException {
		return getFreqLexicals(annotation, true).getStandardDeviation();
	}

	public static double calculateTokenNounMean(Annotation annotation) throws IOException {
		return getFreqNouns(annotation, true).getMean();
	}

	public static double calculateTokenNounSD(Annotation annotation) throws IOException {
		return getFreqNouns(annotation, true).getStandardDeviation();
	}

	public static double calculateTokenVerbMean(Annotation annotation) throws IOException {
		return getFreqVerbs(annotation, true).getMean();
	}

	public static double calculateTokenVerbSD(Annotation annotation) throws IOException {
		return getFreqVerbs(annotation, true).getStandardDeviation();
	}

	public static double calculateTypeAdjectiveMean(Annotation annotation) throws IOException {
		return getFreqAdjectives(annotation, false).getMean();
	}

	public static double calculateTypeAdjectiveSD(Annotation annotation) throws IOException {
		return getFreqAdjectives(annotation, false).getStandardDeviation();
	}

	public static double calculateTypeAdverbMean(Annotation annotation) throws IOException {
		return getFreqAdverbs(annotation, false).getMean();
	}

	public static double calculateTypeAdverbSD(Annotation annotation) throws IOException {
		return getFreqAdverbs(annotation, false).getStandardDeviation();
	}

	public static double calculateTypeAllMean(Annotation annotation) throws IOException {
		return getFreqAllWords(annotation, false).getMean();
	}

	public static double calculateTypeAllSD(Annotation annotation) throws IOException {
		return getFreqAllWords(annotation, false).getStandardDeviation();
	}

	public static double calculateTypeLexicalMean(Annotation annotation) throws IOException {
		return getFreqLexicals(annotation, false).getMean();
	}

	public static double calculateTypeLexicalSD(Annotation annotation) throws IOException {
		return getFreqLexicals(annotation, false).getStandardDeviation();
	}

	public static double calculateTypeNounMean(Annotation annotation) throws IOException {
		return getFreqNouns(annotation, false).getMean();
	}

	public static double calculateTypeNounSD(Annotation annotation) throws IOException {
		return getFreqNouns(annotation, false).getStandardDeviation();
	}

	public static double calculateTypeVerbMean(Annotation annotation) throws IOException {
		return getFreqVerbs(annotation, false).getMean();
	}

	public static double calculateTypeVerbSD(Annotation annotation) throws IOException {
		return getFreqVerbs(annotation, false).getStandardDeviation();
	}
	
	private static DescriptiveStatistics getFreqAdjectives(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getBnc();

		Collection<String> words = LexicalPOS.getTokensAdjectives(annotation, false);
		if(!token) {
			words = new HashSet<>(words);
		}

		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}
		return frequencies;

	}

	private static DescriptiveStatistics getFreqAdverbs(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getBnc();

		Collection<String> words = LexicalPOS.getTokensAdverbs(annotation, false);
		if(!token) {
			words = new HashSet<>(words);
		}

		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}
		return frequencies;

	}

	//get frequencies of word tokens/types
	private static DescriptiveStatistics getFreqAllWords(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getBnc();

		Collection<String> words = LexicalUtils.getWordTokens(annotation, false);
		if(!token) {
			words = LexicalUtils.getWordTypes(annotation, false);
		}

		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}
		return frequencies;

	}

	private static DescriptiveStatistics getFreqLexicals(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getBnc();

		Collection<String> words = LexicalPOS.getTokensLexicals(annotation, false);
		if(!token) {
			words = new HashSet<>(words);
		}

		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}
		return frequencies;

	}

	private static DescriptiveStatistics getFreqNouns(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getBnc();

		Collection<String> words = LexicalPOS.getTokensNouns(annotation, false);
		if(!token) {
			words = new HashSet<>(words);
		}

		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}
		return frequencies;

	}

	private static DescriptiveStatistics getFreqVerbs(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getBnc();

		Collection<String> words = LexicalPOS.getTokensVerbs(annotation, false);
		if(!token) {
			words = new HashSet<>(words);
		}

		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}
		return frequencies;

	}

}






