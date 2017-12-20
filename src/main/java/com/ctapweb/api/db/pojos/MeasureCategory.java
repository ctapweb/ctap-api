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
	private String language;
	private String requiredPipeline;
	private String className;


	public MeasureCategory() {
	}


	public MeasureCategory(long id, String name, String description, String language, String requiredPipeline,
			String className) {
		super(id);
		this.name = name;
		this.description = description;
		this.language = language;
		this.requiredPipeline = requiredPipeline;
		this.className = className;
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


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public String getRequiredPipeline() {
		return requiredPipeline;
	}


	public void setRequiredPipeline(String requiredPipeline) {
		this.requiredPipeline = requiredPipeline;
	}


	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((language == null) ? 0 : language.hashCode());
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
		MeasureCategory other = (MeasureCategory) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}



	
	
	

}
