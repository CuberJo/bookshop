package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;

/**
 * Returns enter point to account page(i.e. sign up or sign in)
 */
public class AccountPageCommand implements Command {

    private static final ResponseContext ACCOUNT_PAGE = () -> "/WEB-INF/jsp/account.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return ACCOUNT_PAGE;
    }
}
