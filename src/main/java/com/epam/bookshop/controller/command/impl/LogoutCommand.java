package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

public class LogoutCommand implements Command {

    private static final String LOGIN = "login";
    private static final String ROLE = "role";

    private static final ResponseContext ACCOUNT_PAGE = () -> "/WEB-INF/jsp/account.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        requestContext.getSession().removeAttribute(LOGIN);
        requestContext.getSession().removeAttribute(ROLE);
        return ACCOUNT_PAGE;
    }
}
