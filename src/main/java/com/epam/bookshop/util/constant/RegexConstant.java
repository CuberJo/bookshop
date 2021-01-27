package com.epam.bookshop.util.constant;

/**
 * Regular expression strings
 */
public class RegexConstant {

    private RegexConstant() {}

    public static final String ISBN_REGEX = "^[\\d]+-[\\d]+-[\\d]+-[\\d]+-[\\d]+$";
    public static final String TITLE_REGEX = "^[-()!\\d\\s.\\p{L}]{1,50}$";
    public static final String AUTHOR_REGEX = "^[-\\s.\\p{L}]{1,50}$";
    public static final String PUBLISHER_REGEX = "^[-&\\p{L}\\s]{1,50}$";
    public static final String GENRE_REGEX = "^[-a-zA-Z&_\\s]{1,50}$";
    public static final String NAME_REGEX = "^[-.\\p{L}]{1,20}$";
    public static final String LOGIN_REGEX = "^[-.\\w]{1,20}$";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]{1,40}@[a-zA-Z0-9-]{2,5}.[a-zA-Z0-9-.]{2,5}$";
//    public static final String EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{1,30}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{1,10}[a-zA-Z0-9])?){1,10}$";//*
    public static final String EMPTY_STRING_REGEX = "^[\\s]+$";
    public static final String IBAN_REGEX = "^[\\d]{4}\\s[\\d]{4}\\s[\\d]{4}\\s[\\d]{4}$";
    public static final String MALICIOUS_REGEX = "[<>*;='#)+&(\"]+";
}
