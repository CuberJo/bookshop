package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.util.processor.BookDataProcessor;
import com.epam.bookshop.util.EntityFinderFacade;
import com.epam.bookshop.util.convertor.ToJsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Collection;

/**
 * Returns books found bu advanced search
 */
public class AdvancedBookSearchCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(AdvancedBookSearchCommand.class);

    private static final int TOTAL_ITEMS_PER_PAGE = 8;

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();
        final String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        Collection<Book> books = EntityFinderFacade.getInstance().findBooksLike(BookDataProcessor.getInstance().buildCriteria(session, locale),
                BookDataProcessor.getInstance().getStartPoint(requestContext, TOTAL_ITEMS_PER_PAGE), TOTAL_ITEMS_PER_PAGE, locale, logger);

        return new CommandResult(CommandResult.ResponseType.JSON, ToJsonConverter.getInstance().write(books));
    }
}
