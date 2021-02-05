package com.epam.bookshop.validator.impl;

import com.epam.bookshop.util.annotation.Size;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.validator.AnnotationValidator;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Validates {@link String} name fields of entities and criteria
 */
public class SizeValidator implements AnnotationValidator<Size> {
    private static final Logger logger = LoggerFactory.getLogger(SizeValidator.class);

    private static final String SIZE_VALIDATION_FAILED = "size_validation_failed";
    private String locale = "US";

    public void setLocale(String locale) {
        this.locale = locale;
    }


    @Override
    public void validateEntity(Field field, Size annotation, Entity entity) throws ValidatorException {

        String errorMessage = "";

        try {
            if (annotation.size() != Integer.MAX_VALUE
                    && field.getType().equals(String.class)
                    && field.get(entity) != null
                    && ((String) field.get(entity)).length() != annotation.size()) {
                errorMessage = String.format(ErrorMessageManager.valueOf(locale).getMessage(SIZE_VALIDATION_FAILED), field.get(entity), annotation.size());
                throw new ValidatorException(errorMessage);
            }
            if (annotation.max() != Integer.MAX_VALUE
                    && field.getType().equals(String.class)
                    && field.get(entity) != null
                    && ((String) field.get(entity)).length() > annotation.size()) {
                errorMessage = String.format(ErrorMessageManager.valueOf(locale).getMessage(SIZE_VALIDATION_FAILED), field.get(entity), annotation.size());
                throw new ValidatorException(errorMessage);
            }

        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
    }



    @Override
    public void validateCriteria(Field field, Size annotation, Criteria<? extends Entity> criteria) throws ValidatorException {

        String errorMessage = "";

        try {
            if (annotation.size() != Integer.MAX_VALUE
                    && field.getType().equals(String.class)
                    && field.get(criteria) != null
                    && ((String) field.get(criteria)).length() != annotation.size()) {
                errorMessage = String.format(ErrorMessageManager.valueOf(locale).getMessage(SIZE_VALIDATION_FAILED), field.get(criteria), annotation.size());
                throw new ValidatorException(errorMessage);
            }
            if (annotation.max() != Integer.MAX_VALUE
                    && field.getType().equals(String.class)
                    && field.get(criteria) != null
                    && ((String) field.get(criteria)).length() > annotation.size()) {
                errorMessage = String.format(ErrorMessageManager.valueOf(locale).getMessage(SIZE_VALIDATION_FAILED), field.get(criteria), annotation.size());
                throw new ValidatorException(errorMessage);
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
