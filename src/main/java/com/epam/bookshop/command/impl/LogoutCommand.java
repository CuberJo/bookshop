package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import constant.RequestConstants;

/**
 * Logouts from application
 */
public class LogoutCommand implements Command {

    private static final ResponseContext ACCOUNT_PAGE = () -> "/home?command=account";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        requestContext.getSession().removeAttribute(RequestConstants.LOGIN);
        requestContext.getSession().removeAttribute(RequestConstants.ROLE);

        return ACCOUNT_PAGE;
    }
}
