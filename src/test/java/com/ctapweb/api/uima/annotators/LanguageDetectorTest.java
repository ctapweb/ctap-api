package com.ctapweb.api.uima.annotators;

import java.io.IOException;
import java.net.URL;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.DocumentAnnotation;
import org.junit.Assert;
import org.junit.Test;

public class LanguageDetectorTest {

	@Test
	public void testLanguageDetector() throws IOException {
		try {
			//create the language detector AE
			JCas enJCas = JCasFactory.createJCas();
			JCas deJCas = JCasFactory.createJCas();
			JCas frJCas = JCasFactory.createJCas();
			JCas esJCas = JCasFactory.createJCas();

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			URL url = classLoader.getResource("descriptors/annotators/LanguageDetector.xml");
			AnalysisEngine langDetectorAE = 
					AnalysisEngineFactory.createEngineFromPath(url.getPath());

			//test English
			enJCas.setDocumentText("An unconfirmed report emerges that the hostage-taker at "
					+ "the supermarket is the person police were looking for in connection "
					+ "with the killing of a policewoman in a southern Paris suburb on Thursday.");
			SimplePipeline.runPipeline(enJCas, langDetectorAE);
			Assert.assertEquals("en", enJCas.getDocumentLanguage());
			
			//test French
			frJCas.setDocumentText("L'Espagnol Pau Gasol, crédité de 22 points et 15 rebonds, et "
					+ "Andrew Bynum (17 points et 14 rebonds malgré un genou douloureux), ont eux "
					+ "aussi été déterminants dans le succès des joueurs locaux.");
			SimplePipeline.runPipeline(frJCas, langDetectorAE);
			Assert.assertEquals("fr", frJCas.getDocumentLanguage());
			
			//test German
			deJCas.setDocumentText("Kerry für Sofortverhandlungen mit Russland über IS-Bekämpfung "
					+ "in SyrienDie USA drängen Russland zu Verhandlungen über den Kampf gegen die "
					+ "Terrormiliz Islamischer Staat (IS) in Syrien.");
			SimplePipeline.runPipeline(deJCas, langDetectorAE);
			Assert.assertEquals("de", deJCas.getDocumentLanguage());

			//test Spanish
			esJCas.setDocumentText("Mentiría si dijera que era del todo nuevo el sentimiento de que "
					+ "ya no iba a poder ser más que lo que era, que era un hombre que había envejecido "
					+ "más de lo que suponía, que había sospechado tener toda la vida por delante y había "
					+ "ido dejando pasar los años a la espera de que llegara su momento, y ahora la tenía a "
					+ "su espalda.");
			SimplePipeline.runPipeline(esJCas, langDetectorAE);
			Assert.assertEquals("es", esJCas.getDocumentLanguage());

		} catch (UIMAException e) {
			e.printStackTrace();
		} 
	}
}
