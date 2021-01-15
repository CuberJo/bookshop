package com.epam.bookshop.util;

public class Regex {

    public static final String ISBN_REGEX = "^[\\d]+-[\\d]+-[\\d]+-[\\d]+-[\\d]+$";
    public static final String TITLE_REGEX = "^[-\\(\\)\\!a-zA-Z\\d\\s\\.]{1,50}$";
    public static final String AUTHOR_REGEX = "^[-a-zA-Z\\s\\.]{1,50}$";
    public static final String PUBLISHER_REGEX = "^[-&a-zA-Z\\s]{1,50}$";
    public static final String GENRE_REGEX = "^[-a-zA-Z&_\\s]{1,50}$";
    public static final String ROLE_REGEX = "^[a-zA-Z]{1,20}$";
    public static final String STATUS_REGEX = "^[a-zA-Z_]{1,20}$";
    public static final String NAME_REGEX = "^[\\.a-zA-Z]{1,20}$";
//    public static final String NAME_REGEX = "^[\\.a-zA-Zа-яёА-ЯЁ]{1,20}$";
    public static final String LOGIN_REGEX = "^[\\d_A-Za-z\\.-]{1,20}$";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]{1,40}@[a-zA-Z0-9-]{2,5}.[a-zA-Z0-9-.]{2,5}$";
//    public static final String EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{1,30}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{1,10}[a-zA-Z0-9])?){1,10}$";//*
    public static final String EMPTY_STRING_REGEX = "^[\\s]+$";
    public static final String IBAN_REGEX = "^[\\d]{16}$";

}
