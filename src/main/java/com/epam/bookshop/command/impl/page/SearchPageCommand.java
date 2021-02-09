package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.PageConstants;

/**
 * Returns 'search.jsp' page for advanced search
 */
public class SearchPageCommand implements Command {

    @Override
    public CommandResult execute(RequestContext requestContext) {
        return new CommandResult(CommandResult.ResponseType.FORWARD, PageConstants.SEARCH.getPage());
    }
}
