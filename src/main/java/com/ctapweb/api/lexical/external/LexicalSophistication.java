package com.ctapweb.api.lexical.external;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.ctapweb.api.lexical.internal.LexicalCounts;

import edu.stanford.nlp.pipeline.Annotation;

public class LexicalSophistication {

	public static double getSubtlexWFTypeAllMean(Annotation annotation) throws IOException {
		return getWFFrequenciesAll(annotation, false).getMean();
	}

	public static double getSubtlexLog10WFTypeAllSD(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesAll(annotation, false).getStandardDeviation();
	}

	public static double getSubtlexWFTokenAllMean(Annotation annotation) throws IOException {
		return getWFFrequenciesAll(annotation, true).getMean();
	}


	public static double getSubtlexWFTokenNounMean(Annotation annotation) throws IOException {
		return getWFFrequenciesNoun(annotation, true).getMean();
	}
	public static double getSubtlexWFTokenNounSD(Annotation annotation) throws IOException {
		return getWFFrequenciesNoun(annotation, true).getStandardDeviation();
	}

	public static double getSubtlexWFTypeNounMean(Annotation annotation) throws IOException {
		return getWFFrequenciesNoun(annotation, false).getMean();
	}
	public static double getSubtlexWFTypeNounSD(Annotation annotation) throws IOException {
		return getWFFrequenciesNoun(annotation, false).getStandardDeviation();
	}

	public static double getSubtlexWFTokenVerbMean(Annotation annotation) throws IOException {
		return getWFFrequenciesVerb(annotation, true).getMean();
	}
	public static double getSubtlexWFTokenVerbSD(Annotation annotation) throws IOException {
		return getWFFrequenciesVerb(annotation, true).getStandardDeviation();
	}

	public static double getSubtlexWFTypeVerbMean(Annotation annotation) throws IOException {
		return getWFFrequenciesVerb(annotation, false).getMean();
	}
	public static double getSubtlexWFTypeVerbSD(Annotation annotation) throws IOException {
		return getWFFrequenciesVerb(annotation, false).getStandardDeviation();
	}

	public static double getSubtlexWFTokenAdjectiveMean(Annotation annotation) throws IOException {
		return getWFFrequenciesAdjective(annotation, true).getMean();
	}
	public static double getSubtlexWFTokenAdjectiveSD(Annotation annotation) throws IOException {
		return getWFFrequenciesAdjective(annotation, true).getStandardDeviation();
	}

	public static double getSubtlexWFTypeAdjectiveMean(Annotation annotation) throws IOException {
		return getWFFrequenciesAdjective(annotation, false).getMean();
	}
	public static double getSubtlexWFTypeAdjectiveSD(Annotation annotation) throws IOException {
		return getWFFrequenciesAdjective(annotation, false).getStandardDeviation();
	}
	public static double getSubtlexLog10WFTokenAdverbMean(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesAdverb(annotation, true).getMean();
	}
	public static double getSubtlexLog10WFTokenAdverbSD(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesAdverb(annotation, true).getStandardDeviation();
	}

	public static double getSubtlexWFTypeAdverbMean(Annotation annotation) throws IOException {
		return getWFFrequenciesAdverb(annotation, false).getMean();
	}
	public static double getSubtlexWFTypeAdverbSD(Annotation annotation) throws IOException {
		return getWFFrequenciesAdverb(annotation, false).getStandardDeviation();
	}

	public static double getSubtlexWFTokenAllSD(Annotation annotation) throws IOException {
		return getWFFrequenciesAll(annotation, true).getStandardDeviation();
	}

	public static double getSubtlexWFTokenLexicalMean(Annotation annotation) throws IOException {
		return getWFFrequenciesLexical(annotation, true).getMean();
	}

	public static double getSubtlexWFTokenLexicalSD(Annotation annotation) throws IOException {
		return getWFFrequenciesLexical(annotation, true).getStandardDeviation();
	}

	public static double getSubtlexWFTypeLexicalMean(Annotation annotation) throws IOException {
		return getWFFrequenciesLexical(annotation, false).getMean();
	}

	public static double getSubtlexWFTypeLexicalSD(Annotation annotation) throws IOException {
		return getWFFrequenciesLexical(annotation, false).getStandardDeviation();
	}

	//get frequencies of word tokens/types
	private static DescriptiveStatistics getWFFrequenciesAll(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_WF();

		List<String> words;
		if(token) {
			//get all tokens
			words = LexicalCounts.getWordTokens(annotation, false);
		} else {
			//get all types
			words = new ArrayList<>(LexicalCounts.getWordTypes(annotation));
		}

		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}
		return frequencies;

	}

	private static DescriptiveStatistics getWFFrequenciesNoun(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_WF();

		//get tokens
		List<String> words = LexicalSophistication.getNounTokens(annotation);

		if(!token) {
			//get types
			words = new ArrayList<>(new HashSet<>(words));
		}

		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}

		return frequencies;

	}
	
	private static DescriptiveStatistics getWFFrequenciesLexical(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_WF();

		//get tokens
		List<String> words = LexicalSophistication.getLexicalTokens(annotation);

		if(!token) {
			//get types
			words = new ArrayList<>(new HashSet<>(words));
		}

		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}

		return frequencies;

	}

	private static DescriptiveStatistics getWFFrequenciesVerb(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_WF();

		//get tokens
		List<String> words = LexicalSophistication.getVerbTokens(annotation);

		if(!token) {
			//get types
			words = new ArrayList<>(new HashSet<>(words));
		}

		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}

		return frequencies;

	}
	
	private static DescriptiveStatistics getWFFrequenciesAdjective(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_WF();

		//get tokens
		List<String> words = LexicalSophistication.getAdjectiveTokens(annotation);

		if(!token) {
			//get types
			words = new ArrayList<>(new HashSet<>(words));
		}

		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}

		return frequencies;

	}
	
	private static DescriptiveStatistics getWFFrequenciesAdverb(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();

		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXus_WF();

		//get tokens
		List<String> words = LexicalSophistication.getAdverbTokens(annotation);

		if(!token) {
			//get types
			words = new ArrayList<>(new HashSet<>(words));
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
	private static DescriptiveStatistics getLog10WFFrequenciesAll(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();
	
		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10WF();
	
		List<String> words;
		if(token) {
			//get all tokens
			words = LexicalCounts.getWordTokens(annotation, false);
		} else {
			//get all types
			words = new ArrayList<>(LexicalCounts.getWordTypes(annotation));
		}
	
		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}
		return frequencies;
	
	}

	private static DescriptiveStatistics getLog10WFFrequenciesAdjective(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();
	
		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10WF();
	
		//get tokens
		List<String> words = LexicalSophistication.getAdjectiveTokens(annotation);
	
		if(!token) {
			//get types
			words = new ArrayList<>(new HashSet<>(words));
		}
	
		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}
	
		return frequencies;
	
	}

	private static DescriptiveStatistics getLog10WFFrequenciesAdverb(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();
	
		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10WF();
	
		//get tokens
		List<String> words = LexicalSophistication.getAdverbTokens(annotation);
	
		if(!token) {
			//get types
			words = new ArrayList<>(new HashSet<>(words));
		}
	
		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}
	
		return frequencies;
	
	}

	private static DescriptiveStatistics getLog10WFFrequenciesLexical(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();
	
		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10WF();
	
		//get tokens
		List<String> words = LexicalSophistication.getLexicalTokens(annotation);
	
		if(!token) {
			//get types
			words = new ArrayList<>(new HashSet<>(words));
		}
	
		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}
	
		return frequencies;
	
	}

	private static DescriptiveStatistics getLog10WFFrequenciesNoun(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();
	
		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10WF();
	
		//get tokens
		List<String> words = LexicalSophistication.getNounTokens(annotation);
	
		if(!token) {
			//get types
			words = new ArrayList<>(new HashSet<>(words));
		}
	
		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}
	
		return frequencies;
	
	}

	private static DescriptiveStatistics getLog10WFFrequenciesVerb(Annotation annotation, boolean token) 
			throws IOException {
		DescriptiveStatistics frequencies = new DescriptiveStatistics();
	
		//get frequency list
		Map<String, Double> freqMap = FrequencyListUtils.getSUBTLEXusLog10WF();
	
		//get tokens
		List<String> words = LexicalSophistication.getVerbTokens(annotation);
	
		if(!token) {
			//get types
			words = new ArrayList<>(new HashSet<>(words));
		}
	
		//look up frequency
		for(String word: words) {
			if(freqMap.containsKey(word)) {
				frequencies.addValue(freqMap.get(word));
			}
		}
	
		return frequencies;
	
	}

	public static double getSubtlexLog10WFTokenAdjectiveMean(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesAdjective(annotation, true).getMean();
	}

	public static double getSubtlexLog10WFTokenAdjectiveSD(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesAdjective(annotation, true).getStandardDeviation();
	}

	public static double getSubtlexWFTokenAdverbMean(Annotation annotation) throws IOException {
		return getWFFrequenciesAdverb(annotation, true).getMean();
	}

	public static double getSubtlexWFTokenAdverbSD(Annotation annotation) throws IOException {
		return getWFFrequenciesAdverb(annotation, true).getStandardDeviation();
	}

	public static double getSubtlexLog10WFTokenAllMean(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesAll(annotation, true).getMean();
	}

	public static double getSubtlexLog10WFTokenAllSD(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesAll(annotation, true).getStandardDeviation();
	}

	public static double getSubtlexLog10WFTokenLexicalMean(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesLexical(annotation, true).getMean();
	}

	public static double getSubtlexLog10WFTokenLexicalSD(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesLexical(annotation, true).getStandardDeviation();
	}

	public static double getSubtlexLog10WFTokenNounMean(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesNoun(annotation, true).getMean();
	}

	public static double getSubtlexLog10WFTokenNounSD(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesNoun(annotation, true).getStandardDeviation();
	}

	public static double getSubtlexLog10WFTokenVerbMean(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesVerb(annotation, true).getMean();
	}

	public static double getSubtlexLog10WFTokenVerbSD(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesVerb(annotation, true).getStandardDeviation();
	}

	public static double getSubtlexLog10WFTypeAdjectiveMean(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesAdjective(annotation, false).getMean();
	}

	public static double getSubtlexLog10WFTypeAdjectiveSD(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesAdjective(annotation, false).getStandardDeviation();
	}

	public static double getSubtlexLog10WFTypeAdverbMean(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesAdverb(annotation, false).getMean();
	}

	public static double getSubtlexLog10WFTypeAdverbSD(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesAdverb(annotation, false).getStandardDeviation();
	}

	public static double getSubtlexLog10WFTypeAllMean(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesAll(annotation, false).getMean();
	}

	public static double getSubtlexWFTypeAllSD(Annotation annotation) throws IOException {
		return getWFFrequenciesAll(annotation, false).getStandardDeviation();
	}

	public static double getSubtlexLog10WFTypeLexicalMean(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesLexical(annotation, false).getMean();
	}

	public static double getSubtlexLog10WFTypeLexicalSD(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesLexical(annotation, false).getStandardDeviation();
	}

	public static double getSubtlexLog10WFTypeNounMean(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesNoun(annotation, false).getMean();
	}

	public static double getSubtlexLog10WFTypeNounSD(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesNoun(annotation, false).getStandardDeviation();
	}

	public static double getSubtlexLog10WFTypeVerbMean(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesVerb(annotation, false).getMean();
	}

	public static double getSubtlexLog10WFTypeVerbSD(Annotation annotation) throws IOException {
		return getLog10WFFrequenciesVerb(annotation, false).getStandardDeviation();
	}

}






