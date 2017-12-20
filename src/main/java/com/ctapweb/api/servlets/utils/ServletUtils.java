package com.ctapweb.api.servlets.utils;

public class ServletUtils {
	public static String createLinkHeader(String url, String rel) {
		return new StringBuilder()
				.append("<" + url + ">;")
				.append("rel=\"" + rel + "\"")
				.toString();
	}
}
