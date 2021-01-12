package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.criteria.impl.GenreCriteria;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.awt.font.GlyphMetrics.WHITESPACE;

public class AddIBANCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(Command.class);

    private static final String FROM_AJAX = "froAjax";

    private static final String ROLE_ATTR = "role";
    private static final String LOGIN_ATTR = "login";
    private static final String LOCALE_ATTR = "locale";
    private static final String IBAN = "iban";
    private static final String IBANs_ATR = "ibans";
    private static final String GET_ADD_IBAN_PAGE_ATTR = "getAddIBANPage";

    private static final String EMPTY_STRING = "";
    private static final String BACK_TO_CART = "back_to_cart";
    private static final String FROM_CART_PAGE = "fromCartPage";
    private static final String CREATE_ADDITIONAL_IBAN = "additional_iban";

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext ADD_IBAN_PAGE = () -> "/WEB-INF/jsp/add_iban.jsp";
    private static final ResponseContext CHOOSE_IBAN_PAGE_FORWARD = () -> "/WEB-INF/jsp/choose_iban.jsp";
    private static final ResponseContext PERSONAL_PAGE_PAGE = () -> "/home?command=personal_page";
    private static final ResponseContext CHOOSE_IBAN_PAGE_SEND_REDIRECT = () -> "/home?command=choose_iban";
    private static final ResponseContext CART_PAGE = () -> "/home?command=cart";

    private static final String USER_NOT_FOUND = "user_not_found";
    private static final String INVALID_INPUT_DATA = "invalid_input_data";



    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final HttpSession session = requestContext.getSession();

        String locale = (String) session.getAttribute(LOCALE_ATTR);

        String role = (String) session.getAttribute(ROLE_ATTR);
        String login = (String) session.getAttribute(LOGIN_ATTR);

        if (Objects.isNull(role) || Objects.isNull(login)) {
            return HOME_PAGE;
        }

        if (Objects.nonNull(session.getAttribute(GET_ADD_IBAN_PAGE_ATTR)) || Objects.nonNull(requestContext.getParameter(GET_ADD_IBAN_PAGE_ATTR))) {
            if (Objects.nonNull(requestContext.getParameter(CREATE_ADDITIONAL_IBAN))) {
                session.setAttribute(CREATE_ADDITIONAL_IBAN, CREATE_ADDITIONAL_IBAN);
            }
            session.removeAttribute(GET_ADD_IBAN_PAGE_ATTR);
            return ADD_IBAN_PAGE;
        }

        List<String> IBANs;
        try {
            IBANs = findIBANs(login, locale);
            if (Objects.isNull(session.getAttribute(IBANs_ATR))) {
                session.setAttribute(IBANs_ATR, IBANs);
            }

            if (Objects.nonNull(requestContext.getParameter(FROM_AJAX))) {
                return CART_PAGE;
            }

            if (IBANs.stream().findAny().isPresent() && Objects.nonNull(session.getAttribute(FROM_CART_PAGE))) {
                session.removeAttribute(FROM_CART_PAGE);
                return CHOOSE_IBAN_PAGE_FORWARD;
            }

            if ((IBANs.stream().findAny().isEmpty()
                    || Objects.nonNull(requestContext.getAttribute(CREATE_ADDITIONAL_IBAN)))
                    || Objects.nonNull(session.getAttribute(CREATE_ADDITIONAL_IBAN))) {
                String iban = createIBAN(requestContext, login);
                IBANs.add(iban);

                if (Objects.nonNull(session.getAttribute(CREATE_ADDITIONAL_IBAN))) {
                    session.removeAttribute(CREATE_ADDITIONAL_IBAN);
                }
            }

        } catch (ValidatorException e) {
            e.printStackTrace();
        }

        if (Objects.nonNull(session.getAttribute(BACK_TO_CART))) {
            session.removeAttribute(BACK_TO_CART);
            return CART_PAGE;
        }

        return PERSONAL_PAGE_PAGE;
    }



    private String createIBAN(RequestContext requestContext, String login) {

        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        String locale = (String) requestContext.getSession().getAttribute(LOCALE_ATTR);
        service.setLocale(locale);

        String errorMessage = "";
        String iban = "";

        try {

            Optional<User> optionalUser = service.find(UserCriteria.builder().login(login).build());

            if (optionalUser.isEmpty()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(USER_NOT_FOUND) + WHITESPACE + login;
                throw new EntityNotFoundException(errorMessage);
            }

            iban = requestContext.getParameter(IBAN);

            service.createUserBankAccount(iban, optionalUser.get().getEntityId());

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(INVALID_INPUT_DATA);
            throw new RuntimeException(errorMessage, e);
        } catch (EntityNotFoundException e) {
            logger.error("", e);
        }

        return iban;
    }



    private List<String> findIBANs(String login, String locale) throws ValidatorException {

        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);

        String error = "";

        Optional<User> optionalUser = service.find(UserCriteria.builder().login(login).build());
        if (optionalUser.isEmpty()) {
            error = ErrorMessageManager.valueOf(locale).getMessage(USER_NOT_FOUND);
            throw new RuntimeException(error);
        }

        List<String> IBANs = service.findUserBankAccounts(optionalUser.get().getEntityId());

//        return optionalUser.get().getIBANs().stream()
//                .filter(Objects::nonNull)
//                .filter(s -> !s.equals(EMPTY_STRING))
//                .collect(Collectors.toList());
        return IBANs;
    }
}
