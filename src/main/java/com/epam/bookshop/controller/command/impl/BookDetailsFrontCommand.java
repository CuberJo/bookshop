package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.FrontCommand;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.constant.RequestConstants;
import com.epam.bookshop.util.constant.UtilStringConstants;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class BookDetailsFrontCommand implements FrontCommand {
    private static final Logger logger = LoggerFactory.getLogger(BookDetailsFrontCommand.class);

    private static final ResponseContext NOT_FOUND_PAGE = () -> "/404error.jsp";
    private static final ResponseContext BOOK_DETAILS_PAGE = () -> "/WEB-INF/jsp/book_details.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final String ISBN = requestContext.getParameter(RequestConstants.ISBN);

        final HttpSession session = requestContext.getSession();
        String locale = (String) requestContext.getSession().getAttribute(RequestConstants.LOCALE);
        String errorMessage = "";

        try {

            BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
            service.setLocale(locale);
            BookCriteria criteria = BookCriteria.builder()
                    .ISBN(ISBN)
                    .build();
            Optional<Book> optionalBook = service.find(criteria);

            if (optionalBook.isEmpty()) {
                return NOT_FOUND_PAGE;
            }

            session.setAttribute(RequestConstants.BOOK, optionalBook.get());

            service.findImageForBook(optionalBook.get());

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.ISBN_INCORRECT)
                    + UtilStringConstants.WHITESPACE + ISBN;
            session.setAttribute(ErrorMessageConstants.ERROR_LOG_MESSAGE, errorMessage);
            logger.error(errorMessage, e);
            return NOT_FOUND_PAGE;
        }

        return BOOK_DETAILS_PAGE;
    }
}
