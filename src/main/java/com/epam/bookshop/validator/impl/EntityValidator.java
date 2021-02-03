package com.epam.bookshop.validator.impl;

import com.epam.bookshop.util.annotation.Naming;
import com.epam.bookshop.util.annotation.Size;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.constant.RegexConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.criteria.Criteria;

import java.lang.reflect.Field;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates {@link Entity} instances for accordance
 * to regular expression pattern
 */
public class EntityValidator {

    private final NamingValidator namingValidator = new NamingValidator();
    private final SizeValidator sizeValidator = new SizeValidator();

    private String locale = "US";

    /**
     * Setter for {@code locale} field
     *
     * @param locale language for error messages
     */
    public void setLocale(String locale) {
        namingValidator.setLocale(locale);
        sizeValidator.setLocale(locale);
        this.locale = locale;
    }


    /**
     * Validates passed entity instance fields
     *
     * @param entity entity instance to validate
     * @throws ValidatorException if entity fails validation
     */
    public void validate(Entity entity) throws ValidatorException {

        sizeValidator.setLocale(locale);
        namingValidator.setLocale(locale);

        for (Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Size.class)) {
                sizeValidator.validateEntity(field, field.getAnnotation(Size.class), entity);
            }
            if (field.isAnnotationPresent(Naming.class)) {
                namingValidator.validateEntity(field, field.getAnnotation(Naming.class), entity);
            }
        }
    }
}
