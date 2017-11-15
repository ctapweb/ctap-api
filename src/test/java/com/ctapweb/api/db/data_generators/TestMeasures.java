package com.ctapweb.api.db.data_generators;

import java.util.ArrayList;
import java.util.List;

import com.ctapweb.api.db.pojos.Measure;

/**
 * For generating measures for testing.
 * @author xiaobin
 *
 */
public class TestMeasures {
	/**
	 * Generate a measure for certain language.
	 * @param language
	 * @return
	 */
	public Measure generateMeasure(long categoryId, String language) {
		return generateMeasures(categoryId, language,  1).get(0);
	}

	/**
	 * Generate the specified number of measures of a certain language.
	 * @param categoryId
	 * @param language
	 * @param size
	 * @return
	 */
	public List<Measure> generateMeasures(long categoryId, String language, int size) {
		List<Measure> measureList = new ArrayList<>();

		for(int i = 1; i <= size; i++) {
			Measure measure = new Measure(i, 
					categoryId,
					language,
					categoryId + language + " Measure Name " + i, 
					"Description of measure " + i);

			measureList.add(measure);

		}
		return measureList;
	}



}
