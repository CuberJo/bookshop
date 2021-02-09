package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.PageConstants;

/**
 * Returns 'contact_us.jsp' page
 */
public class ContactUsPageCommand implements Command {

    @Override
    public CommandResult execute(RequestContext requestContext) {
        return new CommandResult(CommandResult.ResponseType.FORWARD, PageConstants.CONTACT_US.getPage());
    }
}
