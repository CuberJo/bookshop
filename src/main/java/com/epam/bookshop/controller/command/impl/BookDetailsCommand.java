package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.criteria.impl.BookCriteria;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class BookDetailsCommand implements Command {

    private static final String ERROR_LOG_MESSAGE = "error_log_message";

    private static final ResponseContext NOT_FOUND_PAGE = () -> "/404error.jsp";
    private static final ResponseContext BOOK_DETAILS_PAGE = () -> "/WEB-INF/jsp/book-details.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final String ISBN = requestContext.getParameter(UtilStrings.ISBN);

        final HttpSession session = requestContext.getSession();
        String locale = (String) requestContext.getSession().getAttribute(UtilStrings.LOCALE);
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

            session.setAttribute(UtilStrings.BOOK, optionalBook.get());

            try {
                service.findImageForBook(optionalBook.get());
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.ISBN_INCORRECT) + UtilStrings.WHITESPACE + ISBN;
            session.setAttribute(ERROR_LOG_MESSAGE, errorMessage);
            e.printStackTrace();
            return NOT_FOUND_PAGE;
        }

        return BOOK_DETAILS_PAGE;
    }
}
