package com.epam.bookshop.util.manager;

import java.util.Locale;
import java.util.ResourceBundle;

public enum ErrorMessageManager {
//    EN(ResourceBundle.getBundle(("resources.error_message"), new Locale("en", "US"))),
//    RU(ResourceBundle.getBundle(("resources.error_message"), new Locale("ru", "RU")));

    EN(ResourceBundle.getBundle(("error_message"), new Locale("en", "US"))),
    RU(ResourceBundle.getBundle(("error_message"), new Locale("ru", "RU")));

    private ResourceBundle bundle;

    ErrorMessageManager(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String getMessage(String key) {
        return bundle.getString(key);
    }

    //    INSTANCE;
//
//    private ResourceBundle bundle;
//    private final String resourceName = "resources.error_message";
//
//    ErrorMessageManager() {
//        bundle = ResourceBundle.getBundle(resourceName, new Locale("en", "US"));
//    }
//
//    public void changeResource(Locale locale) {
//        bundle = ResourceBundle.getBundle(resourceName, locale);
//    }
//
//    public String getMessage(String key) {
//        return bundle.getString(key);
//    }

}