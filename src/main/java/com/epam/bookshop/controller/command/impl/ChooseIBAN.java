package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

import javax.servlet.http.HttpSession;
import java.util.Objects;

public class ChooseIBAN implements Command {

    private static final String ROLE_ATTR = "role";
    private static final String LOGIN_ATTR = "login";
    private static final String LOCALE_ATTR = "locale";

    private static final ResponseContext CHOOSE_IBAN_PAGE = () -> "/WEB-INF/jsp/choose_iban.jsp";
    private static final ResponseContext HOME_PAGE = () -> "/home?command=choose_iban";


    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final HttpSession session = requestContext.getSession();

        if (Objects.isNull(session.getAttribute(ROLE_ATTR)) || Objects.isNull(session.getAttribute(LOGIN_ATTR))) {
            return HOME_PAGE;
        }

        return CHOOSE_IBAN_PAGE;
    }
}
