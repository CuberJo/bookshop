package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.util.manager.ErrorMessageManager;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Objects;

public class RemoveFromCartCommand implements Command {

    private static final ResponseContext CART_PAGE = () -> "/home?command=cart";
    private static final ResponseContext HOME_PAGE = () -> "/home";

    private static final String ROLE_ATTR = "role";
    private static final String LOGIN_ATTR = "login";
    private static final String LOCALE = "locale";
    private static final String CART = "cart";

    private static final String EMPTY_CART = "emmpty_cart";
    private static final String BOOK_TO_REMOVE = "boot_to_remove";
    private static final String BOOK_NOT_FOUND_IN_CART = "book_not_found_in_cart";

    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final HttpSession session = requestContext.getSession();

        if (Objects.isNull(session.getAttribute(ROLE_ATTR)) || Objects.isNull(session.getAttribute(LOGIN_ATTR))) {
            return HOME_PAGE;
        }

        String locale = (String) session.getAttribute(LOCALE);
        String error = "";

        ArrayList<Book> cart = (ArrayList<Book>) session.getAttribute(CART);
        if (Objects.isNull(cart)) {
            error = ErrorMessageManager.valueOf(locale).getMessage(EMPTY_CART);
            throw new RuntimeException(error);
        }

        Book bookToRemove = (Book) session.getAttribute(BOOK_TO_REMOVE);

        boolean bookPresent = false;
        for (Book book : cart) {
            if (bookToRemove.equals(book)) {
                bookPresent = true;
                cart.remove(bookToRemove);
                break;
            }
        }

        requestContext.getSession().removeAttribute(BOOK_TO_REMOVE);

        if (!bookPresent) {
            error = ErrorMessageManager.valueOf(locale).getMessage(BOOK_NOT_FOUND_IN_CART);
            throw new RuntimeException(error);
        }

        return CART_PAGE;
    }
}
