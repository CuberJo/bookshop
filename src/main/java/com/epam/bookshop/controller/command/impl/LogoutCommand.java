package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.util.UtilStrings;

public class LogoutCommand implements Command {

    private static final ResponseContext ACCOUNT_PAGE = () -> "/WEB-INF/jsp/account.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        requestContext.getSession().removeAttribute(UtilStrings.LOGIN);
        requestContext.getSession().removeAttribute(UtilStrings.ROLE);

        return ACCOUNT_PAGE;
    }
}
