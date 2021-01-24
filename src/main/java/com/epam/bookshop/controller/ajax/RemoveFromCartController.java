package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@WebServlet("/remove_from_cart")
public class RemoveFromCartController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RemoveFromCartController.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final HttpSession session = req.getSession();

        remove(session, (String) session.getAttribute(UtilStrings.LOCALE), req.getParameter(UtilStrings.ISBN));
    }


    /**
     * Removes {@link Book} from {@link ArrayList<Book>} cart
     * @param session current {@link HttpSession} session
     * @param locale {@link String} language for error messages
     * @param isbn {@link String} ISBN to remove
     */
    private void remove(HttpSession session, String locale, String isbn) {
        ArrayList<Book> cart = getCart(session, locale);

        Book bookToRemove = findBookInCart(cart, isbn, locale);

        boolean bookPresent = false;
        for (Book book : cart) {
            if (bookToRemove.equals(book)) {
                bookPresent = true;
                cart.remove(bookToRemove);
                break;
            }
        }
        if (!bookPresent) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND_IN_CART);
            logger.error(error);
            throw new RuntimeException(error);
        }

        session.removeAttribute(UtilStrings.BOOK_TO_REMOVE);
    }


    /**
     * Gets cart of user in current {@link HttpSession} session
     * @param session current {@link HttpSession} session
     * @param locale {@link String} language for error messages
     * @return {@link ArrayList<Book>} cart associated with user in current session
     */
    private ArrayList<Book> getCart(HttpSession session, String locale) {
        ArrayList<Book> cart = (ArrayList<Book>) session.getAttribute(UtilStrings.CART);
        if (Objects.isNull(cart)) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMPTY_CART);
            logger.error(error);
            throw new RuntimeException(error);
        }
        return cart;
    }

    /**
     * Finds {@link Book} in cart by ISBN
     * @param cart {@link ArrayList<Book>} cart where to be looking for
     * @param isbn {@link String} ISBN of Book to be found
     * @param locale {@link String} language for error messages
     * @return {@link Book} if it was found
     */
    private Book findBookInCart(ArrayList<Book> cart, String isbn, String locale) {
        Optional<Book> optionalBook = cart.stream().
                filter(book -> book.getISBN().equals(isbn))
                .findFirst();
        if (optionalBook.isEmpty()) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND)
                    + UtilStrings.WHITESPACE + isbn;
            logger.error(error);
            throw new RuntimeException(error);
        }

        return optionalBook.get();
    }
}
