package com.ctapweb.api.lexical.external;

import static com.ctapweb.api.lexical.internal.TTRUtils.calcCTTR;
import static com.ctapweb.api.lexical.internal.TTRUtils.calcGTTR;
import static com.ctapweb.api.lexical.internal.TTRUtils.calcLogTTR;
import static com.ctapweb.api.lexical.internal.TTRUtils.calcSTTR;
import static com.ctapweb.api.lexical.internal.TTRUtils.calcTTR;
import static com.ctapweb.api.lexical.internal.TTRUtils.calcUberTTR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctapweb.api.lexical.internal.LexicalCounts;
import com.ctapweb.api.lexical.internal.LexicalPOS;
import com.ctapweb.api.lexical.internal.LexicalUtils;

import edu.stanford.nlp.pipeline.Annotation;

public class LexicalSophisticationNGSL {
	private static Logger logger = LogManager.getLogger();
	private static Set<String> sophWordSet;
	private static Set<String> easyWordSet;
	
	static{
		try {
			sophWordSet = FrequencyListUtils.getNgslLemmaAll();
			easyWordSet = FrequencyListUtils.getNgslLemmaTop1000();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static double calculateSophCTTRAdjectives(Annotation annotation) throws IOException {
		return calcCTTR(getSophLemmaTokensAdjectives(annotation, 
				sophWordSet));
	}
	public static double calculateEasyCTTRAdjectives(Annotation annotation) throws IOException {
		return calcCTTR(getEasyLemmaTokensAdjectives(annotation, 
				easyWordSet));
	}
	public static double calculateEasyCTTRAdverbs(Annotation annotation) throws IOException {
		return calcCTTR(getEasyLemmaTokensAdverbs(annotation, 
				easyWordSet));
	}
	public static double calculateEasyCTTRNouns(Annotation annotation) throws IOException {
		return calcCTTR(getEasyLemmaTokensNouns(annotation, 
				easyWordSet));
	}
	public static double calculateEasyCTTRVerbs(Annotation annotation) throws IOException {
		return calcCTTR(getEasyLemmaTokensVerbs(annotation, 
				easyWordSet));
	}
	public static double calculateEasyCTTRLexicals(Annotation annotation) throws IOException {
		return calcCTTR(getEasyLemmaTokensLexicals(annotation, 
				easyWordSet));
	}
	public static double calculateEasyCTTRAllWords(Annotation annotation) throws IOException {
		return calcCTTR(getEasyLemmaTokensAllWords(annotation, 
				easyWordSet));
	}
	public static double calculateEasySTTRAdjectives(Annotation annotation) throws IOException {
		return calcSTTR(getEasyLemmaTokensAdjectives(annotation, 
				easyWordSet));
	}
	public static double calculateEasySTTRAdverbs(Annotation annotation) throws IOException {
		return calcSTTR(getEasyLemmaTokensAdverbs(annotation, 
				easyWordSet));
	}
	public static double calculateEasySTTRNouns(Annotation annotation) throws IOException {
		return calcSTTR(getEasyLemmaTokensNouns(annotation, 
				easyWordSet));
	}
	public static double calculateEasySTTRVerbs(Annotation annotation) throws IOException {
		return calcSTTR(getEasyLemmaTokensVerbs(annotation, 
				easyWordSet));
	}
	public static double calculateEasySTTRLexicals(Annotation annotation) throws IOException {
		return calcSTTR(getEasyLemmaTokensLexicals(annotation, 
				easyWordSet));
	}
	public static double calculateEasySTTRAllWords(Annotation annotation) throws IOException {
		return calcSTTR(getEasyLemmaTokensAllWords(annotation, 
				easyWordSet));
	}
	public static double calculateEasyLogTTRAdjectives(Annotation annotation) throws IOException {
		return calcLogTTR(getEasyLemmaTokensAdjectives(annotation, 
				easyWordSet));
	}
	public static double calculateEasyLogTTRAdverbs(Annotation annotation) throws IOException {
		return calcLogTTR(getEasyLemmaTokensAdverbs(annotation, 
				easyWordSet));
	}
	public static double calculateEasyLogTTRNouns(Annotation annotation) throws IOException {
		return calcLogTTR(getEasyLemmaTokensNouns(annotation, 
				easyWordSet));
	}
	public static double calculateEasyLogTTRVerbs(Annotation annotation) throws IOException {
		return calcLogTTR(getEasyLemmaTokensVerbs(annotation, 
				easyWordSet));
	}
	public static double calculateEasyLogTTRLexicals(Annotation annotation) throws IOException {
		return calcLogTTR(getEasyLemmaTokensLexicals(annotation, 
				easyWordSet));
	}
	public static double calculateEasyLogTTRAllWords(Annotation annotation) throws IOException {
		return calcLogTTR(getEasyLemmaTokensAllWords(annotation, 
				easyWordSet));
	}
	public static double calculateEasyUberTTRAdjectives(Annotation annotation) throws IOException {
		return calcUberTTR(getEasyLemmaTokensAdjectives(annotation, 
				easyWordSet));
	}
	public static double calculateEasyUberTTRAdverbs(Annotation annotation) throws IOException {
		return calcUberTTR(getEasyLemmaTokensAdverbs(annotation, 
				easyWordSet));
	}
	public static double calculateEasyUberTTRNouns(Annotation annotation) throws IOException {
		return calcUberTTR(getEasyLemmaTokensNouns(annotation, 
				easyWordSet));
	}
	public static double calculateEasyUberTTRVerbs(Annotation annotation) throws IOException {
		return calcUberTTR(getEasyLemmaTokensVerbs(annotation, 
				easyWordSet));
	}
	public static double calculateEasyUberTTRLexicals(Annotation annotation) throws IOException {
		return calcUberTTR(getEasyLemmaTokensLexicals(annotation, 
				easyWordSet));
	}
	public static double calculateEasyUberTTRAllWords(Annotation annotation) throws IOException {
		return calcUberTTR(getEasyLemmaTokensAllWords(annotation, 
				easyWordSet));
	}
	public static double calculateEasyGTTRAdjectives(Annotation annotation) throws IOException {
		return calcGTTR(getEasyLemmaTokensAdjectives(annotation, 
				easyWordSet));
	}
	public static double calculateEasyGTTRAdverbs(Annotation annotation) throws IOException {
		return calcGTTR(getEasyLemmaTokensAdverbs(annotation, 
				easyWordSet));
	}
	public static double calculateEasyGTTRNouns(Annotation annotation) throws IOException {
		return calcGTTR(getEasyLemmaTokensNouns(annotation, 
				easyWordSet));
	}
	public static double calculateEasyGTTRVerbs(Annotation annotation) throws IOException {
		return calcGTTR(getEasyLemmaTokensVerbs(annotation, 
				easyWordSet));
	}
	public static double calculateEasyGTTRLexicals(Annotation annotation) throws IOException {
		return calcGTTR(getEasyLemmaTokensLexicals(annotation, 
				easyWordSet));
	}
	public static double calculateEasyGTTRAllWords(Annotation annotation) throws IOException {
		return calcGTTR(getEasyLemmaTokensAllWords(annotation, 
				easyWordSet));
	}
	public static double calculateSophCTTRAdverbs(Annotation annotation) throws IOException {
		return calcCTTR(getSophLemmaTokensAdverbs(annotation, 
				sophWordSet));
	}

	public static double calculateSophCTTRAllWords(Annotation annotation) throws IOException {
		return calcCTTR(getSophLemmaTokensAllWords(annotation, 
				sophWordSet));
	}

	public static double calculateSophCTTRLexicals(Annotation annotation) throws IOException {
		return calcCTTR(getSophLemmaTokensLexicals(annotation, 
				sophWordSet));
	}

	public static double calculateSophCTTRNouns(Annotation annotation) throws IOException {
		return calcCTTR(getSophLemmaTokensNouns(annotation, 
				sophWordSet));
	}

	public static double calculateSophCTTRVerbs(Annotation annotation) throws IOException {
		return calcCTTR(getSophLemmaTokensVerbs(annotation, 
				sophWordSet));
	}

	public static double calculateSophGTTRAdjectives(Annotation annotation) throws IOException {
		return calcGTTR(getSophLemmaTokensAdjectives(annotation, 
				sophWordSet));
	}

	public static double calculateSophGTTRAdverbs(Annotation annotation) throws IOException {
		return calcGTTR(getSophLemmaTokensAdverbs(annotation, 
				sophWordSet));
	}

	public static double calculateSophGTTRAllWords(Annotation annotation) throws IOException {
		return calcGTTR(getSophLemmaTokensAllWords(annotation, 
				sophWordSet));
	}

	public static double calculateSophGTTRLexicals(Annotation annotation) throws IOException {
		return calcGTTR(getSophLemmaTokensLexicals(annotation, 
				sophWordSet));
	}

	public static double calculateSophGTTRNouns(Annotation annotation) throws IOException {
		return calcGTTR(getSophLemmaTokensNouns(annotation, 
				sophWordSet));
	}

	public static double calculateSophGTTRVerbs(Annotation annotation) throws IOException {
		return calcGTTR(getSophLemmaTokensVerbs(annotation, 
				sophWordSet));
	}

	public static double calculateSophLogTTRAdjectives(Annotation annotation) throws IOException {
		return calcLogTTR(getSophLemmaTokensAdjectives(annotation, 
				sophWordSet));
	}

	public static double calculateSophLogTTRAdverbs(Annotation annotation) throws IOException {
		return calcLogTTR(getSophLemmaTokensAdverbs(annotation, 
				sophWordSet));
	}

	public static double calculateSophLogTTRAllWords(Annotation annotation) throws IOException {
		return calcLogTTR(getSophLemmaTokensAllWords(annotation, 
				sophWordSet));
	}

	public static double calculateSophLogTTRLexicals(Annotation annotation) throws IOException {
		return calcLogTTR(getSophLemmaTokensLexicals(annotation, 
				sophWordSet));
	}

	public static double calculateSophLogTTRNouns(Annotation annotation) throws IOException {
		return calcLogTTR(getSophLemmaTokensNouns(annotation, 
				sophWordSet));
	}

	public static double calculateSophLogTTRVerbs(Annotation annotation) throws IOException {
		return calcLogTTR(getSophLemmaTokensVerbs(annotation, 
				sophWordSet));
	}

	public static double calculateSophRatioTokensAdjectives(Annotation annotation) throws IOException {
		return (double) countNSophLemmaTokensAdjectives(annotation) / 
				LexicalPOS.countNTokensAdjectives(annotation, true);
	}

	public static double calculateEasyRatioTokensAdjectives(Annotation annotation) throws IOException {
		return (double) countNEasyLemmaTokensAdjectives(annotation) / 
				LexicalPOS.countNTokensAdjectives(annotation, true);
	}
	public static double calculateEasyRatioTokensAdverbs(Annotation annotation) throws IOException {
		return (double) countNEasyLemmaTokensAdverbs(annotation) / 
				LexicalPOS.countNTokensAdverbs(annotation, true);
	}
	public static double calculateEasyRatioTokensNouns(Annotation annotation) throws IOException {
		return (double) countNEasyLemmaTokensNouns(annotation) / 
				LexicalPOS.countNTokensNouns(annotation, true);
	}
	public static double calculateEasyRatioTokensVerbs(Annotation annotation) throws IOException {
		return (double) countNEasyLemmaTokensVerbs(annotation) / 
				LexicalPOS.countNTokensVerbs(annotation, true);
	}
	public static double calculateEasyRatioTokensLexicals(Annotation annotation) throws IOException {
		return (double) countNEasyLemmaTokensLexicals(annotation) / 
				LexicalPOS.countNTokensLexicals(annotation, true);
	}
	public static double calculateEasyRatioTokensAllWords(Annotation annotation) throws IOException {
		return (double) countNEasyLemmaTokensAllWords(annotation) / 
				LexicalCounts.countNTokensWords(annotation);
	}
	public static double calculateEasyRatioTypesLexicals(Annotation annotation) throws IOException {
		return (double) countNEasyLemmaTypesLexicals(annotation) / 
				LexicalPOS.countNTypesLexicals(annotation, true);
	}
	public static double calculateEasyRatioTypesNouns(Annotation annotation) throws IOException {
		return (double) countNEasyLemmaTypesNouns(annotation) / 
				LexicalPOS.countNTypesNouns(annotation, true);
	}
	public static double calculateEasyRatioTypesVerbs(Annotation annotation) throws IOException {
		return (double) countNEasyLemmaTypesVerbs(annotation) / 
				LexicalPOS.countNTypesVerbs(annotation, true);
	}
	public static double calculateEasyRatioTypesAdjectives(Annotation annotation) throws IOException {
		return (double) countNEasyLemmaTypesAdjectives(annotation) / 
				LexicalPOS.countNTypesAdjectives(annotation, true);
	}
	public static double calculateEasyRatioTypesAdverbs(Annotation annotation) throws IOException {
		return (double) countNEasyLemmaTypesAdverbs(annotation) / 
				LexicalPOS.countNTypesAdverbs(annotation, true);
	}
	public static double calculateEasyRatioTypesAllWords(Annotation annotation) throws IOException {
		return (double) countNEasyLemmaTypesAllWords(annotation) / 
				LexicalCounts.countNTypesWords(annotation);
	}
	public static double calculateSophRatioTokensAdverbs(Annotation annotation) throws IOException {
		return (double) countNSophLemmaTokensAdverbs(annotation) / 
				LexicalPOS.countNTokensAdverbs(annotation, true);
	}

	public static double calculateSophRatioTokensAllWords(Annotation annotation) throws IOException {
		return (double) countNSophLemmaTokensAllWords(annotation) / 
				LexicalCounts.countNTokensWords(annotation);
	}

	public static double calculateSophRatioTokensLexicals(Annotation annotation) throws IOException {
		return (double) countNSophLemmaTokensLexicals(annotation) / 
				LexicalPOS.countNTokensLexicals(annotation, true);
	}

	public static double calculateSophRatioTokensNouns(Annotation annotation) throws IOException {
		return (double) countNSophLemmaTokensNouns(annotation) / 
				LexicalPOS.countNTokensNouns(annotation, true);
	}

	public static double calculateSophRatioTokensVerbs(Annotation annotation) throws IOException {
		return (double) countNSophLemmaTokensVerbs(annotation) / 
				LexicalPOS.countNTokensVerbs(annotation, true);
	}

	public static double calculateSophRatioTypesAdjectives(Annotation annotation) throws IOException {
		return (double) countNSophLemmaTypesAdjectives(annotation) / 
				LexicalPOS.countNTypesAdjectives(annotation, true);
	}

	public static double calculateSophRatioTypesAdverbs(Annotation annotation) throws IOException {
		return (double) countNSophLemmaTypesAdverbs(annotation) / 
				LexicalPOS.countNTypesAdverbs(annotation, true);
	}

	public static double calculateSophRatioTypesAllWords(Annotation annotation) throws IOException {
		return (double) countNSophLemmaTypesAllWords(annotation) / 
				LexicalCounts.countNTypesWords(annotation);
	}

	public static double calculateSophRatioTypesLexicals(Annotation annotation) throws IOException {
		return (double) countNSophLemmaTypesLexicals(annotation) / 
				LexicalPOS.countNTypesLexicals(annotation, true);
	}
	public static double calculateSophRatioTypesNouns(Annotation annotation) throws IOException {
		return (double) countNSophLemmaTypesNouns(annotation) / 
				LexicalPOS.countNTypesNouns(annotation, true);
	}

	public static double calculateSophRatioTypesVerbs(Annotation annotation) throws IOException {
		return (double) countNSophLemmaTypesVerbs(annotation) / 
				LexicalPOS.countNTypesVerbs(annotation, true);
	}

	public static double calculateSophSTTRAdjectives(Annotation annotation) throws IOException {
		return calcSTTR(getSophLemmaTokensAdjectives(annotation, 
				sophWordSet));
	}	

	public static double calculateSophSTTRAdverbs(Annotation annotation) throws IOException {
		return calcSTTR(getSophLemmaTokensAdverbs(annotation, 
				sophWordSet));
	}	
	public static double calculateSophSTTRAllWords(Annotation annotation) throws IOException {
		return calcSTTR(getSophLemmaTokensAllWords(annotation, 
				sophWordSet));
	}	
	public static double calculateSophSTTRLexicals(Annotation annotation) throws IOException {
		return calcSTTR(getSophLemmaTokensLexicals(annotation, 
				sophWordSet));
	}	
	public static double calculateSophSTTRNouns(Annotation annotation) throws IOException {
		return calcSTTR(getSophLemmaTokensNouns(annotation, 
				sophWordSet));
	}	
	public static double calculateSophSTTRVerbs(Annotation annotation) throws IOException {
		return calcSTTR(getSophLemmaTokensVerbs(annotation, 
				sophWordSet));
	}	

	public static double calculateSophTTRAdjectives(Annotation annotation) throws IOException {
		return calcTTR(getSophLemmaTokensAdjectives(annotation, 
				sophWordSet));
	}

	public static double calculateEasyTTRAdjectives(Annotation annotation) throws IOException {
		return calcTTR(getEasyLemmaTokensAdjectives(annotation, 
				easyWordSet));
	}
	
	public static double calculateEasyTTRAdverbs(Annotation annotation) throws IOException {
		return calcTTR(getEasyLemmaTokensAdverbs(annotation, 
				easyWordSet));
	}

	public static double calculateEasyTTRVerbs(Annotation annotation) throws IOException {
		return calcTTR(getEasyLemmaTokensVerbs(annotation, 
				easyWordSet));
	}
	public static double calculateEasyTTRNouns(Annotation annotation) throws IOException {
		return calcTTR(getEasyLemmaTokensNouns(annotation, 
				easyWordSet));
	}
	public static double calculateEasyTTRLexicals(Annotation annotation) throws IOException {
		return calcTTR(getEasyLemmaTokensLexicals(annotation, 
				easyWordSet));
	}
	public static double calculateEasyTTRAllWords(Annotation annotation) throws IOException {
		return calcTTR(getEasyLemmaTokensAllWords(annotation, 
				easyWordSet));
	}
	public static double calculateSophTTRAdverbs(Annotation annotation) throws IOException {
		return calcTTR(getSophLemmaTokensAdverbs(annotation, 
				sophWordSet));
	}

	public static double calculateSophTTRAllWords(Annotation annotation) throws IOException {
		return calcTTR(getSophLemmaTokensAllWords(annotation, 
				sophWordSet));
	}
	public static double calculateSophTTRLexicals(Annotation annotation) throws IOException {
		return calcTTR(getSophLemmaTokensLexicals(annotation, 
				sophWordSet));
	}
	public static double calculateSophTTRNouns(Annotation annotation) throws IOException {
		return calcTTR(getSophLemmaTokensNouns(annotation, 
				sophWordSet));
	}
	public static double calculateSophTTRVerbs(Annotation annotation) throws IOException {
		return calcTTR(getSophLemmaTokensVerbs(annotation, 
				sophWordSet));
	}

	public static double calculateSophUberTTRAdjectives(Annotation annotation) throws IOException {
		return calcUberTTR(getSophLemmaTokensAdjectives(annotation, 
				sophWordSet));
	}

	public static double calculateSophUberTTRAdverbs(Annotation annotation) throws IOException {
		return calcUberTTR(getSophLemmaTokensAdverbs(annotation, 
				sophWordSet));
	}

	public static double calculateSophUberTTRAllWords(Annotation annotation) throws IOException {
		return calcUberTTR(getSophLemmaTokensAllWords(annotation, 
				sophWordSet));
	}

	public static double calculateSophUberTTRLexicals(Annotation annotation) throws IOException {
		return calcUberTTR(getSophLemmaTokensLexicals(annotation, 
				sophWordSet));
	}

	public static double calculateSophUberTTRNouns(Annotation annotation) throws IOException {
		return calcUberTTR(getSophLemmaTokensNouns(annotation, 
				sophWordSet));
	}

	public static double calculateSophUberTTRVerbs(Annotation annotation) throws IOException {
		return calcUberTTR(getSophLemmaTokensVerbs(annotation, 
				sophWordSet));
	}

	public static int countNSophLemmaTokensAdjectives(Annotation annotation) throws IOException {
		return getSophLemmaTokensAdjectives(annotation, sophWordSet).size();
	}

	public static int countNSophLemmaTokensAdverbs(Annotation annotation) throws IOException {
		return getSophLemmaTokensAdverbs(annotation, sophWordSet).size();
	}

	public static int countNSophLemmaTokensAllWords(Annotation annotation) throws IOException {
		return getSophLemmaTokensAllWords(annotation, sophWordSet).size();
	}

	public static int countNSophLemmaTokensLexicals(Annotation annotation) throws IOException {
		return getSophLemmaTokensLexicals(annotation, sophWordSet).size();
	}

	public static int countNSophLemmaTokensNouns(Annotation annotation) throws IOException {
		return getSophLemmaTokensNouns(annotation, sophWordSet).size();
	}

	public static int countNSophLemmaTokensVerbs(Annotation annotation) throws IOException {
		return getSophLemmaTokensVerbs(annotation, sophWordSet).size();
	}

	public static int countNSophLemmaTypesAdjectives(Annotation annotation) throws IOException {
		return getSophLemmaTypesAdjectives(annotation, sophWordSet).size();
	}

	public static int countNSophLemmaTypesAdverbs(Annotation annotation) throws IOException {
		return getSophLemmaTypesAdverbs(annotation, sophWordSet).size();
	}

	public static int countNSophLemmaTypesAllWords(Annotation annotation) throws IOException {
		return getSophLemmaTypesAllWords(annotation, sophWordSet).size();
	}

	public static int countNSophLemmaTypesLexicals(Annotation annotation) throws IOException {
		return getSophLemmaTypesLexicals(annotation, sophWordSet).size();
	}

	public static int countNSophLemmaTypesNouns(Annotation annotation) throws IOException {
		return getSophLemmaTypesNouns(annotation, sophWordSet).size();
	}

	public static int countNSophLemmaTypesVerbs(Annotation annotation) throws IOException {
		return getSophLemmaTypesVerbs(annotation, sophWordSet).size();
	}

	//gets a list of easy lemmas
	private static List<String> filterEasy(Collection<String> originalList, Set<String> easyWordSet) 
			throws IOException {
		List<String> filteredList = new ArrayList<>();

		for(String word: originalList) {
			if(easyWordSet.contains(word)) {
				filteredList.add(word);
			}
		}
		return filteredList;
	}

	//filters a word list, leaving only sophisticated items
	//lemmas that are not in the freqWordSet are sophisticated
	private static List<String> filterSophisticated(Collection<String> originalList, Set<String> freqWordSet) 
			throws IOException {
		List<String> filteredList = new ArrayList<>();

		for(String word: originalList) {
			if(!freqWordSet.contains(word)) {
				filteredList.add(word);
			}
		}
		return filteredList;
	}

	public static int countNEasyLemmaTokensAllWords(Annotation annotation) throws IOException {
		return getEasyLemmaTokensAllWords(annotation, 
				easyWordSet).size() +
				LexicalCounts.countNTokensNumbers(annotation); 
	}

	public static int countNEasyLemmaTokensNouns(Annotation annotation) throws IOException {
		return getEasyLemmaTokensNouns(annotation, 
				easyWordSet).size();
	}

	public static int countNEasyLemmaTokensVerbs(Annotation annotation) throws IOException {
		return getEasyLemmaTokensVerbs(annotation, 
				easyWordSet).size();
	}

	public static int countNEasyLemmaTokensAdverbs(Annotation annotation) throws IOException {
		return getEasyLemmaTokensAdverbs(annotation, 
				easyWordSet).size();
	}
	public static int countNEasyLemmaTokensLexicals(Annotation annotation) throws IOException {
		return getEasyLemmaTokensLexicals(annotation, 
				easyWordSet).size();
	}




	public static int countNEasyLemmaTypesAllWords(Annotation annotation) throws IOException {
		return getEasyLemmaTypesAllWords(annotation, 
				easyWordSet).size() +
				LexicalCounts.countNTypesNumbers(annotation);
	}

	public static int countNEasyLemmaTypesNouns(Annotation annotation) throws IOException {
		return getEasyLemmaTypesNouns(annotation, 
				easyWordSet).size();
	}

	public static int countNEasyLemmaTypesVerbs(Annotation annotation) throws IOException {
		return getEasyLemmaTypesVerbs(annotation, 
				easyWordSet).size();

	}
	public static int countNEasyLemmaTypesAdjectives(Annotation annotation) throws IOException {
		return getEasyLemmaTypesAdjectives(annotation, 
				easyWordSet).size();
	}
	public static int countNEasyLemmaTypesAdverbs(Annotation annotation) throws IOException {
		return getEasyLemmaTypesAdverbs(annotation, 
				easyWordSet).size();

	}
	public static int countNEasyLemmaTypesLexicals(Annotation annotation) throws IOException {
		return getEasyLemmaTypesLexicals(annotation, 
				easyWordSet).size();
	}



	private static List<String> getSophLemmaTokensAdjectives(Annotation annotation, Set<String> frequentLemmaSet) throws IOException {
		List<String> allLemmas = LexicalPOS.getTokensAdjectives(annotation, true);
		List<String> sophLemmas = filterSophisticated(allLemmas, frequentLemmaSet);

		return sophLemmas;
	}
	private static List<String> getSophLemmaTokensAdverbs(Annotation annotation, Set<String> frequentLemmaSet) throws IOException {
		List<String> allLemmas = LexicalPOS.getTokensAdverbs(annotation, true);
		List<String> sophLemmas = filterSophisticated(allLemmas, frequentLemmaSet);

		return sophLemmas;
	}

	//gets a list of lemmas that are not in the frequent lemma set
	private static List<String> getSophLemmaTokensAllWords(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {

		List<String> allLemmas = LexicalUtils.getWordTokens(annotation, true);
		List<String> sophLemmas = filterSophisticated(allLemmas, frequentLemmaSet);

		return sophLemmas;
	}

	//gets a list of lemmas that are in the frequent lemma set
	private static List<String> getEasyLemmaTokensAllWords(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {

		List<String> easyLemmaList = new ArrayList<>();
		//get word tokens
		easyLemmaList.addAll(filterEasy(LexicalUtils.getWordTokens(annotation, true), 
				frequentLemmaSet));
		easyLemmaList.addAll(LexicalUtils.getPOSTokens(annotation, "NNP", true));
		easyLemmaList.addAll(LexicalUtils.getPOSTokens(annotation, "NNPS", true));

		return easyLemmaList;
	}

	private static List<String> getEasyLemmaTokensNouns(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {

		List<String> easyLemmaList = new ArrayList<>();
		//get word tokens
		easyLemmaList.addAll(filterEasy(LexicalPOS.getTokensNouns(annotation, true),
				frequentLemmaSet));

		return easyLemmaList;
	}
	private static List<String> getEasyLemmaTokensVerbs(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {

		List<String> easyLemmaList = new ArrayList<>();
		//get word tokens
		easyLemmaList.addAll(filterEasy(LexicalPOS.getTokensVerbs(annotation, true),
				frequentLemmaSet));

		return easyLemmaList;
	}
	private static List<String> getEasyLemmaTokensAdjectives(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {

		List<String> easyLemmaList = new ArrayList<>();
		//get word tokens
		easyLemmaList.addAll(filterEasy(LexicalPOS.getTokensAdjectives(annotation, true),
				frequentLemmaSet));

		return easyLemmaList;
	}
	private static Set<String> getEasyLemmaTypesAdjectives(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {
		return new HashSet<>(getEasyLemmaTokensAdjectives(annotation, 
				frequentLemmaSet));
	}
	private static Set<String> getEasyLemmaTypesNouns(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {
		return new HashSet<>(getEasyLemmaTokensNouns(annotation, 
				frequentLemmaSet));
	}
	private static Set<String> getEasyLemmaTypesVerbs(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {
		return new HashSet<>(getEasyLemmaTokensVerbs(annotation, 
				frequentLemmaSet));
	}
	private static Set<String> getEasyLemmaTypesAdverbs(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {
		return new HashSet<>(getEasyLemmaTokensAdverbs(annotation, 
				frequentLemmaSet));
	}
	private static Set<String> getEasyLemmaTypesLexicals(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {
		return new HashSet<>(getEasyLemmaTokensLexicals(annotation, 
				frequentLemmaSet));
	}
	private static Set<String> getEasyLemmaTypesAllWords(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {
		return new HashSet<>(getEasyLemmaTokensAllWords(annotation, 
				frequentLemmaSet));
	}
	private static List<String> getEasyLemmaTokensAdverbs(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {

		List<String> easyLemmaList = new ArrayList<>();
		//get word tokens
		easyLemmaList.addAll(filterEasy(LexicalPOS.getTokensAdverbs(annotation, true),
				frequentLemmaSet));

		return easyLemmaList;
	}
	private static List<String> getEasyLemmaTokensLexicals(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {

		List<String> easyLemmaList = new ArrayList<>();
		//get word tokens
		easyLemmaList.addAll(filterEasy(LexicalPOS.getTokensLexicals(annotation, true),
				frequentLemmaSet));

		return easyLemmaList;
	}

	private static List<String> getSophLemmaTokensLexicals(Annotation annotation, Set<String> frequentLemmaSet) throws IOException {
		List<String> allLemmas = LexicalPOS.getTokensLexicals(annotation, true);
		List<String> sophLemmas = filterSophisticated(allLemmas, frequentLemmaSet);

		return sophLemmas;
	}

	private static List<String> getSophLemmaTokensNouns(Annotation annotation, Set<String> frequentLemmaSet) throws IOException {
		List<String> allLemmas = LexicalPOS.getTokensNouns(annotation, true);
		List<String> sophLemmas = filterSophisticated(allLemmas, frequentLemmaSet);

		return sophLemmas;
	}
	private static List<String> getSophLemmaTokensVerbs(Annotation annotation, Set<String> frequentLemmaSet) throws IOException {
		List<String> allLemmas = LexicalPOS.getTokensVerbs(annotation, true);
		List<String> sophLemmas = filterSophisticated(allLemmas, frequentLemmaSet);

		return sophLemmas;
	}
	private static Set<String> getSophLemmaTypesAdjectives(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {
		return new HashSet<>(getSophLemmaTokensAdjectives(annotation, 
				frequentLemmaSet));
	}
	private static Set<String> getSophLemmaTypesAdverbs(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {
		return new HashSet<>(getSophLemmaTokensAdverbs(annotation, 
				frequentLemmaSet));
	}

	private static Set<String> getSophLemmaTypesAllWords(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {
		return new HashSet<>(getSophLemmaTokensAllWords(annotation, 
				frequentLemmaSet));
	}

	private static Set<String> getSophLemmaTypesLexicals(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {
		return new HashSet<>(getSophLemmaTokensLexicals(annotation, 
				frequentLemmaSet));
	}

	private static Set<String> getSophLemmaTypesNouns(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {
		return new HashSet<>(getSophLemmaTokensNouns(annotation, 
				frequentLemmaSet));
	}
	private static Set<String> getSophLemmaTypesVerbs(Annotation annotation, Set<String> frequentLemmaSet) 
			throws IOException {
		return new HashSet<>(getSophLemmaTokensVerbs(annotation, 
				frequentLemmaSet));	}

	public static int countNEasyLemmaTokensAdjectives(Annotation annotation) throws IOException {
		return getEasyLemmaTokensAdjectives(annotation, 
				easyWordSet).size();
	}
}






