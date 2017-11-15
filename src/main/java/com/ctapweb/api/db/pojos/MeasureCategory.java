/**
 * 
 */
package com.ctapweb.api.db.pojos;

/**
 * A POJO for user account.
 * @author xiaobin
 *
 */
public class MeasureCategory extends TablePojo {
	private String name;
	private String description;

	public MeasureCategory() {
	}

	public MeasureCategory(long id, String name, String description) {
		super(id);
		this.name = name;
		this.description = description;
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

	
	

}
