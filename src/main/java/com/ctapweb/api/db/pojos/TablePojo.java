/**
 * 
 */
package com.ctapweb.api.db.pojos;

/**
 * An abstract class for all table POJOs.
 * @author xiaobin
 *
 */
public abstract class TablePojo {
	protected long id;

	public TablePojo() {
	}

	public TablePojo(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
}
