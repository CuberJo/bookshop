package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.FrontCommand;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

/**
 * Returns 'search.jsp' page for advanced search
 */
public class SearchFrontCommand implements FrontCommand {

    private static final ResponseContext SEARCH_PAGE = () -> "/WEB-INF/jsp/search.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return SEARCH_PAGE;
    }
}
