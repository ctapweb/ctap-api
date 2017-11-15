package com.ctapweb.api.db.data_generators;

import java.util.ArrayList;
import java.util.List;

import com.ctapweb.api.db.pojos.MeasureCategory;

/**
 * For generating measure categories for testing.
 * @author xiaobin
 *
 */
public class TestCategories {
	public MeasureCategory generateCategory() {
		return generateCategories(1).get(0);
	}
	
	public List<MeasureCategory> generateCategories(int size) {
		List<MeasureCategory> categoryList = new ArrayList<>();
		
		for(int i = 1; i <= size; i++) {
			MeasureCategory category = new MeasureCategory(i, 
					"Category " + i, 
					"Description " + i);
			categoryList.add(category);
		}
		return categoryList;
	}
	
	

}
