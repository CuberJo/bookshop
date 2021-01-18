package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
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

    private static final String ACCOUNT = "account";

    private static final String BOOK_TO_CART = "book_to_cart";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        final HttpSession session = request.getSession();

        ArrayList<Book> cart = (ArrayList<Book>) session.getAttribute(UtilStrings.CART);
        if (Objects.isNull(cart)) {
            cart = new ArrayList<>();
            session.setAttribute(UtilStrings.CART, cart);
        }

        Book bookToCart = (Book) session.getAttribute(BOOK_TO_CART);

        for (Book book : cart) {
            if (bookToCart.equals(book)) {
                return;
//                return resolvePage(session);
            }
        }

        if (bookExistsInLibrary(bookToCart, session)) {
            return;
        }

        cart.add(bookToCart);

        session.removeAttribute(BOOK_TO_CART);

        return;
//        return resolvePage(session);
    }


    /**
     * Checks whether we have already bought the book
     * @param bookToCart
     * @param session
     * @return
     */
    private boolean bookExistsInLibrary(Book bookToCart, HttpSession session) {
        List<Book> library = (List<Book>) session.getAttribute(UtilStrings.LIBRARY);

        if (Objects.isNull(library)) {
            return false;
        }

        for (Book book : library) {
            if (bookToCart.equals(book)) {
                return true;
            }
        }

        return false;
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
