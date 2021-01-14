package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.util.UtilStrings;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Objects;

public class AddToCartCommand implements Command {

    private static final String ACCOUNT = "account";

    private static final String BOOK_TO_CART = "book_to_cart";

    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final HttpSession session = requestContext.getSession();

        ArrayList<Book> cart = (ArrayList<Book>) session.getAttribute(UtilStrings.CART);
        if (Objects.isNull(cart)) {
            cart = new ArrayList<>();
            session.setAttribute(UtilStrings.CART, cart);
        }

        Book bookToCart = (Book) session.getAttribute(BOOK_TO_CART);

        for (Book book : cart) {
            if (bookToCart.equals(book)) {
                return resolvePage(session);
            }
        }

        cart.add(bookToCart);

        session.removeAttribute(BOOK_TO_CART);

        return resolvePage(session);
    }



    private ResponseContext resolvePage(HttpSession session) {

        String backToCart = "back_to_cart";
        String page = "/home?command=%s";
        
        String login = (String) session.getAttribute(UtilStrings.LOGIN);
        String role = (String) session.getAttribute(UtilStrings.ROLE);
        
        if (Objects.nonNull(login) && Objects.nonNull(role)) {
            page = String.format(page, UtilStrings.CART);
        } else {
            page = String.format(page, ACCOUNT);
            // to return back after registration or singing in
            session.setAttribute(backToCart, backToCart);
        }
        
        String pageToReturn = page;
        return  () -> pageToReturn;
    }
}
