package com.ctapweb.api.measures.complexity.syntactic;

import costmodel.PerEditOperationStringNodeDataCostModel;
import distance.APTED;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import node.Node;
import node.StringNodeData;
import parser.BracketStringInputParser;

public class SyntacticUtils {
	public static final String TREE_TYPE_POS = "pos";
	public static final String TREE_TYPE_TOKEN = "token";
	public static final String TREE_TYPE_LEMMA = "lemma";

	// Trees should use the bracket notation {A{B{X}{Y}{F}}{C}}
	//Using library https://github.com/DatabaseGroup/apted, which has been manually 
	//built and installed as a maven library
	public static double calculateEditDistTrees(CoreMap sentence1, CoreMap sentence2, String treeType) {
		
		int lenSent1 = sentence1.get(TokensAnnotation.class).size();
		int lenSent2 = sentence2.get(TokensAnnotation.class).size();
		double meanLen = (double) (lenSent1 + lenSent2) / 2;
		
		BracketStringInputParser parser = new BracketStringInputParser();
//		Node<StringNodeData> t1 = parser.fromString("{a{b{d}{e}}{c}}");
//		Node<StringNodeData> t2 = parser.fromString("{a{b{g}}{c{f}}}");
		
		Node<StringNodeData> tree1 = parser.fromString(getTree(sentence1, treeType));
		Node<StringNodeData> tree2 = parser.fromString(getTree(sentence2, treeType));

		APTED<PerEditOperationStringNodeDataCostModel, StringNodeData> apted =
				new APTED<PerEditOperationStringNodeDataCostModel, StringNodeData>(new PerEditOperationStringNodeDataCostModel(1, 1, 1));
		double editDistance = apted.computeEditDistance(tree1, tree2);		

		//return normalized ed
		return editDistance / meanLen;
	}
	
	//type can be: POS, token, or lemma
	private static String getTree(CoreMap sentence, String type) {
		String treeStr = "";
		
		//get pos tags 
		int nTokens = 0;
		for(CoreLabel token: sentence.get(TokensAnnotation.class)) {
			String tokenStr = ""; 
			if(TREE_TYPE_POS.equals(type)) {
			 tokenStr = token.get(PartOfSpeechAnnotation.class);
			} else if(TREE_TYPE_TOKEN.equals(type)) {
			 tokenStr = token.get(TextAnnotation.class);
			} else if(TREE_TYPE_LEMMA.equals(type)) {
				tokenStr = token.get(LemmaAnnotation.class);
			}
			treeStr += "{" + tokenStr;
			nTokens++;
		}
		
		for(int i = 0; i < nTokens; i++) {
			treeStr += "}";
		}
		
		return "{" + treeStr + "}";
	}
	
}
