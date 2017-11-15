package com.ctapweb.api.measure.complexity.lexical.internal;

import static com.ctapweb.api.measure.complexity.lexical.internal.TTRUtils.calcCTTR;
import static com.ctapweb.api.measure.complexity.lexical.internal.TTRUtils.calcGTTR;
import static com.ctapweb.api.measure.complexity.lexical.internal.TTRUtils.calcLogTTR;
import static com.ctapweb.api.measure.complexity.lexical.internal.TTRUtils.calcSTTR;
import static com.ctapweb.api.measure.complexity.lexical.internal.TTRUtils.calcTTR;
import static com.ctapweb.api.measure.complexity.lexical.internal.TTRUtils.calcUberTTR;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.stanford.nlp.pipeline.Annotation;

public class LexicalTTR {
	private static Logger logger = LogManager.getLogger();

	/**
	 * Calculate corrected type-token ratio, or CTTRServlet.
	 * @param annotation
	 * @return
	 */
	public static double calculateCTTRAllWords(Annotation annotation) {
		return calcCTTR(LexicalUtils.getWordTokens(annotation, false));
	}

	/**
	 * Calculate Guiraud's type-token ratio, or GTTR.
	 * @param annotation
	 * @return
	 */
	public static double calculateGTTRAllWords(Annotation annotation) {
		return calcGTTR(LexicalUtils.getWordTokens(annotation, false));
	}

	public static double calculateGTTRNouns(Annotation annotation) {
		return calcGTTR(LexicalPOS.getTokensNouns(annotation, false));
	}

	public static double calculateGTTRVerbs(Annotation annotation) {
		return calcGTTR(LexicalPOS.getTokensVerbs(annotation, false));
	}
	public static double calculateGTTRAdjectives(Annotation annotation) {
		return calcGTTR(LexicalPOS.getTokensAdjectives(annotation, false));
	}
	public static double calculateGTTRAdverbs(Annotation annotation) {
		return calcGTTR(LexicalPOS.getTokensAdverbs(annotation, false));
	}
	public static double calculateGTTRLexicals(Annotation annotation) {
		return calcGTTR(LexicalPOS.getTokensLexicals(annotation, false));
	}

	/**
	 * Calculate bilogarithmic type-token ratio.
	 * @param annotation
	 * @return
	 */
	public static double calculateLogTTRAllWords(Annotation annotation) {
		return calcLogTTR(LexicalUtils.getWordTokens(annotation, false));
	}

	public static double calculateLogTTRNouns(Annotation annotation) {
		return calcLogTTR(LexicalPOS.getTokensNouns(annotation, false));
	}
	public static double calculateLogTTRVerbs(Annotation annotation) {
		return calcLogTTR(LexicalPOS.getTokensVerbs(annotation, false));
	}
	public static double calculateLogTTRAdjectives(Annotation annotation) {
		return calcLogTTR(LexicalPOS.getTokensAdjectives(annotation, false));
	}
	public static double calculateLogTTRAdverbs(Annotation annotation) {
		return calcLogTTR(LexicalPOS.getTokensAdverbs(annotation, false));
	}
	public static double calculateLogTTRLexicals(Annotation annotation) {
		return calcLogTTR(LexicalPOS.getTokensLexicals(annotation, false));
	}

	/**
	 * Calculate mean evenly segmented type-token ratio.
	 * @param annotation
	 * @param nSegments the number of segments
	 * @return
	 */
	public static double calculateMESTTR(Annotation annotation, int nSegments) {
		double mesttr = 0;
	
		//get all word tokens, excluding puncts and numbers
		List<String> tokensList = LexicalUtils.getWordTokens(annotation, false);
	
		//divide the tokens list into a number of even segments
		int listSize = tokensList.size();
		int segLength = (int) Math.round((double)listSize / nSegments);
	
		double sumTTR = 0;
		logger.trace("Token list size: {}, segLength: {}  ", listSize, segLength);
		int i = 0;
		for(; i < nSegments; i++) {
			int toIndex = (i + 1) * segLength >= listSize ? listSize : (i + 1) * segLength;
			int fromIndex = i * segLength;
			if(fromIndex > toIndex) break;
			List<String> subList = tokensList.subList(fromIndex, toIndex);
			sumTTR += calcTTR(subList);
			logger.trace("i: {}, sumTTR: {}", i, sumTTR);
		}
		nSegments = i; //number of segments is not always 10
		logger.trace("sumTTR: {}, nSegments: {}" , sumTTR, nSegments);
		mesttr = sumTTR / nSegments;
	
		return mesttr;
	}

	/**
	 * Calculate mean segmented type-token ratio.
	 * @param annotation
	 * @param segmentLength the length of each segment
	 * @return
	 */
	public static double calculateMSTTR(Annotation annotation, int segmentLength) {
		double msttr = 0;
	
		//get all word tokens, excluding puncts and numbers
		List<String> tokensList = LexicalUtils.getWordTokens(annotation, false);
	
		//extract segments of set length and calculate TTR for each segment
		int listSize = tokensList.size();
		int nSegments = (int) Math.ceil((double)listSize / segmentLength);
		double sumTTR = 0;
		logger.trace("Token list size: {}, nSegments: {}  ", listSize, nSegments);
		for(int i = 0; i < nSegments; i++) {
			int toIndex = (i + 1) * 50 >= listSize ? listSize : (i + 1) * 50;
			List<String> subList = tokensList.subList(i * 50, toIndex);
			sumTTR += calcTTR(subList);
		}
		logger.trace("sumTTR: {}" , sumTTR);
		msttr = sumTTR / nSegments;
	
		return msttr;
	}

	/**
	 * Calculate type-token ratio.
	 * @param annotation
	 * @return
	 */
	public static double calculateTTRAllWords(Annotation annotation) {
		return calcTTR(LexicalUtils.getWordTokens(annotation, false));
	}

	/**
	 * Calculate Uber type-token ratio.
	 * @param annotation
	 * @return
	 */
	public static double calculateUberTTRAllWords(Annotation annotation) {
		return calcUberTTR(LexicalUtils.getWordTokens(annotation, false));
	}

	public static double calculateUberTTRNouns(Annotation annotation) {
		return calcUberTTR(LexicalPOS.getTokensNouns(annotation, false));
	}

	public static double calculateUberTTRVerbs(Annotation annotation) {
		return calcUberTTR(LexicalPOS.getTokensVerbs(annotation, false));
	}
	public static double calculateUberTTRAdjectives(Annotation annotation) {
		return calcUberTTR(LexicalPOS.getTokensAdjectives(annotation, false));
	}
	public static double calculateUberTTRAdverbs(Annotation annotation) {
		return calcUberTTR(LexicalPOS.getTokensAdverbs(annotation, false));
	}
	public static double calculateUberTTRLexicals(Annotation annotation) {
		return calcUberTTR(LexicalPOS.getTokensLexicals(annotation, false));
	}

	public static double calculateCTTRAdjectives(Annotation annotation) {
		return calcCTTR(LexicalPOS.getTokensAdjectives(annotation, false));
	}

	public static double calculateSTTRAllWords(Annotation annotation) {
		return calcSTTR(LexicalUtils.getWordTokens(annotation, false));
	}

	public static double calculateSTTRAdjectives(Annotation annotation) {
		return calcSTTR(LexicalPOS.getTokensAdjectives(annotation, false));
	}

	public static double calculateSTTRLexicals(Annotation annotation) {
		return calcSTTR(LexicalPOS.getTokensLexicals(annotation, false));
	}

	public static double calculateTTRAdjectives(Annotation annotation) {
		return calcTTR(LexicalPOS.getTokensAdjectives(annotation, false));
	}

	public static double calculateTTR2Adjectives(Annotation annotation) {
		return (double) LexicalPOS.countNTypesAdjectives(annotation, false) / 
				LexicalPOS.countNTokensLexicals(annotation, false);
	}

	public static double calculateCTTRAdverbs(Annotation annotation) {
		return calcCTTR(LexicalPOS.getTokensAdverbs(annotation, false));
	}

	public static double calculateSTTRAdverbs(Annotation annotation) {
		return calcSTTR(LexicalPOS.getTokensAdverbs(annotation, false));
	}

	public static double calculateTTRAdverbs(Annotation annotation) {
		return calcTTR(LexicalPOS.getTokensAdverbs(annotation, false));
	}

	public static double calculateTTR2Adverbs(Annotation annotation) {
		return (double) LexicalPOS.countNTypesAdverbs(annotation, false) / 
				LexicalPOS.countNTokensLexicals(annotation, false);
	}

	public static double calculateTTRLexicals(Annotation annotation) {
		return calcTTR(LexicalPOS.getTokensLexicals(annotation, false));
	}

	public static double calculateCTTRNouns(Annotation annotation) {
		return calcCTTR(LexicalPOS.getTokensNouns(annotation, false));
	}

	public static double calculateCTTRLexicals(Annotation annotation) {
		return calcCTTR(LexicalPOS.getTokensLexicals(annotation, false));
	}

	public static double calculateSTTRNouns(Annotation annotation) {
		return calcSTTR(LexicalPOS.getTokensNouns(annotation, false));
	}

	public static double calculateTTRNouns(Annotation annotation) {
		return calcTTR(LexicalPOS.getTokensNouns(annotation, false));
	}

	public static double calculateTTR2Nouns(Annotation annotation) {
		return (double) LexicalPOS.countNTypesNouns(annotation, false) / 
				LexicalPOS.countNTokensLexicals(annotation, false);
	}

	public static double calculateCTTRVerbs(Annotation annotation) {
		return calcCTTR(LexicalPOS.getTokensVerbs(annotation, false));
	}

	public static double calculateSTTRVerbs(Annotation annotation) {
		return calcSTTR(LexicalPOS.getTokensVerbs(annotation, false));
	}

	public static double calculateTTRVerbs(Annotation annotation) {
		return calcTTR(LexicalPOS.getTokensVerbs(annotation, false));
	}

	public static double calculateTTR2Verbs(Annotation annotation) {
		return (double) LexicalPOS.countNTypesVerbs(annotation, false) / 
				LexicalPOS.countNTokensLexicals(annotation, false);
	}

	/** For calculating lexical density with formula:  nLexicalTokens/nTokens
	 * Lexical words are nouns, verbs, adjectives, and adverbs.
	 * @param annotation
	 * @return
	 */
	public static double calculateLexicalDensity(Annotation annotation) {
		int nLexicalTokens = LexicalPOS.countNTokensLexicals(annotation, false);
		int nTokens = LexicalCounts.countNTokensWords(annotation);
	
		return nTokens != 0 ? (double)nLexicalTokens / nTokens : 0;
	}

	public static double calculateModifierVariation(Annotation annotation) {
		return (double) (LexicalPOS.countNTypesAdjectives(annotation, false) + 
				LexicalPOS.countNTypesAdverbs(annotation, false)) / 
				LexicalPOS.countNTokensLexicals(annotation, false);
	}

}
