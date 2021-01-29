package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

/**
 * returns admin page
 */
public class AdminCommand implements Command {

    private static final ResponseContext ADMIN_PAGE = () -> "/WEB-INF/jsp/admin.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return ADMIN_PAGE;
    }
}
