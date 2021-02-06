package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.RequestConstants;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Returns 'cart.jsp' page
 */
public class CartPageCommand implements Command {

    private static final ResponseContext CART_PAGE_FORWARD = () -> "/WEB-INF/jsp/cart.jsp";
    private static final ResponseContext CART_PAGE_REDIRECT = () -> "/home?command=cart";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        ResponseContext cartPage = CART_PAGE_FORWARD;

        String chosenIBAN = requestContext.getParameter(RequestConstants.CHOSEN_IBAN);
        if (Objects.nonNull(chosenIBAN)) {
            session.setAttribute(RequestConstants.CHOSEN_IBAN, chosenIBAN);
            cartPage = CART_PAGE_REDIRECT;
        }

        String backToChooseIBAN = requestContext.getParameter(RequestConstants.BACK_TO_CHOOSE_IBAN);
        if (Objects.nonNull(backToChooseIBAN)) {
            session.removeAttribute(RequestConstants.BACK_TO_CHOOSE_IBAN);
        }

        session.removeAttribute(RequestConstants.BOOK_TO_CART);

        return cartPage;
    }
}
