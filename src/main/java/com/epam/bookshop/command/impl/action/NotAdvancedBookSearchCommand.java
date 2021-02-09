package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.util.BookDataProcessor;
import com.epam.bookshop.util.EntityFinderFacade;
import com.epam.bookshop.util.ToJsonConverter;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Collection;

/**
 * Returns book found by usual search
 */
public class NotAdvancedBookSearchCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(NotAdvancedBookSearchCommand.class);

    private static final int TOTAL_ITEMS_PER_PAGE = 8;

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        String searchStr = (String) session.getAttribute(RequestConstants.SEARCH_STR);
        Criteria<Book> criteria = BookCriteria.builder()
                .title(searchStr)
                .author(searchStr)
                .publisher(searchStr)
                .build();

        Collection<Book> books = EntityFinderFacade.getInstance().findBooksLike(criteria,
                BookDataProcessor.getInstance().getStartPoint(requestContext, TOTAL_ITEMS_PER_PAGE), TOTAL_ITEMS_PER_PAGE,
                (String) session.getAttribute(RequestConstants.LOCALE), logger);

        return new CommandResult(CommandResult.ResponseType.JSON,
                ToJsonConverter.getInstance().write(books));
    }
}
