package com.epam.bookshop.util.manager.language;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Enum responsible for bundling with {@code error_message} file
 */
public enum ErrorMessageManager {
    US(ResourceBundle.getBundle(("error_message"), new Locale("en", "US"))),
    RU(ResourceBundle.getBundle(("error_message"), new Locale("ru", "RU")));

    private ResourceBundle bundle;

    ErrorMessageManager(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String getMessage(String key) {
        return bundle.getString(key);
    }
}