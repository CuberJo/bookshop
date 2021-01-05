package com.epam.bookshop.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Naming {
    boolean ISBN() default false;
    boolean email() default false;
    boolean title() default false;
    boolean author() default false;
    boolean publisher() default false;
    boolean name() default false;
    boolean login() default false;
    boolean genre() default false;
    boolean role() default false;
    boolean status() default false;
}
