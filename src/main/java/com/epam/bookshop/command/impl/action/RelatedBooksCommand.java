package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.util.EntityFinderFacade;
import com.epam.bookshop.util.convertor.ToJsonConverter;

import java.util.Collection;

/**
 * The logic of this command must pick books, which characteristics
 * are most similar to book user reviewing now. But in basic
 * variant it returns books from 23d in amount of 4
 */
public class RelatedBooksCommand implements Command {

    private static final int START_POINT = 23;
    private static final int TOTAL_ITEMS = 4;

    @Override
    public CommandResult execute(RequestContext requestContext) {
        Collection<Book> books = EntityFinderFacade.getInstance().getBooks(START_POINT, TOTAL_ITEMS,
                (String) requestContext.getSession().getAttribute(RequestConstants.LOCALE));

        return new CommandResult(CommandResult.ResponseType.JSON,
                ToJsonConverter.getInstance().write(books));
    }
}
