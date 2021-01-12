package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.domain.impl.Book;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PurchaseCommand implements Command {

    private static final String ROLE_ATTR = "role";
    private static final String LOGIN_ATTR = "login";
    private static final String CHOSEN_IBAN_ATTR = "chosen_iban";
    private static final String LIBRARY_ATTR = "library";
    private static final String CART_ATTR = "cart";

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext FINISHED_PURCHASE_PAGE = () -> "/home?command=finished_purchase";



    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final HttpSession session = requestContext.getSession();

        String login = (String) session.getAttribute(LOGIN_ATTR);
        Object role = session.getAttribute(ROLE_ATTR);

        if (Objects.isNull(role) || Objects.isNull(login)) {
            return HOME_PAGE;
        }

        purchase(session);
        clearCart(session);

        return FINISHED_PURCHASE_PAGE;
    }



    private void purchase(HttpSession session) {
        session.removeAttribute(CHOSEN_IBAN_ATTR);
    }

    private void clearCart(HttpSession session) {
        List<Book> cart = (List<Book>) session.getAttribute(CART_ATTR);
        addBooksToLibrary(session, cart);
        cart.clear();
    }

    private void addBooksToLibrary(HttpSession session, List<Book> cart) {
        List<Book> library = (List<Book>) session.getAttribute(LIBRARY_ATTR);

        if (Objects.isNull(library)) {
            library = new ArrayList<>();
            session.setAttribute(LIBRARY_ATTR, library);
        }

        library.addAll(cart);
    }
}
