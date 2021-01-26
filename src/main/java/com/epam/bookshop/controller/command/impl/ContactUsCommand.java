package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

public class ContactUsCommand implements Command {

    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/contact_us.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return HOME_PAGE;
    }
}
