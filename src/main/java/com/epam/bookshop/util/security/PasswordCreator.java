package com.epam.bookshop.util.security;

import java.security.SecureRandom;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Generates random {@link String} password
 */
public class PasswordCreator {

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static PasswordCreator instance;

    private PasswordCreator() {

    }

    public static PasswordCreator getInstance() {
        LOCK.lock();
        try {
            if (instance == null) {
                instance = new PasswordCreator();
            }
        } finally {
            LOCK.unlock();
        }

        return instance;
    }

    /**
     * Generates new random {@link String} password
     * using special algorithm
     *
     * @param len password length
     * @param randNumOrigin random origin number
     * @param randNumBound random bound number
     * @return new generated password
     */
    public String generateRandomPassword(int len, int randNumOrigin, int randNumBound) {
        SecureRandom random = new SecureRandom();
        return random.ints(randNumOrigin, randNumBound + 1)
                .filter(i -> Character.isAlphabetic(i) || Character.isDigit(i))
                .limit(len)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }
}
