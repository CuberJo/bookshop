package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

public class ForgotPasswordCommand implements Command {

    private static final ResponseContext FORGOT_PASSWORD_PAGE = () -> "/WEB-INF/jsp/forgot_password.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return FORGOT_PASSWORD_PAGE;
    }
}
