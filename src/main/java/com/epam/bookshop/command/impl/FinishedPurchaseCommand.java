package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;

/**
 * Returns 'finished_purchase.jsp' page that shows user
 * completeness of purchase operation message
 */
public class FinishedPurchaseCommand implements Command {

    private static final ResponseContext FINISHED_PURCHASE_PAGE = () -> "/WEB-INF/jsp/finished_purchase.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return FINISHED_PURCHASE_PAGE;
    }
}
