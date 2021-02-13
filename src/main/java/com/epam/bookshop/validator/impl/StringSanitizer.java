package com.epam.bookshop.validator.impl;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Removes all html or script tags from string
 */
public class StringSanitizer {

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static StringSanitizer instance;

    private StringSanitizer() {}

    public static StringSanitizer getInstance() {
        LOCK.lock();
        try {
            if (instance == null) {
                instance = new StringSanitizer();
            }
        } finally {
            LOCK.unlock();
        }

        return instance;
    }

    /**
     *  Removes all html or script tags from string
     *
     * @param s string to romove tags from
     * @return string without tags
     */
    public String sanitize(String s) {
        return Jsoup.clean(s, Whitelist.none());
    }
}
