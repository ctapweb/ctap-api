package com.ctapweb.api.db.pojos;

import java.net.URL;

/**
 * A POJO for tables.
 * @author xiaobin
 *
 */
public class Table {
	String name;
	long numEntries;
	URL url;
	
	public Table(String name, long numEntries, URL url) {
		super();
		this.name = name;
		this.numEntries = numEntries;
		this.url = url;
	}
	
	public Table() {
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getNumEntries() {
		return numEntries;
	}
	public void setNumEntries(long numEntries) {
		this.numEntries = numEntries;
	}
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	
	

}
