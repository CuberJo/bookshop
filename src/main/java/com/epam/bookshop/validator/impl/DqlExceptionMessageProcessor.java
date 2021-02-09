package com.epam.bookshop.validator.impl;

import com.epam.bookshop.exception.DqlException;

import java.util.concurrent.locks.ReentrantLock;

/**
 * This utility class is used to process DQL exceptions in more
 * understandable messages
 */
public class DqlExceptionMessageProcessor {

    private static final String DUPLICATE = "duplicate";

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static DqlExceptionMessageProcessor instance;

    public static DqlExceptionMessageProcessor getInstance() {
        LOCK.lock();
        try {
            if (instance == null) {
                instance = new DqlExceptionMessageProcessor();
            }
        } finally {
            LOCK.unlock();
        }

        return instance;
    }


    public String process(DqlException e) {
        StringBuffer errorMes = new StringBuffer();

        if (e.getMessage().toLowerCase().contains(DUPLICATE)) {
            errorMes.append("Cannot accept provided data, because similar one already exists");
        }

        return errorMes.toString();
    }
}
