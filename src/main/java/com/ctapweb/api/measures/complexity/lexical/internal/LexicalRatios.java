package com.ctapweb.api.measures.complexity.lexical.internal;

import edu.stanford.nlp.pipeline.Annotation;

public class LexicalRatios {
	public static double calculateModalPerToken(Annotation annotation) {
		return (double) LexicalCounts.countNTokensMD(annotation) / LexicalCounts.countNTokensWords(annotation);
	}
	
	public static double calculateModalPerVerb(Annotation annotation) {
		return (double) LexicalCounts.countNTokensMD(annotation) / 
				(LexicalPOS.countNTokensVerbs(annotation, false) + LexicalCounts.countNTokensMD(annotation));
	}
	
}
