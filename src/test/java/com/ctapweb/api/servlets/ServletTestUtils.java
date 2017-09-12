package com.ctapweb.api.servlets;

import org.eclipse.jetty.client.HttpClient;

public class ServletTestUtils {
	private static HttpClient httpClient = null;

	/**
	 * Gets a singleton http client.
	 * @return
	 * @throws Exception
	 */
	public static HttpClient getHttpClient() throws Exception {
		if(httpClient == null) {
			httpClient = new HttpClient();
			httpClient.start();
		}
		return httpClient;
	}
}
