/**
 * 
 */
package com.ctapweb.api.main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.operations.AnalysisTableOperations;
import com.ctapweb.api.db.operations.FS_METableOperations;
import com.ctapweb.api.db.operations.MeasureCategoryTableOperations;
import com.ctapweb.api.db.operations.MeasureTableOperations;
import com.ctapweb.api.db.operations.ResultTableOperations;
import com.ctapweb.api.db.operations.TextTableOperations;
import com.ctapweb.api.db.pojos.Analysis;
import com.ctapweb.api.db.pojos.Fs_Me;
import com.ctapweb.api.db.pojos.Measure;
import com.ctapweb.api.db.pojos.MeasureCategory;
import com.ctapweb.api.db.pojos.Result;
import com.ctapweb.api.db.pojos.Text;
import com.ctapweb.api.measures.annotations.MeasureCategory.Pipelines;
import com.ctapweb.api.measures.utils.NLPPipeLinesManager;
import com.ctapweb.api.servlets.exceptions.CTAPException;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * For running an analysis.
 * @author xiaobin
 *
 */
public class RunAnalysis {
	private StanfordCoreNLP pipeline;	
	private long analysisId;
	private Analysis analysis;
	private AnalysisTableOperations analysisTableOperations;
	private TextTableOperations textTableOperations;
	private MeasureTableOperations measureTableOperations;
	private FS_METableOperations fs_METableOperations;
	private MeasureCategoryTableOperations categoryTableOperations;
	private ResultTableOperations resultTableOperations;
	private LanguageDetector languageDetector;
	private Logger logger = LogManager.getLogger();
	private Map<String, Integer> pipelineWeights;

	public RunAnalysis(long analysisId) throws ClassNotFoundException, IOException, SQLException {
		//init table operations
		this.analysisId = analysisId;
		DataSource dataSource = DataSourceManager.getDataSource();
		analysisTableOperations = new AnalysisTableOperations(dataSource);
		textTableOperations = new TextTableOperations(dataSource);
		fs_METableOperations = new FS_METableOperations(dataSource);
		measureTableOperations = new MeasureTableOperations(dataSource);
		categoryTableOperations = new MeasureCategoryTableOperations(dataSource);
		resultTableOperations = new ResultTableOperations(dataSource);
//		pipeline = NLPPipeLinesManager.getCompletePipe();
		languageDetector = new OptimaizeLangDetector().loadModels();
		
		pipelineWeights = new HashMap<>();
		pipelineWeights.put(com.ctapweb.api.measures.annotations.MeasureCategory.Pipelines.TOKENIZER_PIPELINE, 1);
		pipelineWeights.put(com.ctapweb.api.measures.annotations.MeasureCategory.Pipelines.POSTAGGER_PIPELINE, 2);
		pipelineWeights.put(com.ctapweb.api.measures.annotations.MeasureCategory.Pipelines.LEMMATIZER_PIPELINE, 3);
		pipelineWeights.put(com.ctapweb.api.measures.annotations.MeasureCategory.Pipelines.PARSER_PIPELINE, 4);
		pipelineWeights.put(com.ctapweb.api.measures.annotations.MeasureCategory.Pipelines.COMPLETE_PIPELINE, 5);

	}

	public void run() throws SQLException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, CTAPException, IOException {
		//gets analysis info
		analysis = analysisTableOperations.getEntry(analysisId);
		long corpusId = analysis.getCorpusId();
		long featureSetId = analysis.getFeatureSetId();

		//gets all texts in corpus
		List<Text> texts = textTableOperations.getAllEntriesByCorpus(corpusId);
		if(texts == null) {
			throw logger.throwing(new CTAPException("No texts in corpus " + corpusId + "."));
		}

		//gets all measures in feature set
		List<Measure> measureList = new ArrayList<>();
		List<Fs_Me> fs_Mes = fs_METableOperations.getAllEntriesByFeatureSet(featureSetId);
		if(fs_Mes == null) {
			throw logger.throwing(new CTAPException("No measures in feature set " + featureSetId + "."));
		}
		for(Fs_Me fs_me: fs_Mes) {
			Measure measure = measureTableOperations.getEntry(fs_me.getMeasure_id());
			measureList.add(measure);
		}

		//initialize analysis
		analysisTableOperations.updateStatus(analysisId, Analysis.Status.RUNNING);
		analysisTableOperations.updateProgress(analysisId, 0);
		resultTableOperations.deleteEntriesByAnalysis(analysisId);
		
		//find the measure with the most annotation demand---the heaviest pipeline
		pipeline = getHeaviestPipeline(measureList);
		
		//for each measure, search for the method that extracts the measure value
		long nMeasuresCompleted = 0;
		for(Measure measure: measureList) {
			//get the category the measure is in
			MeasureCategory category = categoryTableOperations.getEntry(measure.getCategoryId());
			Class clazz = Class.forName(category.getClassName());
			Method method = findMethod(clazz, measure.getName());		

			//get the annotation pipeline and supported language
//			pipeline = NLPPipeLinesManager.getPipeline(category.getRequiredPipeline());
			String measureLanguage = category.getLanguage();

			//invoke the method on annotated texts and store the result into db
			for(Text text: texts) {
				String textContent = text.getContent();
				long textId = text.getId();

				//detect text language, if not the language of the measure, skip the text
				String textLanguage = languageDetector.detect(textContent).getLanguage();
				if(!textLanguage.equals(measureLanguage)) {
					logger.trace("Detected text language '{}' not supported by measure language '{}'. Skipping the text...", 
							textLanguage, measureLanguage);
					continue;
				}
				
				//annotate the text
				Annotation annotation = new Annotation(textContent);
				pipeline.annotate(annotation);
				double measureValue = Double.valueOf(method.invoke(null, annotation).toString());
				
				//store annotated text in conllu format
//				OutputStream outputStream = new ByteArrayOutputStream();
//				CoNLLUOutputter.conllUPrint(annotation, outputStream);
//				CoNLLUOutputter.conllUPrint(annotation, System.out);
//				logger.info("Annotated content: {} ", outputStream.toString());
//				logger.info(annotation.toShorterString());
//				textTableOperations.updateAnnotatedContent(textId, outputStream.toString());

				//store the value
				Result resultEntry = new Result(1, analysisId, textId, measure.getId(), measureValue);
				resultTableOperations.addEntry(resultEntry);
				logger.trace("Added result {} of measure {} for analysis {} on text {}.", 
						measureValue, measure.getId(), analysisId, textId);

				//check analysis status, if status is stopped, stop the analysis
				String status = analysisTableOperations.getEntry(analysisId).getStatus();
				if(status.equals(Analysis.Status.STOPPED)) {
					//clear existing results
					resultTableOperations.deleteEntriesByAnalysis(analysisId);
					logger.info("Analysis {} stopped by user.", analysisId);
					return;
				}
			}

			//update progress: percentage of measures completed
			nMeasuresCompleted++;
			analysisTableOperations.updateProgress(analysisId, (double)nMeasuresCompleted/measureList.size());

		}
		analysisTableOperations.updateStatus(analysisId, Analysis.Status.FINISHED);
	}

	//find in a class the method that is annotated with @Measure and has the specified name
	private Method findMethod(Class clazz, String name) {
		for(Method method: clazz.getMethods()) {
			com.ctapweb.api.measures.annotations.Measure measureAnnotation = 
					(com.ctapweb.api.measures.annotations.Measure) method.getAnnotation(com.ctapweb.api.measures.annotations.Measure.class);
			//find the method with the an annotation of measure name
			if(measureAnnotation != null && measureAnnotation.name().equals(name)) {
				return method;
			}
		}
		return null;
	}

	
	private StanfordCoreNLP getHeaviestPipeline(List<Measure> measureList) throws SQLException {
		int heaviest = 1;
		StanfordCoreNLP heaviestPipeline = NLPPipeLinesManager.getPipeline(Pipelines.TOKENIZER_PIPELINE);

		for(Measure measure: measureList) {
			//get the category the measure is in
			MeasureCategory category = categoryTableOperations.getEntry(measure.getCategoryId());

			//get the annotation pipeline and supported language
			String requirePipeline = category.getRequiredPipeline();
			if(pipelineWeights.get(requirePipeline) > heaviest) {
				heaviestPipeline = NLPPipeLinesManager.getPipeline(requirePipeline);
			}
		}
		
		return heaviestPipeline;
	}

}










