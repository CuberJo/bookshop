package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.FrontCommand;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

public class AccountFrontCommand implements FrontCommand {

    private static final ResponseContext ACCOUNT_PAGE = () -> "/WEB-INF/jsp/account.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return ACCOUNT_PAGE;
    }
}
