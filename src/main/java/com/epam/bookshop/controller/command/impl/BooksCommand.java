package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.util.constant.UtilStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class BooksCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(BooksCommand.class);

    private static final ResponseContext BOOKS_PAGE_FORWARD = () -> "/WEB-INF/jsp/books.jsp";
    private static final ResponseContext BOOKS_PAGE_REDIRECT = () -> "/home?command=books";

    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final HttpSession session = requestContext.getSession();

        if (Objects.nonNull(requestContext.getParameter(UtilStrings.NOT_ADVANCED_SEARCH))) {
            session.setAttribute(UtilStrings.NOT_ADVANCED_SEARCH, requestContext.getParameter(UtilStrings.NOT_ADVANCED_SEARCH));
            session.setAttribute(UtilStrings.SEARCH_STR, requestContext.getParameter(UtilStrings.SEARCH_STR));
            session.setAttribute(UtilStrings.REQUEST_FROM_SEARCH_INPUT, true);

            return BOOKS_PAGE_REDIRECT;
        }

        if (Objects.nonNull(requestContext.getParameter(UtilStrings.SEARCH_CRITERIA))) {
            session.setAttribute(UtilStrings.SEARCH_CRITERIA, requestContext.getParameter(UtilStrings.SEARCH_CRITERIA));
            session.setAttribute(UtilStrings.CUSTOMIZED_SEARCH, "true");
            session.setAttribute(UtilStrings.SEARCH_STR, requestContext.getParameter(UtilStrings.SEARCH_STR));
            session.setAttribute(UtilStrings.REQUEST_FROM_SEARCH_PAGE, true);

            return BOOKS_PAGE_REDIRECT;
        }

        String genreName = decode(requestContext.getParameter(UtilStrings.GENRE));
        if (Objects.nonNull(genreName) && genreName.isEmpty()) {
            requestContext.setAttribute(UtilStrings.GENRE, null);
        } else {
            requestContext.setAttribute(UtilStrings.GENRE, genreName);
        }

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
