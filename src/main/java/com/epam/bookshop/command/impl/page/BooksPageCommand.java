package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.PageConstants;
import com.epam.bookshop.constant.RequestConstants;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Returns 'books.jsp' page
 */
public class BooksPageCommand implements Command {

    @Override
    public CommandResult execute(RequestContext requestContext) {
        requestContext.setAttribute(RequestConstants.GENRE,  decode(requestContext.getParameter(RequestConstants.GENRE)));

        return new CommandResult(CommandResult.ResponseType.FORWARD, PageConstants.BOOKS.getPage());
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
