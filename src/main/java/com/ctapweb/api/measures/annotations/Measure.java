/**
 * 
 */
package com.ctapweb.api.measures.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
/**
 * for annotating methods that extract measures
 * @author xiaobin
 *
 */
public @interface Measure {
	String name();
	String description() default "";
}
