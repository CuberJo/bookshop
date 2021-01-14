package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

public class ChooseIBAN implements Command {

    private static final ResponseContext CHOOSE_IBAN_PAGE = () -> "/WEB-INF/jsp/choose_iban.jsp";


    @Override
    public ResponseContext execute(RequestContext requestContext) {

        return CHOOSE_IBAN_PAGE;
    }
}
