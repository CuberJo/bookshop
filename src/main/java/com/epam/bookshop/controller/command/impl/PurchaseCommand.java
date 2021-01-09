package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.util.manager.ErrorMessageManager;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PurchaseCommand implements Command {

    private static final String ROLE_ATTR = "role";
    private static final String LOGIN_ATTR = "login";
    private static final String LOCALE = "locale";
    private static final String FROM = "from";
    private static final String PURCHASE_COMMAND = "purchase";
    private static final String IBANs_ATR = "ibans";
    private static final String CHOSEN_IBAN_ATR = "chosen_iban";

    private static final String LOGIN_INCORRECT = "login_incorrect";
    private static final String USER_NOT_FOUND = "user_not_found";

//    private static final ResponseContext HOME_PAGE = () -> "/home";
//    private static final ResponseContext CART_PAGE = () -> "/home?command=cart";
//    private static final ResponseContext ADD_BANK_ACCOUNT_PAGE = () -> "/home?command=add_bank_account";
//    private static final ResponseContext CHOOSE_IBAN_PAGE = () -> "/home?command=choose_iban";

    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/home.jsp";
    private static final ResponseContext CART_PAGE = () -> "/WEB-INF/jsp/cart.jsp";
    private static final ResponseContext ADD_BANK_ACCOUNT_PAGE = () -> "/WEB-INF/jsp/add_bank_account.jsp";
    private static final ResponseContext CHOOSE_IBAN_PAGE = () -> "/WEB-INF/jsp/choose_iban.jsp";


    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final HttpSession session = requestContext.getSession();

        String login = (String) session.getAttribute(LOGIN_ATTR);

        if (Objects.isNull(session.getAttribute(ROLE_ATTR)) || Objects.isNull(login)) {
            return HOME_PAGE;
        }

        String error = "";
        String locale = (String) session.getAttribute(LOCALE);

        try {
            List<String> IBANs = findIBANs(login, locale);

            session.setAttribute(IBANs_ATR, IBANs);

            if (IBANs.stream().findAny().isEmpty()) {
                requestContext.setAttribute(FROM, PURCHASE_COMMAND);
                return ADD_BANK_ACCOUNT_PAGE;
            }

            if (Objects.isNull(session.getAttribute(CHOSEN_IBAN_ATR))) {
                return CHOOSE_IBAN_PAGE;
            }

            purchase();

        } catch (ValidatorException e) {
            error = ErrorMessageManager.valueOf(locale).getMessage(LOGIN_INCORRECT);
            throw new RuntimeException(error, e);
        }

        return CART_PAGE;
    }



    private List<String> findIBANs(String login, String locale) throws ValidatorException {

        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);

        String error = "";

        Optional<User> optionalUser = service.find(UserCriteria.builder().login(login).build());
        if (optionalUser.isEmpty()) {
            error = ErrorMessageManager.valueOf(locale).getMessage(USER_NOT_FOUND);
            throw new RuntimeException(error);
        }

        return optionalUser.get().getIBANs().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    private void purchase() {

    }
}
