package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

public class AccountCommand implements Command {

    private static final ResponseContext ACCOUNT_PAGE = () -> "/WEB-INF/jsp/account.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return ACCOUNT_PAGE;
    }
}
