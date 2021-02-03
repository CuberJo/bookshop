package com.epam.bookshop.util.locale_manager;

import java.util.Locale;
import java.util.ResourceBundle;

public enum MessageManager {
    US(ResourceBundle.getBundle(("message"), new Locale("en", "US"))),
    RU(ResourceBundle.getBundle(("message"), new Locale("ru", "RU")));

    private ResourceBundle bundle;

    MessageManager(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String getMessage(String key) {
        return bundle.getString(key);
    }
}
