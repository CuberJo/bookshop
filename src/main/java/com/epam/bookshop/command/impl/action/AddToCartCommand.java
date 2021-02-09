package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.domain.impl.Book;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Adds selected {@link Book} to cart
 */
public class AddToCartCommand implements Command {

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        add(session, (Book) session.getAttribute(RequestConstants.BOOK_TO_CART));

        return new CommandResult(CommandResult.ResponseType.NO_ACTION, UtilStringConstants.EMPTY_STRING);
    }

    /**
     * Adds {@link Book} to {@link List<Book>} cart object in {@link HttpSession}
     *
     * @param session current {@link HttpSession} session used to set attributes
     * @param book {@link Book} to be added to cart
     */
    private void add(HttpSession session, Book book) {
        List<Book> cart = getCart(session);

        for (Book b : cart) {
            if (Objects.nonNull(book) && book.equals(b)) {
                return;
            }
        }

        if (bookExistsInLibrary(book, session)) {
            return;
        }

        cart.add(book);
    }

    /**
     * Checks whether we have already bought the book
     *
     * @param book {@link Book} book to be checked
     * @param session current {@link HttpSession} session used to set attributes
     * @return true if and only if book already exists in library, otherwise - false
     */
    private boolean bookExistsInLibrary(Book book, HttpSession session) {
        List<Book> library = (List<Book>) session.getAttribute(RequestConstants.LIBRARY);

        if (Objects.isNull(library)) {
            return false;
        }

        for (Book b : library) {
            if (Objects.nonNull(book) && book.equals(b)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Fetches {@link List<Book>} cart object from {@link HttpSession}.
     * If it not exists creates new one.
     *
     * @param session current {@link HttpSession} session used to set attributes
     * @return {@link List<Book>} of books
     */
    private List<Book> getCart(HttpSession session) {
        List<Book> cart = (List<Book>) session.getAttribute(RequestConstants.CART);
        if (Objects.isNull(cart)) {
            cart = new ArrayList<>();
            session.setAttribute(RequestConstants.CART, cart);
        }

        return cart;
    }
}
