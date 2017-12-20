package com.ctapweb.api.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.corpus_tools.pepper.cli.PepperStarterConfiguration;
import org.corpus_tools.pepper.common.CorpusDesc;
import org.corpus_tools.pepper.connectors.PepperConnector;
import org.corpus_tools.pepper.connectors.impl.PepperOSGiConnector;
import org.eclipse.emf.common.util.URI;

import com.ctapweb.api.db.DataSourceManager;
import com.ctapweb.api.db.operations.TextTableOperations;
import com.ctapweb.api.db.pojos.Text;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoNLLUOutputter;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;


public class CompleteAnnotateCorpus {

	private TextTableOperations textTableOperations;
	private StanfordCoreNLP pipeline;

	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {

		CompleteAnnotateCorpus completeAnnotateCorpus = new CompleteAnnotateCorpus();
//				completeAnnotateCorpus.annotateCorpus(1);
		completeAnnotateCorpus.conlluToAnnis();
//		completeAnnotateCorpus.testAnnotateCorpus();


	}

	public CompleteAnnotateCorpus() throws ClassNotFoundException, IOException, SQLException {
		DataSource dataSource = DataSourceManager.getDataSource();
		textTableOperations = new TextTableOperations(dataSource);
//		pipeline = NLPPipeLinesManager.getCompletePipe();
	}

	public void annotateCorpus(long corpusId) throws SQLException, IOException {
		List<Text> texts = textTableOperations.getAllEntriesByCorpus(corpusId);

		for(Text text: texts) {
			long textId = text.getId();
			String content = text.getContent();

			Annotation annotation = new Annotation(content);

			pipeline.annotate(annotation);

			OutputStream outputStream = new ByteArrayOutputStream();
			CoNLLUOutputter.conllUPrint(annotation, outputStream);
			
			List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
			for(CoreMap sentence: sentences) {
				Tree tree = sentence.get(TreeAnnotation.class);
				tree.pennPrint(System.out);
				
			}

			textTableOperations.updateAnnotatedContent(textId, outputStream.toString());

		}
	}

	private void conlluToAnnis() {
		PepperStarterConfiguration pepperConf = new PepperStarterConfiguration();
		pepperConf.setProperty(PepperStarterConfiguration.PROP_PLUGIN_PATH, "/home/xiaobin/tmp/pepper/plugins");
		PepperConnector pepper = new PepperOSGiConnector();
		pepper.setConfiguration(pepperConf);
		pepper.init();
		
		

//				for (PepperModuleDesc moduleDesc: pepper.getRegisteredModules()){
//					System.out.println(moduleDesc.getName());
//					System.out.println(moduleDesc.getVersion());
//					System.out.println(moduleDesc.getDesc());
//					System.out.println(moduleDesc.getModuleType());
//					System.out.println(moduleDesc.getSupplierContact());
//					System.out.println(moduleDesc.getSupportedFormats());
//				}

		CorpusDesc 
		
		
		CorpusDesc corpusExport= new CorpusDesc().setCorpusPath(URI.createFileURI("/home/xiaobin/tmp/pepper/corpus"));
		corpusExport.getFormatDesc().setFormatName("conll");
		corpusExport.getFormatDesc().setFormatVersion("0.001");

		CorpusDesc corpusImport= new CorpusDesc().setCorpusPath(URI.createFileURI("/home/xiaobin/tmp/pepper/corpus_output1"));
		corpusImport.getFormatDesc().setFormatName("annis");
		corpusImport.getFormatDesc().setFormatVersion("0.002");

		String jobId= pepper.createJob();
		PepperJob job= pepper.getJob(jobId);
		job.addStepDesc(new StepDesc().setProps(new Properties()).setCorpusDesc(corpusImport));
		job.addStepDesc(new StepDesc().setProps(new Properties()).setCorpusDesc(corpusExport));

		//start conversion
		job.convert(); 
	}
	
	public void testAnnotateCorpus() throws SQLException, IOException {

		String text = "Prosecutors have charged three men with the murder of Daphne Caruana Galizia, Maltese media reported on Tuesday. The 53-year-old journalist died when a bomb tore through her car on October 16 — a killing that shook the EU's smallest member state and raised Europe-wide concerns about the rule of law in the Mediterranean island country. Under tight security in Valletta, the suspects, Vince Muscat and brothers Alfred and George Degiorgio — all of whom have criminal records — pleaded not guilty to charges that include the possession of bomb-making material and weapons. Police did not immediately make clear whether they believed that the men had acted under contract to carry out the murder or plotted it on their own.";

			Annotation annotation = new Annotation(text);

			pipeline.annotate(annotation);

			CoNLLUOutputter.conllUPrint(annotation, System.out);
			
			List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
			for(CoreMap sentence: sentences) {
				Tree tree = sentence.get(TreeAnnotation.class);
				tree.pennPrint(System.out);
				
			}

	}

}
