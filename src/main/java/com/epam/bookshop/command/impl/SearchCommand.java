package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;

/**
 * Returns 'search.jsp' page for advanced search
 */
public class SearchCommand implements Command {

    private static final ResponseContext SEARCH_PAGE = () -> "/WEB-INF/jsp/search.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return SEARCH_PAGE;
    }
}
