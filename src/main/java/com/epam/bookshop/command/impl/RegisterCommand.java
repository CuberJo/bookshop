package com.epam.bookshop.command.impl;

import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.mail.MailSender;
import com.epam.bookshop.validator.impl.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Registers in application
 */
public class RegisterCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(RegisterCommand.class);

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext ACCOUNT_PAGE = () -> "/home?command=account";
    private static final ResponseContext CART_PAGE = () -> "/home?command=cart";

    private static final String REGISTER_USER_SUBJECT = "Registration completed";
    private static final String REGISTER_RESPONSE = "You have successfully registered!";

    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final String name = requestContext.getParameter(RequestConstants.NAME);
        final String login = requestContext.getParameter(RequestConstants.LOGIN);
        final String email = requestContext.getParameter(RequestConstants.EMAIL);
        final String password = requestContext.getParameter(RequestConstants.PASSWORD);

        final HttpSession session = requestContext.getSession();
        String locale = (String) requestContext.getSession().getAttribute(RequestConstants.LOCALE);
        String errorMessage = "";

        User user = null;

        try {

            user = register(name, login, email, password, locale);

            MailSender.getInstance().send(email, REGISTER_USER_SUBJECT, REGISTER_RESPONSE);

            session.setAttribute(RequestConstants.LOGIN, login);
            session.setAttribute(RequestConstants.ROLE, RequestConstants.USER_ROLE);
            session.setAttribute(RequestConstants.NEED_TO_LINK_BANK_ACCOUNT, true);

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
            session.setAttribute(ErrorMessageConstants.ERROR_REG_MESSAGE, errorMessage);
            logger.error(e.getMessage(), e);
            return ACCOUNT_PAGE;
        } catch (MessagingException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.COULD_NOT_REACH_EMAIL_ADDRESS) + UtilStringConstants.NEW_LINE + email;
            session.setAttribute(ErrorMessageConstants.ERROR_REG_MESSAGE, errorMessage);
            try {
                EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
                service.setLocale(locale);
                UserCriteria criteria = UserCriteria.builder()
                        .login(user.getLogin())
                        .build();
                long userId = service.find(criteria).get().getEntityId();
                service.delete(userId);
            } catch (EntityNotFoundException | ValidatorException e1) {
                logger.error(e1.getMessage(), e1);
            }
            logger.error(e.getMessage(), e);
            return ACCOUNT_PAGE;
        }

        if (Objects.nonNull(requestContext.getSession().getAttribute(RequestConstants.BACK_TO_CART))) {
            requestContext.getSession().removeAttribute(RequestConstants.BACK_TO_CART);
            return CART_PAGE;
        }

        return HOME_PAGE;
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
