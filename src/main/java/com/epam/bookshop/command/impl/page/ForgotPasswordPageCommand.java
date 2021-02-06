package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;

/**
 * Returns 'forgot_password.jsp' page that allows user
 * to reset his/her password
 */
public class ForgotPasswordPageCommand implements Command {

    private static final ResponseContext FORGOT_PASSWORD_PAGE = () -> "/WEB-INF/jsp/forgot_password.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return FORGOT_PASSWORD_PAGE;
    }
}
