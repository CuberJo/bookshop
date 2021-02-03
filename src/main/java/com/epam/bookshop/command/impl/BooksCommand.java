package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class BooksCommand implements Command {

    private static final ResponseContext BOOKS_PAGE_FORWARD = () -> "/WEB-INF/jsp/books.jsp";
    private static final ResponseContext BOOKS_PAGE_REDIRECT = () -> "/home?command=books";

    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final HttpSession session = requestContext.getSession();

        if (Objects.nonNull(requestContext.getParameter(RequestConstants.NOT_ADVANCED_SEARCH))) {
            session.setAttribute(RequestConstants.NOT_ADVANCED_SEARCH, requestContext.getParameter(RequestConstants.NOT_ADVANCED_SEARCH));
            session.setAttribute(RequestConstants.SEARCH_STR, requestContext.getParameter(RequestConstants.SEARCH_STR));
            session.setAttribute(RequestConstants.REQUEST_FROM_SEARCH_INPUT, true);

            return BOOKS_PAGE_REDIRECT;
        }

        if (Objects.nonNull(requestContext.getParameter(RequestConstants.SEARCH_CRITERIA))) {
            session.setAttribute(RequestConstants.SEARCH_CRITERIA, requestContext.getParameter(RequestConstants.SEARCH_CRITERIA));
            session.setAttribute(RequestConstants.CUSTOMIZED_SEARCH, UtilStringConstants.TRUE);
            session.setAttribute(RequestConstants.SEARCH_STR, requestContext.getParameter(RequestConstants.SEARCH_STR));
            session.setAttribute(RequestConstants.REQUEST_FROM_SEARCH_PAGE, true);

            return BOOKS_PAGE_REDIRECT;
        }

        String genreName = decode(requestContext.getParameter(RequestConstants.GENRE));
        requestContext.setAttribute(RequestConstants.GENRE, genreName);

        return BOOKS_PAGE_FORWARD;
    }


    /**
     * Decodes encoded  string
     *
     * @param encodedString enoded {@link String}
     * @return encoded {@link String}
     */
    public static String decode(String encodedString) {
        String decodedString = "";

        if(Objects.nonNull(encodedString)) {
            decodedString = URLDecoder.decode(encodedString, StandardCharsets.UTF_8);
        }

        return decodedString;
    }
}
