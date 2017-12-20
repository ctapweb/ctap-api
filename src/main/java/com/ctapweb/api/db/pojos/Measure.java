/**
 * 
 */
package com.ctapweb.api.db.pojos;

/**
 * A POJO for Measure.
 * @author xiaobin
 *
 */
public class Measure extends TablePojo {

	private long categoryId;
	private String name;
	private String description;

	public Measure() {
	}


	public Measure(long id, long categoryId, String name, String description) {
		super(id);
		this.categoryId = categoryId;
		this.name = name;
		this.description = description;
	}
	
	public long getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (categoryId ^ (categoryId >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Measure other = (Measure) obj;
		if (categoryId != other.categoryId)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
