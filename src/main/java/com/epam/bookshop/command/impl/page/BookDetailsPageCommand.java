package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.PageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Returns page 'book_details.jsp' page with information about
 * certain book, that user have chosen
 */
public class BookDetailsPageCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(BookDetailsPageCommand.class);

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final String ISBN = requestContext.getParameter(RequestConstants.ISBN);
        final HttpSession session = requestContext.getSession();
        final String locale = (String) requestContext.getSession().getAttribute(RequestConstants.LOCALE);

        try {
            BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
            service.setLocale(locale);
            Optional<Book> optionalBook = service.find(BookCriteria.builder().ISBN(ISBN).build());

            if (optionalBook.isEmpty()) {
                return new CommandResult(CommandResult.ResponseType.FORWARD, PageConstants.NOT_FOUND.getPage());
            }
            session.setAttribute(RequestConstants.BOOK, optionalBook.get());
            service.findImageForBook(optionalBook.get());

        } catch (ValidatorException e) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.ISBN_INCORRECT)
                    + UtilStringConstants.WHITESPACE + ISBN;
            session.setAttribute(ErrorMessageConstants.ERROR_LOG_MESSAGE, errorMessage);
            logger.error(errorMessage, e);
            return new CommandResult(CommandResult.ResponseType.FORWARD, PageConstants.NOT_FOUND.getPage());
        }

        return new CommandResult(CommandResult.ResponseType.FORWARD, PageConstants.BOOK_DETAILS.getPage());
    }
}
