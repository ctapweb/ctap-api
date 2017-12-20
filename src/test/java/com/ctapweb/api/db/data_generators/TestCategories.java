package com.ctapweb.api.db.data_generators;

import java.util.ArrayList;
import java.util.List;

import com.ctapweb.api.db.pojos.MeasureCategory;
import com.ctapweb.api.measures.annotations.MeasureCategory.Languages;
import com.ctapweb.api.measures.annotations.MeasureCategory.Pipelines;

/**
 * For generating measure categories for testing.
 * @author xiaobin
 *
 */
public class TestCategories {
	public MeasureCategory generateCategory() {
		return generateCategories(Languages.ENGLISH, Pipelines.TOKENIZER_PIPELINE, 1).get(0);
	}
	
	public List<MeasureCategory> generateCategories(String language, String requiredPipeline, int size) {
		List<MeasureCategory> categoryList = new ArrayList<>();
		
		for(int i = 1; i <= size; i++) {
			MeasureCategory category = new MeasureCategory(i, 
					"Category name of " + language + i,
					"Description " + i,
					language,
					requiredPipeline,
					"Class name " + i
					);
			categoryList.add(category);
		}
		return categoryList;
	}
	
	

}
