/**
 * 
 */
package com.ctapweb.api.measures.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
/**
 * An annotation for annotating measure categories.
 * @author xiaobin
 *
 */
public @interface MeasureCategory {
	String language();
	String name();
	String description() default "";
	String requiredPipeline();

	public class Categories {
		public static final String COMPLEXITY_LEXICAL_DENSITY = "complexity/lexical/density";
		public static final String COMPLEXITY_LEXICAL_VARIATION = "complexity/lexical/variation";
		public static final String COMPLEXITY_LEXICAL_SOPHISTICATION = "complexity/lexical/sophistication";

		public static final String COMPLEXITY_SYNTACTIC_DENSITY = "complexity/syntactic/density";
		public static final String COMPLEXITY_SYNTACTIC_RATIO = "complexity/syntactic/ratio";
		public static final String COMPLEXITY_MORHPOLOGY = "complexity/morphology";

		public static final String COMPLEXITY_DISCOURSE_COHESION = "complexity/discourse/cohesion";

		public static final String COMPLEXITY_COGNITIVE_PROCESSING = "complexity/cognitive_processing";

		public static final String ACCURACY_COUNT = "accuracy/count";
	}

	public class Pipelines {
		public static final String TOKENIZER_PIPELINE = "tokenizer";
		public static final String POSTAGGER_PIPELINE = "posTagger";
		public static final String LEMMATIZER_PIPELINE = "lemmatizer";
		public static final String PARSER_PIPELINE = "parser";
		public static final String COMPLETE_PIPELINE = "completePipe";
	}

	public class Languages {
		public static final String ENGLISH = "en";
		public static final String GERMAN = "de";
		public static final String FRENCH = "fr";
	}

}
