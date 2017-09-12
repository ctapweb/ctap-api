package com.ctapweb.api.uima.annotators;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.junit.Assert;
import org.junit.Test;

import com.ctapweb.api.uima.types.Token;

public class SentenceSplitterTest {

	@Test
	public void testAggregate() throws IOException {
		try {
			JCas enJCas = JCasFactory.createJCas();

			//create the AAE pipeline
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			URL languageDetectorURL = classLoader.getResource("descriptors/annotators/LanguageDetector.xml");
			URL tokenizerURL = classLoader.getResource("descriptors/annotators/Tokenizer.xml");
			URL sentenceSplitterURL = classLoader.getResource("descriptors/annotators/SentenceSplitter.xml");
			AnalysisEngine langDetectorAE = 
					AnalysisEngineFactory.createEngineFromPath(languageDetectorURL.getPath());
			AnalysisEngine tokenizerAE = 
					AnalysisEngineFactory.createEngineFromPath(tokenizerURL.getPath());
			AnalysisEngine sentenceSplitterAE = 
					AnalysisEngineFactory.createEngineFromPath(sentenceSplitterURL.getPath());

			//run the AAE pipeline
			enJCas.setDocumentText("The projectile was apparently tracked by the three Aegis destroyers, "
					+ "each equipped with Standard Missile-3 interceptor missiles that are constantly "
					+ "deployed in the Sea of Japan. A second layer of close-in defense is provided by "
					+ "the Air Self-Defense Force's ground-based Patriot Advanced Capability-3 missiles, "
					+ "with the ASDF's PAC-3 unit in Hokkaido based at Chitose Air base. \"By the time "
					+ "this got over Japan, this thing was very high and moving extremely fast,\" "
					+ "Lance Gatling, a defense analyst and president of Tokyo-based Nexial Research Inc., told DW.");
			SimplePipeline.runPipeline(enJCas, langDetectorAE, tokenizerAE, sentenceSplitterAE);
			Assert.assertEquals("en", enJCas.getDocumentLanguage());

			Iterator<Token> tokenIterator = enJCas.getAnnotationIndex(Token.class).iterator();
			int nTokens = 0;
			while(tokenIterator.hasNext()) {
				Token token = tokenIterator.next();
				nTokens++;
			}
//			Assert.assertEquals(33, nTokens);


		} catch (UIMAException e) {
			e.printStackTrace();
		} 
	}

	@Test(expected = AnalysisEngineProcessException.class)
	public void testPrimitive() throws IOException, UIMAException {
		//create the primitive AE
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL tokenizerURL = classLoader.getResource("descriptors/annotators/Tokenizer.xml");
		AnalysisEngine tokenizerAE = AnalysisEngineFactory.createEngineFromPath(tokenizerURL.getPath());
		JCas enJCas;
		enJCas = JCasFactory.createJCas();
		enJCas.setDocumentText("An unconfirmed report emerges that the hostage-taker at "
				+ "the supermarket is the person police were looking for in connection "
				+ "with the killing of a policewoman in a southern Paris suburb on Thursday.");
		enJCas.setDocumentLanguage("");
		SimplePipeline.runPipeline(enJCas, tokenizerAE);


	}
}
