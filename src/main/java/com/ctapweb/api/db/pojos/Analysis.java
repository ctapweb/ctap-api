/**
 * 
 */
package com.ctapweb.api.db.pojos;

import java.sql.Timestamp;

/**
 * A POJO for Corpus.
 * @author xiaobin
 *
 */
public class Analysis extends TablePojo {

	private String name;
	private String description;
	private long corpusId;
	private long featureSetId;
	private String status;
	private double progress;
	private Timestamp createDate;

	public Analysis() {
	}

	public Analysis(long id, String name, String description, long corpusId, long featureSetId, 
			String status, double progress, Timestamp createDate) {
		super(id);
		this.name = name;
		this.description = description;
		this.corpusId = corpusId;
		this.featureSetId = featureSetId;
		this.status = status;
		this.progress = progress;
		this.createDate = createDate;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCorpusId() {
		return corpusId;
	}

	public void setCorpusId(long corpusId) {
		this.corpusId = corpusId;
	}

	public long getFeatureSetId() {
		return featureSetId;
	}

	public void setFeatureSetId(long featureSetId) {
		this.featureSetId = featureSetId;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public static class Status {
		public static final String STOPPED = "stopped";
		public static final String RUNNING = "running";
		public static final String FINISHED = "finished";
		
	}
	

}
