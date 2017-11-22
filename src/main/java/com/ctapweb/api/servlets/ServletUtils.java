package com.ctapweb.api.servlets;

public class ServletUtils {
	public static String createLinkHeader(String url, String rel) {
		return new StringBuilder()
				.append("<" + url + ">;")
				.append("rel=\"" + rel + "\"")
				.toString();
	}
}
