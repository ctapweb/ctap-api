package com.ctapweb.api.db.data_generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ctapweb.api.db.pojos.Result;

/**
 * For generating results for testing.
 * @author xiaobin
 *
 */
public class TestResults {
	/**
	 * Generate a result.
	 * @param corpusId
	 * @return
	 */
	public Result generateResult(long textId, long measureId) {
		return generateResults(textId, measureId, 1).get(0);
	}

	/**
	 * Generate the specified number of results.
	 * @param textId
	 * @param size
	 * @return
	 */
	public List<Result> generateResults(long textId, Long measureId, int size) {
		List<Result> resultList = new ArrayList<>();
		Random rand = new Random();

		for(int i = 1; i <= size; i++) {
			Result text = new Result(i, 
					textId,
					measureId,
					rand.nextDouble()
					);
			resultList.add(text);
		}
		return resultList;
	}



}
