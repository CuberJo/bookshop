package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.util.JsonConverter;

import javax.servlet.http.HttpSession;
import java.util.Collection;

public class BestsellersCommand implements Command {

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        int start = 0, total = 12;
        Collection<Book> books = EntityFinder.getInstance().getBooks(start, total, locale);

        return () -> JsonConverter.getInstance().write(books);
    }
}
