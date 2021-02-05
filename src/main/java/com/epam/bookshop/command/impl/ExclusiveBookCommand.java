package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.JsonConverter;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class ExclusiveBookCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(ExclusiveBookCommand.class);

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        Book book = getRandomBook(locale);

        return () -> JsonConverter.getInstance().write(book);
    }


    /**
     * Finds random row in table
     *
     * @return found random book
     */
    private Book getRandomBook(String locale) {
        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        Optional<Book> optionalBook = service.findRand();
        if (optionalBook.isEmpty()) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND);
            logger.error(error);
            throw new RuntimeException(error);
        }
        service.findImageForBook(optionalBook.get());

        return optionalBook.get();
    }
}
