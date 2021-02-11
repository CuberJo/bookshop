package com.epam.bookshop.constant;

import com.epam.bookshop.validator.impl.RegexValidator;

/**
 * Regular expression strings
 */
public class RegexConstants {

    private RegexConstants() {}

    public static final String ISBN_REGEX = "^[\\d]+-[\\d]+-[\\d]+-[\\d]+-[\\d]+$";
    public static final String TITLE_REGEX = "^[-(),!\\d\\s.\\p{L}]{1,100}$";
    public static final String AUTHOR_REGEX = "^[-\\s.\\p{L}\\d]{1,50}$";
    public static final String PUBLISHER_REGEX = "^[-&\\p{L}\\s\\d]{1,50}$";
//    public static final String GENRE_REGEX = "^[-a-zA-Z&_\\s]{1,50}$";
    public static final String GENRE_REGEX = "^[-\\p{L}&_\\s]{1,50}$";
    public static final String NAME_REGEX = "^[-.\\p{L}]{1,20}$";
    public static final String LOGIN_REGEX = "^[-.\\w]{1,20}$";
    public static final String PRICE_REGEX = "^[0-9]+(\\.[0-9]+)?$";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]{1,40}@[a-zA-Z0-9-]{2,5}.[a-zA-Z0-9-.]{2,5}$";
    public static final String STRONG_EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{1,30}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{1,10}[a-zA-Z0-9])?){1,10}$";//*
    public static final String EMPTY_STRING_REGEX = "^[\\s]+$";
    public static final String IBAN_REGEX = "^[\\d]{4}\\s[\\d]{4}\\s[\\d]{4}\\s[\\d]{4}$";
    public static final String MALICIOUS_REGEX = "[<>*;='#)+&(\"]+";
    public static final String SPACES_BEFORE_STR = "^\\s++";
    public static final String SPACES_AFTER_STR = "\\s++$";
    public static final String CYRILLIC = "[а-яА-ЯёЁ]+";
}
