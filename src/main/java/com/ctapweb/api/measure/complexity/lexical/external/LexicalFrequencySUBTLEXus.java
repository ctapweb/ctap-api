package com.ctapweb.api.measure.complexity.lexical.external;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.ctapweb.api.measure.complexity.lexical.internal.LexicalPOS;
import com.ctapweb.api.measure.complexity.lexical.internal.LexicalUtils;

import edu.stanford.nlp.pipeline.Annotation;

public class LexicalFrequencySUBTLEXus {

	private static DescriptiveStatistics getCDAdjectives(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_CD();

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

	private static DescriptiveStatistics getCDAdverbs(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_CD();

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
	private static DescriptiveStatistics getCDAllWords(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_CD();

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


	private static DescriptiveStatistics getCDLexicals(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_CD();

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
	private static DescriptiveStatistics getCDNouns(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_CD();

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

	private static DescriptiveStatistics getCDVerbs(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_CD();

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
	private static DescriptiveStatistics getLog10CDAdjectives(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10CD();

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

	private static DescriptiveStatistics getLog10CDAdverbs(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10CD();

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
	private static DescriptiveStatistics getLog10CDAllWords(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10CD();

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

	private static DescriptiveStatistics getLog10CDLexicals(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10CD();

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
	private static DescriptiveStatistics getLog10CDNouns(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10CD();

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

	private static DescriptiveStatistics getLog10CDVerbs(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10CD();

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
	private static DescriptiveStatistics getLog10WFAdjectives(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10WF();

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

	private static DescriptiveStatistics getLog10WFAdverbs(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10WF();

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
	private static DescriptiveStatistics getLog10WFAllWords(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10WF();

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
	private static DescriptiveStatistics getLog10WFLexicals(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10WF();

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
	private static DescriptiveStatistics getLog10WFNouns(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10WF();

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

	private static DescriptiveStatistics getLog10WFVerbs(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10WF();

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
	public static double calculateLog10WFTokenAdjectiveMean(Annotation annotation) throws IOException {
		return getLog10WFAdjectives(annotation, true).getMean();
	}

	public static double calculateLog10WFTokenAdjectiveSD(Annotation annotation) throws IOException {
		return getLog10WFAdjectives(annotation, true).getStandardDeviation();
	}

	public static double calculateLog10WFTokenAdverbMean(Annotation annotation) throws IOException {
		return getLog10WFAdverbs(annotation, true).getMean();
	}

	public static double calculateLog10WFTokenAdverbSD(Annotation annotation) throws IOException {
		return getLog10WFAdverbs(annotation, true).getStandardDeviation();
	}

	public static double calculateLog10WFTokenAllMean(Annotation annotation) throws IOException {
		return getLog10WFAllWords(annotation, true).getMean();
	}

	public static double calculateLog10WFTokenAllSD(Annotation annotation) throws IOException {
		return getLog10WFAllWords(annotation, true).getStandardDeviation();
	}

	public static double calculateLog10WFTokenLexicalMean(Annotation annotation) throws IOException {
		return getLog10WFLexicals(annotation, true).getMean();
	}

	public static double calculateLog10WFTokenLexicalSD(Annotation annotation) throws IOException {
		return getLog10WFLexicals(annotation, true).getStandardDeviation();
	}
	
	public static double calculateLog10WFTokenNounMean(Annotation annotation) throws IOException {
		return getLog10WFNouns(annotation, true).getMean();
	}

	public static double calculateLog10WFTokenNounSD(Annotation annotation) throws IOException {
		return getLog10WFNouns(annotation, true).getStandardDeviation();
	}
	
	public static double calculateLog10WFTokenVerbMean(Annotation annotation) throws IOException {
		return getLog10WFVerbs(annotation, true).getMean();
	}
	
	public static double calculateLog10WFTokenVerbSD(Annotation annotation) throws IOException {
		return getLog10WFVerbs(annotation, true).getStandardDeviation();
	}

	public static double calculateLog10WFTypeAdjectiveMean(Annotation annotation) throws IOException {
		return getLog10WFAdjectives(annotation, false).getMean();
	}

	public static double calculateLog10WFTypeAdjectiveSD(Annotation annotation) throws IOException {
		return getLog10WFAdjectives(annotation, false).getStandardDeviation();
	}

	public static double calculateLog10WFTypeAdverbMean(Annotation annotation) throws IOException {
		return getLog10WFAdverbs(annotation, false).getMean();
	}

	public static double calculateLog10WFTypeAdverbSD(Annotation annotation) throws IOException {
		return getLog10WFAdverbs(annotation, false).getStandardDeviation();
	}

	public static double calculateLog10WFTypeAllMean(Annotation annotation) throws IOException {
		return getLog10WFAllWords(annotation, false).getMean();
	}

	public static double calculateLog10WFTypeAllSD(Annotation annotation) throws IOException {
		return getLog10WFAllWords(annotation, false).getStandardDeviation();
	}

	public static double calculateLog10WFTypeLexicalMean(Annotation annotation) throws IOException {
		return getLog10WFLexicals(annotation, false).getMean();
	}

	public static double calculateLog10WFTypeLexicalSD(Annotation annotation) throws IOException {
		return getLog10WFLexicals(annotation, false).getStandardDeviation();
	}

	public static double calculateLog10WFTypeNounMean(Annotation annotation) throws IOException {
		return getLog10WFNouns(annotation, false).getMean();
	}

	public static double calculateLog10WFTypeNounSD(Annotation annotation) throws IOException {
		return getLog10WFNouns(annotation, false).getStandardDeviation();
	}

	public static double calculateLog10WFTypeVerbMean(Annotation annotation) throws IOException {
		return getLog10WFVerbs(annotation, false).getMean();
	}

	public static double calculateLog10WFTypeVerbSD(Annotation annotation) throws IOException {
		return getLog10WFVerbs(annotation, false).getStandardDeviation();
	}

	public static double calculateWFTokenAdjectiveMean(Annotation annotation) throws IOException {
		return getWFAdjectives(annotation, true).getMean();
	}

	public static double calculateWFTokenAdjectiveSD(Annotation annotation) throws IOException {
		return getWFAdjectives(annotation, true).getStandardDeviation();
	}

	public static double calculateWFTokenAdverbMean(Annotation annotation) throws IOException {
		return getWFAdverbs(annotation, true).getMean();
	}

	public static double calculateWFTokenAdverbSD(Annotation annotation) throws IOException {
		return getWFAdverbs(annotation, true).getStandardDeviation();
	}

	public static double calculateWFTokenAllMean(Annotation annotation) throws IOException {
		return getWFAllWords(annotation, true).getMean();
	}

	public static double calculateWFTokenAllSD(Annotation annotation) throws IOException {
		return getWFAllWords(annotation, true).getStandardDeviation();
	}

	public static double calculateWFTokenLexicalMean(Annotation annotation) throws IOException {
		return getWFLexicals(annotation, true).getMean();
	}

	public static double calculateWFTokenLexicalSD(Annotation annotation) throws IOException {
		return getWFLexicals(annotation, true).getStandardDeviation();
	}

	public static double calculateWFTokenNounMean(Annotation annotation) throws IOException {
		return getWFNouns(annotation, true).getMean();
	}

	public static double calculateWFTokenNounSD(Annotation annotation) throws IOException {
		return getWFNouns(annotation, true).getStandardDeviation();
	}

	public static double calculateWFTokenVerbMean(Annotation annotation) throws IOException {
		return getWFVerbs(annotation, true).getMean();
	}

	public static double calculateWFTokenVerbSD(Annotation annotation) throws IOException {
		return getWFVerbs(annotation, true).getStandardDeviation();
	}

	public static double calculateWFTypeAdjectiveMean(Annotation annotation) throws IOException {
		return getWFAdjectives(annotation, false).getMean();
	}

	public static double calculateWFTypeAdjectiveSD(Annotation annotation) throws IOException {
		return getWFAdjectives(annotation, false).getStandardDeviation();
	}

	public static double calculateWFTypeAdverbMean(Annotation annotation) throws IOException {
		return getWFAdverbs(annotation, false).getMean();
	}

	public static double calculateWFTypeAdverbSD(Annotation annotation) throws IOException {
		return getWFAdverbs(annotation, false).getStandardDeviation();
	}

	public static double calculateWFTypeAllMean(Annotation annotation) throws IOException {
		return getWFAllWords(annotation, false).getMean();
	}

	public static double calculateWFTypeAllSD(Annotation annotation) throws IOException {
		return getWFAllWords(annotation, false).getStandardDeviation();
	}
	
	public static double calculateWFTypeLexicalMean(Annotation annotation) throws IOException {
		return getWFLexicals(annotation, false).getMean();
	}

	public static double calculateWFTypeLexicalSD(Annotation annotation) throws IOException {
		return getWFLexicals(annotation, false).getStandardDeviation();
	}

	public static double calculateWFTypeNounMean(Annotation annotation) throws IOException {
		return getWFNouns(annotation, false).getMean();
	}

	public static double calculateWFTypeNounSD(Annotation annotation) throws IOException {
		return getWFNouns(annotation, false).getStandardDeviation();
	}

	public static double calculateWFTypeVerbMean(Annotation annotation) throws IOException {
		return getWFVerbs(annotation, false).getMean();
	}

	public static double calculateWFTypeVerbSD(Annotation annotation) throws IOException {
		return getWFVerbs(annotation, false).getStandardDeviation();
	}	
	public static double calculateLog10CDTokenAdjectiveMean(Annotation annotation) throws IOException {
		return getLog10CDAdjectives(annotation, true).getMean();
	}

	public static double calculateLog10CDTokenAdjectiveSD(Annotation annotation) throws IOException {
		return getLog10CDAdjectives(annotation, true).getStandardDeviation();
	}

	public static double calculateLog10CDTokenAdverbMean(Annotation annotation) throws IOException {
		return getLog10CDAdverbs(annotation, true).getMean();
	}

	public static double calculateLog10CDTokenAdverbSD(Annotation annotation) throws IOException {
		return getLog10CDAdverbs(annotation, true).getStandardDeviation();
	}

	public static double calculateLog10CDTokenAllMean(Annotation annotation) throws IOException {
		return getLog10CDAllWords(annotation, true).getMean();
	}

	public static double calculateLog10CDTokenAllSD(Annotation annotation) throws IOException {
		return getLog10CDAllWords(annotation, true).getStandardDeviation();
	}

	public static double calculateLog10CDTokenLexicalMean(Annotation annotation) throws IOException {
		return getLog10CDLexicals(annotation, true).getMean();
	}

	public static double calculateLog10CDTokenLexicalSD(Annotation annotation) throws IOException {
		return getLog10CDLexicals(annotation, true).getStandardDeviation();
	}

	public static double calculateLog10CDTokenNounMean(Annotation annotation) throws IOException {
		return getLog10CDNouns(annotation, true).getMean();
	}

	public static double calculateLog10CDTokenNounSD(Annotation annotation) throws IOException {
		return getLog10CDNouns(annotation, true).getStandardDeviation();
	}

	public static double calculateLog10CDTokenVerbMean(Annotation annotation) throws IOException {
		return getLog10CDVerbs(annotation, true).getMean();
	}

	public static double calculateLog10CDTokenVerbSD(Annotation annotation) throws IOException {
		return getLog10CDVerbs(annotation, true).getStandardDeviation();
	}

	public static double calculateLog10CDTypeAdjectiveMean(Annotation annotation) throws IOException {
		return getLog10CDAdjectives(annotation, false).getMean();
	}

	public static double calculateLog10CDTypeAdjectiveSD(Annotation annotation) throws IOException {
		return getLog10CDAdjectives(annotation, false).getStandardDeviation();
	}

	public static double calculateLog10CDTypeAdverbMean(Annotation annotation) throws IOException {
		return getLog10CDAdverbs(annotation, false).getMean();
	}

	public static double calculateLog10CDTypeAdverbSD(Annotation annotation) throws IOException {
		return getLog10CDAdverbs(annotation, false).getStandardDeviation();
	}

	public static double calculateLog10CDTypeAllMean(Annotation annotation) throws IOException {
		return getLog10CDAllWords(annotation, false).getMean();
	}

	public static double calculateLog10CDTypeAllSD(Annotation annotation) throws IOException {
		return getLog10CDAllWords(annotation, false).getStandardDeviation();
	}
	
	public static double calculateLog10CDTypeLexicalMean(Annotation annotation) throws IOException {
		return getLog10CDLexicals(annotation, false).getMean();
	}

	public static double calculateLog10CDTypeLexicalSD(Annotation annotation) throws IOException {
		return getLog10CDLexicals(annotation, false).getStandardDeviation();
	}

	public static double calculateLog10CDTypeNounMean(Annotation annotation) throws IOException {
		return getLog10CDNouns(annotation, false).getMean();
	}

	public static double calculateLog10CDTypeNounSD(Annotation annotation) throws IOException {
		return getLog10CDNouns(annotation, false).getStandardDeviation();
	}

	public static double calculateLog10CDTypeVerbMean(Annotation annotation) throws IOException {
		return getLog10CDVerbs(annotation, false).getMean();
	}

	public static double calculateLog10CDTypeVerbSD(Annotation annotation) throws IOException {
		return getLog10CDVerbs(annotation, false).getStandardDeviation();
	}	
	public static double calculateCDTokenAdjectiveMean(Annotation annotation) throws IOException {
		return getCDAdjectives(annotation, true).getMean();
	}

	public static double calculateCDTokenAdjectiveSD(Annotation annotation) throws IOException {
		return getCDAdjectives(annotation, true).getStandardDeviation();
	}

	public static double calculateCDTokenAdverbMean(Annotation annotation) throws IOException {
		return getCDAdverbs(annotation, true).getMean();
	}

	public static double calculateCDTokenAdverbSD(Annotation annotation) throws IOException {
		return getCDAdverbs(annotation, true).getStandardDeviation();
	}

	public static double calculateCDTokenAllMean(Annotation annotation) throws IOException {
		return getCDAllWords(annotation, true).getMean();
	}

	public static double calculateCDTokenAllSD(Annotation annotation) throws IOException {
		return getCDAllWords(annotation, true).getStandardDeviation();
	}

	public static double calculateCDTokenLexicalMean(Annotation annotation) throws IOException {
		return getCDLexicals(annotation, true).getMean();
	}

	public static double calculateCDTokenLexicalSD(Annotation annotation) throws IOException {
		return getCDLexicals(annotation, true).getStandardDeviation();
	}

	public static double calculateCDTokenNounMean(Annotation annotation) throws IOException {
		return getCDNouns(annotation, true).getMean();
	}

	public static double calculateCDTokenNounSD(Annotation annotation) throws IOException {
		return getCDNouns(annotation, true).getStandardDeviation();
	}

	public static double calculateCDTokenVerbMean(Annotation annotation) throws IOException {
		return getCDVerbs(annotation, true).getMean();
	}

	public static double calculateCDTokenVerbSD(Annotation annotation) throws IOException {
		return getCDVerbs(annotation, true).getStandardDeviation();
	}

	public static double calculateCDTypeAdjectiveMean(Annotation annotation) throws IOException {
		return getCDAdjectives(annotation, false).getMean();
	}

	public static double calculateCDTypeAdjectiveSD(Annotation annotation) throws IOException {
		return getCDAdjectives(annotation, false).getStandardDeviation();
	}

	public static double calculateCDTypeAdverbMean(Annotation annotation) throws IOException {
		return getCDAdverbs(annotation, false).getMean();
	}

	public static double calculateCDTypeAdverbSD(Annotation annotation) throws IOException {
		return getCDAdverbs(annotation, false).getStandardDeviation();
	}

	public static double calculateCDTypeAllMean(Annotation annotation) throws IOException {
		return getCDAllWords(annotation, false).getMean();
	}

	public static double calculateCDTypeAllSD(Annotation annotation) throws IOException {
		return getCDAllWords(annotation, false).getStandardDeviation();
	}
	
	public static double calculateCDTypeLexicalMean(Annotation annotation) throws IOException {
		return getCDLexicals(annotation, false).getMean();
	}

	public static double calculateCDTypeLexicalSD(Annotation annotation) throws IOException {
		return getCDLexicals(annotation, false).getStandardDeviation();
	}

	public static double calculateCDTypeNounMean(Annotation annotation) throws IOException {
		return getCDNouns(annotation, false).getMean();
	}

	public static double calculateCDTypeNounSD(Annotation annotation) throws IOException {
		return getCDNouns(annotation, false).getStandardDeviation();
	}

	public static double calculateCDTypeVerbMean(Annotation annotation) throws IOException {
		return getCDVerbs(annotation, false).getMean();
	}

	public static double calculateCDTypeVerbSD(Annotation annotation) throws IOException {
		return getCDVerbs(annotation, false).getStandardDeviation();
	}	
	private static DescriptiveStatistics getWFAdjectives(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_WF();

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

	private static DescriptiveStatistics getWFAdverbs(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_WF();

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
	private static DescriptiveStatistics getWFAllWords(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_WF();

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

	private static DescriptiveStatistics getWFLexicals(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_WF();

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

	private static DescriptiveStatistics getWFNouns(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_WF();

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

	private static DescriptiveStatistics getWFVerbs(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_WF();

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






