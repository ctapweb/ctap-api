/**
 * 
 */
package com.ctapweb.api.db.pojos;

/**
 * A POJO for entry of table Result.
 * @author xiaobin
 *
 */
public class Result extends TablePojo {
	private long textId;
	private long measureId;
	private double value;
	
	public Result() {
	}
	
	public Result(long id, long textId, long measureId, double value) {
		super(id);
		this.textId = textId;
		this.measureId = measureId;
		this.value = value;
	}

	public long getTextId() {
		return textId;
	}
	public void setTextId(long textId) {
		this.textId = textId;
	}
	public long getMeasureId() {
		return measureId;
	}
	public void setMeasureId(long measureId) {
		this.measureId = measureId;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	

}
