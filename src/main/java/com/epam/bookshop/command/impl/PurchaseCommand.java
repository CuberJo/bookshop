package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Payment;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.PaymentService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.PaymentCriteria;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
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
        final String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        alreadyBought(session, locale);
        purchase();
        addBooksToLibrary(session, locale);
        clearCart(session);


        return FINISHED_PURCHASE_PAGE;
    }

    /**
     * Removes book from  <b>cart</b> object of type of {@link List<Book>},
     * stored in session, if it has already been bought.
     *
     * @param session current {@link HttpSession} object
     * @param locale language for error messages whether ones take place
     */
    private void alreadyBought(HttpSession session, String locale) {
        PaymentService service = (PaymentService) ServiceFactory.getInstance().create(EntityType.PAYMENT);
        service.setLocale(locale);

        List<Book> cart = (List<Book>) session.getAttribute(RequestConstants.CART);
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
     *
     * @param session current {@link HttpSession} session
     */
    private void clearCart(HttpSession session) {
        List<Book> cart = (List<Book>) session.getAttribute(RequestConstants.CART);
        cart.clear();
    }


    /**
     * Commits purchase operation in database, and adds bought {@link Book} to <b>library</b>
     * object of type of {@link List<Book>}, stored in session
     *
     * @param session current {@link HttpSession} session
     * @param locale language for error messages whether ones take place
     */
    private void addBooksToLibrary(HttpSession session, String locale) {
        List<Book> cart = (List<Book>) session.getAttribute(RequestConstants.CART);

        PaymentService service = (PaymentService) ServiceFactory.getInstance().create(EntityType.PAYMENT);
        service.setLocale(locale);

        User user;
        try {
            Criteria<User> criteria = UserCriteria.builder()
                    .login((String) session.getAttribute(RequestConstants.LOGIN))
                    .build();
            user = EntityFinder.getInstance().find(session, logger, criteria);

            for (Book book : cart) {
                service.create(new Payment(user, book, LocalDateTime.now(), book.getPrice()));
            }

            session.removeAttribute(RequestConstants.CHOSEN_IBAN);
        } catch (ValidatorException e) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.LOGIN_INCORRECT)
                    + UtilStringConstants.WHITESPACE + session.getAttribute(RequestConstants.LOGIN);
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
