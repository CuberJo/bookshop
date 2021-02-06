package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;

/**
 * Returns 'contact_us.jsp' page
 */
public class ContactUsPageCommand implements Command {

    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/contact_us.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return HOME_PAGE;
    }
}
