package com.github.paulcwarren.ginkgo4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Ginkgo4jConfiguration {

	public static int DEFAULT_THREADS = 4;
	
	int threads() default DEFAULT_THREADS;

}
