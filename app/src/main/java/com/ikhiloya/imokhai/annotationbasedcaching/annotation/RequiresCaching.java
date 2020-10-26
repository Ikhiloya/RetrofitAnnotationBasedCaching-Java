package com.ikhiloya.imokhai.annotationbasedcaching.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a request for caching
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface RequiresCaching {
}
