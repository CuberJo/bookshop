package com.epam.bookshop.strategy.validator.impl;

import com.epam.bookshop.context.annotation.Naming;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.*;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.domain.impl.*;
import com.epam.bookshop.strategy.validator.Validatable;
import com.epam.bookshop.exception.ValidatorException;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamingValidator implements Validatable<Naming> {


    private static final String ISBN_REGEX = "^\\d{3}-\\d-\\d{5}-\\d{3}-\\d$";
    private static final String TITLE_REGEX = "^[-\\(\\)\\!a-zA-Z\\d\\s\\.]{1,50}$";
    private static final String AUTHOR_REGEX = "^[-a-zA-Z\\s\\.]{1,50}$";
    private static final String PUBLISHER_REGEX = "^[-&a-zA-Z\\s]{1,50}$";
    private static final String GENRE_REGEX = "^[-a-zA-Z&\\s]{1,50}$";
    private static final String ROLE_REGEX = "^[a-zA-Z]{1,20}$";
    private static final String STATUS_REGEX = "^[a-zA-Z_]{1,20}$";
    private static final String NAME_REGEX = "^[\\.a-zA-Z]{1,20}$";
    private static final String LOGIN_REGEX = "^[\\d_A-Za-z\\.-]{1,20}$";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]{1,40}@[a-zA-Z0-9-]{2,5}.[a-zA-Z0-9-.]{2,5}$";
//    private static final String EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{1,30}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{1,10}[a-zA-Z0-9])?){1,10}$";//*


    @Override
    public void validateEntity(Field field, Naming annotation, Entity entity) throws ValidatorException {

        Pattern p;
        Matcher m;

        if (annotation.ISBN() && ((Book) entity).getISBN() != null) {
            p = Pattern.compile(ISBN_REGEX);
            m = p.matcher(((Book) entity).getISBN());

            if (!m.matches()) {
                throw new ValidatorException("ISBN \"" + ((Book) entity).getISBN() + "\" incorrect");
            }
        }
        if (annotation.title() && ((Book) entity).getTitle() != null) {
            p = Pattern.compile(TITLE_REGEX);
            m = p.matcher(((Book) entity).getTitle());

            if (!m.matches()) {
                throw new ValidatorException("Title \"" + ((Book) entity).getTitle() + "\" incorrect");
            }
        }
        if (annotation.author() && ((Book) entity).getAuthor() != null) {
            p = Pattern.compile(AUTHOR_REGEX);
            m = p.matcher(((Book) entity).getAuthor());

            if (!m.matches()) {
                throw new ValidatorException("Author \"" + ((Book) entity).getAuthor() + "\" incorrect");
            }
        }
        if (annotation.publisher() && ((Book) entity).getPublisher() != null) {
            p = Pattern.compile(PUBLISHER_REGEX);
            m = p.matcher(((Book) entity).getPublisher());

            if (!m.matches()) {
                throw new ValidatorException("Publisher \"" + ((Book) entity).getPublisher() + "\" incorrect");
            }
        }
        if (annotation.login() && ((User) entity).getLogin() != null) {
            p = Pattern.compile(LOGIN_REGEX);
            m = p.matcher(((User) entity).getLogin());

            if (!m.matches()) {
                throw new ValidatorException("Login \"" + ((User) entity).getLogin() + "\" incorrect");
            }
        }
        if (annotation.email() && ((User) entity).getEmail() != null) {
            p = Pattern.compile(EMAIL_REGEX);
            m = p.matcher(((User) entity).getEmail());

            if (!m.matches()) {
                throw new ValidatorException("Email \"" + ((User) entity).getEmail() + "\" incorrect");
            }
        }
        if (annotation.name() && ((User) entity).getName() != null) {
            p = Pattern.compile(NAME_REGEX);
            m = p.matcher(((User) entity).getName());

            if (!m.matches()) {
                throw new ValidatorException("Name \"" + ((User) entity).getName() + "\" incorrect");
            }
        }
        if (annotation.genre() && ((Genre) entity).getGenre() != null) {
            p = Pattern.compile(GENRE_REGEX);
            m = p.matcher(((Genre) entity).getGenre());

            if (!m.matches()) {
                throw new ValidatorException("Genre \"" + ((Genre) entity).getGenre() + "\" incorrect");
            }
        }
        if (annotation.role() && ((Role) entity).getRole() != null) {
            p = Pattern.compile(ROLE_REGEX);
            m = p.matcher(((Role) entity).getRole());

            if (!m.matches()) {
                throw new ValidatorException("Role \"" + ((Role) entity).getRole() + "\" incorrect");
            }
        }
        if (annotation.status() && ((Status) entity).getStatus() != null) {
            p = Pattern.compile(STATUS_REGEX);
            m = p.matcher(((Status) entity).getStatus());

            if (!m.matches()) {
                throw new ValidatorException("Status \"" + ((Status) entity).getStatus() + "\" incorrect");
            }
        }
    }

    @Override
    public void validateCriteria(Field field, Naming annotation, Criteria criteria) throws ValidatorException {

        Pattern p;
        Matcher m;

        if (annotation.ISBN() && ((BookCriteria) criteria).getISBN() != null) {
            p = Pattern.compile(ISBN_REGEX);
            m = p.matcher(((BookCriteria) criteria).getISBN());

            if (!m.matches()) {
                throw new ValidatorException("ISBN \"" + ((BookCriteria) criteria).getISBN() + "\" incorrect");
            }
        }
        if (annotation.title() && ((BookCriteria) criteria).getTitle() != null) {
            p = Pattern.compile(TITLE_REGEX);
            m = p.matcher(((BookCriteria) criteria).getTitle());

            if (!m.matches()) {
                throw new ValidatorException("Title \"" + ((BookCriteria) criteria).getTitle() + "\" incorrect");
            }
        }
        if (annotation.author() && ((BookCriteria) criteria).getAuthor() != null) {
            p = Pattern.compile(AUTHOR_REGEX);
            m = p.matcher(((BookCriteria) criteria).getAuthor());

            if (!m.matches()) {
                throw new ValidatorException("Author \"" + ((BookCriteria) criteria).getAuthor() + "\" incorrect");
            }
        }
        if (annotation.publisher() && ((BookCriteria) criteria).getPublisher() != null) {
            p = Pattern.compile(PUBLISHER_REGEX);
            m = p.matcher(((BookCriteria) criteria).getPublisher());

            if (!m.matches()) {
                throw new ValidatorException("Publisher \"" + ((BookCriteria) criteria).getPublisher() + "\" incorrect");
            }
        }
//        if (annotation.login()) {
//            p = Pattern.compile(LOGIN_REGEX);
//            m = p.matcher(((UserCriteria) criteria).getLogin());
//
//            if (!m.matches()) {
//                throw new InvalidStateException("Login \"" + ((User) entity).getLogin() + "\" incorrect");
//            }
//        }
        if (annotation.email() && ((UserCriteria) criteria).getEmail() != null) {
            p = Pattern.compile(EMAIL_REGEX);
            m = p.matcher(((UserCriteria) criteria).getEmail());

            if (!m.matches()) {
                throw new ValidatorException("Email \"" + ((UserCriteria) criteria).getEmail() + "\" incorrect");
            }
        }
        if (annotation.name() && ((UserCriteria) criteria).getName() != null) {
            p = Pattern.compile(NAME_REGEX);
            m = p.matcher(((UserCriteria) criteria).getName());

            if (!m.matches()) {
                throw new ValidatorException("Name \"" + ((UserCriteria) criteria).getName() + "\" incorrect");
            }
        }
        if (annotation.genre() && ((GenreCriteria) criteria).getGenre() != null) {
            p = Pattern.compile(GENRE_REGEX);
            m = p.matcher(((GenreCriteria) criteria).getGenre());

            if (!m.matches()) {
                throw new ValidatorException("Genre \"" + ((GenreCriteria) criteria).getGenre() + "\" incorrect");
            }
        }
        if (annotation.role() && ((RoleCriteria) criteria).getRole() != null) {
            p = Pattern.compile(ROLE_REGEX);
            m = p.matcher(((RoleCriteria) criteria).getRole());

            if (!m.matches()) {
                throw new ValidatorException("Role \"" + ((RoleCriteria) criteria).getRole() + "\" incorrect");
            }
        }
        if (annotation.status() && ((StatusCriteria) criteria).getStatus() != null) {
            p = Pattern.compile(STATUS_REGEX);
            m = p.matcher(((StatusCriteria) criteria).getStatus());

            if (!m.matches()) {
                throw new ValidatorException("Status \"" + ((StatusCriteria) criteria).getStatus() + "\" incorrect");
            }
        }
    }
}