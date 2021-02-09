package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.PageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.PaymentService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.EntityFinderFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Objects;

/**
 * Returns personal page with filled data about user
 */
public class PersonalPageCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(PersonalPageCommand.class);

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        if (Objects.isNull(session.getAttribute(RequestConstants.LIBRARY))) {
            fillLibrary(session, (String) session.getAttribute(RequestConstants.LOCALE));
        }
        session.setAttribute(RequestConstants.EMAIL,
                EntityFinderFacade.getInstance().findUserInSession(session, logger).getEmail());

        return new CommandResult(CommandResult.ResponseType.FORWARD, PageConstants.PERSONAL_PAGE.getPage());
    }


    /**
     * Fills {@code library} object of type of {@code List<Book>}
     * that is stored in session with books, that user has already
     * bought
     *
     * @param session current {@link HttpSession} object
     * @param locale language of error messages whether ones taka place
     */
    private void fillLibrary(HttpSession session, String locale) {
        User user = EntityFinderFacade.getInstance().findUserInSession(session, logger);

        PaymentService paymentService = (PaymentService) ServiceFactory.getInstance().create(EntityType.PAYMENT);
        paymentService.setLocale(locale);
        Collection<Book> library = paymentService.findAllBooksInPayment(user.getEntityId());

        BookService bookService = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        bookService.setLocale(locale);
        bookService.findImagesForBooks(library);

        session.setAttribute(RequestConstants.LIBRARY, library);
    }
}
