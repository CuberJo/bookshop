package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.PaymentService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Objects;

/**
 * Returns personal page
 */
public class PersonalPageCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(PersonalPageCommand.class);

    private static final ResponseContext PERSONAL_PAGE = () -> "/WEB-INF/jsp/personal_page.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        if (Objects.isNull(session.getAttribute(RequestConstants.LIBRARY))) {
            try {

                fillLibrary(session, locale);

            } catch (ValidatorException e) {
                String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
                logger.error(error, e);
                throw new RuntimeException(error, e);
            }
        }

        return PERSONAL_PAGE;
    }


    /**
     * Fills <b>library</b> object of type of {@code List<Book>}
     * that is stored in session with books, that user has already
     * bought
     *
     * @param session current {@link HttpSession} object
     * @param locale language of error messages whether ones taka place
     * @throws ValidatorException if criteria {@link Criteria<User>} composed
     */
    private void fillLibrary(HttpSession session, String locale) throws ValidatorException {
        Criteria<User> criteria = UserCriteria.builder()
                .login((String) session.getAttribute(RequestConstants.LOGIN))
                .build();
        User user = EntityFinder.getInstance().find(session, logger, criteria);

        PaymentService service = (PaymentService) ServiceFactory.getInstance().create(EntityType.PAYMENT);
        service.setLocale(locale);
        Collection<Book> library = service.findAllBooksInPayment(user.getEntityId());


        BookService bookService = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        bookService.setLocale(locale);
        bookService.findImagesForBooks(library);

        session.setAttribute(RequestConstants.LIBRARY, library);
    }
}
