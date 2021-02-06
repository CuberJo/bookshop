package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;

/**
 * Returns 'home.jsp' page
 */
public class HomePageCommand implements Command {

    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/home.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return HOME_PAGE;
    }
}
