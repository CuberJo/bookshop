package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

import javax.servlet.http.HttpSession;
import java.util.Objects;

public class CartCommand implements Command {

    private static final String ROLE = "role";
    private static final String LOGIN = "login";

    private static final String CHOSEN_IBAN_PARAM = "chosen_iban";

    private static final ResponseContext CART_PAGE_FORWARD = () -> "/WEB-INF/jsp/cart.jsp";
    private static final ResponseContext CART_PAGE_REDIRECT = () -> "/home?command=cart";
    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/home.jsp";


    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final HttpSession session = requestContext.getSession();

        ResponseContext cartPage = CART_PAGE_FORWARD;

        String login = (String) session.getAttribute(LOGIN);
        String role = (String) session.getAttribute(ROLE);

        if (Objects.isNull(login) || Objects.isNull(role)) {
            return HOME_PAGE;
        }

        String chosenIBAN = requestContext.getParameter(CHOSEN_IBAN_PARAM);
        if (Objects.nonNull(chosenIBAN)) {
            session.setAttribute(CHOSEN_IBAN_PARAM, chosenIBAN);
            cartPage = CART_PAGE_REDIRECT;

        }

        return cartPage;
    }
}
