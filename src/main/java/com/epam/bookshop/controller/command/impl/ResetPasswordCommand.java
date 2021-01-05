package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

import java.security.SecureRandom;

public class ResetPasswordCommand implements Command {

    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/home.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final int len = 30;
        final int randNumOrigin = 48, randNumBound = 122;

        System.out.println(generateRandomPassword(len, randNumOrigin, randNumBound));



        return HOME_PAGE;
    }

    private static String generateRandomPassword(int len, int randNumOrigin, int randNumBound) {
        SecureRandom random = new SecureRandom();
        return random.ints(randNumOrigin, randNumBound + 1)
                .filter(i -> Character.isAlphabetic(i) || Character.isDigit(i))
                .limit(len)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }
}
