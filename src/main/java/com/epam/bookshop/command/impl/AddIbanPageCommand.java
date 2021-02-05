package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;

public class AddIbanPageCommand implements Command {

    private static final ResponseContext IBAN_PAGE = () -> "/WEB-INF/jsp/add_iban.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return IBAN_PAGE;
    }
}
