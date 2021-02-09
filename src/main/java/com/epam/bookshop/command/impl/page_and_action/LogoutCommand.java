package com.epam.bookshop.command.impl.page_and_action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.RouteConstants;

import javax.servlet.http.HttpSession;

/**
 * Logouts from application
 */
public class LogoutCommand implements Command {

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        session.removeAttribute(RequestConstants.LOGIN);
        session.removeAttribute(RequestConstants.ROLE);
        session.removeAttribute(RequestConstants.LIBRARY);
        session.removeAttribute(RequestConstants.IBANs);
        session.removeAttribute(RequestConstants.CART);

        session.removeAttribute(RequestConstants.BACK_TO_CHOOSE_IBAN);
        session.removeAttribute(RequestConstants.FROM_CART_PAGE);
        session.removeAttribute(RequestConstants.BACK_TO_CART);

        return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.ACCOUNT.getRoute());
    }
}
