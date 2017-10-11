package com.ctapweb.api.complexity.lexical.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;

public class LexicalCounts {
	private static Logger logger = LogManager.getLogger();

	/**
	 * number of all tokens
	 * @param annotation
	 * @return
	 */
	public static int countNTokensAll(Annotation annotation) {
		return annotation.get(TokensAnnotation.class).size();
	}

	public static int countNTokensCC(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "CC");
	}

	public static int countNTypesCC(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "CC");
	}

	public static int countNTokensCD(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "CD");
	}

	public static int countNTypesCD(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "CD");
	}

	public static int countNTokensDT(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "DT");
	}

	public static int countNTypesDT(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "DT");
	}

	public static int countNTokensEX(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "EX");
	}

	public static int countNTypesEX(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "EX");
	}

	public static int countNTokensIN(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "IN");
	}

	public static int countNTypesIN(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "IN");
	}

	public static int countNTokensJJ(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "JJ");
	}

	public static int countNTypesJJ(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "JJ");
	}

	public static int countNTokensJJR(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "JJR");
	}

	public static int countNTypesJJR(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "JJR");
	}

	public static int countNTokensJJS(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "JJS");
	}

	public static int countNTypesJJS(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "JJS");
	}

	public static int countNTokensLS(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "LS");
	}

	public static int countNTypesLS(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "LS");
	}

	public static int countNTokensMD(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "MD");
	}

	public static int countNTypesMD(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "MD");
	}

	public static int countNTokensNN(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "NN");
	}

	public static int countNTypesNN(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "NN");
	}

	public static int countNTokensNNS(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "NNS");
	}

	public static int countNTypesNNS(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "NNS");
	}

	public static int countNTokensNNP(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "NNP");
	}

	public static int countNTypesNNP(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "NNP");
	}

	public static int countNTokensNNPS(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "NNPS");
	}

	public static int countNTypesNNPS(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "NNPS");
	}

	public static int countNTokensPDT(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "PDT");
	}

	public static int countNTypesPDT(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "PDT");
	}

	public static int countNTokensPOS(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "POS");
	}

	public static int countNTypesPOS(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "POS");
	}

	public static int countNTokensPRP(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "PRP");
	}

	public static int countNTypesPRP(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "PRP");
	}

	public static int countNTokensPPS(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "PP$");
	}

	public static int countNTypesPPS(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "PP$");
	}

	public static int countNTokensRB(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "RB");
	}

	public static int countNTypesRB(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "RB");
	}

	public static int countNTokensRBR(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "RBR");
	}

	public static int countNTypesRBR(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "RBR");
	}

	public static int countNTokensRBS(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "RBS");
	}

	public static int countNTypesRBS(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "RBS");
	}

	public static int countNTokensRP(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "RP");
	}

	public static int countNTypesRP(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "RP");
	}

	public static int countNTokensSYM(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "SYM");
	}

	public static int countNTypesSYM(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "SYM");
	}

	public static int countNTokensTO(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "TO");
	}

	public static int countNTypesTO(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "TO");
	}

	public static int countNTokensUH(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "UH");
	}

	public static int countNTypesUH(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "UH");
	}

	public static int countNTokensVB(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "VB");
	}

	public static int countNTypesVB(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "VB");
	}

	public static int countNTokensVBD(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "VBD");
	}

	public static int countNTypesVBD(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "VBD");
	}

	public static int countNTokensVBG(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "VBG");
	}

	public static int countNTypesVBG(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "VBG");
	}

	public static int countNTokensVBN(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "VBN");
	}

	public static int countNTypesVBN(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "VBN");
	}

	public static int countNTokensVBP(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "VBP");
	}

	public static int countNTypesVBP(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "VBP");
	}

	public static int countNTokensVBZ(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "VBZ");
	}

	public static int countNTypesVBZ(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "VBZ");
	}

	public static int countNTokensWDT(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "WDT");
	}

	public static int countNTypesWDT(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "WDT");
	}

	public static int countNTokensWP(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "WP");
	}

	public static int countNTypesWP(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "WP");
	}

	public static int countNTokensWPS(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "WP$");
	}

	public static int countNTypesWPS(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "WP$");
	}

	public static int countNTokensWRB(Annotation annotation) {
		return LexicalUtils.getN_POS_Tokens(annotation, "WRB");
	}

	public static int countNTypesWRB(Annotation annotation) {
		return LexicalUtils.getN_POS_Types(annotation, "WRB");
	}

	/**
	 * Number of punctuation marks.
	 */
	public static int countNTokensPuncts(Annotation annotation) {
		int nTokensPuncts = 0;

		List<CoreLabel> tokens = annotation.get(TokensAnnotation.class);
		for(CoreLabel token: tokens) {
			String tokenStr = token.get(TextAnnotation.class);
			if(tokenStr.matches("\\p{Punct}+")) {
				nTokensPuncts++;
			}
		}

		return nTokensPuncts;
	}

	/**
	 * Number of numberic tokens.
	 */
	public static int countNTokensNumbers(Annotation annotation) {
		int nTokensNumbers = 0;

		List<CoreLabel> tokens = annotation.get(TokensAnnotation.class);
		for(CoreLabel token: tokens) {
			String tokenStr = token.get(TextAnnotation.class);
			if(tokenStr.matches("\\p{Digit}+")) {
				nTokensNumbers++;
			}
		}

		return nTokensNumbers;
	}

	/**
	 * number of all word types
	 * @param annotation
	 * @return
	 */
	public static int countNTypesAll(Annotation annotation) {
		return LexicalUtils.getWordTypes(annotation, false).size();
	}

	/**
	 * number of punctuation types
	 * @param annotation
	 * @return
	 */
	public static int countNTypesPuncts(Annotation annotation) {
		int nTypesPuncts = 0;
		for(String wordType: LexicalUtils.getWordTypes(annotation, false)) {
			if(wordType.matches("\\p{Punct}+")) {
				nTypesPuncts++;
			}
		}

		return nTypesPuncts;
	}	

	/**
	 * number of numberic types
	 * @param annotation
	 * @return
	 */
	public static int countNTypesNumbers(Annotation annotation) {
		int nTypesPuncts = 0;
		for(String wordType: LexicalUtils.getWordTypes(annotation, false)) {
			if(wordType.matches("\\p{Digit}+")) {
				nTypesPuncts++;
			}
		}

		return nTypesPuncts;
	}	

	/**
	 * Get the number of words that are used only once.
	 * @param annotation
	 * @return
	 */
	public static int countNHapax(Annotation annotation) {
		int nHapax = 0;
		List<String> wordTokens = LexicalUtils.getWordTokens(annotation, false);
		Map<String, Integer> wordCounts = new HashMap<>();

		//count the occurence of tokens
		for(String token: wordTokens) {
			if(wordCounts.containsKey(token)) {
				int count = wordCounts.get(token).intValue();
				wordCounts.put(token, ++count);
			} else {
				wordCounts.put(token, 1);
			}
		}

		//get the hapax words
		for(String key: wordCounts.keySet()) {
			int count = wordCounts.get(key).intValue();
			if(count == 1) {
				nHapax++;
			}
		}

		return nHapax;
	}

	/**
	 * number of all word tokens
	 * @param annotation
	 * @return
	 */
	public static int countNTokensWords(Annotation annotation) {
		return countNTokensAll(annotation) - countNTokensNumbers(annotation) - countNTokensNumbers(annotation);
	}


	/**
	 * Number of non-numeric, non-punctuational word types.
	 * @param annotation
	 * @return
	 */
	public static int countNTypesWords(Annotation annotation) {
		return countNTypesAll(annotation) - countNTypesNumbers(annotation) - countNTypesPuncts(annotation);
	}

	public static int countNSyllables(Annotation annotation) {
		List<String> tokens = LexicalUtils.getWordTokens(annotation, false);
		int sumSyllables = 0;

		//annotate syllables for each token 
		for(String tokenStr: tokens) {
			sumSyllables += calcNSyllablesForWord(tokenStr);
		}

		return sumSyllables;
	}

	private static int calcNSyllablesForWord(String word) {
		int nSyllables = 0;
		//annotate syllables
		//solution from http://stackoverflow.com/questions/33425070/how-to-calculate-syllables-in-text-with-regex-and-java
		if (word.charAt(word.length()-1) == 'e') {
			if (isSilentE(word)){						//silent e, don't count.  
				String newToken= word.substring(0, word.length()-1); //deal with the rest of word
				nSyllables = calcNSyllables(newToken);
			} else {
				//not silent e, count as a syllable
				nSyllables++;
			}
		} else {
			nSyllables = calcNSyllables(word);
		}

		return nSyllables;
	}

	private static int calcNSyllables(String tokenStr) {
		int nSyllables = 0;
		Pattern splitter = Pattern.compile("[^aeiouy]*[aeiouy]+");
		Matcher m = splitter.matcher(tokenStr);

		while (m.find()) {
			nSyllables++;
		}

		return nSyllables;
	}

	private static boolean isSilentE(String word) {
		word = word.substring(0, word.length()-1);

		Pattern yup = Pattern.compile("[aeiouy]");
		Matcher m = yup.matcher(word);

		return m.find() ? true: false;
	}
}











