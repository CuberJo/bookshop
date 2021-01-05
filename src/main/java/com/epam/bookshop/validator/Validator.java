package com.epam.bookshop.validator;

import com.epam.bookshop.context.annotation.Naming;
import com.epam.bookshop.context.annotation.Size;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.strategy.validator.Validatable;
import com.epam.bookshop.strategy.validator.impl.NamingValidator;
import com.epam.bookshop.strategy.validator.impl.SizeValidator;

import java.lang.reflect.Field;

public class Validator {

    private Validatable validatable;

    private static final String SEMICOLON = ";";
    private static final String AND = "AND";
    private static final String WHITESPACE = " ";

    public void validate(Entity entity) throws ValidatorException {

        for (Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Size.class)) {
                validatable = new SizeValidator();
                validatable.validateEntity(field, field.getAnnotation(Size.class), entity);
            }
            if (field.isAnnotationPresent(Naming.class)) {
                validatable = new NamingValidator();
                validatable.validateEntity(field, field.getAnnotation(Naming.class), entity);
            }
        }
    }

    public void validate(Criteria criteria) throws ValidatorException {

        for (Field field : criteria.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Size.class)) {
                validatable = new SizeValidator();
                validatable.validateCriteria(field, field.getAnnotation(Size.class), criteria);
            }
            if (field.isAnnotationPresent(Naming.class)) {
                validatable = new NamingValidator();
                validatable.validateCriteria(field, field.getAnnotation(Naming.class), criteria);
            }
        }
    }

    public String validatedQuery(StringBuffer condition, String sql_query) {
        checkForAND(condition);
        checkForWhitespace(condition);

        return sql_query + condition + SEMICOLON;
    }

    private boolean checkForWhitespace(StringBuffer s) {
        boolean isWhitespacePresent = false;
        int whitespaceIndex = s.lastIndexOf(WHITESPACE);
        if (whitespaceIndex != -1) {
            isWhitespacePresent = true;
            s.deleteCharAt(whitespaceIndex);
        }

        return isWhitespacePresent;
    }

    private boolean checkForAND(StringBuffer s) {
        boolean isANDPresent = false;
        int whitespaceIndex = s.lastIndexOf(AND);
        if (whitespaceIndex != -1) {
            isANDPresent = true;
            s.delete(whitespaceIndex, s.length());
        }

        return isANDPresent;
    }
}
