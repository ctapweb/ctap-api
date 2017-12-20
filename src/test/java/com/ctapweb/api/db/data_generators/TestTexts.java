package com.ctapweb.api.db.data_generators;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ctapweb.api.db.pojos.Text;

/**
 * For generating texts for testing.
 * @author xiaobin
 *
 */
public class TestTexts {
	/**
	 * Generate a text for a corpus.
	 * @param corpusId
	 * @return
	 */
	public Text generateText(long corpusId, long tagId) {
		return generateTexts(corpusId, tagId, 1).get(0);
	}

	/**
	 * Generate the specified number of texts belonging to a certain corpus.
	 * @param corpusId
	 * @param size
	 * @return
	 */
	public List<Text> generateTexts(long corpusId, Long tagId, int size) {
		List<Text> textList = new ArrayList<>();

		for(int i = 1; i <= size; i++) {
			Text text = new Text(i, 
					corpusId,
					"Title " + i,
					"Content of text " + i, 
					tagId,
					new Timestamp(new Date().getTime()));

			textList.add(text);

		}
		return textList;
	}



}
