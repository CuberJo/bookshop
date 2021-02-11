package com.epam.bookshop.validator.impl;

import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.exception.DqlException;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;

import java.util.concurrent.locks.ReentrantLock;

/**
 * This utility class is used to process DQL exceptions in more
 * understandable messages
 */
public class DqlExceptionMessageProcessor {

    private String locale = "US";

    private static final String DUPLICATE = "duplicate";
    private static final String TRUNCATION = "truncation";

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String process(DqlException e) {
        StringBuilder errorMes = new StringBuilder();

        if (e.getMessage().toLowerCase().contains(DUPLICATE)) {
            errorMes.append(ErrorMessageManager.valueOf(locale)
                    .getMessage(ErrorMessageConstants.SQL_DUPLICATE));
        }
        if (e.getMessage().toLowerCase().contains(TRUNCATION)) {
            errorMes.append(ErrorMessageManager.valueOf(locale)
                    .getMessage(ErrorMessageConstants.SQL_TRUNCATION));
        }

        return errorMes.toString();
    }
}
