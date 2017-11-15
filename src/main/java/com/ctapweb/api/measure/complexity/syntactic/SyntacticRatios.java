package com.ctapweb.api.measure.complexity.syntactic;

import static com.ctapweb.api.measure.complexity.syntactic.SyntacticCounts.countNClauses;
import static com.ctapweb.api.measure.complexity.syntactic.SyntacticCounts.countNComplexNorminals;
import static com.ctapweb.api.measure.complexity.syntactic.SyntacticCounts.countNComplexTunits;
import static com.ctapweb.api.measure.complexity.syntactic.SyntacticCounts.countNCoordinateClauses;
import static com.ctapweb.api.measure.complexity.syntactic.SyntacticCounts.countNCoordinatePhrases;
import static com.ctapweb.api.measure.complexity.syntactic.SyntacticCounts.countNDependentClauses;
import static com.ctapweb.api.measure.complexity.syntactic.SyntacticCounts.countNNounPhrases;
import static com.ctapweb.api.measure.complexity.syntactic.SyntacticCounts.countNSentences;
import static com.ctapweb.api.measure.complexity.syntactic.SyntacticCounts.countNTunits;
import static com.ctapweb.api.measure.complexity.syntactic.SyntacticCounts.countNVerbPhrases;

import com.ctapweb.api.measure.complexity.lexical.internal.LexicalCounts;

import edu.stanford.nlp.pipeline.Annotation;


public class SyntacticRatios {

	public static double calculateSynRatioCCperC(Annotation annotation) {
		return (double) countNCoordinateClauses(annotation) / countNClauses(annotation);
	}

	public static double calculateSynRatioCNperC(Annotation annotation) {
		return (double) countNComplexNorminals(annotation) / countNClauses(annotation);
	}

	public static double calculateSynRatioCNperT(Annotation annotation) {
		return (double) countNComplexNorminals(annotation) / countNTunits(annotation);
	}

	public static double calculateSynRatioCNperNP(Annotation annotation) {
		return (double) countNComplexNorminals(annotation) / countNNounPhrases(annotation);
	}
	public static double calculateSynRatioCperS(Annotation annotation) {
		return (double) countNClauses(annotation) / countNSentences(annotation);
	}

	public static double calculateSynRatioCperT(Annotation annotation) {
		return (double) countNClauses(annotation) / countNTunits(annotation);
	}

	public static double calculateSynRatioCPperC(Annotation annotation) {
		return (double) countNCoordinatePhrases(annotation) / countNClauses(annotation);
	}

	public static double calculateSynRatioCPperT(Annotation annotation) {
		return (double) countNCoordinatePhrases(annotation) / countNTunits(annotation);
	}

	public static double calculateSynRatioCTperT(Annotation annotation) {
		return (double) countNComplexTunits(annotation) / countNTunits(annotation);
	}

	public static double calculateSynRatioDCperC(Annotation annotation) {
		return (double) countNDependentClauses(annotation) / countNClauses(annotation);
	}

	public static double calculateSynRatioDCperT(Annotation annotation) {
		return (double) countNDependentClauses(annotation) / countNTunits(annotation);
	}

	public static double calculateSynRatioMLC(Annotation annotation) {
		return (double) (LexicalCounts.countNTokensAll(annotation) 
				- LexicalCounts.countNTokensPuncts(annotation)) / countNClauses(annotation);
	}

	//mean sentence length in tokens
	public static double calculateSynRatioMSLTokens(Annotation annotation) {
		return (double) LexicalCounts.countNTokensWords(annotation) / SyntacticCounts.countNSentences(annotation);
	}
	//mean sentence length in syllables
	public static double calculateSynRatioMSLSyllables(Annotation annotation) {
		return (double) LexicalCounts.countNSyllables(annotation) / SyntacticCounts.countNSentences(annotation);
	}
	public static double calculateSynRatioMLT(Annotation annotation) {
		return (double) (LexicalCounts.countNTokensAll(annotation) 
				- LexicalCounts.countNTokensPuncts(annotation)) / countNTunits(annotation);
	}

	public static double calculateSynRatioTperS(Annotation annotation) {
		return (double) countNTunits(annotation) / countNSentences(annotation);
	}

	public static double calculateSynRatioVPperT(Annotation annotation) {
		return (double) countNVerbPhrases(annotation) / countNTunits(annotation);
	}

}
