package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
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
 * to get to peronal page
 */
public class PersonalPageCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(PersonalPageCommand.class);

    private static final ResponseContext PERSONAL_PAGE = () -> "/WEB-INF/jsp/personal_page.jsp";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();
        String locale = (String) session.getAttribute(UtilStrings.LOCALE);

        if (Objects.isNull(session.getAttribute(UtilStrings.LIBRARY))) {
            try {
                Criteria<User> criteria = UserCriteria.builder()
                        .login((String) session.getAttribute(UtilStrings.LOGIN))
                        .build();
                User user = EntityFinder.getInstance().find(session, logger, criteria);

                PaymentService service = (PaymentService) ServiceFactory.getInstance().create(EntityType.PAYMENT);
                service.setLocale(locale);
                Collection<Book> library = service.findAllBooksInPayment(user.getEntityId());


                BookService bookService = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
                bookService.setLocale(locale);
                bookService.findImagesForBooks(library);


                session.setAttribute(UtilStrings.LIBRARY, library);
            } catch (ValidatorException e) {
                String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
                logger.error(error, e);
                throw new RuntimeException(error, e);
            }
        }

        return PERSONAL_PAGE;
    }
}
