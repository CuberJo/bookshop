package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.constant.UtilStrings;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PurchaseCommand implements Command {

    private static final ResponseContext FINISHED_PURCHASE_PAGE = () -> "/home?command=finished_purchase";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        purchase(session);
        clearCart(session);

        return FINISHED_PURCHASE_PAGE;
    }

    /**
     * Purchase operation
     * @param session current {@link HttpSession} session
     */
    private void purchase(HttpSession session) {
        session.removeAttribute(UtilStrings.CHOSEN_IBAN);
    }


    /**
     * Clearing <b>cart</b> attribute in session
     * @param session current {@link HttpSession} session
     */
    private void clearCart(HttpSession session) {
        List<Book> cart = (List<Book>) session.getAttribute(UtilStrings.CART);
        addBooksToLibrary(session, cart);
        cart.clear();
    }


    /**
     * @param session current {@link HttpSession} session
     * @param cart <b>cart</b> of books, that have been bought
     */
    private void addBooksToLibrary(HttpSession session, List<Book> cart) {
        List<Book> library = (List<Book>) session.getAttribute(UtilStrings.LIBRARY);

        if (Objects.isNull(library)) {
            library = new ArrayList<>();
            session.setAttribute(UtilStrings.LIBRARY, library);
        }

        library.addAll(cart);
    }
}
