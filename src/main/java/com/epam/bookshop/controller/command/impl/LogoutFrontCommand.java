package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.FrontCommand;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.util.constant.RequestConstants;

/**
 * to logout from application
 */
public class LogoutFrontCommand implements FrontCommand {

    private static final ResponseContext ACCOUNT_PAGE = () -> "/home?command=account";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        requestContext.getSession().removeAttribute(RequestConstants.LOGIN);
        requestContext.getSession().removeAttribute(RequestConstants.ROLE);

        return ACCOUNT_PAGE;
    }
}
