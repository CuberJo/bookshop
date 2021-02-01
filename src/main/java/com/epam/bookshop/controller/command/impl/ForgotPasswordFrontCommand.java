package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.FrontCommand;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

/**
 * Returns 'forgot_password.jsp' page
 */
public class ForgotPasswordFrontCommand implements FrontCommand {

    private static final ResponseContext FORGOT_PASSWORD_PAGE = () -> "/WEB-INF/jsp/forgot_password.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return FORGOT_PASSWORD_PAGE;
    }
}
