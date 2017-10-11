package com.ctapweb.api.complexity.syntactic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctapweb.api.utils.NLPPipeLinesManager;

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

public class SyntacticCounts {
	private static Logger logger = LogManager.getLogger();
	
	public static int countNPassiveClauses(Annotation annotation) {
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
	
	//left embeddedness: number of tokens before main verb
	public static int countNLeftEmbed(Annotation annotation) {
		int leftEmbed = 0;
		
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		for(CoreMap sentence: annotation.get(SentencesAnnotation.class)) {
			Tree parseTree = sentence.get(TreeAnnotation.class);
			GrammaticalStructure gs = gsf.newGrammaticalStructure(parseTree);
			Collection<TypedDependency> tdl = gs.typedDependencies();
			for(TypedDependency td: tdl) {
				String relation = td.reln().getShortName();
				if("root".equals(relation)) { //the root relation contains the index of the main verb
					leftEmbed = td.dep().index() - 1; //number of tokens before the main verb
				}
			}
		}
		
		return leftEmbed;
		
	}
	
	//Number of sentences that contains passive voice, either in main or subordinate clauses.
	public static int countNPassiveSentences(Annotation annotation) {
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

	public static int countNAdverbialClauses(Annotation annotation) {
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
	
	public static int countNAdjectivePhrases(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"ADJP"
				});
	}

	public static int countNAdverbPhrases(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"ADVP"
				});
	}

	public static int countNNounPhrases(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"NP"
				});
	}

	public static int countNPrepositionalPhrases(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"PP"
				});
	}

	public static int countNDeclarativeClauses(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"S"
				});
	}

	public static int countNSubordinateClauses(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"SBAR"
				});
	}

	public static int countNDirectQuestions(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"SBARQ"
				});
	}

	public static int countNInvertedDeclarativeSentences(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"SINV"
				});
	}

	public static int countNYesNoQuestions(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"SQ"
				});
	}

	public static int countNWhAdverbPhrases(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"WHADVP"
				});
	}

	public static int countNWhNounPhrases(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"WHNP"
				});
	}

	public static int countNWhPrepositionalPhrases(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"WHPP"
				});
	}

	public static int countNUnknownConstituents(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"X"
				});
	}
	
	

	private static int countNSynContituents(Annotation annotation, String[] tregexPatternStrings) {
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
	
	public static int countNClauses(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"S|SINV|SQ [> ROOT <, (VP <# VB) | <# MD|VBZ|VBP|VBD | < (VP [<# MD|VBP|VBZ|VBD | < CC < (VP <# MD|VBP|VBZ|VBD)])]"
				});
	}
	
	public static int countNComplexNorminals(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"NP !> NP [<< JJ|POS|PP|S|VBG | << (NP $++ NP !$+ CC)]", 
				"SBAR [<# WHNP | <# (IN < That|that|For|for) | <, S] & [$+ VP | > VP]",
				"S < (VP <# VBG|TO) $+ VP"
		});
	}

	public static int countNCoordinatePhrases(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"ADJP|ADVP|NP|VP < CC" 
		});
	}
	
	public static int countNComplexTunits(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"S|SBARQ|SINV|SQ [> ROOT | [$-- S|SBARQ|SINV|SQ !>> SBAR|VP]] << (SBAR < (S|SINV|SQ [> ROOT <, (VP <# VB) | <# MD|VBZ|VBP|VBD | < (VP [<# MD|VBP|VBZ|VBD | < CC < (VP <# MD|VBP|VBZ|VBD)])]))" 
		});
	}
	
	public static int countNDependentClauses(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"SBAR < (S|SINV|SQ [> ROOT <, (VP <# VB) | <# MD|VBZ|VBP|VBD | < (VP [<# MD|VBP|VBZ|VBD | < CC < (VP <# MD|VBP|VBZ|VBD)])])"
		});
	}
	
	public static void main(String[] args) {
		StanfordCoreNLP pipeline = NLPPipeLinesManager.getParser();
		Annotation annotation = new Annotation("This is a sentence that has many words and a clause. This is a sentence that has many words and a clause.");
		pipeline.annotate(annotation);
		countNClauses(annotation);
	}

	public static int countNFragmentClauses(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"FRAG > ROOT !<< (S|SINV|SQ [> TOP <, (VP <# VB) | <# MD|VBZ|VBP|VBD | < (VP [<# MD|VBP|VBZ|VBD | < CC < (VP <# MD|VBP|VBZ|VBD)])])"
		});
	}

	public static int countNFragmentTunits(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"FRAG > ROOT !<< (S|SBARQ|SINV|SQ > ROOT | [$-- S|SBARQ|SINV|SQ !>> SBAR|VP])"
		});
	}

	//Some setences for testing:
	// http://examples.yourdictionary.com/example-adjective-clauses.html
	public static int countNAdjectiveClauses(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"/NN.?/ . (/,/ . SBAR) | /NN.?/ . SBAR | PRP . SBAR | DT . SBAR"
		});
	}
	
	//count number of coordinators connecting sentences (S), adding 1 gets the
	//number of coordinating clauses
	public static int countNCoordinateClauses(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"CC $-- S $++ S"
		}) + 1;
	}
	
	//Some setences for testing:
	//http://www.k12reader.com/term/noun-clause/
	public static int countNNominalClauses(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"SBAR [> VP | > PP | > VP]  [ < WHNP | < WHADVP | < IN] !, NP"
		});
	}

	public static int countNSentences(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"ROOT"
		});
	}

	public static int countNTunits(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"S|SBARQ|SINV|SQ > ROOT | [$-- S|SBARQ|SINV|SQ !>> SBAR|VP]"
		});
	}

	public static int countNVerbPhrases(Annotation annotation) {
		return countNSynContituents(annotation, new String[] {
				"VP > S|SINV|SQ",
				"MD|VBZ|VBP|VBD > (SQ !< VP)"
		});
	}
	
	
}








