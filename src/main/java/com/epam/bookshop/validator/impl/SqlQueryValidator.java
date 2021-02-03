package com.epam.bookshop.validator.impl;

import com.epam.bookshop.constant.UtilStringConstants;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Validates sql query by removing extra characters sequences
 */
public class SqlQueryValidator {

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static SqlQueryValidator instance;

    private SqlQueryValidator() {}

    public static SqlQueryValidator getInstance() {
        LOCK.lock();
        try {
            if (instance == null) {
                instance = new SqlQueryValidator();
            }
        } finally {
            LOCK.unlock();
        }

        return instance;
    }

    /**
     * Removes extra "AND"and whitespaces.
     * If 'like' string present, adds '%' character.
     *
     * @param condition "WHERE" condition which is to be added to sql query
     * @return string without ANDs and whitespaces
     */
    public String validatedQuery(StringBuffer condition) {

        checkForLIKE(condition);
        checkForSeq(condition, UtilStringConstants.AND);
        checkForSeq(condition, UtilStringConstants.WHITESPACE);

        return condition + UtilStringConstants.SEMICOLON;
    }


    /**
     * Wraps string {@code s} with '%' character whether a string {@code s} argument contains 'like'
     * sequence of characters
     *
     * @param s string to be validated
     */
    private void checkForLIKE(StringBuffer s) {
        final String like = "like";
        boolean containsLike = s.toString().toLowerCase().contains(like);
        if (containsLike) {
            s.replace(0, s.length(), s.toString().replaceAll(" '", " '%").replaceAll("' ", "%' "));
        }
    }


    /**
     * Whether a string {@code s} argument has a special sequence of characters
     * {@code s} at the end of a string and removes them if finds
     *
     * @param s string to be validated
     * is present
     */
    private void checkForSeq(StringBuffer s, String seq) {
        int whitespaceIndex = s.lastIndexOf(seq);
        if (whitespaceIndex != -1) {
            s.delete(whitespaceIndex, s.length());
        }
    }
}
