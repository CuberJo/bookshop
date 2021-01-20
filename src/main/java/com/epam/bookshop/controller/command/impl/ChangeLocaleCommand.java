package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.util.UtilStrings;

import javax.servlet.http.HttpSession;
import java.util.Objects;

public class ChangeLocaleCommand implements Command {

    private static final String HOME_PAGE = "/home";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        String localeParam = requestContext.getParameter(UtilStrings.LOCALE);
        if (Objects.isNull(localeParam) || localeParam.isEmpty()) {
            return resolvePage(requestContext);
        }
        switch (localeParam) {
            case UtilStrings.RU:
                session.setAttribute(UtilStrings.LOCALE, UtilStrings.RU);
//                requestContext.getSession().setAttribute(LOCALE, new Locale("ru", "RU"));
                break;
            case UtilStrings.US:
                session.setAttribute(UtilStrings.LOCALE, UtilStrings.US);
//                requestContext.getSession().setAttribute(LOCALE, new Locale("en", "US"));
                break;
            default:
                session.setAttribute(UtilStrings.LOCALE, UtilStrings.US);
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

        String fromPage = requestContext.getParameter(UtilStrings.FROM);

        String page = "/home?command=%s";
        if (Objects.nonNull(fromPage) && !fromPage.isEmpty()) {
            page = String.format(page, fromPage);
        } else {
            page = HOME_PAGE;
        }

        String pageToReturn = page;
        return  () -> pageToReturn;
    }
}
