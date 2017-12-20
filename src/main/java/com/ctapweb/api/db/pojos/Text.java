/**
 * 
 */
package com.ctapweb.api.db.pojos;

import java.sql.Timestamp;

/**
 * A POJO for Text.
 * @author xiaobin
 *
 */
public class Text extends TablePojo {
	private long corpusId;
	private String title;
	private String content;
	private String annotatedContent;
	private Long tagId;
	private Timestamp updateDate;
	
	public Text() {
	}

	public Text(long id, long corpusId, String title, String content, String annotatedContent, Long tagId, 
			Timestamp updateDate) {
		super(id);
		this.corpusId = corpusId;
		this.title = title;
		this.content = content;
		this.annotatedContent = annotatedContent;
		this.tagId = tagId;
		this.updateDate = updateDate;
	}

	public String getAnnotatedContent() {
		return annotatedContent;
	}

	public void setAnnotatedContent(String annotatedContent) {
		this.annotatedContent = annotatedContent;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public long getCorpusId() {
		return corpusId;
	}
	public void setCorpusId(long corpusId) {
		this.corpusId = corpusId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	
	
	

}
