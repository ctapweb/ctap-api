package com.ctapweb.api.measures.complexity.lexical.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TTRUtils {

	public static double calcCTTR(List<String> tokensList) {
		if(tokensList.size() == 0) {
			return 0;
		}
		
		double cttr = 0;
		//make a set 
		Set<String> types = new HashSet<>(tokensList);
		cttr = (double) types.size() / Math.sqrt(2 * tokensList.size());
	
		return cttr;
	}

	public static double calcGTTR(List<String> tokensList) {
		if(tokensList.size() == 0) {
			return 0;
		}
		
		double gttr = 0;
		//make a set 
		Set<String> types = new HashSet<>(tokensList);
		gttr = (double) types.size() / Math.sqrt(tokensList.size());
	
		return gttr;
	}

	public static double calcLogTTR(List<String> tokensList) {
		if(tokensList.size() == 0) {
			return 0;
		}
		
		double logTTR = 0;
		//make a set 
		Set<String> types = new HashSet<>(tokensList);
		logTTR = (double) Math.log10(types.size()) / Math.log10(tokensList.size());
	
		return logTTR;
	}

	public static double calcMTLD(List<String> allWordTokens, boolean forward) {
			//calculate backward run of mtld if required
			if(!forward) {
				Collections.reverse(allWordTokens);
			}
	
			double mtld = 0;
			int size = allWordTokens.size();
			double defaultFactor = 0.720;
			List<String> subList = new ArrayList<>();
			double factors = 0;
			double subListTTR = 0;
			for(int i = 0; i < size; i++ ) {
				subList.add(allWordTokens.get(i));
				subListTTR = calcTTR(subList);
				if(subListTTR <= defaultFactor) {
					factors++;
					subList.clear();
				}
	//			logger.trace("word: {}, factors: {}", allWordTokens.get(i), factors);
			}
			double remainderTTR = 1- (double)(subListTTR - defaultFactor)/(1 - defaultFactor);
			mtld = size / (factors + remainderTTR);
			
	//		logger.trace("mtld: {} ", mtld);
			return mtld;
		}

	public static double calcSTTR(List<String> tokensList) {
		int nTokens = tokensList.size();
		if(nTokens == 0) {
			return 0;
		}
	
		double sttr = 0;
		//make a set 
		Set<String> types = new HashSet<>(tokensList);
		int nTypes = types.size();
	
		sttr = (double) Math.pow(nTokens, 2) / nTypes;
	
		return sttr;
	}

	//gets the ttr from a list of tokens (lower cased)
	public static double calcTTR(List<String> tokensList) {
		if(tokensList.size() == 0) {
			return 0;
		}
	
		double ttr = 0;
		//make a set 
		Set<String> types = new HashSet<>(tokensList);
		ttr = (double) types.size() / tokensList.size();
	
		return ttr;
	
	}

	public static double calcUberTTR(List<String> tokensList) {
		int nTokens = tokensList.size();
		if(nTokens == 0) {
			return 0;
		}
	
		double uberTTR = 0;
		//make a set
		Set<String> types = new HashSet<>(tokensList);
		int nTypes = types.size();
		

		uberTTR = (double) Math.pow(Math.log10(nTokens), 2) / Math.log10((double)nTokens / nTypes);
	
		return Double.isInfinite(uberTTR) ? 0 : uberTTR;
	}

}
