package com.ctapweb.api.accuracy;

import java.io.IOException;
import java.util.List;

import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;

public class AccuracyCounts {
	//number of case errors.
	public static int countNCasingErrors(String text, String lang) throws IOException {
		return countErrorOfType(text, lang, "CASING");
	}
	
	public static int countNAmericanEnglishStyleErrors(String text, String lang) throws IOException {
		return countErrorOfType(text, lang, "AMERICAN_ENGLISH_STYLE");
	}
	
	public static int countNBritishEnglishErrors(String text, String lang) throws IOException {
		return countErrorOfType(text, lang, "BRITISH_ENGLISH");
	}

	public static int countNCollocationsErrors(String text, String lang) throws IOException {
		return countErrorOfType(text, lang, "COLLOCATIONS");
	}
	public static int countNConfusedWordsErrors(String text, String lang) throws IOException {
		return countErrorOfType(text, lang, "CONFUSED_WORDS");
	}
	public static int countNGrammarErrors(String text, String lang) throws IOException {
		return countErrorOfType(text, lang, "GRAMMAR");
	}
	public static int countNMiscErrors(String text, String lang) throws IOException {
		return countErrorOfType(text, lang, "MISC");
	}
	public static int countNPlainEnglishErrors(String text, String lang) throws IOException {
		return countErrorOfType(text, lang, "PLAIN_ENGLISH");
	}
	public static int countNTyposErrors(String text, String lang) throws IOException {
		return countErrorOfType(text, lang, "TYPOS");
	}
	public static int countNPunctuationErrors(String text, String lang) throws IOException {
		return countErrorOfType(text, lang, "PUNCTUATION");
	}
	public static int countNRedundancyErrors(String text, String lang) throws IOException {
		return countErrorOfType(text, lang, "REDUNDANCY");
	}
	public static int countNSemanticsErrors(String text, String lang) throws IOException {
		return countErrorOfType(text, lang, "SEMANTICS");
	}
	public static int countNStyleErrors(String text, String lang) throws IOException {
		return countErrorOfType(text, lang, "STYLE");
	}
	public static int countNTypographyErrors(String text, String lang) throws IOException {
		return countErrorOfType(text, lang, "TYPOGRAPHY");
	}
	public static int countNWikipediaErrors(String text, String lang) throws IOException {
		return countErrorOfType(text, lang, "WIKIPEDIA");
	}
	
	private static int countErrorOfType(String text, String lang, String category) 
			throws IOException {
		int count = 0;
		for(RuleMatch match: getRuleMatches(text, lang)) {
			if(category.equals(match.getRule().getCategory().getId())) {
				count++;
			}
		}
		return count;
	}
	
	private static List<RuleMatch> getRuleMatches(String text, String lang) throws IOException {
		JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());
		// comment in to use statistical ngram data:
		//langTool.activateLanguageModelRules(new File("/data/google-ngram-data"));
		List<RuleMatch> matches = 
				langTool.check(text);
		return matches;
	}
}
