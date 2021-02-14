package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * Removes {@link Book} from cart
 */
public class RemoveFromCartCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(RemoveFromCartCommand.class);

    @Override
    public CommandResult execute(RequestContext requestContext) {
        HttpSession session = requestContext.getSession();

        remove(session, (String) session.getAttribute(RequestConstants.LOCALE),
                requestContext.getParameter(RequestConstants.ISBN));

        return new CommandResult(CommandResult.ResponseType.NO_ACTION, UtilStringConstants.EMPTY_STRING);
    }

    /**
     * Removes {@link Book} from {@link ArrayList<Book>} cart
     *
     * @param session current {@link HttpSession} session
     * @param locale {@link String} language for error messages
     * @param isbn {@link String} ISBN to remove
     */
    private void remove(HttpSession session, String locale, String isbn) {
        ArrayList<Book> cart = getCart(session, locale);

        Book bookToRemove = findBookInCart(cart, isbn, locale);
        cart.removeIf(book -> book.equals(bookToRemove));

        session.removeAttribute(RequestConstants.BOOK_TO_REMOVE);
    }


    /**
     * Gets cart of user in current {@link HttpSession} session
     *
     * @param session current {@link HttpSession} session
     * @param locale {@link String} language for error messages
     * @return {@link ArrayList<Book>} cart associated with user in current session
     */
    private ArrayList<Book> getCart(HttpSession session, String locale) {
        ArrayList<Book> cart = (ArrayList<Book>) session.getAttribute(RequestConstants.CART);
        if (Objects.isNull(cart)) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMPTY_CART);
            logger.error(error);
            throw new RuntimeException(error);
        }
        return cart;
    }

    /**
     * Finds {@link Book} in cart by ISBN
     *
     * @param cart {@link ArrayList<Book>} cart where to be looking for
     * @param isbn {@link String} ISBN of Book to be found
     * @param locale {@link String} language for error messages
     * @return {@link Book} if it was found
     */
    private Book findBookInCart(ArrayList<Book> cart, String isbn, String locale) {
        Optional<Book> optionalBook = cart.stream()
                .filter(book -> book.getISBN().equals(isbn))
                .findFirst();
        if (optionalBook.isEmpty()) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND)
                    + UtilStringConstants.WHITESPACE + isbn;
            logger.error(error);
            throw new RuntimeException(error);
        }

        return optionalBook.get();
    }
}
