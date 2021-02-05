package com.epam.bookshop.validator.impl;

import com.epam.bookshop.constant.RegexConstants;
import com.epam.bookshop.constant.UtilStringConstants;

import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates strings for regular expression accordance
 */
public class RegexValidator {

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static RegexValidator instance;

    public static RegexValidator getInstance() {
        LOCK.lock();
        try {
            if (instance == null) {
                instance = new RegexValidator();
            }
        } finally {
            LOCK.unlock();
        }

        return instance;
    }

    /**
     * Validates string by <b>regex</b> input
     *
     * @param stringToValidate string to validate
     * @param regex regular expression for validation
     */
    public boolean validate(String stringToValidate, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(stringToValidate);

        return m.matches();
    }
}
