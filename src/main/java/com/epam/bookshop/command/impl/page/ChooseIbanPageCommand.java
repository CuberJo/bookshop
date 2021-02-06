package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;

/**
 * Returns 'choose_iban.jsp' page
 */
public class ChooseIbanPageCommand implements Command {

    private static final ResponseContext CHOOSE_IBAN_PAGE = () -> "/WEB-INF/jsp/choose_iban.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return CHOOSE_IBAN_PAGE;
    }
}
