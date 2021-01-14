package com.epam.bookshop.validator;

import com.epam.bookshop.context.annotation.Naming;
import com.epam.bookshop.context.annotation.Size;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.domain.impl.Regex;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.strategy.validator.impl.NamingValidator;
import com.epam.bookshop.strategy.validator.impl.SizeValidator;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private String locale = "EN";

    private NamingValidator namingValidator = new NamingValidator();
    private SizeValidator sizeValidator = new SizeValidator();

    public void setLocale(String locale) {
        namingValidator.setLocale(locale);
        sizeValidator.setLocale(locale);
        this.locale = locale;
    }


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

    public void validate(Criteria criteria) throws ValidatorException {

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

    public String sanitizeString(String s) {
        return Jsoup.clean(s, Whitelist.none());
    }

    public void validateString(String stringToValidate, String regex, String error) throws ValidatorException {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(stringToValidate);

        if (!m.matches()) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(error);
            throw new ValidatorException(errorMessage + UtilStrings.WHITESPACE + stringToValidate);
        }
    }

    /**
     * Removes extra "AND"and whitespaces
     *
     * @param condition "WHERE" condition which is to be added to sql query
     * @return string without ANDs and whitespaces
     */
    public String validatedQuery(StringBuffer condition) {
        checkForSeq(condition, UtilStrings.AND);
        checkForSeq(condition, UtilStrings.WHITESPACE);

        return condition + UtilStrings.SEMICOLON;
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
     * Checks strings for not being empty and correspond to special pattern
     *
     * @param strings - Strings to be validated
     * @return <b>true</b> if, and only if, passed strings not empty and correspond
     * to special pattern
     */
    public boolean emptyStringValidator(String ... strings) {
        for (String string : strings) {
            if (string.equals(UtilStrings.EMPTY_STRING) || string.matches(Regex.EMPTY_STRING_REGEX)) {
                return false;
            }
        }

        return true;
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
