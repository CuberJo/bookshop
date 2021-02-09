package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.PageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.RouteConstants;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Returns 'cart.jsp' page
 */
public class CartPageCommand implements Command {

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        CommandResult cartPage = new CommandResult(CommandResult.ResponseType.FORWARD, PageConstants.CART.getPage());

        String chosenIBAN = requestContext.getParameter(RequestConstants.CHOSEN_IBAN);
        if (Objects.nonNull(chosenIBAN)) {
            session.setAttribute(RequestConstants.CHOSEN_IBAN, chosenIBAN);
            cartPage = new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.CART.getRoute());
        }

        String backToChooseIBAN = requestContext.getParameter(RequestConstants.BACK_TO_CHOOSE_IBAN);
        if (Objects.nonNull(backToChooseIBAN)) {
            session.removeAttribute(RequestConstants.BACK_TO_CHOOSE_IBAN);
        }

        session.removeAttribute(RequestConstants.BOOK_TO_CART);

        return cartPage;
    }
}
