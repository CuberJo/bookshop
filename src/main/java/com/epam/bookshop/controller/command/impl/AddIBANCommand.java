package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.util.constant.RegexConstant;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.util.validator.impl.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AddIBANCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(AddIBANCommand.class);

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

        if (needToProcessGetRequest(requestContext)) {
            return ADD_IBAN_PAGE_FORWARD;
        }

        List<String> IBANs;
        try {
            UserCriteria criteria = UserCriteria.builder()
                    .login((String) session.getAttribute(UtilStrings.LOGIN))
                    .build();
            IBANs = findIBANs(criteria, locale);
            if (Objects.isNull(session.getAttribute(UtilStrings.IBANs))) {
                session.setAttribute(UtilStrings.IBANs, IBANs);
            }

            if (Objects.isNull(session.getAttribute(UtilStrings.CREATE_ADDITIONAL_IBAN))) {
                if (IBANs.stream().findAny().isPresent() && Objects.nonNull(session.getAttribute(UtilStrings.BACK_TO_CART))) {
                    session.removeAttribute(UtilStrings.BACK_TO_CART);
                    return CHOOSE_IBAN_PAGE_FORWARD;
                }
            }

            if ((IBANs.stream().findAny().isEmpty()
                    || Objects.nonNull(session.getAttribute(UtilStrings.CREATE_ADDITIONAL_IBAN)))) {
                if (!validateIBAN(requestContext.getParameter(UtilStrings.IBAN), session, locale)) {
                    session.setAttribute(UtilStrings.GET_ADD_IBAN_PAGE_ATTR, UtilStrings.GET_ADD_IBAN_PAGE_ATTR);
                    return ADD_IBAN_PAGE_REDIRECT;
                }
                String iban = createIBAN(requestContext, criteria, locale);
                IBANs.add(iban);

                if (Objects.nonNull(session.getAttribute(UtilStrings.CREATE_ADDITIONAL_IBAN))) {
                    session.removeAttribute(UtilStrings.CREATE_ADDITIONAL_IBAN);
                }
            }

        } catch (ValidatorException e) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IBAN_INCORRECT);
            session.setAttribute(ErrorMessageConstants.ERROR_ADD_IBAN_MESSAGE, error);
            logger.error(e.getMessage(), e);
            session.setAttribute(UtilStrings.GET_ADD_IBAN_PAGE_ATTR, UtilStrings.GET_ADD_IBAN_PAGE_ATTR);
            return ADD_IBAN_PAGE_REDIRECT;
        }

        if (Objects.nonNull(session.getAttribute(UtilStrings.BACK_TO_CART))) {
            session.removeAttribute(UtilStrings.BACK_TO_CART);
            return CART_PAGE;
        }

        if (Objects.nonNull(session.getAttribute(UtilStrings.BACK_TO_CHOOSE_IBAN))) {
            session.removeAttribute(UtilStrings.BACK_TO_CHOOSE_IBAN);
            session.setAttribute(UtilStrings.IBANs, IBANs);
            return CHOOSE_IBAN_PAGE_SEND_REDIRECT;
        }

        return PERSONAL_PAGE_PAGE;
    }


    /**
     * @param iban IBAN {@link String} to validate
     * @param session current {@link HttpSession} session used to set attributes
     * @param locale {@link String} language for error messages
     * @return true if and only if strings passed validation, otherwise - false
     * @throws ValidatorException if iban argument failed validation
     */
    private boolean validateIBAN(String iban, HttpSession session, String locale) throws ValidatorException {

        Validator validator = new Validator();
        validator.setLocale(locale);

        if (!validator.emptyStringValidator(iban)) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INPUT_IBAN);
            session.setAttribute(ErrorMessageConstants.ERROR_ADD_IBAN_MESSAGE, error);
            return false;
        }

        validator.validateString(iban, RegexConstant.IBAN_REGEX, ErrorMessageConstants.IBAN_INCORRECT);

        return true;
    }


    /**
     * Checks whether it is <b>GET</b> request
     * @param requestContext {@link RequestContext} object, which is request wrapper
     * @return true if and only if it is need to pprocess <b>GET</b> request, otherwise - false
     */
    private boolean needToProcessGetRequest(RequestContext requestContext) {
        HttpSession session = requestContext.getSession();

        if (Objects.nonNull(session.getAttribute(UtilStrings.GET_ADD_IBAN_PAGE_ATTR))
                || Objects.nonNull(requestContext.getParameter(UtilStrings.GET_ADD_IBAN_PAGE_ATTR))) {
            if (Objects.nonNull(requestContext.getParameter(UtilStrings.CREATE_ADDITIONAL_IBAN))) {
                session.setAttribute(UtilStrings.CREATE_ADDITIONAL_IBAN, UtilStrings.CREATE_ADDITIONAL_IBAN);
            }
            session.removeAttribute(UtilStrings.GET_ADD_IBAN_PAGE_ATTR);

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
                        + UtilStrings.WHITESPACE + ((UserCriteria) criteria).getLogin();
                throw new EntityNotFoundException(errorMessage);
            }

            iban = requestContext.getParameter(UtilStrings.IBAN);

            service.createUserBankAccount(iban, optionalUser.get().getEntityId());

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        } catch (EntityNotFoundException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND)
                    + UtilStrings.WHITESPACE + ((UserCriteria) criteria).getLogin();
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }

        return iban;
    }


    /**
     * Find all {@link String} IBANs, associated with the user
     * @param criteria {@link Criteria<User>} criteria by which user is found
     * @param locale {@link String} language for error messages
     * @return all {@link String} IBANs, associated with the user
     * @throws ValidatorException if criteria argument failed validation
     */
    private List<String> findIBANs(Criteria<User> criteria, String locale) throws ValidatorException {

        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        Optional<User> optionalUser = service.find(criteria);
        if (optionalUser.isEmpty()) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND);
            logger.error(error);
            throw new RuntimeException(error);
        }

        return service.findUserBankAccounts(optionalUser.get().getEntityId());
    }
}
