package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

public class SearchCommand implements Command {

    private static final ResponseContext SEARCH_PAGE = () -> "/WEB-INF/jsp/search.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return SEARCH_PAGE;
    }
}
