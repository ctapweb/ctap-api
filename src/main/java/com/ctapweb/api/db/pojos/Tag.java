package com.ctapweb.api.db.pojos;

/**
 * A POJO for entry in Tag table.
 * @author xiaobin
 *
 */
public class Tag extends TablePojo {
	private long corpusId;
	private String name;
	
	

	public Tag(long id, long corpusId, String name) {
		super(id);
		this.corpusId = corpusId;
		this.name = name;
	}
	
	public Tag() {
	}

	public long getCorpusId() {
		return corpusId;
	}
	public void setCorpusId(long corpusId) {
		this.corpusId = corpusId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
