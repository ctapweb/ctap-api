package com.ctapweb.api.db.data_generators;

import java.util.ArrayList;
import java.util.List;

import com.ctapweb.api.db.pojos.Tag;

/**
 * For generating tags for testing.
 * @author xiaobin
 *
 */
public class TestTags {
	/**
	 * Generate a tag for a corpus.
	 * @param corpusId
	 * @return
	 */
	public Tag generateTag(long corpusId) {
		return generateTags(corpusId,1).get(0);
	}

	/**
	 * Generate the specified number of tags belonging to a certain corpus.
	 * @param corpusId
	 * @param size
	 * @return
	 */
	public List<Tag> generateTags(long corpusId, int size) {
		List<Tag> tagList = new ArrayList<>();

		for(int i = 1; i <= size; i++) {
			Tag tag = new Tag(i, 
					corpusId,
					"Tag name " + i);
			tagList.add(tag);
		}
		return tagList;
	}



}
