package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.util.UtilStrings;

import java.util.Objects;

public class ChangeLocaleCommand implements Command {

    private static final String HOME_PAGE = "/home";
    private static final String FROM = "from";
    private static final String RU = "RU";
    private static final String US = "US";

    @Override
    public ResponseContext execute(RequestContext requestContext) {

        ResponseContext respPage = resolvePage(requestContext);

        String localeParam = requestContext.getParameter(UtilStrings.LOCALE);
        if (Objects.isNull(localeParam) || localeParam.isEmpty()) {
            return respPage;
        }
        switch (localeParam) {
            case RU:
                requestContext.getSession().setAttribute(UtilStrings.LOCALE, RU);
//                requestContext.getSession().setAttribute(LOCALE, new Locale("ru", "RU"));
                break;
            case US:
                requestContext.getSession().setAttribute(UtilStrings.LOCALE, US);
//                requestContext.getSession().setAttribute(LOCALE, new Locale("en", "US"));
                break;
            default:
                requestContext.getSession().setAttribute(UtilStrings.LOCALE, US);
//                requestContext.getSession().setAttribute(LOCALE, new Locale("en", "US"));
        }

        return resolvePage(requestContext);
    }



    private ResponseContext resolvePage(RequestContext requestContext) {

        String fromPage = requestContext.getParameter(FROM);

        String page = "/home?command=%s";
        if (Objects.nonNull(fromPage) || fromPage.isEmpty()) {
            page = String.format(page, fromPage);
        } else {
            page = HOME_PAGE;
        }

        String pageToReturn = page;
        return  () -> pageToReturn;
    }
}
