package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;

import javax.servlet.http.HttpSession;
import java.util.Objects;

public class AddBankAccountCommand implements Command {

    private static final String ROLE_ATTR = "role";
    private static final String LOGIN_ATTR = "login";

    private static final String PURCHASE_COMMAND = "purchase";

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext ADD_BANK_ACCOUNT_PAGE = () -> "/home?command=add_banck_account";
    private static final ResponseContext CART_PAGE = () -> "/home?command=cart";


    @Override
    public ResponseContext execute(RequestContext requestContext) {

        HttpSession session = requestContext.getSession();

        if (Objects.isNull(session.getAttribute(ROLE_ATTR)) || Objects.isNull(LOGIN_ATTR)) {
            return HOME_PAGE;
        }

        // если мы пришли сюда из Purchase_Command, то нам просто нужно перенапрваить на др. страницу, если же нет, то мы должны обработать добавление нового аккаунта
        if (Objects.nonNull(requestContext.getAttribute(PURCHASE_COMMAND))) {
            requestContext.removeAttribute(PURCHASE_COMMAND);
            return ADD_BANK_ACCOUNT_PAGE;
        }

//        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
//        service.createUserBankAccount()

        return CART_PAGE;
    }
}
