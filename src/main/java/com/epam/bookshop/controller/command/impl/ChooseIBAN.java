package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.FrontCommand;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

/**
 * Returns 'choose_iban.jsp' page
 */
public class ChooseIBAN implements FrontCommand {

    private static final ResponseContext CHOOSE_IBAN_PAGE = () -> "/WEB-INF/jsp/choose_iban.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return CHOOSE_IBAN_PAGE;
    }
}
