package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.controller.command.FrontCommand;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.util.validator.impl.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * to register in application
 */
public class RegisterFrontCommand implements FrontCommand {
    private static final Logger logger = LoggerFactory.getLogger(RegisterFrontCommand.class);

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext ACCOUNT_PAGE = () -> "/home?command=account";
    private static final ResponseContext CART_PAGE = () -> "/home?command=cart";

    private static final String REGISTER_USER_SUBJECT = "Registration completed";
    private static final String REGISTER_RESPONSE = "You have successfully registered!";

    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final String name = requestContext.getParameter(UtilStrings.NAME);
        final String login = requestContext.getParameter(UtilStrings.LOGIN);
        final String email = requestContext.getParameter(UtilStrings.EMAIL);
        final String password = requestContext.getParameter(UtilStrings.PASSWORD);

        final HttpSession session = requestContext.getSession();
        String locale = (String) requestContext.getSession().getAttribute(UtilStrings.LOCALE);
        String errorMessage = "";

        User user = null;

        try {
            if (!validateEmptyInput(name, login, email, password, locale, session)) {
                return ACCOUNT_PAGE;
            }

            user = register(name, login, email, password, locale);

//            MailSender.getInstance().send(email, REGISTER_USER_SUBJECT, REGISTER_RESPONSE);

            session.setAttribute(UtilStrings.LOGIN, login);
            session.setAttribute(UtilStrings.ROLE, UtilStrings.USER_ROLE);
            session.setAttribute(UtilStrings.NEED_TO_LINK_BANK_ACCOUNT, true);

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
            session.setAttribute(ErrorMessageConstants.ERROR_REG_MESSAGE, errorMessage);
            logger.error(e.getMessage(), e);
            return ACCOUNT_PAGE;
        } /*catch (MessagingException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.COULD_NOT_REACH_EMAIL_ADDRESS) + UtilStrings.NEW_LINE + email;
            session.setAttribute(ErrorMessageConstants.ERROR_REG_MESSAGE, errorMessage);
            try {
                EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
                service.setLocale(locale);
                long userId = service.find(UserCriteria.builder().login(user.getLogin()).build()).get().getEntityId();
                service.delete(userId);
            } catch (EntityNotFoundException | ValidatorException e1) {
                logger.error(e1.getMessage(), e1);
            }
            logger.error(e.getMessage(), e);
            return ACCOUNT_PAGE;
        }*/

        if (Objects.nonNull(requestContext.getSession().getAttribute(UtilStrings.BACK_TO_CART))) {
            requestContext.getSession().removeAttribute(UtilStrings.BACK_TO_CART);
            return CART_PAGE;
        }

        return HOME_PAGE;
    }


    /**
     * Validates passed name, login, email, password for emptiness
     *
     * @param name {@link String} user name
     * @param login {@link String} user login
     * @param email {@link String} user email
     * @param password {@link String} user password
     * @param locale {@link String} language for error messages
     * @param session current {@link HttpSession} session
     * @return true if and only if strings passed validation, otherwise - false
     */
    private boolean validateEmptyInput(String name, String login, String email, String password,
                                       String locale, HttpSession session) {

        if (new Validator().empty(name, login, email, password)) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.FIELDS_CANNOT_BE_EMPTY);
            session.setAttribute(ErrorMessageConstants.ERROR_REG_MESSAGE, errorMessage);
            return false;
        }

        return true;
    }


    /**
     * Registers user by given name, login, email, password
     * and create new {@link User} in database
     *
     * @param name {@link String} user name
     * @param login {@link String} user login
     * @param email {@link String} user email
     * @param password {@link String} user password
     * @param locale {@link String} language for error messages
     * @return user if he/she was found in database
     * @throws ValidatorException if {@link User} object's data fails validation
     */
    private User register(String name, String login, String email, String password, String locale) throws ValidatorException {

        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        User user = new User(name, login, password, email, false);

        return service.create(user);
    }
}
