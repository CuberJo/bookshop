package com.epam.bookshop.util;

import java.security.SecureRandom;

public class PasswordCreator {

    private static PasswordCreator instance;

    private PasswordCreator() {

    }

    public static PasswordCreator getInstance() {
        if (instance == null) {
            return new PasswordCreator();
        }

        return instance;
    }

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
