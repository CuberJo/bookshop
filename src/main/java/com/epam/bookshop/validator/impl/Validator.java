package com.epam.bookshop.validator.impl;

import com.epam.bookshop.util.annotation.Naming;
import com.epam.bookshop.util.annotation.Size;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.exception.ValidatorException;
import constant.RegexConstants;
import constant.UtilStringConstants;
import com.epam.bookshop.util.criteria.Criteria;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates instances
 */
public class Validator {

    private String locale = "US";

    private final NamingValidator namingValidator = new NamingValidator();
    private final SizeValidator sizeValidator = new SizeValidator();

    /**
     * Setter for <b>locale</b> field
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


    /**
     * Validates string by <b>regex</b> input
     *
     * @param stringToValidate string to validate
     * @param regex regular expression for validation
     */
    public boolean validate(String stringToValidate, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(stringToValidate);

        return m.matches();
    }


    /**
     * Removes extra "AND"and whitespaces.
     * If 'like' string present, adds '%' character.
     *
     * @param condition "WHERE" condition which is to be added to sql query
     * @return string without ANDs and whitespaces
     */
    public String validatedQuery(StringBuffer condition) {

        checkForLIKE(condition);
        checkForSeq(condition, UtilStringConstants.AND);
        checkForSeq(condition, UtilStringConstants.WHITESPACE);

        return condition + UtilStringConstants.SEMICOLON;
    }


    /**
     * Add '%' character whether a string <b>s</b> argument contains 'like' seq
     *
     * @param s string to be validated
     * @return new string with '%' character if 'like present'
     */
    private StringBuffer checkForLIKE(StringBuffer s) {
        final String like = "like";
        boolean containsLike = s.toString().toLowerCase().contains(like);
        if (containsLike) {
            s.replace(0, s.length(), s.toString().replaceAll(" '", " '%").replaceAll("' ", "%' "));
        }

        return s;
    }


    /**
     * Whether a string <b>s</b> argument has a special sequence of characters
     * <b>seq</b>
     *
     * @param s string to be validated
     * @return <b>true</b> if special sequence of characters <b>seq</b>
     * is present
     */
    private boolean checkForSeq(StringBuffer s, String seq) {
        boolean isSeqPresent = false;
        int whitespaceIndex = s.lastIndexOf(seq);
        if (whitespaceIndex != -1) {
            isSeqPresent = true;
            s.delete(whitespaceIndex, s.length());
        }

        return isSeqPresent;
    }


    /**
     * Checks strings for not being empty
     *
     * @param strings - Strings to be validated
     * @return <b>true</b> if, and only if, passed strings are empty
     */
    public boolean empty(String ... strings) {
        for (String string : strings) {
            if (string.equals(UtilStringConstants.EMPTY_STRING) || string.matches(RegexConstants.EMPTY_STRING_REGEX)) {
                return true;
            }
        }

        return false;
    }


    //    private boolean checkForAND(StringBuffer s) {
//        boolean isANDPresent = false;
//        int whitespaceIndex = s.lastIndexOf(AND);
//        if (whitespaceIndex != -1) {
//            isANDPresent = true;
//            s.delete(whitespaceIndex, s.length());
//        }
//
//        return isANDPresent;
//    }
//    private boolean checkForWhitespace(StringBuffer s) {
//        boolean isWhitespacePresent = false;
//        int whitespaceIndex = s.lastIndexOf(WHITESPACE);
//        if (whitespaceIndex != -1) {
//            isWhitespacePresent = true;
//            s.deleteCharAt(whitespaceIndex);
//        }
//
//        return isWhitespacePresent;
//    }

}
