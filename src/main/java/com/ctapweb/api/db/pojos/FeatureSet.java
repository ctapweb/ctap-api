/**
 * 
 */
package com.ctapweb.api.db.pojos;

import java.sql.Timestamp;
import java.util.Date;

/**
 * A POJO for feature set.
 * @author xiaobin
 *
 */
public class FeatureSet extends TablePojo {

	private long ownerId;
	private String name;
	private String description;
	private Timestamp createDate;

	public FeatureSet() {
	}

	public FeatureSet(long id, long ownerId, String name, String description, Timestamp createDate) {
		super(id);
		this.ownerId = ownerId;
		this.name = name;
		this.description = description;
		this.createDate = createDate;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	

}
