package com.epam.bookshop.util.manager.language;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Enum responsible for bundling with {@code message} file
 */
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
