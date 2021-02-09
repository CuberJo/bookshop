package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.RouteConstants;
import com.epam.bookshop.constant.UtilStringConstants;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Changes application locale
 */
public class ChangeLocaleCommand implements Command {

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        String locale = requestContext.getParameter(RequestConstants.LOCALE);
        if (Objects.isNull(locale) || locale.isEmpty()) {
            return resolvePage(requestContext);
        }

        switch (locale) {
            case UtilStringConstants.RU:
                session.setAttribute(RequestConstants.LOCALE, UtilStringConstants.RU);
                break;
            case UtilStringConstants.US:
            default:
                session.setAttribute(RequestConstants.LOCALE, UtilStringConstants.US);
        }

        return resolvePage(requestContext);
    }


    /**
     * Resolves page where to get back
     *
     * @param requestContext {@link RequestContext} object, which is request wrapper
     * @return resolved by "from" parameter {@link CommandResult} object
     */
    private CommandResult resolvePage(RequestContext requestContext) {

        String fromPage = requestContext.getParameter(UtilStringConstants.FROM);
        String isbn = requestContext.getParameter(RequestConstants.ISBN);

        String page = "/home?command=%s";
        if (Objects.nonNull(fromPage) && !fromPage.isEmpty()) {
            page = String.format(page, fromPage);
            if (Objects.nonNull(isbn) && !isbn.isEmpty()) {
                page += "&isbn=" + isbn;
            }
        } else {
            page = RouteConstants.HOME.getRoute();
        }

        return new CommandResult(CommandResult.ResponseType.REDIRECT, page);
    }
}
