package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

public class BookDetailsCommand implements Command {

    private static final ResponseContext BOOK_DETAILS_PAGE = () -> "/WEB-INF/jsp/book-details.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return BOOK_DETAILS_PAGE;
    }
}
