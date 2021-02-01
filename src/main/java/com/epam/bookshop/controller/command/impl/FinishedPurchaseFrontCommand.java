package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.FrontCommand;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

/**
 * Returns 'finished_purchase.jsp' page that shows
 * completeness of purchase operation message
 */
public class FinishedPurchaseFrontCommand implements FrontCommand {

    private static final ResponseContext FINISHED_PURCHASE_PAGE = () -> "/WEB-INF/jsp/finished_purchase.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return FINISHED_PURCHASE_PAGE;
    }
}
