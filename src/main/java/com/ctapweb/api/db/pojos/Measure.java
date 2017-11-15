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
	private String language;
	private String name;
	private String description;

	public Measure() {
	}


	public Measure(long id, long categoryId, String language, String name, String description) {
		super(id);
		this.categoryId = categoryId;
		this.language = language;
		this.name = name;
		this.description = description;
	}
	
	public long getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
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

	//the language a measure is applicable
	public static class Languages {
		public static final String English = "en";
		public static final String German = "de";
		public static final String French = "fr";
	}
}
