package com.ctapweb.api.lexical.external;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;

public class FrequencyListUtils {
	private static final String BNC_LEMMA_TOP2000_PATH = "frequency_lists/bnc_lemma_top2000.txt";
	private static final String BNC_LEMMA_TOP1000_PATH = "frequency_lists/bnc_lemma_top1000.txt";
	private static final String SUBTLEXUS_SUBTL_WF_PATH = "frequency_lists/SUBTLEXus_SUBTLWF.csv";
	private static final String SUBTLEXUS_SUBTL_LOG10WF_PATH = "frequency_lists/SUBTLEXus_Log10WF.csv";
	private static final String SUBTLEXUS_SUBTL_CD_PATH = "frequency_lists/SUBTLEXus_SUBTLCD.csv";
	private static final String SUBTLEXUS_SUBTL_LOG10CD_PATH = "frequency_lists/SUBTLEXus_Log10CD.csv";
	private static Set<String> bncLemmaTop2000 = null;
	private static Set<String> bncLemmaTop1000 = null;
	private static Map<String, Double> subtlexUsWF = null;
	private static Map<String, Double> subtlexUsLog10WF = null;
	private static Map<String, Double> subtlexUsCD = null;
	private static Map<String, Double> subtlexUsLog10CD = null;

	private FrequencyListUtils() { }

	public static Set<String> getBncLemmaTop2000() throws IOException {
		if(bncLemmaTop2000 == null) {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream ins = classLoader.getResourceAsStream(BNC_LEMMA_TOP2000_PATH);
			List<String> wordList = IOUtils.readLines(ins, Charset.defaultCharset());
			bncLemmaTop2000 = new HashSet<>(wordList);
		}
		return bncLemmaTop2000;
	}

	public static Set<String> getBncLemmaTop1000() throws IOException {
		if(bncLemmaTop1000 == null) {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream ins = classLoader.getResourceAsStream(BNC_LEMMA_TOP1000_PATH);
			List<String> wordList = IOUtils.readLines(ins, Charset.defaultCharset());
			bncLemmaTop1000 = new HashSet<>(wordList);
		}
		return bncLemmaTop1000;
	}

	public static Map<String, Double> getSUBTLEXus_WF() throws IOException {
		if(subtlexUsWF== null) {
			subtlexUsWF = loadFreqList(SUBTLEXUS_SUBTL_WF_PATH);
		}
		return subtlexUsWF;
	}

	public static Map<String, Double> getSUBTLEXusLog10WF() throws IOException {
		if(subtlexUsLog10WF== null) {
			subtlexUsLog10WF = loadFreqList(SUBTLEXUS_SUBTL_LOG10WF_PATH);
		}
		return subtlexUsLog10WF;
	}

	public static Map<String, Double> getSUBTLEXus_CD() throws IOException {
		if(subtlexUsCD== null) {
			subtlexUsCD = loadFreqList(SUBTLEXUS_SUBTL_CD_PATH);
		}
		return subtlexUsCD;
	}

	public static Map<String, Double> getSUBTLEXusLog10CD() throws IOException {
		if(subtlexUsLog10CD== null) {
			subtlexUsLog10CD = loadFreqList(SUBTLEXUS_SUBTL_LOG10CD_PATH);
		}
		return subtlexUsLog10CD;
	}

	private static Map<String, Double> loadFreqList(String listPath) throws IOException {
		Map<String,  Double> freqMap = new HashMap<>();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream ins = classLoader.getResourceAsStream(listPath);
		List<String> lines = IOUtils.readLines(ins, Charset.defaultCharset());
		for(String line: lines) {
			String[] lineParts = line.split("\t");
			freqMap.put(lineParts[0], Double.parseDouble(lineParts[1]));
		}

		return freqMap;
	}

	public static void main(String[] args) {
		try {
			Map<String, Double> freqMap = getSUBTLEXus_WF();
			Set<String> keys = freqMap.keySet();
			for(String key: keys) {
				System.out.println(key + ", " + freqMap.get(key));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
