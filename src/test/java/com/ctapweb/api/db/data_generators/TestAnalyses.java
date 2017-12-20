package com.ctapweb.api.db.data_generators;

import java.sql.Timestamp;
import java.util.Date;

import com.ctapweb.api.db.pojos.Analysis;

/**
 * For generating analyses for testing.
 * @author xiaobin
 *
 */
public class TestAnalyses {
	/**
	 * Generate an analysis for certain user.
	 * @param ownerId
	 * @return
	 */
	public Analysis generateAnalysis(long corpusId, long featureSetId) {
		return new Analysis(1, 
				"Analysis Name ", 
				"Description of analysis", 
				corpusId, 
				featureSetId,
				null,
				0, 
				new Timestamp(new Date().getTime()));
	}

}
