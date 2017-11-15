/**
 * 
 */
package com.ctapweb.api.measure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctapweb.api.measure.complexity.cohesion.ReferentialCohesion;
import com.ctapweb.api.measure.complexity.lexical.external.LexicalFrequencyBNC;
import com.ctapweb.api.measure.complexity.lexical.external.LexicalFrequencySUBTLEXus;
import com.ctapweb.api.measure.complexity.lexical.external.LexicalSophisticationBNC;
import com.ctapweb.api.measure.complexity.lexical.external.LexicalSophisticationNGSL;
import com.ctapweb.api.measure.complexity.lexical.internal.LexicalCounts;
import com.ctapweb.api.measure.complexity.lexical.internal.LexicalPOS;
import com.ctapweb.api.measure.complexity.lexical.internal.LexicalTTR;
import com.ctapweb.api.measure.complexity.syntactic.SyntacticCounts;
import com.ctapweb.api.measure.complexity.syntactic.SyntacticIndexes;
import com.ctapweb.api.measure.complexity.syntactic.SyntacticRatios;
import com.ctapweb.api.utils.NLPPipeLinesManager;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/** For analyzing all the files in a folder.
 * Extract all the features provided by the api.
 * @author xiaobin
 * 
 */
public class AnalyzeFolder {

//	private static File folderToAnalyze = new File("/home/xiaobin/tmp/analysis/texts/");
	private static File folderToAnalyze = 
			new File("/home/xiaobin/work/project/Goepferich/corpus_clean/en");
//	private static File resultsFile = new File("/home/xiaobin/tmp/analysis/results.csv");
	private static File resultsFile = 
			new File("/home/xiaobin/work/project/Goepferich/corpus_clean/en_complexity.csv");
	private static Logger logger = LogManager.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//		StanfordCoreNLP pipeline = NLPPipeLinesManager.getParser();
		StanfordCoreNLP pipeline = NLPPipeLinesManager.getCompletePipe();
		//traverse all files in the folder
		Iterator<File> fit = FileUtils.iterateFiles(folderToAnalyze, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

		//write the results header
		try {
			FileUtils.writeStringToFile(resultsFile, 
					"file_name,feature,value\n", 
					"UTF-8", false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int fileCount = 1;
		while(fit.hasNext()) {
			File file = fit.next();

			try {
				String fileName = file.getName();
				String fileContent = FileUtils.readFileToString(file, "UTF-8");
				Annotation annotation = new Annotation(fileContent);

				logger.trace("Analyzing file {}: {}...", fileCount++, fileName);

				//nlp annotate the file content
				pipeline.annotate(annotation);

				//extract features: lexical counts
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NHapax" + "," + 
								LexicalCounts.countNHapax(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NSyllables" + "," + 
								LexicalCounts.countNSyllables(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensAll" + "," + 
								LexicalCounts.countNTokensAll(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensCC" + "," + 
								LexicalCounts.countNTokensCC(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensCD" + "," + 
								LexicalCounts.countNTokensCD(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensDT" + "," + 
								LexicalCounts.countNTokensDT(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensEX" + "," + 
								LexicalCounts.countNTokensEX(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensIN" + "," + 
								LexicalCounts.countNTokensIN(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensJJ" + "," + 
								LexicalCounts.countNTokensJJ(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensJJR" + "," + 
								LexicalCounts.countNTokensJJR(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensJJS" + "," + 
								LexicalCounts.countNTokensJJS(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensLS" + "," + 
								LexicalCounts.countNTokensLS(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensMD" + "," + 
								LexicalCounts.countNTokensMD(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensNN" + "," + 
								LexicalCounts.countNTokensNN(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensNNP" + "," + 
								LexicalCounts.countNTokensNNP(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensNNPS" + "," + 
								LexicalCounts.countNTokensNNPS(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensNNS" + "," + 
								LexicalCounts.countNTokensNNS(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensNumbers" + "," + 
								LexicalCounts.countNTokensNumbers(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensPDT" + "," + 
								LexicalCounts.countNTokensPDT(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensPOS" + "," + 
								LexicalCounts.countNTokensPOS(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensPPS" + "," + 
								LexicalCounts.countNTokensPPS(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensPRP" + "," + 
								LexicalCounts.countNTokensPRP(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensPuncts" + "," + 
								LexicalCounts.countNTokensPuncts(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensRB" + "," + 
								LexicalCounts.countNTokensRB(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensRBR" + "," + 
								LexicalCounts.countNTokensRBR(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensRBS" + "," + 
								LexicalCounts.countNTokensRBS(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensRP" + "," + 
								LexicalCounts.countNTokensRP(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensSYM" + "," + 
								LexicalCounts.countNTokensSYM(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensTO" + "," + 
								LexicalCounts.countNTokensTO(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensUH" + "," + 
								LexicalCounts.countNTokensUH(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensVB" + "," + 
								LexicalCounts.countNTokensVB(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensVBD" + "," + 
								LexicalCounts.countNTokensVBD(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensVBG" + "," + 
								LexicalCounts.countNTokensVBG(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensVBN" + "," + 
								LexicalCounts.countNTokensVBN(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensVBP" + "," + 
								LexicalCounts.countNTokensVBP(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensVBZ" + "," + 
								LexicalCounts.countNTokensVBZ(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensWDT" + "," + 
								LexicalCounts.countNTokensWDT(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensWords" + "," + 
								LexicalCounts.countNTokensWords(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensWP" + "," + 
								LexicalCounts.countNTokensWP(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensWPS" + "," + 
								LexicalCounts.countNTokensWPS(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTokensWRB" + "," + 
								LexicalCounts.countNTokensWRB(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesAll" + "," + 
								LexicalCounts.countNTypesAll(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesCC" + "," + 
								LexicalCounts.countNTypesCC(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesCD" + "," + 
								LexicalCounts.countNTypesCD(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesDT" + "," + 
								LexicalCounts.countNTypesDT(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesEX" + "," + 
								LexicalCounts.countNTypesEX(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesIN" + "," + 
								LexicalCounts.countNTypesIN(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesJJ" + "," + 
								LexicalCounts.countNTypesJJ(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesJJR" + "," + 
								LexicalCounts.countNTypesJJR(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesJJS" + "," + 
								LexicalCounts.countNTypesJJS(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesLS" + "," + 
								LexicalCounts.countNTypesLS(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesMD" + "," + 
								LexicalCounts.countNTypesMD(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesNN" + "," + 
								LexicalCounts.countNTypesNN(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesNNP" + "," + 
								LexicalCounts.countNTypesNNP(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesNNPS" + "," + 
								LexicalCounts.countNTypesNNPS(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesNNS" + "," + 
								LexicalCounts.countNTypesNNS(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesNumbers" + "," + 
								LexicalCounts.countNTypesNumbers(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesPDT" + "," + 
								LexicalCounts.countNTypesPDT(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesPOS" + "," + 
								LexicalCounts.countNTypesPOS(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesPPS" + "," + 
								LexicalCounts.countNTypesPPS(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesPRP" + "," + 
								LexicalCounts.countNTypesPRP(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesPuncts" + "," + 
								LexicalCounts.countNTypesPuncts(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesRB" + "," + 
								LexicalCounts.countNTypesRB(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesRBR" + "," + 
								LexicalCounts.countNTypesRBR(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesRBS" + "," + 
								LexicalCounts.countNTypesRBS(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesRP" + "," + 
								LexicalCounts.countNTypesRP(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesSYM" + "," + 
								LexicalCounts.countNTypesSYM(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesTO" + "," + 
								LexicalCounts.countNTypesTO(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesUH" + "," + 
								LexicalCounts.countNTypesUH(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesVB" + "," + 
								LexicalCounts.countNTypesVB(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesVBD" + "," + 
								LexicalCounts.countNTypesVBD(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesVBG" + "," + 
								LexicalCounts.countNTypesVBG(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesVBN" + "," + 
								LexicalCounts.countNTypesVBN(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesVBP" + "," + 
								LexicalCounts.countNTypesVBP(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesVBZ" + "," + 
								LexicalCounts.countNTypesVBZ(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesWDT" + "," + 
								LexicalCounts.countNTypesWDT(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesWords" + "," + 
								LexicalCounts.countNTypesWords(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesWP" + "," + 
								LexicalCounts.countNTypesWP(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesWPS" + "," + 
								LexicalCounts.countNTypesWPS(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NTypesWRB" + "," + 
								LexicalCounts.countNTypesWRB(annotation) + "\n", "UTF-8", true);
			
				//from lexicalPOS
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NAdjectivesTokens" + "," + 
								LexicalPOS.countNTokensAdjectives(annotation, false) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NAdverbsTokens" + "," + 
								LexicalPOS.countNTokensAdverbs(annotation, false) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NLexicalsTokens" + "," + 
								LexicalPOS.countNTokensLexicals(annotation, false) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NNounsTokens" + "," + 
								LexicalPOS.countNTokensNouns(annotation, false) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NVerbsTokens" + "," + 
								LexicalPOS.countNTokensVerbs(annotation, false) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NAdjectivesTokenTypes" + "," + 
								LexicalPOS.countNTypesAdjectives(annotation, false) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NAdjectivesLemmaTypes" + "," + 
								LexicalPOS.countNTypesAdjectives(annotation, true) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NAdverbsTokenTypes" + "," + 
								LexicalPOS.countNTypesAdverbs(annotation, false) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NAdverbsLemmaTypes" + "," + 
								LexicalPOS.countNTypesAdverbs(annotation, true) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NLexicalsTokenTypes" + "," + 
								LexicalPOS.countNTypesLexicals(annotation, false) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NLexicalsLemmaTypes" + "," + 
								LexicalPOS.countNTypesLexicals(annotation, true) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NNounsTokenTypes" + "," + 
								LexicalPOS.countNTypesNouns(annotation, false) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NNounsLemmaTypes" + "," + 
								LexicalPOS.countNTypesNouns(annotation, true) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NVerbsTokenTypes" + "," + 
								LexicalPOS.countNTypesVerbs(annotation, false) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "NVerbsLemmaTypes" + "," + 
								LexicalPOS.countNTypesVerbs(annotation, true) + "\n", "UTF-8", true);

				//lexicalTTR
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CTTRAdjectives" + "," + 
								LexicalTTR.calculateCTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CTTRAdverbs" + "," + 
								LexicalTTR.calculateCTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CTTRAllWords" + "," + 
								LexicalTTR.calculateCTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CTTRLexicals" + "," + 
								LexicalTTR.calculateCTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CTTRNouns" + "," + 
								LexicalTTR.calculateCTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CTTRVerbs" + "," + 
								LexicalTTR.calculateCTTRVerbs(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "GTTRAdjectives" + "," + 
								LexicalTTR.calculateGTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "GTTRAdverbs" + "," + 
								LexicalTTR.calculateGTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "GTTRAllWords" + "," + 
								LexicalTTR.calculateGTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + " GWLexicals" + "," + 
								LexicalTTR.calculateGTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "GTTRNouns" + "," + 
								LexicalTTR.calculateGTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "GTTRVerbs" + "," + 
			LexicalTTR.calculateGTTRVerbs(annotation) + "\n", "UTF-8", true);
				
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LogTTRAdjectives" + "," + 
								LexicalTTR.calculateLogTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LogTTRAdverbs" + "," + 
								LexicalTTR.calculateLogTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LogTTRAllWords" + "," + 
								LexicalTTR.calculateLogTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LogTTRLexicals" + "," + 
								LexicalTTR.calculateLogTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LogTTRNouns" + "," + 
								LexicalTTR.calculateLogTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LogTTRVerbs" + "," + 
								LexicalTTR.calculateLogTTRVerbs(annotation) + "\n", "UTF-8", true);
			
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "STTRAdjectives" + "," + 
								LexicalTTR.calculateSTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "STTRAdverbs" + "," + 
								LexicalTTR.calculateSTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "STTRAllWords" + "," + 
								LexicalTTR.calculateSTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "STTRLexicals" + "," + 
								LexicalTTR.calculateSTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "STTRNouns" + "," + 
								LexicalTTR.calculateSTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "STTRVerbs" + "," + 
								LexicalTTR.calculateSTTRVerbs(annotation) + "\n", "UTF-8", true);
			
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "TTRAdjectives" + "," + 
								LexicalTTR.calculateTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "TTRAdverbs" + "," + 
								LexicalTTR.calculateTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "TTRAllWords" + "," + 
								LexicalTTR.calculateTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "TTRLexicals" + "," + 
								LexicalTTR.calculateTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "TTRNouns" + "," + 
								LexicalTTR.calculateTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "TTRVerbs" + "," + 
								LexicalTTR.calculateTTRVerbs(annotation) + "\n", "UTF-8", true);
			
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "UberTTRAdjectives" + "," + 
								LexicalTTR.calculateUberTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "UberTTRAdverbs" + "," + 
								LexicalTTR.calculateUberTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "UberTTRAllWords" + "," + 
								LexicalTTR.calculateUberTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "UberTTRLexicals" + "," + 
								LexicalTTR.calculateUberTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "UberTTRNouns" + "," + 
								LexicalTTR.calculateUberTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "UberTTRVerbs" + "," + 
								LexicalTTR.calculateUberTTRVerbs(annotation) + "\n", "UTF-8", true);
			
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "TTR2Adjectives" + "," + 
								LexicalTTR.calculateTTR2Adjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "TTR2Adverbs" + "," + 
								LexicalTTR.calculateTTR2Adverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "TTR2Nouns" + "," + 
								LexicalTTR.calculateTTR2Nouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "TTR2Verbs" + "," + 
								LexicalTTR.calculateTTR2Verbs(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexicalDensity" + "," + 
								LexicalTTR.calculateLexicalDensity(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "MESTTR-10" + "," + 
								LexicalTTR.calculateMESTTR(annotation, 10) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "ModifierVariation" + "," + 
								LexicalTTR.calculateModifierVariation(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "MSTTR-50" + "," + 
								LexicalTTR.calculateMSTTR(annotation, 50) + "\n", "UTF-8", true);
			
				//lexical frequency BNC
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTokenAdjectiveMeanBNC" + "," + 
								LexicalFrequencyBNC.calculateTokenAdjectiveMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTokenAdjectiveSDBNC" + "," + 
								LexicalFrequencyBNC.calculateTokenAdjectiveSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTokenAdverbMeanBNC" + "," + 
								LexicalFrequencyBNC.calculateTokenAdverbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTokenAdverbSDBNC" + "," + 
								LexicalFrequencyBNC.calculateTokenAdverbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTokenVerbMeanBNC" + "," + 
								LexicalFrequencyBNC.calculateTokenVerbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTokenVerbSDBNC" + "," + 
								LexicalFrequencyBNC.calculateTokenVerbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTokenNounMeanBNC" + "," + 
								LexicalFrequencyBNC.calculateTokenNounMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTokenNounSDBNC" + "," + 
								LexicalFrequencyBNC.calculateTokenNounSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTokenAllMeanBNC" + "," + 
								LexicalFrequencyBNC.calculateTokenAllMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTokenAllSDBNC" + "," + 
								LexicalFrequencyBNC.calculateTokenAllSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTokenLexicalMeanBNC" + "," + 
								LexicalFrequencyBNC.calculateTokenLexicalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTokenLexicalSDBNC" + "," + 
								LexicalFrequencyBNC.calculateTokenLexicalSD(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTypeAdjectiveMeanBNC" + "," + 
								LexicalFrequencyBNC.calculateTypeAdjectiveMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTypeAdjectiveSDBNC" + "," + 
								LexicalFrequencyBNC.calculateTypeAdjectiveSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTypeAdverbMeanBNC" + "," + 
								LexicalFrequencyBNC.calculateTypeAdverbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTypeAdverbSDBNC" + "," + 
								LexicalFrequencyBNC.calculateTypeAdverbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTypeVerbMeanBNC" + "," + 
								LexicalFrequencyBNC.calculateTypeVerbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTypeVerbSDBNC" + "," + 
								LexicalFrequencyBNC.calculateTypeVerbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTypeNounMeanBNC" + "," + 
								LexicalFrequencyBNC.calculateTypeNounMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTypeNounSDBNC" + "," + 
								LexicalFrequencyBNC.calculateTypeNounSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTypeAllMeanBNC" + "," + 
								LexicalFrequencyBNC.calculateTypeAllMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTypeAllSDBNC" + "," + 
								LexicalFrequencyBNC.calculateTypeAllSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTypeLexicalMeanBNC" + "," + 
								LexicalFrequencyBNC.calculateTypeLexicalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqTypeLexicalSDBNC" + "," + 
								LexicalFrequencyBNC.calculateTypeLexicalSD(annotation) + "\n", "UTF-8", true);

				//lexical frequency SUBTLEXus
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTokenAdjectiveMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTokenAdjectiveMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTokenAdjectiveSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTokenAdjectiveSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTokenAdverbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTokenAdverbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTokenAdverbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTokenAdverbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTokenVerbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTokenVerbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTokenVerbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTokenVerbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTokenNounMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTokenNounMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTokenNounSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTokenNounSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTokenAllMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTokenAllMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTokenAllSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTokenAllSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTokenLexicalMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTokenLexicalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTokenLexicalSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTokenLexicalSD(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTypeAdjectiveMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTypeAdjectiveMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTypeAdjectiveSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTypeAdjectiveSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTypeAdverbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTypeAdverbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTypeAdverbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTypeAdverbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTypeVerbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTypeVerbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTypeVerbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTypeVerbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTypeNounMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTypeNounMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTypeNounSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTypeNounSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTypeAllMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTypeAllMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTypeAllSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTypeAllSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTypeLexicalMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTypeLexicalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqCDTypeLexicalSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateCDTypeLexicalSD(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTokenAdjectiveMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTokenAdjectiveMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTokenAdjectiveSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTokenAdjectiveSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTokenAdverbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTokenAdverbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTokenAdverbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTokenAdverbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTokenVerbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTokenVerbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTokenVerbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTokenVerbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTokenNounMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTokenNounMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTokenNounSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTokenNounSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTokenAllMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTokenAllMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTokenAllSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTokenAllSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTokenLexicalMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTokenLexicalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTokenLexicalSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTokenLexicalSD(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTypeAdjectiveMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTypeAdjectiveMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTypeAdjectiveSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTypeAdjectiveSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTypeAdverbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTypeAdverbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTypeAdverbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTypeAdverbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTypeVerbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTypeVerbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTypeVerbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTypeVerbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTypeNounMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTypeNounMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTypeNounSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTypeNounSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTypeAllMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTypeAllMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTypeAllSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTypeAllSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTypeLexicalMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTypeLexicalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqWFTypeLexicalSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateWFTypeLexicalSD(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTokenAdjectiveMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTokenAdjectiveMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTokenAdjectiveSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTokenAdjectiveSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTokenAdverbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTokenAdverbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTokenAdverbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTokenAdverbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTokenVerbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTokenVerbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTokenVerbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTokenVerbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTokenNounMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTokenNounMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTokenNounSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTokenNounSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTokenAllMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTokenAllMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTokenAllSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTokenAllSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTokenLexicalMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTokenLexicalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTokenLexicalSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTokenLexicalSD(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTypeAdjectiveMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTypeAdjectiveMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTypeAdjectiveSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTypeAdjectiveSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTypeAdverbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTypeAdverbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTypeAdverbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTypeAdverbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTypeVerbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTypeVerbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTypeVerbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTypeVerbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTypeNounMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTypeNounMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTypeNounSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTypeNounSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTypeAllMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTypeAllMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTypeAllSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTypeAllSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTypeLexicalMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTypeLexicalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10CDTypeLexicalSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10CDTypeLexicalSD(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTokenAdjectiveMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTokenAdjectiveMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTokenAdjectiveSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTokenAdjectiveSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTokenAdverbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTokenAdverbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTokenAdverbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTokenAdverbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTokenVerbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTokenVerbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTokenVerbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTokenVerbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTokenNounMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTokenNounMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTokenNounSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTokenNounSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTokenAllMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTokenAllMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTokenAllSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTokenAllSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTokenLexicalMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTokenLexicalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTokenLexicalSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTokenLexicalSD(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTypeAdjectiveMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTypeAdjectiveMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTypeAdjectiveSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTypeAdjectiveSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTypeAdverbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTypeAdverbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTypeAdverbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTypeAdverbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTypeVerbMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTypeVerbMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTypeVerbSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTypeVerbSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTypeNounMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTypeNounMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTypeNounSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTypeNounSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTypeAllMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTypeAllMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTypeAllSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTypeAllSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTypeLexicalMeanSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTypeLexicalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "FreqLog10WFTypeLexicalSDSubtlex" + "," + 
								LexicalFrequencySUBTLEXus.calculateLog10WFTypeLexicalSD(annotation) + "\n", "UTF-8", true);

				//lexical sophistication BNC
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyCTTRAdjectives" + "," + 
								LexicalSophisticationBNC.calculateEasyCTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyCTTRAdverbs" + "," + 
								LexicalSophisticationBNC.calculateEasyCTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyCTTRAllWords" + "," + 
								LexicalSophisticationBNC.calculateEasyCTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyCTTRLexicals" + "," + 
								LexicalSophisticationBNC.calculateEasyCTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyCTTRNouns" + "," + 
								LexicalSophisticationBNC.calculateEasyCTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyCTTRVerbs" + "," + 
								LexicalSophisticationBNC.calculateEasyCTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyGTTRAdjectives" + "," + 
								LexicalSophisticationBNC.calculateEasyGTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyGTTRAdverbs" + "," + 
								LexicalSophisticationBNC.calculateEasyGTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyGTTRAllWords" + "," + 
								LexicalSophisticationBNC.calculateEasyGTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyGTTRLexicals" + "," + 
								LexicalSophisticationBNC.calculateEasyGTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyGTTRNouns" + "," + 
								LexicalSophisticationBNC.calculateEasyGTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyGTTRVerbs" + "," + 
								LexicalSophisticationBNC.calculateEasyGTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyLogTTRAdjectives" + "," + 
								LexicalSophisticationBNC.calculateEasyLogTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyLogTTRAdverbs" + "," + 
								LexicalSophisticationBNC.calculateEasyLogTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyLogTTRAllWords" + "," + 
								LexicalSophisticationBNC.calculateEasyLogTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyLogTTRLexicals" + "," + 
								LexicalSophisticationBNC.calculateEasyLogTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyLogTTRNouns" + "," + 
								LexicalSophisticationBNC.calculateEasyLogTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyLogTTRVerbs" + "," + 
								LexicalSophisticationBNC.calculateEasyLogTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasySTTRAdjectives" + "," + 
								LexicalSophisticationBNC.calculateEasySTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasySTTRAdverbs" + "," + 
								LexicalSophisticationBNC.calculateEasySTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasySTTRAllWords" + "," + 
								LexicalSophisticationBNC.calculateEasySTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasySTTRLexicals" + "," + 
								LexicalSophisticationBNC.calculateEasySTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasySTTRNouns" + "," + 
								LexicalSophisticationBNC.calculateEasySTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasySTTRVerbs" + "," + 
								LexicalSophisticationBNC.calculateEasySTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyTTRAdjectives" + "," + 
								LexicalSophisticationBNC.calculateEasyTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyTTRAdverbs" + "," + 
								LexicalSophisticationBNC.calculateEasyTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyTTRAllWords" + "," + 
								LexicalSophisticationBNC.calculateEasyTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyTTRLexicals" + "," + 
								LexicalSophisticationBNC.calculateEasyTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyTTRNouns" + "," + 
								LexicalSophisticationBNC.calculateEasyTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyTTRVerbs" + "," + 
								LexicalSophisticationBNC.calculateEasyTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyUberTTRAdjectives" + "," + 
								LexicalSophisticationBNC.calculateEasyUberTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyUberTTRAdverbs" + "," + 
								LexicalSophisticationBNC.calculateEasyUberTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyUberTTRAllWords" + "," + 
								LexicalSophisticationBNC.calculateEasyUberTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyUberTTRLexicals" + "," + 
								LexicalSophisticationBNC.calculateEasyUberTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyUberTTRNouns" + "," + 
								LexicalSophisticationBNC.calculateEasyUberTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyUberTTRVerbs" + "," + 
								LexicalSophisticationBNC.calculateEasyUberTTRVerbs(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyRatioTokensAdjectives" + "," + 
								LexicalSophisticationBNC.calculateEasyRatioTokensAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyRatioTokensAdverbs" + "," + 
								LexicalSophisticationBNC.calculateEasyRatioTokensAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyRatioTokensVerbs" + "," + 
								LexicalSophisticationBNC.calculateEasyRatioTokensVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyRatioTokensNouns" + "," + 
								LexicalSophisticationBNC.calculateEasyRatioTokensNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyRatioTokensLexicals" + "," + 
								LexicalSophisticationBNC.calculateEasyRatioTokensLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyRatioTokensAllWords" + "," + 
								LexicalSophisticationBNC.calculateEasyRatioTokensAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyRatioTypesAdjectives" + "," + 
								LexicalSophisticationBNC.calculateEasyRatioTypesAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyRatioTypesAdverbs" + "," + 
								LexicalSophisticationBNC.calculateEasyRatioTypesAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyRatioTypesVerbs" + "," + 
								LexicalSophisticationBNC.calculateEasyRatioTypesVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyRatioTypesNouns" + "," + 
								LexicalSophisticationBNC.calculateEasyRatioTypesNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyRatioTypesLexicals" + "," + 
								LexicalSophisticationBNC.calculateEasyRatioTypesLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCEasyRatioTypesAllWords" + "," + 
								LexicalSophisticationBNC.calculateEasyRatioTypesAllWords(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophCTTRAdjectives" + "," + 
								LexicalSophisticationBNC.calculateSophCTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophCTTRAdverbs" + "," + 
								LexicalSophisticationBNC.calculateSophCTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophCTTRAllWords" + "," + 
								LexicalSophisticationBNC.calculateSophCTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophCTTRLexicals" + "," + 
								LexicalSophisticationBNC.calculateSophCTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophCTTRNouns" + "," + 
								LexicalSophisticationBNC.calculateSophCTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophCTTRVerbs" + "," + 
								LexicalSophisticationBNC.calculateSophCTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophGTTRAdjectives" + "," + 
								LexicalSophisticationBNC.calculateSophGTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophGTTRAdverbs" + "," + 
								LexicalSophisticationBNC.calculateSophGTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophGTTRAllWords" + "," + 
								LexicalSophisticationBNC.calculateSophGTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophGTTRLexicals" + "," + 
								LexicalSophisticationBNC.calculateSophGTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophGTTRNouns" + "," + 
								LexicalSophisticationBNC.calculateSophGTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophGTTRVerbs" + "," + 
								LexicalSophisticationBNC.calculateSophGTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophLogTTRAdjectives" + "," + 
								LexicalSophisticationBNC.calculateSophLogTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophLogTTRAdverbs" + "," + 
								LexicalSophisticationBNC.calculateSophLogTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophLogTTRAllWords" + "," + 
								LexicalSophisticationBNC.calculateSophLogTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophLogTTRLexicals" + "," + 
								LexicalSophisticationBNC.calculateSophLogTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophLogTTRNouns" + "," + 
								LexicalSophisticationBNC.calculateSophLogTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophLogTTRVerbs" + "," + 
								LexicalSophisticationBNC.calculateSophLogTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophSTTRAdjectives" + "," + 
								LexicalSophisticationBNC.calculateSophSTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophSTTRAdverbs" + "," + 
								LexicalSophisticationBNC.calculateSophSTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophSTTRAllWords" + "," + 
								LexicalSophisticationBNC.calculateSophSTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophSTTRLexicals" + "," + 
								LexicalSophisticationBNC.calculateSophSTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophSTTRNouns" + "," + 
								LexicalSophisticationBNC.calculateSophSTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophSTTRVerbs" + "," + 
								LexicalSophisticationBNC.calculateSophSTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophTTRAdjectives" + "," + 
								LexicalSophisticationBNC.calculateSophTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophTTRAdverbs" + "," + 
								LexicalSophisticationBNC.calculateSophTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophTTRAllWords" + "," + 
								LexicalSophisticationBNC.calculateSophTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophTTRLexicals" + "," + 
								LexicalSophisticationBNC.calculateSophTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophTTRNouns" + "," + 
								LexicalSophisticationBNC.calculateSophTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophTTRVerbs" + "," + 
								LexicalSophisticationBNC.calculateSophTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophUberTTRAdjectives" + "," + 
								LexicalSophisticationBNC.calculateSophUberTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophUberTTRAdverbs" + "," + 
								LexicalSophisticationBNC.calculateSophUberTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophUberTTRAllWords" + "," + 
								LexicalSophisticationBNC.calculateSophUberTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophUberTTRLexicals" + "," + 
								LexicalSophisticationBNC.calculateSophUberTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophUberTTRNouns" + "," + 
								LexicalSophisticationBNC.calculateSophUberTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophUberTTRVerbs" + "," + 
								LexicalSophisticationBNC.calculateSophUberTTRVerbs(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophRatioTokensAdjectives" + "," + 
								LexicalSophisticationBNC.calculateSophRatioTokensAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophRatioTokensAdverbs" + "," + 
								LexicalSophisticationBNC.calculateSophRatioTokensAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophRatioTokensVerbs" + "," + 
								LexicalSophisticationBNC.calculateSophRatioTokensVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophRatioTokensNouns" + "," + 
								LexicalSophisticationBNC.calculateSophRatioTokensNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophRatioTokensLexicals" + "," + 
								LexicalSophisticationBNC.calculateSophRatioTokensLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophRatioTokensAllWords" + "," + 
								LexicalSophisticationBNC.calculateSophRatioTokensAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophRatioTypesAdjectives" + "," + 
								LexicalSophisticationBNC.calculateSophRatioTypesAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophRatioTypesAdverbs" + "," + 
								LexicalSophisticationBNC.calculateSophRatioTypesAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophRatioTypesVerbs" + "," + 
								LexicalSophisticationBNC.calculateSophRatioTypesVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophRatioTypesNouns" + "," + 
								LexicalSophisticationBNC.calculateSophRatioTypesNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophRatioTypesLexicals" + "," + 
								LexicalSophisticationBNC.calculateSophRatioTypesLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCSophRatioTypesAllWords" + "," + 
								LexicalSophisticationBNC.calculateSophRatioTypesAllWords(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNEasyLemmaTokensAdjectives" + "," + 
								LexicalSophisticationBNC.countNEasyLemmaTokensAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNEasyLemmaTokensAdverb" + "," + 
								LexicalSophisticationBNC.countNEasyLemmaTokensAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNEasyLemmaTokensVerbs" + "," + 
								LexicalSophisticationBNC.countNEasyLemmaTokensVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNEasyLemmaTokensNouns" + "," + 
								LexicalSophisticationBNC.countNEasyLemmaTokensNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNEasyLemmaTokensLexicals" + "," + 
								LexicalSophisticationBNC.countNEasyLemmaTokensLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNEasyLemmaTokensAllWords" + "," + 
								LexicalSophisticationBNC.countNEasyLemmaTokensAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNEasyLemmaTypesAdjectives" + "," + 
								LexicalSophisticationBNC.countNEasyLemmaTypesAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNEasyLemmaTypesAdverb" + "," + 
								LexicalSophisticationBNC.countNEasyLemmaTypesAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNEasyLemmaTypesVerbs" + "," + 
								LexicalSophisticationBNC.countNEasyLemmaTypesVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNEasyLemmaTypesNouns" + "," + 
								LexicalSophisticationBNC.countNEasyLemmaTypesNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNEasyLemmaTypesLexicals" + "," + 
								LexicalSophisticationBNC.countNEasyLemmaTypesLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNEasyLemmaTypesAllWords" + "," + 
								LexicalSophisticationBNC.countNEasyLemmaTypesAllWords(annotation) + "\n", "UTF-8", true);
		
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNSophLemmaTokensAdjectives" + "," + 
								LexicalSophisticationBNC.countNSophLemmaTokensAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNSophLemmaTokensAdverb" + "," + 
								LexicalSophisticationBNC.countNSophLemmaTokensAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNSophLemmaTokensVerbs" + "," + 
								LexicalSophisticationBNC.countNSophLemmaTokensVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNSophLemmaTokensNouns" + "," + 
								LexicalSophisticationBNC.countNSophLemmaTokensNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNSophLemmaTokensLexicals" + "," + 
								LexicalSophisticationBNC.countNSophLemmaTokensLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNSophLemmaTokensAllWords" + "," + 
								LexicalSophisticationBNC.countNSophLemmaTokensAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNSophLemmaTypesAdjectives" + "," + 
								LexicalSophisticationBNC.countNSophLemmaTypesAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNSophLemmaTypesAdverb" + "," + 
								LexicalSophisticationBNC.countNSophLemmaTypesAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNSophLemmaTypesVerbs" + "," + 
								LexicalSophisticationBNC.countNSophLemmaTypesVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNSophLemmaTypesNouns" + "," + 
								LexicalSophisticationBNC.countNSophLemmaTypesNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNSophLemmaTypesLexicals" + "," + 
								LexicalSophisticationBNC.countNSophLemmaTypesLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophBNCNSophLemmaTypesAllWords" + "," + 
								LexicalSophisticationBNC.countNSophLemmaTypesAllWords(annotation) + "\n", "UTF-8", true);

				//lexical sophistication NGSL
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyCTTRAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateEasyCTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyCTTRAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateEasyCTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyCTTRAllWords" + "," + 
								LexicalSophisticationNGSL.calculateEasyCTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyCTTRLexicals" + "," + 
								LexicalSophisticationNGSL.calculateEasyCTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyCTTRNouns" + "," + 
								LexicalSophisticationNGSL.calculateEasyCTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyCTTRVerbs" + "," + 
								LexicalSophisticationNGSL.calculateEasyCTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyGTTRAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateEasyGTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyGTTRAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateEasyGTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyGTTRAllWords" + "," + 
								LexicalSophisticationNGSL.calculateEasyGTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyGTTRLexicals" + "," + 
								LexicalSophisticationNGSL.calculateEasyGTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyGTTRNouns" + "," + 
								LexicalSophisticationNGSL.calculateEasyGTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyGTTRVerbs" + "," + 
								LexicalSophisticationNGSL.calculateEasyGTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyLogTTRAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateEasyLogTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyLogTTRAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateEasyLogTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyLogTTRAllWords" + "," + 
								LexicalSophisticationNGSL.calculateEasyLogTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyLogTTRLexicals" + "," + 
								LexicalSophisticationNGSL.calculateEasyLogTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyLogTTRNouns" + "," + 
								LexicalSophisticationNGSL.calculateEasyLogTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyLogTTRVerbs" + "," + 
								LexicalSophisticationNGSL.calculateEasyLogTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasySTTRAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateEasySTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasySTTRAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateEasySTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasySTTRAllWords" + "," + 
								LexicalSophisticationNGSL.calculateEasySTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasySTTRLexicals" + "," + 
								LexicalSophisticationNGSL.calculateEasySTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasySTTRNouns" + "," + 
								LexicalSophisticationNGSL.calculateEasySTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasySTTRVerbs" + "," + 
								LexicalSophisticationNGSL.calculateEasySTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyTTRAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateEasyTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyTTRAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateEasyTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyTTRAllWords" + "," + 
								LexicalSophisticationNGSL.calculateEasyTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyTTRLexicals" + "," + 
								LexicalSophisticationNGSL.calculateEasyTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyTTRNouns" + "," + 
								LexicalSophisticationNGSL.calculateEasyTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyTTRVerbs" + "," + 
								LexicalSophisticationNGSL.calculateEasyTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyUberTTRAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateEasyUberTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyUberTTRAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateEasyUberTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyUberTTRAllWords" + "," + 
								LexicalSophisticationNGSL.calculateEasyUberTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyUberTTRLexicals" + "," + 
								LexicalSophisticationNGSL.calculateEasyUberTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyUberTTRNouns" + "," + 
								LexicalSophisticationNGSL.calculateEasyUberTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyUberTTRVerbs" + "," + 
								LexicalSophisticationNGSL.calculateEasyUberTTRVerbs(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyRatioTokensAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateEasyRatioTokensAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyRatioTokensAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateEasyRatioTokensAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyRatioTokensVerbs" + "," + 
								LexicalSophisticationNGSL.calculateEasyRatioTokensVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyRatioTokensNouns" + "," + 
								LexicalSophisticationNGSL.calculateEasyRatioTokensNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyRatioTokensLexicals" + "," + 
								LexicalSophisticationNGSL.calculateEasyRatioTokensLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyRatioTokensAllWords" + "," + 
								LexicalSophisticationNGSL.calculateEasyRatioTokensAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyRatioTypesAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateEasyRatioTypesAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyRatioTypesAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateEasyRatioTypesAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyRatioTypesVerbs" + "," + 
								LexicalSophisticationNGSL.calculateEasyRatioTypesVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyRatioTypesNouns" + "," + 
								LexicalSophisticationNGSL.calculateEasyRatioTypesNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyRatioTypesLexicals" + "," + 
								LexicalSophisticationNGSL.calculateEasyRatioTypesLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLEasyRatioTypesAllWords" + "," + 
								LexicalSophisticationNGSL.calculateEasyRatioTypesAllWords(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophCTTRAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateSophCTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophCTTRAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateSophCTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophCTTRAllWords" + "," + 
								LexicalSophisticationNGSL.calculateSophCTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophCTTRLexicals" + "," + 
								LexicalSophisticationNGSL.calculateSophCTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophCTTRNouns" + "," + 
								LexicalSophisticationNGSL.calculateSophCTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophCTTRVerbs" + "," + 
								LexicalSophisticationNGSL.calculateSophCTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophGTTRAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateSophGTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophGTTRAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateSophGTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophGTTRAllWords" + "," + 
								LexicalSophisticationNGSL.calculateSophGTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophGTTRLexicals" + "," + 
								LexicalSophisticationNGSL.calculateSophGTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophGTTRNouns" + "," + 
								LexicalSophisticationNGSL.calculateSophGTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophGTTRVerbs" + "," + 
								LexicalSophisticationNGSL.calculateSophGTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophLogTTRAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateSophLogTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophLogTTRAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateSophLogTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophLogTTRAllWords" + "," + 
								LexicalSophisticationNGSL.calculateSophLogTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophLogTTRLexicals" + "," + 
								LexicalSophisticationNGSL.calculateSophLogTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophLogTTRNouns" + "," + 
								LexicalSophisticationNGSL.calculateSophLogTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophLogTTRVerbs" + "," + 
								LexicalSophisticationNGSL.calculateSophLogTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophSTTRAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateSophSTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophSTTRAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateSophSTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophSTTRAllWords" + "," + 
								LexicalSophisticationNGSL.calculateSophSTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophSTTRLexicals" + "," + 
								LexicalSophisticationNGSL.calculateSophSTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophSTTRNouns" + "," + 
								LexicalSophisticationNGSL.calculateSophSTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophSTTRVerbs" + "," + 
								LexicalSophisticationNGSL.calculateSophSTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophTTRAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateSophTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophTTRAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateSophTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophTTRAllWords" + "," + 
								LexicalSophisticationNGSL.calculateSophTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophTTRLexicals" + "," + 
								LexicalSophisticationNGSL.calculateSophTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophTTRNouns" + "," + 
								LexicalSophisticationNGSL.calculateSophTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophTTRVerbs" + "," + 
								LexicalSophisticationNGSL.calculateSophTTRVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophUberTTRAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateSophUberTTRAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophUberTTRAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateSophUberTTRAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophUberTTRAllWords" + "," + 
								LexicalSophisticationNGSL.calculateSophUberTTRAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophUberTTRLexicals" + "," + 
								LexicalSophisticationNGSL.calculateSophUberTTRLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophUberTTRNouns" + "," + 
								LexicalSophisticationNGSL.calculateSophUberTTRNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophUberTTRVerbs" + "," + 
								LexicalSophisticationNGSL.calculateSophUberTTRVerbs(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophRatioTokensAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateSophRatioTokensAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophRatioTokensAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateSophRatioTokensAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophRatioTokensVerbs" + "," + 
								LexicalSophisticationNGSL.calculateSophRatioTokensVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophRatioTokensNouns" + "," + 
								LexicalSophisticationNGSL.calculateSophRatioTokensNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophRatioTokensLexicals" + "," + 
								LexicalSophisticationNGSL.calculateSophRatioTokensLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophRatioTokensAllWords" + "," + 
								LexicalSophisticationNGSL.calculateSophRatioTokensAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophRatioTypesAdjectives" + "," + 
								LexicalSophisticationNGSL.calculateSophRatioTypesAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophRatioTypesAdverbs" + "," + 
								LexicalSophisticationNGSL.calculateSophRatioTypesAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophRatioTypesVerbs" + "," + 
								LexicalSophisticationNGSL.calculateSophRatioTypesVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophRatioTypesNouns" + "," + 
								LexicalSophisticationNGSL.calculateSophRatioTypesNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophRatioTypesLexicals" + "," + 
								LexicalSophisticationNGSL.calculateSophRatioTypesLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLSophRatioTypesAllWords" + "," + 
								LexicalSophisticationNGSL.calculateSophRatioTypesAllWords(annotation) + "\n", "UTF-8", true);

				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNEasyLemmaTokensAdjectives" + "," + 
								LexicalSophisticationNGSL.countNEasyLemmaTokensAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNEasyLemmaTokensAdverb" + "," + 
								LexicalSophisticationNGSL.countNEasyLemmaTokensAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNEasyLemmaTokensVerbs" + "," + 
								LexicalSophisticationNGSL.countNEasyLemmaTokensVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNEasyLemmaTokensNouns" + "," + 
								LexicalSophisticationNGSL.countNEasyLemmaTokensNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNEasyLemmaTokensLexicals" + "," + 
								LexicalSophisticationNGSL.countNEasyLemmaTokensLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNEasyLemmaTokensAllWords" + "," + 
								LexicalSophisticationNGSL.countNEasyLemmaTokensAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNEasyLemmaTypesAdjectives" + "," + 
								LexicalSophisticationNGSL.countNEasyLemmaTypesAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNEasyLemmaTypesAdverb" + "," + 
								LexicalSophisticationNGSL.countNEasyLemmaTypesAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNEasyLemmaTypesVerbs" + "," + 
								LexicalSophisticationNGSL.countNEasyLemmaTypesVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNEasyLemmaTypesNouns" + "," + 
								LexicalSophisticationNGSL.countNEasyLemmaTypesNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNEasyLemmaTypesLexicals" + "," + 
								LexicalSophisticationNGSL.countNEasyLemmaTypesLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNEasyLemmaTypesAllWords" + "," + 
								LexicalSophisticationNGSL.countNEasyLemmaTypesAllWords(annotation) + "\n", "UTF-8", true);
		
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNSophLemmaTokensAdjectives" + "," + 
								LexicalSophisticationNGSL.countNSophLemmaTokensAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNSophLemmaTokensAdverb" + "," + 
								LexicalSophisticationNGSL.countNSophLemmaTokensAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNSophLemmaTokensVerbs" + "," + 
								LexicalSophisticationNGSL.countNSophLemmaTokensVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNSophLemmaTokensNouns" + "," + 
								LexicalSophisticationNGSL.countNSophLemmaTokensNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNSophLemmaTokensLexicals" + "," + 
								LexicalSophisticationNGSL.countNSophLemmaTokensLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNSophLemmaTokensAllWords" + "," + 
								LexicalSophisticationNGSL.countNSophLemmaTokensAllWords(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNSophLemmaTypesAdjectives" + "," + 
								LexicalSophisticationNGSL.countNSophLemmaTypesAdjectives(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNSophLemmaTypesAdverb" + "," + 
								LexicalSophisticationNGSL.countNSophLemmaTypesAdverbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNSophLemmaTypesVerbs" + "," + 
								LexicalSophisticationNGSL.countNSophLemmaTypesVerbs(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNSophLemmaTypesNouns" + "," + 
								LexicalSophisticationNGSL.countNSophLemmaTypesNouns(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNSophLemmaTypesLexicals" + "," + 
								LexicalSophisticationNGSL.countNSophLemmaTypesLexicals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "LexSophNGSLNSophLemmaTypesAllWords" + "," + 
								LexicalSophisticationNGSL.countNSophLemmaTypesAllWords(annotation) + "\n", "UTF-8", true);

				//syntactic complexity
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNAdjectiveClauses" + "," + 
								SyntacticCounts.countNAdjectiveClauses(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNAdjectivePhrases" + "," + 
								SyntacticCounts.countNAdjectivePhrases(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNAdverbialClauses" + "," + 
								SyntacticCounts.countNAdverbialClauses(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNAdverbPhrases" + "," + 
								SyntacticCounts.countNAdverbPhrases(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNClauses" + "," + 
								SyntacticCounts.countNClauses(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNComplexNorminals" + "," + 
								SyntacticCounts.countNComplexNorminals(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNComplexTunits" + "," + 
								SyntacticCounts.countNComplexTunits(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNCoordinateClauses" + "," + 
								SyntacticCounts.countNCoordinateClauses(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNCoordinatePhrases" + "," + 
								SyntacticCounts.countNCoordinatePhrases(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNDeclarativeClauses" + "," + 
								SyntacticCounts.countNDeclarativeClauses(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNDependentClauses" + "," + 
								SyntacticCounts.countNDependentClauses(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNDirectQuestions" + "," + 
								SyntacticCounts.countNDirectQuestions(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNFragmentClauses" + "," + 
								SyntacticCounts.countNFragmentClauses(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNFragmentTunits" + "," + 
								SyntacticCounts.countNFragmentTunits(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNInvertedDeclarativeSentences" + "," + 
								SyntacticCounts.countNInvertedDeclarativeSentences(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNLeftEmbed" + "," + 
								SyntacticCounts.countNLeftEmbed(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNNominalClauses" + "," + 
								SyntacticCounts.countNNominalClauses(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNNounPhrases" + "," + 
								SyntacticCounts.countNNounPhrases(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNPassiveClauses" + "," + 
								SyntacticCounts.countNPassiveClauses(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNPassiveSentences" + "," + 
								SyntacticCounts.countNPassiveSentences(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNPrepositionalPhrases" + "," + 
								SyntacticCounts.countNPrepositionalPhrases(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNSentences" + "," + 
								SyntacticCounts.countNSentences(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNSubordinateClauses" + "," + 
								SyntacticCounts.countNSubordinateClauses(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNTunits" + "," + 
								SyntacticCounts.countNTunits(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNUnknownConstituents" + "," + 
								SyntacticCounts.countNUnknownConstituents(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNVerbPhrases" + "," + 
								SyntacticCounts.countNVerbPhrases(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNWhAdverbPhrases" + "," + 
								SyntacticCounts.countNWhAdverbPhrases(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNWhNounPhrases" + "," + 
								SyntacticCounts.countNWhNounPhrases(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNWhPrepositionalPhrases" + "," + 
								SyntacticCounts.countNWhPrepositionalPhrases(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynNYesNoQuestions" + "," + 
								SyntacticCounts.countNYesNoQuestions(annotation) + "\n", "UTF-8", true);
				
				//syntactic ratios
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioCCperC" + "," + 
								SyntacticRatios.calculateSynRatioCCperC(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioCNperC" + "," + 
								SyntacticRatios.calculateSynRatioCNperC(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioCNperNP" + "," + 
								SyntacticRatios.calculateSynRatioCNperNP(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioCNperT" + "," + 
								SyntacticRatios.calculateSynRatioCNperT(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioCperS" + "," + 
								SyntacticRatios.calculateSynRatioCperS(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioCperT" + "," + 
								SyntacticRatios.calculateSynRatioCperT(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioCPperC" + "," + 
								SyntacticRatios.calculateSynRatioCPperC(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioCPperT" + "," + 
								SyntacticRatios.calculateSynRatioCPperT(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioCTperT" + "," + 
								SyntacticRatios.calculateSynRatioCTperT(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioDCperC" + "," + 
								SyntacticRatios.calculateSynRatioDCperC(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioDCperT" + "," + 
								SyntacticRatios.calculateSynRatioDCperT(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioMLC" + "," + 
								SyntacticRatios.calculateSynRatioMLC(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioMLT" + "," + 
								SyntacticRatios.calculateSynRatioMLT(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioMSLSyllables" + "," + 
								SyntacticRatios.calculateSynRatioMSLSyllables(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioMSLTokens" + "," + 
								SyntacticRatios.calculateSynRatioMSLTokens(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioTperS" + "," + 
								SyntacticRatios.calculateSynRatioTperS(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynRatioVPperT" + "," + 
								SyntacticRatios.calculateSynRatioVPperT(annotation) + "\n", "UTF-8", true);
				
				//syntactic indexes
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynEditDistanceLemmaGlobalMean" + "," + 
								SyntacticIndexes.calculateEditDistanceLemmaGlobalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynEditDistanceLemmaGlobalSD" + "," + 
								SyntacticIndexes.calculateEditDistanceLemmaGlobalSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynEditDistanceLemmaLocalMean" + "," + 
								SyntacticIndexes.calculateEditDistanceLemmaLocalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynEditDistanceLemmaLocalSD" + "," + 
								SyntacticIndexes.calculateEditDistanceLemmaLocalSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynEditDistancePosGlobalMean" + "," + 
								SyntacticIndexes.calculateEditDistancePosGlobalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynEditDistancePosGlobalSD" + "," + 
								SyntacticIndexes.calculateEditDistancePosGlobalSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynEditDistancePosLocalMean" + "," + 
								SyntacticIndexes.calculateEditDistancePosLocalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynEditDistancePosLocalSD" + "," + 
								SyntacticIndexes.calculateEditDistancePosLocalSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynEditDistanceTokenGlobalMean" + "," + 
								SyntacticIndexes.calculateEditDistanceTokenGlobalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynEditDistanceTokenGlobalSD" + "," + 
								SyntacticIndexes.calculateEditDistanceTokenGlobalSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynEditDistanceTokenLocalMean" + "," + 
								SyntacticIndexes.calculateEditDistanceTokenLocalMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "SynEditDistanceTokenLocalSD" + "," + 
								SyntacticIndexes.calculateEditDistanceTokenLocalSD(annotation) + "\n", "UTF-8", true);
	
				//cohesion measures
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CohGlobalArgumentOverlap" + "," + 
								ReferentialCohesion.calculateGlobalArgumentOverlap(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CohGlobalLexicalOverlapMean" + "," + 
								ReferentialCohesion.calculateGlobalLexicalOverlapMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CohGlobalLexicalOverlapSD" + "," + 
								ReferentialCohesion.calculateGlobalLexicalOverlapSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CohGlobalNounOverlap" + "," + 
								ReferentialCohesion.calculateGlobalNounOverlap(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CohGlobalStemOverlap" + "," + 
								ReferentialCohesion.calculateGlobalStemOverlap(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CohLocalArgumentOverlap" + "," + 
								ReferentialCohesion.calculateLocalArgumentOverlap(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CohLocalLexicalOverlapMean" + "," + 
								ReferentialCohesion.calculateLocalLexicalOverlapMean(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CohLocalLexicalOverlapSD" + "," + 
								ReferentialCohesion.calculateLocalLexicalOverlapSD(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CohLocalNounOverlap" + "," + 
								ReferentialCohesion.calculateLocalNounOverlap(annotation) + "\n", "UTF-8", true);
				FileUtils.writeStringToFile(resultsFile, 
						fileName + "," + "CohLocalStemOverlap" + "," + 
								ReferentialCohesion.calculateLocalStemOverlap(annotation) + "\n", "UTF-8", true);
	
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
