package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.controller.ajax.BooksController;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.util.JsonConverter;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Objects;

public class NotAdvancedBookSearchCommand implements Command {

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        int start = BooksController.getStartPoint(requestContext, BooksController.ITEMS_PER_PAGE);

        if (Objects.nonNull(session.getAttribute(RequestConstants.NOT_ADVANCED_BOOK_SEARCH))) {
            String searchStr = (String) session.getAttribute(RequestConstants.SEARCH_STR);

            Criteria<Book> criteria = BookCriteria.builder()
                    .title(searchStr)
                    .author(searchStr)
                    .publisher(searchStr)
                    .build();

            Collection<Book> books = findBooksLike(criteria, start, BooksController.ITEMS_PER_PAGE, locale);

            session.removeAttribute(RequestConstants.NOT_ADVANCED_BOOK_SEARCH);
            session.removeAttribute(RequestConstants.SEARCH_STR);

            String jsonStrings = JsonConverter.getInstance().write(books);
            BooksController.writeResp(resp, UtilStringConstants.APPLICATION_JSON, jsonStrings);

        }
    }
}
