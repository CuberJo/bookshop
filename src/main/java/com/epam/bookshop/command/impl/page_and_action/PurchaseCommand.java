package com.epam.bookshop.command.impl.page_and_action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Payment;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.PaymentService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.util.criteria.impl.PaymentCriteria;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Makes book purchase and notifies user about it by returning
 * url to redirect to where appropriate message appears
 */
public class PurchaseCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseCommand.class);

    private static final ResponseContext FINISHED_PURCHASE_PAGE = () -> "/home?command=finished_purchase";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        purchase(requestContext.getSession());

        return FINISHED_PURCHASE_PAGE;
    }


    /**
     * Purchase imitation.
     *
     * @param session current {@link HttpSession} session
     */
    private void purchase(HttpSession session) {
        final String locale = (String) session.getAttribute(RequestConstants.LOCALE);
        List<Book> cart = (List<Book>) session.getAttribute(RequestConstants.CART);

        rmAlreadyBought(locale, cart);
        logger.info("purchased on " + LocalDateTime.now());
        addBooksToLibrary(session, locale, cart);

        cart.clear();
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

        cart.forEach(book -> {
            try {
                if (service.find(PaymentCriteria.builder().bookId(book.getEntityId()).build()).isPresent()) {
                    cart.remove(book);
                }
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
            user = EntityFinder.getInstance().findUserInSession(session, logger);
            for (Book book : cart) {
                service.create(new Payment(user, book, LocalDateTime.now(), book.getPrice()));
            }
            session.removeAttribute(RequestConstants.CHOSEN_IBAN);
        } catch (ValidatorException e) {
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
