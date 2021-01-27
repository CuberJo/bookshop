package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.PaymentCriteria;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Payment;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Makes book purchase
 */
public class PurchaseCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseCommand.class);

    private static final ResponseContext FINISHED_PURCHASE_PAGE = () -> "/home?command=finished_purchase";

    /**
     * Executes purchase command.
     *
     * @param requestContext wrapper object of {@link java.net.http.HttpRequest}
     * @return instance of {@link ResponseContext, which is used to redirect to
     * another page.
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        alreadyBought(session);
        purchase();
        addBooksToLibrary(session);
        clearCart(session);


        return FINISHED_PURCHASE_PAGE;
    }

    /**
     * Removes book from cart if it was bought
     * @param session current {@link HttpSession} object
     */
    private void alreadyBought(HttpSession session) {
        String locale = (String) session.getAttribute(UtilStrings.LOCALE);

        PaymentService service = (PaymentService) ServiceFactory.getInstance().create(EntityType.PAYMENT);
        service.setLocale(locale);

        List<Book> cart = (List<Book>) session.getAttribute(UtilStrings.CART);
        for (int i = 0; i < cart.size(); i++) {
            Criteria<Payment> criteria = PaymentCriteria.builder()
                    .bookId(cart.get(i).getEntityId())
                    .build();
            try {
                if (service.find(criteria).isPresent()) {
                    cart.remove(cart.get(i));
                }
            } catch (ValidatorException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }


    /**
     * Purchase imitation.
     */
    private void purchase() {
        logger.info("purchased on " + LocalDateTime.now());
    }


    /**
     * Clearing <b>cart</b> attribute in session
     * @param session current {@link HttpSession} session
     */
    private void clearCart(HttpSession session) {
        List<Book> cart = (List<Book>) session.getAttribute(UtilStrings.CART);
        cart.clear();
    }


    /**
     * @param session current {@link HttpSession} session
     */
    private void addBooksToLibrary(HttpSession session) {
        String locale = (String) session.getAttribute(UtilStrings.LOCALE);

        List<Book> cart = (List<Book>) session.getAttribute(UtilStrings.CART);

        PaymentService service = (PaymentService) ServiceFactory.getInstance().create(EntityType.PAYMENT);
        service.setLocale(locale);

        User user;
        try {
            Criteria<User> criteria = UserCriteria.builder()
                    .login((String) session.getAttribute(UtilStrings.LOGIN))
                    .build();
            user = EntityFinder.getInstance().find(session, logger, criteria);

            for (Book book : cart) {
                service.create(new Payment(user, book, LocalDateTime.now(), book.getPrice()));
            }

            session.removeAttribute(UtilStrings.CHOSEN_IBAN);
        } catch (ValidatorException e) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.LOGIN_INCORRECT)
                    + UtilStrings.WHITESPACE + session.getAttribute(UtilStrings.LOGIN);
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }


        List<Book> library = (List<Book>) session.getAttribute(UtilStrings.LIBRARY);

        if (Objects.isNull(library)) {
            library = service.findAllBooksInPayment(user.getEntityId());

            BookService bookService = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
            bookService.setLocale(locale);

            bookService.findImagesForBooks(library);

            session.setAttribute(UtilStrings.LIBRARY, library);
        } else {
            library.addAll(cart);
        }
    }
}
