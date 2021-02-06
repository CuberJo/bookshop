package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;

/**
 * Returns 'add_iban.jsp' page
 */
public class AddIbanPageCommand implements Command {

    private static final ResponseContext IBAN_PAGE = () -> "/WEB-INF/jsp/add_iban.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return IBAN_PAGE;
    }
}
