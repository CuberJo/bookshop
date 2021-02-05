package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.controller.ajax.BooksController;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.util.JsonConverter;
import com.epam.bookshop.util.criteria.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Collection;

public class AdvancedBookSearchCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(AdvancedBookSearchCommand.class);

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();
        final String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        Criteria<Book> criteria = BooksController.buildCriteria(session, locale);

        int start = BooksController.getStartPoint(requestContext, BooksController.ITEMS_PER_PAGE);
        Collection<Book> books = EntityFinder.getInstance().findBooksLike(criteria, start, BooksController.ITEMS_PER_PAGE, locale, logger);

        session.removeAttribute(RequestConstants.SEARCH_CRITERIA);
        session.removeAttribute(RequestConstants.SEARCH_STR);

        return () -> JsonConverter.getInstance().write(books);
    }
}
