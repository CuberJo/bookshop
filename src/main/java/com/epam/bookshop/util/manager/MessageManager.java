package com.epam.bookshop.util.manager;

import java.util.Locale;
import java.util.ResourceBundle;

public enum MessageManager {
    US(ResourceBundle.getBundle(("resources.message"), new Locale("en", "US"))),
    RU(ResourceBundle.getBundle(("resources.message"), new Locale("ru", "RU")));

    private ResourceBundle bundle;

    MessageManager(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String getMessage(String key) {
        return bundle.getString(key);
    }
}
