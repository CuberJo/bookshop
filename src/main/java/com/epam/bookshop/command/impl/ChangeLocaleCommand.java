package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Changes application locale
 */
public class ChangeLocaleCommand implements Command {

    private static final String HOME_PAGE = "/home";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        String locale = requestContext.getParameter(RequestConstants.LOCALE);
        if (Objects.isNull(locale) || locale.isEmpty()) {
            return resolvePage(requestContext);
        }

        switch (locale) {
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
