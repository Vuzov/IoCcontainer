package com.vuzov.container.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Annotation that marks a method to be scheduled.
 * Execute the annotated method with a fixed period between invocations.
 * Exactly one fixedRate() attribute must be specified.
 *
 * {@link Scheduled#rate()} default value 0
 * {@link Scheduled#unit()} default value TimeUnit.MILLISECONDS
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Scheduled {

    long rate() default 0;

    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
