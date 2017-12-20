/**
 * 
 */
package com.ctapweb.api.db.pojos;

/**
 * A POJO for fs_me.
 * @author xiaobin
 *
 */
public class Fs_Me extends TablePojo {

	private long fs_id;
	private long measure_id;

	public Fs_Me() {
	}

	public Fs_Me(long id, long fs_id, long measure_id) {
		super(id);
		this.fs_id = fs_id;
		this.measure_id = measure_id;
	}

	public long getFs_id() {
		return fs_id;
	}

	public void setFs_id(long fs_id) {
		this.fs_id = fs_id;
	}

	public long getMeasure_id() {
		return measure_id;
	}

	public void setMeasure_id(long measure_id) {
		this.measure_id = measure_id;
	}




}
