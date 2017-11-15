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
	private Long tagId;
	private String status;
	private Timestamp updateDate;
	
	public static final String STATUS_ANALYZED = "analyzed";
	public static final String STATUS_ANALYZING = "analyzing";
	public static final String STATUS_NOT_ANALYZED = "not analyzed";
	
	public Text() {
	}

	public Text(long id, long corpusId, String title, String content, Long tagId, 
			String status, Timestamp updateDate) {
		super(id);
		this.corpusId = corpusId;
		this.title = title;
		this.content = content;
		this.tagId = tagId;
		this.status = status;
		this.updateDate = updateDate;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	
	
	

}
