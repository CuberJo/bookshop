package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.FrontCommand;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

/**
 * Returns 'home.jsp' page
 */
public class HomeFrontCommand implements FrontCommand {

    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/home.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return HOME_PAGE;
    }
}
