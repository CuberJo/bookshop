package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

public class FinishedPurchaseCommand implements Command {

    private static final ResponseContext FINISHED_PURCHASE_PAGE = () -> "/WEB-INF/jsp/finished_purchase.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return FINISHED_PURCHASE_PAGE;
    }
}
