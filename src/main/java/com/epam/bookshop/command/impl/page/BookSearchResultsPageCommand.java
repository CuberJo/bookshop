package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.PageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.RouteConstants;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Returns 'book_search_results.jsp' page, where user can look up at search results
 */
public class BookSearchResultsPageCommand implements Command {

    private static final String SEARCH_TYPE = "searchType";

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        String searchStr = requestContext.getParameter(RequestConstants.SEARCH_STR);
        if (Objects.nonNull(requestContext.getParameter(RequestConstants.NOT_ADVANCED_BOOK_SEARCH))) {
            session.setAttribute(SEARCH_TYPE, RequestConstants.NOT_ADVANCED_BOOK_SEARCH);
            session.setAttribute(RequestConstants.SEARCH_STR, searchStr);

            return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.BOOK_SEARCH_RESULTS.getRoute());
        }

        if (Objects.nonNull(requestContext.getParameter(RequestConstants.ADVANCED_BOOK_SEARCH))) {
            session.setAttribute(RequestConstants.SEARCH_CRITERIA, requestContext.getParameter(RequestConstants.SEARCH_CRITERIA));
            session.setAttribute(SEARCH_TYPE, RequestConstants.ADVANCED_BOOK_SEARCH);
            session.setAttribute(RequestConstants.SEARCH_STR, searchStr);

            return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.BOOK_SEARCH_RESULTS.getRoute());
        }

        return new CommandResult(CommandResult.ResponseType.FORWARD, PageConstants.BOOK_SEARCH_RESULTS.getPage());
    }
}
