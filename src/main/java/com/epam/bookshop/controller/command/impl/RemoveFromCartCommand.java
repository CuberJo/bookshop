package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Objects;

public class RemoveFromCartCommand implements Command {

    private static final ResponseContext CART_PAGE = () -> "/home?command=cart";
    private static final ResponseContext HOME_PAGE = () -> "/home";

    private static final String BOOK_TO_REMOVE = "book_to_remove";

    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final HttpSession session = requestContext.getSession();

        String locale = (String) session.getAttribute(UtilStrings.LOCALE);
        String error = "";

        ArrayList<Book> cart = (ArrayList<Book>) session.getAttribute(UtilStrings.CART);
        if (Objects.isNull(cart)) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMPTY_CART);
            throw new RuntimeException(error);
        }

        String isbnToRemove = requestContext.getParameter(UtilStrings.ISBN);
        Book bookToRemove = cart.stream().
                filter(book -> book.getISBN().equals(isbnToRemove))
                .findFirst().get();
//        Book bookToRemove = (Book) session.getAttribute(BOOK_TO_REMOVE);
//        Book bookToRemove = requestContext.getParameter(BOOK_TO_REMOVE);

        boolean bookPresent = false;
        for (Book book : cart) {
            if (bookToRemove.equals(book)) {
                bookPresent = true;
                cart.remove(bookToRemove);
                break;
            }
        }

        session.removeAttribute(BOOK_TO_REMOVE);

        if (!bookPresent) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND_IN_CART);
            throw new RuntimeException(error);
        }

        return CART_PAGE;
    }
}
