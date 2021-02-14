package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.util.EntityFinderFacade;
import com.epam.bookshop.util.convertor.ToJsonConverter;

import javax.servlet.http.HttpSession;
import java.util.Collection;

/**
 * The logic of this command must be better, that lists all books, that
 * users have bought recently. But in basic variant it returns books records
 * from 8th row in amount of 16
 */
public class LatestBookProductsCommand implements Command {

    private static final int START_POINT = 8;
    private static final int TOTAL_ITEMS_PER_PAGE = 16;

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        Collection<Book> books = EntityFinderFacade.getInstance().getBooks(START_POINT, TOTAL_ITEMS_PER_PAGE,
                (String) session.getAttribute(RequestConstants.LOCALE));

        return new CommandResult(CommandResult.ResponseType.JSON,
                ToJsonConverter.getInstance().write(books));
    }
}
