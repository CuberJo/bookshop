package com.epam.bookshop.validator.impl;

import com.epam.bookshop.constant.RegexConstants;
import com.epam.bookshop.constant.UtilStringConstants;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Validates strings for emptiness
 */
public class EmptyStringValidator {
    private static final ReentrantLock LOCK = new ReentrantLock();
    private static EmptyStringValidator instance;

    public static EmptyStringValidator getInstance() {
        LOCK.lock();
        try {
            if (instance == null) {
                instance = new EmptyStringValidator();
            }
        } finally {
            LOCK.unlock();
        }

        return instance;
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
