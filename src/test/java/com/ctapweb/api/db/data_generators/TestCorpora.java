package com.ctapweb.api.db.data_generators;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ctapweb.api.db.pojos.Corpus;

/**
 * For generating corpora for testing.
 * @author xiaobin
 *
 */
public class TestCorpora {
	/**
	 * Generate a corpus for certain user.
	 * @param ownerId
	 * @return
	 */
	public Corpus generateCorpus(long ownerId) {
		return generateCorpora(ownerId,1).get(0);
	}

	/**
	 * Generate the specified number of corpora belonging to a certain user.
	 * @param ownerId
	 * @param size
	 * @return
	 */
	public List<Corpus> generateCorpora(long ownerId, int size) {
		List<Corpus> corpusList = new ArrayList<>();

		for(int i = 1; i <= size; i++) {
			Corpus corpus = new Corpus(i, 
					ownerId,
					"Corpus Name " + i,
					"Corpus Description " + i, 
					new Timestamp(new Date().getTime()));

			corpusList.add(corpus);

		}
		return corpusList;
	}



}
