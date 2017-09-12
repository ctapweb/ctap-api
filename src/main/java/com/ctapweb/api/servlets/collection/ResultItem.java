package com.ctapweb.api.servlets.collection;

public class ResultItem {
	public static final String KEY_TEXT_TITLE = "text-title";
	public static final String KEY_AE_NAME = "AE-name";
	public static final String KEY_STAT_NAME = "stat-name";
	public static final String KEY_RESULT_VALUE = "result-value";

	private String textTitle;
	private String aeName;
	private String resultValue;
	public String getTextTitle() {
		return textTitle;
	}
	public void setTextTitle(String textTitle) {
		this.textTitle = textTitle;
	}
	public String getAeName() {
		return aeName;
	}
	public void setAeName(String aeName) {
		this.aeName = aeName;
	}
	public String getResultValue() {
		return resultValue;
	}
	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}
	
	
}
