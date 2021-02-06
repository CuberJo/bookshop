package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.RequestConstants;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Returns 'book_search_results.jsp' page, where user can look up at search results
 */
public class BookSearchResultsPageCommand implements Command {

    private static final ResponseContext BOOK_SEARCH_RESULTS_PAGE_FORWARD = () -> "/WEB-INF/jsp/book_search_results.jsp";
    private static final ResponseContext BOOK_SEARCH_RESULTS_PAGE_REDIRECT = () -> "/home?command=book_search_results";

    private static final String SEARCH_TYPE = "searchType";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        String searchStr = requestContext.getParameter(RequestConstants.SEARCH_STR);
        if (Objects.nonNull(requestContext.getParameter(RequestConstants.NOT_ADVANCED_BOOK_SEARCH))) {
//            session.setAttribute(RequestConstants.NOT_ADVANCED_BOOK_SEARCH, requestContext.getParameter(RequestConstants.NOT_ADVANCED_BOOK_SEARCH));
//            session.setAttribute(RequestConstants.REQUEST_FROM_SEARCH_INPUT, RequestConstants.REQUEST_FROM_SEARCH_INPUT);

            session.setAttribute(SEARCH_TYPE, RequestConstants.NOT_ADVANCED_BOOK_SEARCH);
            session.setAttribute(RequestConstants.SEARCH_STR, searchStr);
//            session.setAttribute("schStr", searchStr);

            return BOOK_SEARCH_RESULTS_PAGE_REDIRECT;
        }

        if (Objects.nonNull(requestContext.getParameter(RequestConstants.ADVANCED_BOOK_SEARCH))) {
            session.setAttribute(RequestConstants.SEARCH_CRITERIA, requestContext.getParameter(RequestConstants.SEARCH_CRITERIA));
//            session.setAttribute(RequestConstants.REQUEST_FROM_SEARCH_PAGE, RequestConstants.REQUEST_FROM_SEARCH_PAGE);

            session.setAttribute(SEARCH_TYPE, RequestConstants.ADVANCED_BOOK_SEARCH);
            session.setAttribute(RequestConstants.SEARCH_STR, searchStr);
//            session.setAttribute("schStr", searchStr);

            return BOOK_SEARCH_RESULTS_PAGE_REDIRECT;
        }

        return BOOK_SEARCH_RESULTS_PAGE_FORWARD;
    }
}
