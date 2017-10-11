package com.ctapweb.api.complexity.syntactic;

import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class SyntacticIndexes {

	//mean edit distances of consecutive sentences 
	public static double calculateEditDistancePosLocalMean(Annotation annotation) {
		return getLocalStats(annotation, SyntacticUtils.TREE_TYPE_POS).getMean();
	}

	public static double calculateEditDistancePosLocalSD(Annotation annotation) {
		return getLocalStats(annotation, SyntacticUtils.TREE_TYPE_POS).getStandardDeviation();
	}

	public static double calculateEditDistancePosGlobalMean(Annotation annotation) {
		return getGlobalStats(annotation, SyntacticUtils.TREE_TYPE_POS).getMean();
	}

	public static double calculateEditDistancePosGlobalSD(Annotation annotation) {
		return getGlobalStats(annotation, SyntacticUtils.TREE_TYPE_POS).getStandardDeviation();
	}


	public static double calculateEditDistanceTokenLocalMean(Annotation annotation) {
		return getLocalStats(annotation, SyntacticUtils.TREE_TYPE_TOKEN).getMean();
	}

	public static double calculateEditDistanceTokenLocalSD(Annotation annotation) {
		return getLocalStats(annotation, SyntacticUtils.TREE_TYPE_TOKEN).getStandardDeviation();
	}

	public static double calculateEditDistanceTokenGlobalMean(Annotation annotation) {
		return getGlobalStats(annotation, SyntacticUtils.TREE_TYPE_TOKEN).getMean();
	}

	public static double calculateEditDistanceTokenGlobalSD(Annotation annotation) {
		return getGlobalStats(annotation, SyntacticUtils.TREE_TYPE_TOKEN).getStandardDeviation();
	}

	public static double calculateEditDistanceLemmaLocalMean(Annotation annotation) {
		return getLocalStats(annotation, SyntacticUtils.TREE_TYPE_LEMMA).getMean();
	}

	public static double calculateEditDistanceLemmaLocalSD(Annotation annotation) {
		return getLocalStats(annotation, SyntacticUtils.TREE_TYPE_LEMMA).getStandardDeviation();
	}

	public static double calculateEditDistanceLemmaGlobalMean(Annotation annotation) {
		return getGlobalStats(annotation, SyntacticUtils.TREE_TYPE_LEMMA).getMean();
	}

	public static double calculateEditDistanceLemmaGlobalSD(Annotation annotation) {
		return getGlobalStats(annotation, SyntacticUtils.TREE_TYPE_LEMMA).getStandardDeviation();
	}

	private static DescriptiveStatistics getLocalStats(Annotation annotation, String treeType) {
		//get sentences
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		DescriptiveStatistics stats = new DescriptiveStatistics();
		int nSentences = sentences.size();
		for(int i = 0; i < nSentences - 1; i++) {
			CoreMap thisSent = sentences.get(i);
			CoreMap nextSent = sentences.get(i + 1);

			stats.addValue(SyntacticUtils.calculateEditDistTrees(thisSent, nextSent, treeType));
		}

		return stats;
	}



	private static DescriptiveStatistics getGlobalStats(Annotation annotation, String treeType) {
		//get sentences
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		DescriptiveStatistics stats = new DescriptiveStatistics();
		int nSentences = sentences.size();
		for(int i = 0; i < nSentences - 1; i++) {
			for(int j = i + 1; j < nSentences; j++) {
				CoreMap thisSent = sentences.get(i);
				CoreMap nextSent = sentences.get(j);
				stats.addValue(SyntacticUtils.calculateEditDistTrees(thisSent, nextSent, treeType));
			}
		}
		return stats;
	}
}

