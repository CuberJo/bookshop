package com.epam.bookshop.validator.impl;

import com.epam.bookshop.constant.RegexConstants;
import com.epam.bookshop.constant.UtilStringConstants;

import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates strings for emptiness and regular
 * expression accordance
 */
public class StringValidator {

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static StringValidator instance;

    public static StringValidator getInstance() {
        LOCK.lock();
        try {
            if (instance == null) {
                instance = new StringValidator();
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


    /**
     * Checks strings for not being empty
     *
     * @param strings - Strings to be validated
     * @return <b>true</b> if, and only if, passed strings are empty
     */
    public boolean empty(String ... strings) {
        for (String string : strings) {
            if (string.equals(UtilStringConstants.EMPTY_STRING) || string.matches(RegexConstants.EMPTY_STRING_REGEX)) {
                return true;
            }
        }

        return false;
    }
}
