package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.RequestConstants;

import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Returns 'books.jsp' page
 */
public class BooksPageCommand implements Command {

    private static final ResponseContext BOOKS_PAGE_FORWARD = () -> "/WEB-INF/jsp/books.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        requestContext.setAttribute(RequestConstants.GENRE,  decode(requestContext.getParameter(RequestConstants.GENRE)));

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
