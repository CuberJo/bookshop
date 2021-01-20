package com.epam.bookshop.strategy.validator.impl;

import com.epam.bookshop.context.annotation.Naming;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.*;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.domain.impl.*;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.strategy.validator.Validatable;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RegexConstant;
import com.epam.bookshop.constant.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamingValidator implements Validatable<Naming> {

    private String locale = "US";

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }


    @Override
    public void validateEntity(Field field, Naming annotation, Entity entity) throws ValidatorException {

        Pattern p;
        Matcher m;

        String errorMessage = "";

        if (annotation.ISBN() && ((Book) entity).getISBN() != null) {
            p = Pattern.compile(RegexConstant.ISBN_REGEX);
            m = p.matcher(((Book) entity).getISBN());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.ISBN_INCORRECT) + UtilStrings.WHITESPACE + ((Book) entity).getISBN();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.title() && ((Book) entity).getTitle() != null) {
            p = Pattern.compile(RegexConstant.TITLE_REGEX);
            m = p.matcher(((Book) entity).getTitle());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.TITLE_INCORRECT) + UtilStrings.WHITESPACE + ((Book) entity).getTitle();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.author() && ((Book) entity).getAuthor() != null) {
            p = Pattern.compile(RegexConstant.AUTHOR_REGEX);
            m = p.matcher(((Book) entity).getAuthor());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.AUTHOR_INCORRECT) + UtilStrings.WHITESPACE + ((Book) entity).getAuthor();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.publisher() && ((Book) entity).getPublisher() != null) {
            p = Pattern.compile(RegexConstant.PUBLISHER_REGEX);
            m = p.matcher(((Book) entity).getPublisher());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.PUBLISHER_INCORRECT) + UtilStrings.WHITESPACE + ((Book) entity).getPublisher();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.login() && ((User) entity).getLogin() != null) {
            p = Pattern.compile(RegexConstant.LOGIN_REGEX);
            m = p.matcher(((User) entity).getLogin());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.LOGIN_INCORRECT) + UtilStrings.WHITESPACE + ((User) entity).getLogin();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.email() && ((User) entity).getEmail() != null) {
            p = Pattern.compile(RegexConstant.EMAIL_REGEX);
            m = p.matcher(((User) entity).getEmail());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMAIL_INCORRECT) + UtilStrings.WHITESPACE + ((User) entity).getEmail();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.name() && ((User) entity).getName() != null) {
            p = Pattern.compile(RegexConstant.NAME_REGEX);
            m = p.matcher(((User) entity).getName());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NAME_INCORRECT) + UtilStrings.WHITESPACE + ((User) entity).getName();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.genre() && ((Genre) entity).getGenre() != null) {
            p = Pattern.compile(RegexConstant.GENRE_REGEX);
            m = p.matcher(((Genre) entity).getGenre());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.GENRE_INCORRECT) + UtilStrings.WHITESPACE + ((Genre) entity).getGenre();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.role() && ((Role) entity).getRole() != null) {
            p = Pattern.compile(RegexConstant.ROLE_REGEX);
            m = p.matcher(((Role) entity).getRole());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.ROLE_INCORRECT) + UtilStrings.WHITESPACE + ((Role) entity).getRole();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.status() && ((Status) entity).getStatus() != null) {
            p = Pattern.compile(RegexConstant.STATUS_REGEX);
            m = p.matcher(((Status) entity).getStatus());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.STATUS_INCORRECT) + UtilStrings.WHITESPACE + ((Status) entity).getStatus();
                throw new ValidatorException(errorMessage);
            }
        }
    }



    @Override
    public void validateCriteria(Field field, Naming annotation, Criteria<? extends Entity> criteria) throws ValidatorException {
        Pattern p;
        Matcher m;

        String errorMessage = "";

        if (annotation.ISBN() && ((BookCriteria) criteria).getISBN() != null) {
            p = Pattern.compile(RegexConstant.ISBN_REGEX);
            m = p.matcher(((BookCriteria) criteria).getISBN());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.ISBN_INCORRECT) + UtilStrings.WHITESPACE + ((BookCriteria) criteria).getISBN();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.title() && ((BookCriteria) criteria).getTitle() != null) {
            p = Pattern.compile(RegexConstant.TITLE_REGEX);
            m = p.matcher(((BookCriteria) criteria).getTitle());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.TITLE_INCORRECT) + UtilStrings.WHITESPACE + ((BookCriteria) criteria).getTitle();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.author() && ((BookCriteria) criteria).getAuthor() != null) {
            p = Pattern.compile(RegexConstant.AUTHOR_REGEX);
            m = p.matcher(((BookCriteria) criteria).getAuthor());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.AUTHOR_INCORRECT) + UtilStrings.WHITESPACE + ((BookCriteria) criteria).getAuthor();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.publisher() && ((BookCriteria) criteria).getPublisher() != null) {
            p = Pattern.compile(RegexConstant.PUBLISHER_REGEX);
            m = p.matcher(((BookCriteria) criteria).getPublisher());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.PUBLISHER_INCORRECT) + UtilStrings.WHITESPACE + ((BookCriteria) criteria).getPublisher();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.login()) {
            p = Pattern.compile(RegexConstant.LOGIN_REGEX);
            m = p.matcher(((UserCriteria) criteria).getLogin());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.LOGIN_INCORRECT) + UtilStrings.WHITESPACE + ((UserCriteria) criteria).getLogin();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.email() && ((UserCriteria) criteria).getEmail() != null) {
            p = Pattern.compile(RegexConstant.EMAIL_REGEX);
            m = p.matcher(((UserCriteria) criteria).getEmail());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMAIL_INCORRECT) + UtilStrings.WHITESPACE + ((UserCriteria) criteria).getEmail();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.name() && ((UserCriteria) criteria).getName() != null) {
            p = Pattern.compile(RegexConstant.NAME_REGEX);
            m = p.matcher(((UserCriteria) criteria).getName());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NAME_INCORRECT) + UtilStrings.WHITESPACE + ((UserCriteria) criteria).getName();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.genre() && ((GenreCriteria) criteria).getGenre() != null) {
            p = Pattern.compile(RegexConstant.GENRE_REGEX);
            m = p.matcher(((GenreCriteria) criteria).getGenre());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.GENRE_INCORRECT) + UtilStrings.WHITESPACE + ((GenreCriteria) criteria).getGenre();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.role() && ((RoleCriteria) criteria).getRole() != null) {
            p = Pattern.compile(RegexConstant.ROLE_REGEX);
            m = p.matcher(((RoleCriteria) criteria).getRole());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.ROLE_INCORRECT) + UtilStrings.WHITESPACE + ((RoleCriteria) criteria).getRole();
                throw new ValidatorException(errorMessage);
            }
        }
        if (annotation.status() && ((StatusCriteria) criteria).getStatus() != null) {
            p = Pattern.compile(RegexConstant.STATUS_REGEX);
            m = p.matcher(((StatusCriteria) criteria).getStatus());

            if (!m.matches()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.STATUS_INCORRECT) + UtilStrings.WHITESPACE + ((StatusCriteria) criteria).getStatus();
                throw new ValidatorException(errorMessage);
            }
        }
    }
}