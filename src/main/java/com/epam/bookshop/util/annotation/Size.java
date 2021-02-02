package com.epam.bookshop.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for string fields that is used in validation and
 * clarificates for validator how to validate concrete string
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Size {
    int size() default Integer.MAX_VALUE;
    int max() default Integer.MAX_VALUE;
}
