package com.epam.bookshop.command.impl.page_and_action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.VerifyReCaptcha;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.validator.impl.EmptyStringValidator;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Optional;

/**
 * to login {@link User} to application
 */
public class LoginCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(LoginCommand.class);

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext ACCOUNT_PAGE = () -> "/home?command=account";
    private static final ResponseContext CART_PAGE = () -> "/home?command=cart";

    private static final String G_RECAPTCHA_RESPONSE = "g-recaptcha-response";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();
        final String locale = (String) requestContext.getSession().getAttribute(RequestConstants.LOCALE);

        final String login = requestContext.getParameter(RequestConstants.LOGIN);
        final String password = requestContext.getParameter(RequestConstants.PASSWORD);

        try {
            if (EmptyStringValidator.getInstance().empty(login, password)) {
                session.setAttribute(ErrorMessageConstants.ERROR_LOG_MESSAGE,
                        ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.FIELDS_CANNOT_BE_EMPTY));
                return ACCOUNT_PAGE;
            }
            if (!VerifyReCaptcha.getInstance().verify(requestContext.getParameter(G_RECAPTCHA_RESPONSE))) {
                session.setAttribute(ErrorMessageConstants.ERROR_LOG_MESSAGE,
                        ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.FAILED_RECAPTCHA));
                return ACCOUNT_PAGE;
            }

            Optional<User> optionalUser = authenticate(login, password, locale);
            if (optionalUser.isEmpty()) {
                session.setAttribute(ErrorMessageConstants.ERROR_LOG_MESSAGE,
                        ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INCORRECT_LOGIN_OR_PASSWORD));
                return ACCOUNT_PAGE;
            }
            session.setAttribute(RequestConstants.LOGIN, login);
            authorize(login, password, session);

        } catch (ValidatorException e) {
            session.setAttribute(ErrorMessageConstants.ERROR_LOG_MESSAGE, e.getMessage());
            logger.error(e.getMessage(), e);
            return ACCOUNT_PAGE;
        }

        if (Objects.nonNull(session.getAttribute(RequestConstants.BACK_TO_CART))) {
            session.removeAttribute(RequestConstants.BACK_TO_CART);
            return CART_PAGE;
        }

        return HOME_PAGE;
    }



    /**
     * Authenticates user by given login and password
     *
     * @param login login of user to be authenticated
     * @param password password of user to be authenticated
     * @param locale {@link String} language for error messages
     * @return user if he/she was found in database
     * @throws ValidatorException if {@link Criteria<User>} object fails validation
     */
    private Optional<User> authenticate(String login, String password, String locale) throws ValidatorException {
        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        Optional<User> optionalUser = service.find(UserCriteria.builder().login(login).build());

        if (optionalUser.isEmpty() || !BCrypt.checkpw(password, optionalUser.get().getPassword())) {
            return Optional.empty();
        }

        return optionalUser;
    }



    /**
     * Authorizes user by given login and password
     *
     * @param login login of user to be authenticated
     * @param password password of user to be authenticated
     */
    private void authorize(String login, String password, HttpSession session) {
        final String admin = "admin";
        final String adminPass = "admin";

        if (login.equals(admin) && password.equals(adminPass)) {
            session.setAttribute(RequestConstants.ROLE, RequestConstants.ADMIN_ROLE);
        } else {
            session.setAttribute(RequestConstants.ROLE, RequestConstants.USER_ROLE);
        }
    }
}
