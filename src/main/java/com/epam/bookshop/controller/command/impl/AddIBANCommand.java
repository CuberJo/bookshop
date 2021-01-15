package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.util.Regex;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.awt.font.GlyphMetrics.WHITESPACE;

public class AddIBANCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(Command.class);

    private static final String ERROR_MESSAGE = "error_add_iban_message";

    private static final String GET_ADD_IBAN_PAGE_ATTR = "getAddIBANPage";

    private static final String BACK_TO_CART = "back_to_cart";
    private static final String FROM_CART_PAGE = "fromCartPage";
    private static final String CREATE_ADDITIONAL_IBAN = "additional_iban";
    private static final String BACK_TO_CHOOSE_IBAN = "back_to_choose_iban";

    private static final ResponseContext ADD_IBAN_PAGE_FORWARD = () -> "/WEB-INF/jsp/add_iban.jsp";
    private static final ResponseContext ADD_IBAN_PAGE_REDIRECT = () -> "/home?command=add_iban";
    private static final ResponseContext CHOOSE_IBAN_PAGE_FORWARD = () -> "/WEB-INF/jsp/choose_iban.jsp";
    private static final ResponseContext PERSONAL_PAGE_PAGE = () -> "/home?command=personal_page";
    private static final ResponseContext CHOOSE_IBAN_PAGE_SEND_REDIRECT = () -> "/home?command=choose_iban";
    private static final ResponseContext CART_PAGE = () -> "/home?command=cart";


    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final HttpSession session = requestContext.getSession();

        String locale = (String) session.getAttribute(UtilStrings.LOCALE);
        String login = (String) session.getAttribute(UtilStrings.LOGIN);

        if (needToProcessGetRequest(requestContext)) {
            return ADD_IBAN_PAGE_FORWARD;
        }

        List<String> IBANs;
        try {
            IBANs = findIBANs(login, locale);
            if (Objects.isNull(session.getAttribute(UtilStrings.IBANs))) {
                session.setAttribute(UtilStrings.IBANs, IBANs);
            }

            if (IBANs.stream().findAny().isPresent() && Objects.nonNull(session.getAttribute(BACK_TO_CART))) {
                session.removeAttribute(BACK_TO_CART);
                return CHOOSE_IBAN_PAGE_FORWARD;
            }

            if ((IBANs.stream().findAny().isEmpty()
                    || Objects.nonNull(session.getAttribute(CREATE_ADDITIONAL_IBAN)))) {
                if (!validateIBAN(requestContext.getParameter(UtilStrings.IBAN), session, locale)) {
                    session.setAttribute(GET_ADD_IBAN_PAGE_ATTR, GET_ADD_IBAN_PAGE_ATTR);
                    return ADD_IBAN_PAGE_REDIRECT;
                }
                String iban = createIBAN(requestContext, login);
                IBANs.add(iban);

                if (Objects.nonNull(session.getAttribute(CREATE_ADDITIONAL_IBAN))) {
                    session.removeAttribute(CREATE_ADDITIONAL_IBAN);
                }
            }

        } catch (ValidatorException e) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IBAN_INCORRECT);
            session.setAttribute(ERROR_MESSAGE, error);
            logger.error(e.getMessage(), e);
            session.setAttribute(GET_ADD_IBAN_PAGE_ATTR, GET_ADD_IBAN_PAGE_ATTR);
            return ADD_IBAN_PAGE_REDIRECT;
        }

        if (Objects.nonNull(session.getAttribute(BACK_TO_CART))) {
            session.removeAttribute(BACK_TO_CART);
            return CART_PAGE;
        }

        if (Objects.nonNull(session.getAttribute(BACK_TO_CHOOSE_IBAN))) {
            session.removeAttribute(BACK_TO_CHOOSE_IBAN);
            session.setAttribute(UtilStrings.IBANs, IBANs);
            return CHOOSE_IBAN_PAGE_SEND_REDIRECT;
        }

        return PERSONAL_PAGE_PAGE;
    }


    private boolean validateIBAN(String iban, HttpSession session, String locale) throws ValidatorException {

        String error = "";
        Validator validator = new Validator();
        validator.setLocale(locale);

        if (!validator.emptyStringValidator(iban)) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INPUT_IBAN);
            session.setAttribute(ERROR_MESSAGE, error);
            return false;
        }

        validator.validateString(iban, Regex.IBAN_REGEX, ErrorMessageConstants.IBAN_INCORRECT);

        return true;
    }


    private boolean needToProcessGetRequest(RequestContext requestContext) {

        HttpSession session = requestContext.getSession();

        if (Objects.nonNull(session.getAttribute(GET_ADD_IBAN_PAGE_ATTR))
                || Objects.nonNull(requestContext.getParameter(GET_ADD_IBAN_PAGE_ATTR))) {
            if (Objects.nonNull(requestContext.getParameter(CREATE_ADDITIONAL_IBAN))) {
                session.setAttribute(CREATE_ADDITIONAL_IBAN, CREATE_ADDITIONAL_IBAN);
            }
            session.removeAttribute(GET_ADD_IBAN_PAGE_ATTR);

            return true;
        }

        return false;
    }



    private String createIBAN(RequestContext requestContext, String login) {

        String locale = (String) requestContext.getSession().getAttribute(UtilStrings.LOCALE);

        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        String errorMessage = "";
        String iban = "";

        try {

            Optional<User> optionalUser = service.find(UserCriteria.builder().login(login).build());

            if (optionalUser.isEmpty()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND) + WHITESPACE + login;
                throw new EntityNotFoundException(errorMessage);
            }

            iban = requestContext.getParameter(UtilStrings.IBAN);

            service.createUserBankAccount(iban, optionalUser.get().getEntityId());

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
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
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND);
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
