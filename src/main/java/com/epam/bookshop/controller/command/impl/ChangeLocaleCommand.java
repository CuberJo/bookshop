package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

import java.util.Objects;

public class ChangeLocaleCommand implements Command {

    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/home.jsp";

    private static final String LOCALE = "locale";
    private static final String RU = "RU";
    private static final String EN = "US";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        String localeParam = requestContext.getParameter(LOCALE);
        if (Objects.isNull(localeParam) || localeParam.isEmpty()) {
            return HOME_PAGE;
        }
        switch (localeParam) {
            case RU:
                requestContext.getSession().setAttribute(LOCALE, RU);
                break;
            case EN:
                requestContext.getSession().setAttribute(LOCALE, EN);
                break;
            default:
                requestContext.getSession().setAttribute(LOCALE, EN);
        }

        return HOME_PAGE;
    }
}
