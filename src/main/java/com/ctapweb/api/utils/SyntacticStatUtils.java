package com.ctapweb.api.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctapweb.api.lexical.internal.LexicalCounts;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.util.CoreMap;

public class SyntacticStatUtils {
	private static Logger logger = LogManager.getLogger();
	
	public static int getNPassiveClauses(Annotation annotation) {
		int nPassives = 0;
		
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		for(CoreMap sentence: annotation.get(SentencesAnnotation.class)) {
			Tree parseTree = sentence.get(TreeAnnotation.class);
			GrammaticalStructure gs = gsf.newGrammaticalStructure(parseTree);
			Collection<TypedDependency> tdl = gs.typedDependencies();
			for(TypedDependency td: tdl) {
				String relation = td.reln().getShortName();
				if("nsubjpass".equals(relation)) {
					nPassives++;
				}
			}
		}
		
		return nPassives;
		
	}
	
	//Number of sentences that contains passive voice, either in main or subordinate clauses.
	public static int getNPassiveSentences(Annotation annotation) {
		int nPassives = 0;
		
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		for(CoreMap sentence: annotation.get(SentencesAnnotation.class)) {
			Tree parseTree = sentence.get(TreeAnnotation.class);
			GrammaticalStructure gs = gsf.newGrammaticalStructure(parseTree);
			Collection<TypedDependency> tdl = gs.typedDependencies();
			for(TypedDependency td: tdl) {
				String relation = td.reln().getShortName();
				if("nsubjpass".equals(relation)) {
					nPassives++;
					break; //break once a passive is found in a sentence, avoid counting clauses
				}
			}
		}
		return nPassives;
	}

	public static int getNAdverbialClauses(Annotation annotation) {
		int nAdvClauses = 0;
		
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		for(CoreMap sentence: annotation.get(SentencesAnnotation.class)) {
			Tree parseTree = sentence.get(TreeAnnotation.class);
			GrammaticalStructure gs = gsf.newGrammaticalStructure(parseTree);
			Collection<TypedDependency> tdl = gs.typedDependencies();
			for(TypedDependency td: tdl) {
				String relation = td.reln().getShortName();
				if("advcl".equals(relation)) {
					nAdvClauses++;
				}
			}
		}
		return nAdvClauses;
	}
	
	public static int getNSynConstituentsAdjectivePhrases(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"ADJP"
				});
	}

	public static int getNSynConstituentsAdverbPhrases(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"ADVP"
				});
	}

	public static int getNSynConstituentsNounPhrases(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"NP"
				});
	}

	public static int getNSynConstituentsPrepositionalPhrases(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"PP"
				});
	}

	public static int getNSynConstituentsDeclarativeClauses(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"S"
				});
	}

	public static int getNSynConstituentsSubordinateClauses(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"SBAR"
				});
	}

	public static int getNSynConstituentsDirectQuestions(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"SBARQ"
				});
	}

	public static int getNSynConstituentsInvertedDeclarativeSentences(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"SINV"
				});
	}

	public static int getNSynConstituentsYesNoQuestions(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"SQ"
				});
	}

	public static int getNSynConstituentsVP(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"VP"
				});
	}

	public static int getNSynConstituentsWhAdverbPhrases(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"WHADVP"
				});
	}

	public static int getNSynConstituentsWhNounPhrases(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"WHNP"
				});
	}

	public static int getNSynConstituentsPrepPhrases(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"WHPP"
				});
	}

	public static int getNSynConstituentsUnknownConstituents(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"X"
				});
	}
	
	

	public static int getNSynContituents(Annotation annotation, String[] tregexPatternStrings) {
		int nConstituents = 0;

		//compile tregex patterns
		List<TregexPattern> tregexPatterns = new ArrayList<>();
		for(String patternStr: tregexPatternStrings) {
			tregexPatterns.add(TregexPattern.compile(patternStr));
		}

		//get the annotated tree
		for(CoreMap sentence: annotation.get(SentencesAnnotation.class)) {
			Tree tree = sentence.get(TreeAnnotation.class);
			
			//use tregex to extract constituents
			for(TregexPattern pattern: tregexPatterns) {
				TregexMatcher matcher = pattern.matcher(tree);
				while(matcher.find()) {
//					matcher.getMatch().pennPrint();
					nConstituents++;
				}
			}
		}
		
		return nConstituents;
	}
	
	public static int getNSynConstituentsClauses(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"S|SINV|SQ [> ROOT <, (VP <# VB) | <# MD|VBZ|VBP|VBD | < (VP [<# MD|VBP|VBZ|VBD | < CC < (VP <# MD|VBP|VBZ|VBD)])]"
				});
	}
	
	public static int getNSynConstituentsComplexNorminals(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"NP !> NP [<< JJ|POS|PP|S|VBG | << (NP $++ NP !$+ CC)]", 
				"SBAR [<# WHNP | <# (IN < That|that|For|for) | <, S] & [$+ VP | > VP]",
				"S < (VP <# VBG|TO) $+ VP"
		});
	}

	public static int getNSynConstituentsCoordinatePhrases(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"ADJP|ADVP|NP|VP < CC" 
		});
	}
	
	public static int getNSynConstituentsComplexTunits(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"S|SBARQ|SINV|SQ [> ROOT | [$-- S|SBARQ|SINV|SQ !>> SBAR|VP]] << (SBAR < (S|SINV|SQ [> ROOT <, (VP <# VB) | <# MD|VBZ|VBP|VBD | < (VP [<# MD|VBP|VBZ|VBD | < CC < (VP <# MD|VBP|VBZ|VBD)])]))" 
		});
	}
	
	public static int getNSynConstituentsDependentClauses(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"SBAR < (S|SINV|SQ [> ROOT <, (VP <# VB) | <# MD|VBZ|VBP|VBD | < (VP [<# MD|VBP|VBZ|VBD | < CC < (VP <# MD|VBP|VBZ|VBD)])])"
		});
	}
	
	public static void main(String[] args) {
		StanfordCoreNLP pipeline = NLPPipeLinesManager.getParser();
		Annotation annotation = new Annotation("This is a sentence that has many words and a clause. This is a sentence that has many words and a clause.");
		pipeline.annotate(annotation);
		getNSynConstituentsClauses(annotation);
	}

	public static int getNSynConstituentsFragmentClauses(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"FRAG > ROOT !<< (S|SINV|SQ [> TOP <, (VP <# VB) | <# MD|VBZ|VBP|VBD | < (VP [<# MD|VBP|VBZ|VBD | < CC < (VP <# MD|VBP|VBZ|VBD)])])"
		});
	}

	public static int getNSynConstituentsFragmentTunits(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"FRAG > ROOT !<< (S|SBARQ|SINV|SQ > ROOT | [$-- S|SBARQ|SINV|SQ !>> SBAR|VP])"
		});
	}

	//Some setences for testing:
	// http://examples.yourdictionary.com/example-adjective-clauses.html
	public static int getNSynConstituentsAdjectiveClauses(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"/NN.?/ . (/,/ . SBAR) | /NN.?/ . SBAR | PRP . SBAR | DT . SBAR"
		});
	}
	
	//count number of coordinators connecting sentences (S), adding 1 gets the
	//number of coordinating clauses
	public static int getNSynConstituentsCoordinateClauses(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"CC $-- S $++ S"
		}) + 1;
	}
	
	//Some setences for testing:
	//http://www.k12reader.com/term/noun-clause/
	public static int getNSynConstituentsNominalClauses(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"SBAR [> VP | > PP | > VP]  [ < WHNP | < WHADVP | < IN] !, NP"
		});
	}

	public static int getNSynConstituentsSentences(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"ROOT"
		});
	}

	public static int getNSynConstituentsTunits(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"S|SBARQ|SINV|SQ > ROOT | [$-- S|SBARQ|SINV|SQ !>> SBAR|VP]"
		});
	}

	public static int getNSynConstituentsVerbPhrases(Annotation annotation) {
		return getNSynContituents(annotation, new String[] {
				"VP > S|SINV|SQ",
				"MD|VBZ|VBP|VBD > (SQ !< VP)"
		});
	}
	
	public static double getSynRatioCNperC(Annotation annotation) {
		return (double) getNSynConstituentsComplexNorminals(annotation) / getNSynConstituentsClauses(annotation);
	}
	
	public static double getSynRatioCCperC(Annotation annotation) {
		return (double) getNSynConstituentsCoordinateClauses(annotation) / getNSynConstituentsClauses(annotation);
	}
	
	public static double getSynRatioCNperT(Annotation annotation) {
		return (double) getNSynConstituentsComplexNorminals(annotation) / getNSynConstituentsTunits(annotation);
	}
	
	public static double getSynRatioCperS(Annotation annotation) {
		return (double) getNSynConstituentsClauses(annotation) / getNSynConstituentsSentences(annotation);
	}

	public static double getSynRatioCperT(Annotation annotation) {
		return (double) getNSynConstituentsClauses(annotation) / getNSynConstituentsTunits(annotation);
	}

	public static double getSynRatioCPperC(Annotation annotation) {
		return (double) getNSynConstituentsCoordinatePhrases(annotation) / getNSynConstituentsClauses(annotation);
	}

	public static double getSynRatioCPperT(Annotation annotation) {
		return (double) getNSynConstituentsCoordinatePhrases(annotation) / getNSynConstituentsTunits(annotation);
	}

	public static double getSynRatioCTperT(Annotation annotation) {
		return (double) getNSynConstituentsComplexTunits(annotation) / getNSynConstituentsTunits(annotation);
	}

	public static double getSynRatioDCperC(Annotation annotation) {
		return (double) getNSynConstituentsDependentClauses(annotation) / getNSynConstituentsClauses(annotation);
	}

	public static double getSynRatioDCperT(Annotation annotation) {
		return (double) getNSynConstituentsDependentClauses(annotation) / getNSynConstituentsTunits(annotation);
	}
	
	public static double getSynRatioMLC(Annotation annotation) {
		return (double) (LexicalCounts.countNTokensAll(annotation) 
				- LexicalCounts.countNTokensPuncts(annotation)) / getNSynConstituentsClauses(annotation);
	}
	
	public static double getSynRatioMLT(Annotation annotation) {
		return (double) (LexicalCounts.countNTokensAll(annotation) 
				- LexicalCounts.countNTokensPuncts(annotation)) / getNSynConstituentsTunits(annotation);
	}
	
	public static double getSynRatioTperS(Annotation annotation) {
		return (double) getNSynConstituentsTunits(annotation) / getNSynConstituentsSentences(annotation);
	}
	
	public static double getSynRatioVPperT(Annotation annotation) {
		return (double) getNSynConstituentsVerbPhrases(annotation) / getNSynConstituentsTunits(annotation);
	}
	
	
}








