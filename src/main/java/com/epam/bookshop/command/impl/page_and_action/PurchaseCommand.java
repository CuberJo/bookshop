package com.epam.bookshop.command.impl.page_and_action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.PageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.RouteConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Payment;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.DqlException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.PaymentService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.EntityFinderFacade;
import com.epam.bookshop.util.criteria.impl.PaymentCriteria;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Makes book purchase and notifies user about it by returning
 * url to redirect to where appropriate message appears
 */
public class PurchaseCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseCommand.class);

    private static final String ERROR = "errCartMsg";

    @Override
    public CommandResult execute(RequestContext requestContext) {
        if (!purchase(requestContext.getSession())) {
            return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.CART.getRoute());
        }

        return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.FINISHED_PURCHASE.getRoute());
    }


    /**
     * Purchase imitation.
     *
     * @param session current {@link HttpSession} session
     * @return true if purchase done successfully, otherwise returns false
     */
    private boolean purchase(HttpSession session) {
        final String locale = (String) session.getAttribute(RequestConstants.LOCALE);
        List<Book> cart = (List<Book>) session.getAttribute(RequestConstants.CART);

        if (alreadyBought(session, cart)) {
            session.setAttribute(ERROR,
                    ErrorMessageManager.valueOf((String) session.getAttribute(RequestConstants.LOCALE))
                            .getMessage(ErrorMessageConstants.BOOK_ALREADY_BOUGHT));
            return false;
        }
        logger.info("purchased on " + LocalDateTime.now());
        addBooksToLibrary(session, locale, cart);

        cart.clear();
        return true;
    }



    /**
     * Removes book from  {@code cart} object of type of {@link List<Book>},
     * stored in session, if it has already been bought.
     *
     * @param session current {@link HttpSession} session
     * @param cart user's cart
     */
    private boolean alreadyBought(HttpSession session, List<Book> cart) {
        PaymentService service = (PaymentService) ServiceFactory.getInstance().create(EntityType.PAYMENT);
        service.setLocale((String) session.getAttribute(RequestConstants.LOCALE));

        long libraryUserId = EntityFinderFacade.getInstance().findUserInSession(session, logger).getEntityId();
        for (int i = 0; i < cart.size(); i++) {
            try {
                if (service.find(PaymentCriteria.builder()
                        .bookId(cart.get(i).getEntityId())
                        .libraryUserId(libraryUserId)
                        .build()).isPresent()) {
                    return true;
                }
            } catch (ValidatorException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        return false;
    }


    /**
     * Removes book from  {@code cart} object of type of {@link List<Book>},
     * stored in session, if it has already been bought.
     *
     * @param locale language for error messages whether ones take place
     * @param cart user's cart
     */
    private void rmAlreadyBought(String locale, List<Book> cart) {
        PaymentService service = (PaymentService) ServiceFactory.getInstance().create(EntityType.PAYMENT);
        service.setLocale(locale);

        cart.removeIf(book -> {
            try {
                return service.find(PaymentCriteria.builder().bookId(book.getEntityId()).build()).isPresent();
            } catch (ValidatorException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }


    /**
     * Commits purchase operation in database, and adds bought {@link Book} to <b>library</b>
     * object of type of {@link List<Book>}, stored in session
     *
     * @param session current {@link HttpSession} session
     * @param locale language for error messages whether ones take place
     * @param cart user's cart
     */
    private void addBooksToLibrary(HttpSession session, String locale, List<Book> cart) {
        PaymentService service = (PaymentService) ServiceFactory.getInstance().create(EntityType.PAYMENT);
        service.setLocale(locale);

        User user;
        try {
            user = EntityFinderFacade.getInstance().findUserInSession(session, logger);
            for (Book book : cart) {
                service.create(new Payment(user, book, LocalDateTime.now(), book.getPrice()));
            }
            session.removeAttribute(RequestConstants.CHOSEN_IBAN);
        } catch (ValidatorException | DqlException e) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.PAYMENT_CREATION_FAILED);
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }

        List<Book> library = (List<Book>) session.getAttribute(RequestConstants.LIBRARY);
        if (Objects.isNull(library)) {
            library = service.findAllBooksInPayment(user.getEntityId());

            BookService bookService = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
            bookService.setLocale(locale);
            bookService.findImagesForBooks(library);

            session.setAttribute(RequestConstants.LIBRARY, library);
        } else {
            library.addAll(cart);
        }
    }
}
