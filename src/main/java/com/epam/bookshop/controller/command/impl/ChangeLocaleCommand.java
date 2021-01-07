package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

import java.util.Locale;
import java.util.Objects;

public class ChangeLocaleCommand implements Command {

    private static final String LOCALE = "locale";
    private static final String FROM = "from";
    private static final String RU = "RU";
    private static final String US = "US";

    @Override
    public ResponseContext execute(RequestContext requestContext) {

        String fromPage = requestContext.getParameter(FROM);

        String unresolvedPage = "/home?command=%s";
        String resolvedPage = String.format(unresolvedPage, fromPage);
        ResponseContext respPage = () -> resolvedPage;


        String localeParam = requestContext.getParameter(LOCALE);
        if (Objects.isNull(localeParam) || localeParam.isEmpty()) {
            return respPage;
        }
        switch (localeParam) {
            case RU:
                requestContext.getSession().setAttribute(LOCALE, RU);
//                requestContext.getSession().setAttribute(LOCALE, new Locale("ru", "RU"));
                break;
            case US:
                requestContext.getSession().setAttribute(LOCALE, US);
//                requestContext.getSession().setAttribute(LOCALE, new Locale("en", "US"));
                break;
            default:
                requestContext.getSession().setAttribute(LOCALE, US);
//                requestContext.getSession().setAttribute(LOCALE, new Locale("en", "US"));
        }

        return respPage;
    }
}
