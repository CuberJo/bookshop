package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.FrontCommand;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.util.constant.RequestConstants;
import com.epam.bookshop.util.constant.UtilStringConstants;

import javax.servlet.http.HttpSession;
import java.util.Objects;

public class ChangeLocaleFrontCommand implements FrontCommand {

    private static final String HOME_PAGE = "/home";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        String localeParam = requestContext.getParameter(RequestConstants.LOCALE);
        if (Objects.isNull(localeParam) || localeParam.isEmpty()) {
            return resolvePage(requestContext);
        }
        switch (localeParam) {
            case UtilStringConstants.RU:
                session.setAttribute(RequestConstants.LOCALE, UtilStringConstants.RU);
//                requestContext.getSession().setAttribute(LOCALE, new Locale("ru", "RU"));
                break;
            case UtilStringConstants.US:
                session.setAttribute(RequestConstants.LOCALE, UtilStringConstants.US);
//                requestContext.getSession().setAttribute(LOCALE, new Locale("en", "US"));
                break;
            default:
                session.setAttribute(RequestConstants.LOCALE, UtilStringConstants.US);
//                requestContext.getSession().setAttribute(LOCALE, new Locale("en", "US"));
        }

        return resolvePage(requestContext);
    }


    /**
     * Resolves page where to get back
     * @param requestContext {@link RequestContext} object, which is request wrapper
     * @return resolved by "from" parameter {@link ResponseContext} object
     */
    private ResponseContext resolvePage(RequestContext requestContext) {

        String fromPage = requestContext.getParameter(UtilStringConstants.FROM);
        String isbn = requestContext.getParameter(RequestConstants.ISBN);

        String page = "/home?command=%s";
        if (Objects.nonNull(fromPage) && !fromPage.isEmpty()) {
            page = String.format(page, fromPage);
            if (Objects.nonNull(isbn) && !isbn.isEmpty()) {
                page += "&isbn=" + isbn;
            }
        } else {
            page = HOME_PAGE;
        }

        String pageToReturn = page;
        return  () -> pageToReturn;
    }
}
