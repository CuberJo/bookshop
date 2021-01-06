package com.epam.bookshop.strategy.validator.impl;

import com.epam.bookshop.context.annotation.Size;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.strategy.validator.Validatable;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.util.manager.ErrorMessageManager;

import java.lang.reflect.Field;

public class SizeValidator implements Validatable<Size> {

    private static final String VALIDATION_FAILED = "validation_fail";

    private String locale = "EN";

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }


    @Override
    public void validateEntity(Field field, Size annotation, Entity entity) throws ValidatorException {

        String errorMessage = "";

        try { // get(0) or get(book)
            if (annotation.size() != Integer.MAX_VALUE && field.getType().equals(String.class) && field.get(entity) != null && ((String) field.get(entity)).length() != annotation.size()) {
                errorMessage = String.format(ErrorMessageManager.valueOf(locale).getMessage(VALIDATION_FAILED), field.get(entity), annotation.size());
                throw new ValidatorException(errorMessage);
//                throw new ValidatorException("validation failed, length of \"" + field.get(entity) + "\" not equals " + annotation.size());
            }
            if (annotation.max() != Integer.MAX_VALUE && field.getType().equals(String.class) && field.get(entity) != null && ((String) field.get(entity)).length() > annotation.size()) {
                errorMessage = String.format(ErrorMessageManager.valueOf(locale).getMessage(VALIDATION_FAILED), field.get(entity), annotation.size());
                throw new ValidatorException(errorMessage);
                //                throw new ValidatorException("validation failed, " + field.get(entity) + " not equals " + annotation.size());
            }
//            if (field.getType().equals(int.class) && (int) field.get(0) < annotation.size()) {
//                throw new InvalidStateException("validation failed, " + field.get(0) + " length more than " + annotation.size());            } else {
//            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void validateCriteria(Field field, Size annotation, Criteria criteria) throws ValidatorException {

        String errorMessage = "";

        try { // get(0) or get(book)
            if (annotation.size() != Integer.MAX_VALUE && field.getType().equals(String.class) && field.get(criteria) != null && ((String) field.get(criteria)).length() != annotation.size()) {
                errorMessage = String.format(ErrorMessageManager.valueOf(locale).getMessage(VALIDATION_FAILED), field.get(criteria), annotation.size());
                throw new ValidatorException(errorMessage);
                //                throw new ValidatorException("validation failed, length of \"" + field.get(criteria) + "\" not equals " + annotation.size());
            }
            if (annotation.max() != Integer.MAX_VALUE && field.getType().equals(String.class) && field.get(criteria) != null && ((String) field.get(criteria)).length() > annotation.size()) {
                errorMessage = String.format(ErrorMessageManager.valueOf(locale).getMessage(VALIDATION_FAILED), field.get(criteria), annotation.size());
                throw new ValidatorException(errorMessage);
                //                throw new ValidatorException("validation failed, " + field.get(criteria) + " not equals " + annotation.size());
            }
//            if (field.getType().equals(int.class) && (int) field.get(0) < annotation.size()) {
//                throw new InvalidStateException("validation failed, " + field.get(0) + " length more than " + annotation.size());            } else {
//            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
