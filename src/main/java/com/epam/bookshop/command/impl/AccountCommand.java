package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;

public class AccountCommand implements Command {

    private static final ResponseContext ACCOUNT_PAGE = () -> "/WEB-INF/jsp/account.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return ACCOUNT_PAGE;
    }
}
