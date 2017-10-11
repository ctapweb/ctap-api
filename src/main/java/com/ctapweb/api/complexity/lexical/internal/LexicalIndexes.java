package com.ctapweb.api.complexity.lexical.internal;

import static com.ctapweb.api.complexity.lexical.internal.TTRUtils.calcMTLD;

import java.util.List;

import org.apache.commons.math3.distribution.HypergeometricDistribution;

import edu.stanford.nlp.pipeline.Annotation;

public class LexicalIndexes {

	public static double calculateMTLD(Annotation annotation) {
			List<String> allWordTokens = LexicalUtils.getWordTokens(annotation, false);
			int size = allWordTokens.size();
			double forward = calcMTLD(allWordTokens, true);
			double backward = calcMTLD(allWordTokens, false);
	//		logger.trace("size: {}, forward: {}, backward: {}", size, forward, backward);
	
			return (forward + backward) /2;
		}

	//implementation of McCarthy & Jarvis (20017) HDD, p. 465
	public static double calculateHDD(Annotation annotation) {
		double hdd = 0;
		int populationSize = LexicalCounts.countNTokensWords(annotation);
		int sampleSize = 42;
		
		//for each word type, calculate probability
		List<String> tokens = LexicalUtils.getWordTokens(annotation, false);
		for(String wordType: LexicalUtils.getWordTypes(annotation, false)) {
			int nSuccess = countOccurences(tokens, wordType);
			HypergeometricDistribution hd = 
					new HypergeometricDistribution(populationSize, nSuccess, sampleSize);
			double attr = (1 - hd.probability(0)) / sampleSize;
			hdd += attr;
		}

		return hdd;
	}

	public static void main(String[] args) {
		//testing the hypogeometric function
		HypergeometricDistribution hd = new HypergeometricDistribution(100, 10, 35);
		System.out.println(hd.probability(0));
	}

	//count the number of instances of a word
	private static int countOccurences(List<String> tokens, String word) {
		int occurences = 0;
		for(String token: tokens) {
			if(word.equals(token)) {
				occurences++;
			}
		}

		return occurences;
	}
}






