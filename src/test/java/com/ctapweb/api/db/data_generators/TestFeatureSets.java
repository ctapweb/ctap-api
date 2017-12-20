package com.ctapweb.api.db.data_generators;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ctapweb.api.db.pojos.FeatureSet;

/**
 * For generating feature sets for testing.
 * @author xiaobin
 *
 */
public class TestFeatureSets {
	/**
	 * Generate a feature set for certain user.
	 * @param ownerId
	 * @return
	 */
	public FeatureSet generateFeatureSet(long ownerId) {
		return generateFeatureSets(ownerId,1).get(0);
	}

	/**
	 * Generate the specified number of feature sets belonging to a certain user.
	 * @param ownerId
	 * @param size
	 * @return
	 */
	public List<FeatureSet> generateFeatureSets(long ownerId, int size) {
		List<FeatureSet> corpusList = new ArrayList<>();

		for(int i = 1; i <= size; i++) {
			FeatureSet featureSet = new FeatureSet(i, 
					ownerId,
					"Feature Set Name " + i,
					"Feature Set Description " + i, 
					new Timestamp(new Date().getTime()));

			corpusList.add(featureSet);

		}
		return corpusList;
	}



}
