package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.FrontCommand;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.util.constant.RequestConstants;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.util.constant.RegexConstants;
import com.epam.bookshop.util.constant.UtilStringConstants;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.util.validator.impl.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AddIBANFrontCommand implements FrontCommand {
    private static final Logger logger = LoggerFactory.getLogger(AddIBANFrontCommand.class);

    private static final ResponseContext ADD_IBAN_PAGE_FORWARD = () -> "/WEB-INF/jsp/add_iban.jsp";
    private static final ResponseContext ADD_IBAN_PAGE_REDIRECT = () -> "/home?command=add_iban";
    private static final ResponseContext CHOOSE_IBAN_PAGE_FORWARD = () -> "/WEB-INF/jsp/choose_iban.jsp";
    private static final ResponseContext PERSONAL_PAGE_PAGE = () -> "/home?command=personal_page";
    private static final ResponseContext CHOOSE_IBAN_PAGE_SEND_REDIRECT = () -> "/home?command=choose_iban";
    private static final ResponseContext CART_PAGE = () -> "/home?command=cart";


    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        if (needToProcessGetRequest(requestContext)) {
            return ADD_IBAN_PAGE_FORWARD;
        }

        List<String> IBANs;
        try {
            UserCriteria criteria = UserCriteria.builder()
                    .login((String) session.getAttribute(RequestConstants.LOGIN))
                    .build();
            IBANs = EntityFinder.getInstance().findIBANs(criteria, logger, locale);
            if (Objects.isNull(session.getAttribute(RequestConstants.IBANs))) {
                session.setAttribute(RequestConstants.IBANs, IBANs);
            }

            if (Objects.isNull(session.getAttribute(RequestConstants.CREATE_ADDITIONAL_IBAN))) {
                if (IBANs.stream().findAny().isPresent() && Objects.nonNull(session.getAttribute(RequestConstants.BACK_TO_CART))) {
                    session.removeAttribute(RequestConstants.BACK_TO_CART);
                    return CHOOSE_IBAN_PAGE_FORWARD;
                }
            }

            if ((IBANs.stream().findAny().isEmpty()
                    || Objects.nonNull(session.getAttribute(RequestConstants.CREATE_ADDITIONAL_IBAN)))) {
                if (!validateIBAN(requestContext.getParameter(RequestConstants.IBAN), session, locale)) {
                    session.setAttribute(RequestConstants.GET_ADD_IBAN_PAGE_ATTR, RequestConstants.GET_ADD_IBAN_PAGE_ATTR);
                    return ADD_IBAN_PAGE_REDIRECT;
                }
                String iban = createIBAN(requestContext, criteria, locale);
                IBANs.add(iban);

                if (Objects.nonNull(session.getAttribute(RequestConstants.CREATE_ADDITIONAL_IBAN))) {
                    session.removeAttribute(RequestConstants.CREATE_ADDITIONAL_IBAN);
                }
            }

        } catch (ValidatorException e) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IBAN_INCORRECT);
            session.setAttribute(ErrorMessageConstants.ERROR_ADD_IBAN_MESSAGE, error);
            logger.error(e.getMessage(), e);
            session.setAttribute(RequestConstants.GET_ADD_IBAN_PAGE_ATTR, RequestConstants.GET_ADD_IBAN_PAGE_ATTR);
            return ADD_IBAN_PAGE_REDIRECT;
        }

        if (Objects.nonNull(session.getAttribute(RequestConstants.BACK_TO_CART))) {
            session.removeAttribute(RequestConstants.BACK_TO_CART);
            return CART_PAGE;
        }

        if (Objects.nonNull(session.getAttribute(RequestConstants.BACK_TO_CHOOSE_IBAN))) {
            session.removeAttribute(RequestConstants.BACK_TO_CHOOSE_IBAN);
            session.setAttribute(RequestConstants.IBANs, IBANs);
            return CHOOSE_IBAN_PAGE_SEND_REDIRECT;
        }

        return PERSONAL_PAGE_PAGE;
    }


    /**
     * Validates passed by client IBAN string
     *
     * @param iban IBAN {@link String} to validate
     * @param session current {@link HttpSession} session used to set attributes
     * @param locale {@link String} language for error messages
     * @return true if and only if strings passed validation, otherwise - false
     */
    private boolean validateIBAN(String iban, HttpSession session, String locale) {

        Validator validator = new Validator();
        validator.setLocale(locale);

        String error;

        if (validator.empty(iban)) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INPUT_IBAN);
            session.setAttribute(ErrorMessageConstants.ERROR_ADD_IBAN_MESSAGE, error);
            return false;
        }

        if(!validator.validate(iban, RegexConstants.IBAN_REGEX)) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IBAN_INCORRECT);
            session.setAttribute(ErrorMessageConstants.ERROR_ADD_IBAN_MESSAGE, error);
            return false;
        }

        return true;
    }


    /**
     * Checks whether it is <b>GET</b> request
     * @param requestContext {@link RequestContext} object, which is request wrapper
     * @return true if and only if it is need to pprocess <b>GET</b> request, otherwise - false
     */
    private boolean needToProcessGetRequest(RequestContext requestContext) {
        HttpSession session = requestContext.getSession();

        if (Objects.nonNull(session.getAttribute(RequestConstants.GET_ADD_IBAN_PAGE_ATTR))
                || Objects.nonNull(requestContext.getParameter(RequestConstants.GET_ADD_IBAN_PAGE_ATTR))) {
            if (Objects.nonNull(requestContext.getParameter(RequestConstants.CREATE_ADDITIONAL_IBAN))) {
                session.setAttribute(RequestConstants.CREATE_ADDITIONAL_IBAN, RequestConstants.CREATE_ADDITIONAL_IBAN);
            }
            session.removeAttribute(RequestConstants.GET_ADD_IBAN_PAGE_ATTR);

            return true;
        }

        return false;
    }


    /**
     * Creates user IBAN for user, whi is found by {@link Criteria<User>} criteria
     * @param requestContext {@link RequestContext} object, which is request wrapper
     * @param criteria {@link Criteria<User>} criteria by which user is found
     * @param locale {@link String} language for error messages
     * @return {@link String} object of created IBAN
     */
    private String createIBAN(RequestContext requestContext, Criteria<User> criteria, String locale) {
        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        String errorMessage = "";
        String iban = "";

        try {

            Optional<User> optionalUser = service.find(criteria);
            if (optionalUser.isEmpty()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND)
                        + UtilStringConstants.WHITESPACE + ((UserCriteria) criteria).getLogin();
                throw new EntityNotFoundException(errorMessage);
            }

            iban = requestContext.getParameter(RequestConstants.IBAN);

            service.createUserBankAccount(iban, optionalUser.get().getEntityId());

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        } catch (EntityNotFoundException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND)
                    + UtilStringConstants.WHITESPACE + ((UserCriteria) criteria).getLogin();
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }

        return iban;
    }
}
