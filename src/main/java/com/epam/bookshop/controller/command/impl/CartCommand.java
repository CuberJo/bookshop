package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

import javax.servlet.http.HttpSession;
import java.util.Objects;

public class CartCommand implements Command {


    private static final String CHOSEN_IBAN_PARAM = "chosen_iban";

    private static final String BACK_TO_CHOOSE_IBAN = "back_to_choose_iban";

    private static final ResponseContext CART_PAGE_FORWARD = () -> "/WEB-INF/jsp/cart.jsp";
    private static final ResponseContext CART_PAGE_REDIRECT = () -> "/home?command=cart";
    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/home.jsp";


    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final HttpSession session = requestContext.getSession();

        ResponseContext cartPage = CART_PAGE_FORWARD;

        String chosenIBAN = requestContext.getParameter(CHOSEN_IBAN_PARAM);
        if (Objects.nonNull(chosenIBAN)) {
            session.setAttribute(CHOSEN_IBAN_PARAM, chosenIBAN);
            cartPage = CART_PAGE_REDIRECT;

        }

        String backToChooseIBAN = requestContext.getParameter(BACK_TO_CHOOSE_IBAN);
        if (Objects.nonNull(backToChooseIBAN)) {
            session.removeAttribute(BACK_TO_CHOOSE_IBAN);
        }

        return cartPage;
    }
}
