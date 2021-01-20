package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.util.UtilStrings;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet("/add_to_cart")
public class AddToCartController extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        final HttpSession session = request.getSession();

        add(session, (Book) session.getAttribute(UtilStrings.BOOK_TO_CART));
    }


    /**
     * Checks whether we have already bought the book
     * @param book {@link Book} book to be chacked
     * @param session current {@link HttpSession} session used to set attributes
     * @return true if and only if book already exists in library, otherwise - false
     */
    private boolean bookExistsInLibrary(Book book, HttpSession session) {
        List<Book> library = (List<Book>) session.getAttribute(UtilStrings.LIBRARY);

        if (Objects.isNull(library)) {
            return false;
        }

        for (Book b : library) {
            if (book.equals(b)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Adds {@link Book} to cart
     * @param session current {@link HttpSession} session used to set attributes
     * @param book {@link Book} to be added to cart
     */
    private void add(HttpSession session, Book book) {
        List<Book> cart = getCart(session);

        for (Book b : cart) {
            if (book.equals(b)) {
                return;
            }
        }

        if (bookExistsInLibrary(book, session)) {
            return;
        }

        cart.add(book);
        session.removeAttribute(UtilStrings.BOOK_TO_CART);
    }


    /**
     * Fetches {@link List<Book>} cart from {@link HttpSession}. If it not exists creates new cart.
     * @param session current {@link HttpSession} session used to set attributes
     * @return {@link List<Book>} of books
     */
    private List<Book> getCart(HttpSession session) {
        List<Book> cart = (List<Book>) session.getAttribute(UtilStrings.CART);
        if (Objects.isNull(cart)) {
            cart = new ArrayList<>();
            session.setAttribute(UtilStrings.CART, cart);
        }

        return cart;
    }
}
