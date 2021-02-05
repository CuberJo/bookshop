package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;

import javax.servlet.http.HttpSession;
import java.util.Objects;

public class BookSearchResultsCommand implements Command {

    private static final ResponseContext BOOK_SEARCH_RESULTS_PAGE = () -> "/WEB-INF/jsp/book_search_results.jsp";

    private static final String SEARCH_TYPE = "searchType";


    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        if (Objects.nonNull(requestContext.getParameter(RequestConstants.NOT_ADVANCED_BOOK_SEARCH))) {
            session.setAttribute(RequestConstants.NOT_ADVANCED_BOOK_SEARCH, requestContext.getParameter(RequestConstants.NOT_ADVANCED_BOOK_SEARCH));
            session.setAttribute(RequestConstants.REQUEST_FROM_SEARCH_INPUT, RequestConstants.REQUEST_FROM_SEARCH_INPUT);

            requestContext.setAttribute(SEARCH_TYPE, RequestConstants.NOT_ADVANCED_BOOK_SEARCH);
        }

        if (Objects.nonNull(requestContext.getParameter(RequestConstants.ADVANCED_BOOK_SEARCH))) {
            session.setAttribute(RequestConstants.SEARCH_CRITERIA, requestContext.getParameter(RequestConstants.SEARCH_CRITERIA));
            session.setAttribute(RequestConstants.REQUEST_FROM_SEARCH_PAGE, RequestConstants.REQUEST_FROM_SEARCH_PAGE);

            requestContext.setAttribute(SEARCH_TYPE, RequestConstants.ADVANCED_BOOK_SEARCH);
        }

        session.setAttribute(RequestConstants.SEARCH_STR, requestContext.getParameter(RequestConstants.SEARCH_STR));

        return BOOK_SEARCH_RESULTS_PAGE;
    }
}
