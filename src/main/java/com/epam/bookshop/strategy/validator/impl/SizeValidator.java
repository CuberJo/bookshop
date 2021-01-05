package com.epam.bookshop.strategy.validator.impl;

import com.epam.bookshop.context.annotation.Size;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.strategy.validator.Validatable;
import com.epam.bookshop.exception.ValidatorException;

import java.lang.reflect.Field;

public class SizeValidator implements Validatable<Size> {

    private static SizeValidator instance;

    private SizeValidator() {

    }

    public static SizeValidator getInstance() {
        if (instance == null) {
            instance = new SizeValidator();
        }

        return instance;
    }

    @Override
    public void validateEntity(Field field, Size annotation, Entity entity) throws ValidatorException {

        try { // get(0) or get(book)
            if (annotation.size() != Integer.MAX_VALUE && field.getType().equals(String.class) && field.get(entity) != null && ((String) field.get(entity)).length() != annotation.size()) {
                throw new ValidatorException("validation failed, length of \"" + field.get(entity) + "\" not equals " + annotation.size());
            }
            if (annotation.max() != Integer.MAX_VALUE && field.getType().equals(String.class) && field.get(entity) != null && ((String) field.get(entity)).length() > annotation.size()) {
                throw new ValidatorException("validation failed, " + field.get(entity) + " not equals " + annotation.size());
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

        try { // get(0) or get(book)
            if (annotation.size() != Integer.MAX_VALUE && field.getType().equals(String.class) && field.get(criteria) != null && ((String) field.get(criteria)).length() != annotation.size()) {
                throw new ValidatorException("validation failed, length of \"" + field.get(criteria) + "\" not equals " + annotation.size());
            }
            if (annotation.max() != Integer.MAX_VALUE && field.getType().equals(String.class) && field.get(criteria) != null && ((String) field.get(criteria)).length() > annotation.size()) {
                throw new ValidatorException("validation failed, " + field.get(criteria) + " not equals " + annotation.size());
            }
//            if (field.getType().equals(int.class) && (int) field.get(0) < annotation.size()) {
//                throw new InvalidStateException("validation failed, " + field.get(0) + " length more than " + annotation.size());            } else {
//            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
