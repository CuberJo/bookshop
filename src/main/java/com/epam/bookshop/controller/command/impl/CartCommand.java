package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.util.UtilStrings;

import javax.servlet.http.HttpSession;
import java.util.Objects;

public class CartCommand implements Command {

    private static final ResponseContext CART_PAGE_FORWARD = () -> "/WEB-INF/jsp/cart.jsp";
    private static final ResponseContext CART_PAGE_REDIRECT = () -> "/home?command=cart";

    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final HttpSession session = requestContext.getSession();

        ResponseContext cartPage = CART_PAGE_FORWARD;

        String chosenIBAN = requestContext.getParameter(UtilStrings.CHOSEN_IBAN);
        if (Objects.nonNull(chosenIBAN)) {
            session.setAttribute(UtilStrings.CHOSEN_IBAN, chosenIBAN);
            cartPage = CART_PAGE_REDIRECT;

        }

        String backToChooseIBAN = requestContext.getParameter(UtilStrings.BACK_TO_CHOOSE_IBAN);
        if (Objects.nonNull(backToChooseIBAN)) {
            session.removeAttribute(UtilStrings.BACK_TO_CHOOSE_IBAN);
        }

        return cartPage;
    }
}
