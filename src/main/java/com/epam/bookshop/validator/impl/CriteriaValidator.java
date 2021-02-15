package com.epam.bookshop.validator.impl;

import com.epam.bookshop.constant.RegexConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.util.annotation.Naming;
import com.epam.bookshop.util.annotation.Size;
import com.epam.bookshop.util.criteria.Criteria;

import java.lang.reflect.Field;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Validates {@link com.epam.bookshop.util.criteria.Criteria} instances fields for
 * accordance regular expression patters
 */
public class CriteriaValidator {

    private final NamingValidator namingValidator = new NamingValidator();
    private final SizeValidator sizeValidator = new SizeValidator();

    private String locale = UtilStringConstants.US;

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
     * Validates passed criteria instance fields
     *
     * @param criteria needs to be validated
     * @throws ValidatorException if one of the entities fails validation
     */
    public void validate(Criteria<? extends Entity> criteria) throws ValidatorException {

        for (Field field : criteria.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Size.class)) {
                sizeValidator.validateCriteria(field, field.getAnnotation(Size.class), criteria);
            }
            if (field.isAnnotationPresent(Naming.class)) {
                namingValidator.validateCriteria(field, field.getAnnotation(Naming.class), criteria);
            }
        }
    }
}
