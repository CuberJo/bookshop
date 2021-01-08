package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

public class RemoveFromCartCommand implements Command {

    private static final ResponseContext CART_PAGE = () -> "/WEB-INF/jsp/cart.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return CART_PAGE;
    }
}
