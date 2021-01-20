package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;
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


    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final String login = requestContext.getParameter(UtilStrings.LOGIN);
        final String password = requestContext.getParameter(UtilStrings.PASSWORD);

        final HttpSession session = requestContext.getSession();
        String locale = (String) requestContext.getSession().getAttribute(UtilStrings.LOCALE);
        String errorMessage = "";


        try {
            if (!new Validator().emptyStringValidator(login, password)) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.FIELDS_CANNOT_BE_EMPTY);
                session.setAttribute(ErrorMessageConstants.ERROR_LOG_MESSAGE, errorMessage);
                return ACCOUNT_PAGE;
            }

            Optional<User> optionalUser = authenticate(login, password, locale);
            if (optionalUser.isEmpty()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INCORRECT_LOGIN_OR_PASSWORD);
                session.setAttribute(ErrorMessageConstants.ERROR_LOG_MESSAGE, errorMessage);
                return ACCOUNT_PAGE;
            }


            session.setAttribute(UtilStrings.LOGIN, login);
            session.setAttribute(UtilStrings.ROLE, optionalUser.get().getRole().getRole());

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
            session.setAttribute(ErrorMessageConstants.ERROR_LOG_MESSAGE, e.getMessage());
            logger.error(errorMessage, e);
            return ACCOUNT_PAGE;
        }

        if (Objects.nonNull(requestContext.getSession().getAttribute(UtilStrings.BACK_TO_CART))) {
            requestContext.getSession().removeAttribute(UtilStrings.BACK_TO_CART);
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
     * @throws ValidatorException if {@link com.epam.bookshop.criteria.Criteria<User>} object fails validation
     */
    private Optional<User> authenticate(String login, String password, String locale) throws ValidatorException {

        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        UserCriteria criteria = UserCriteria.builder()
                .login(login)
                .build();

        Optional<User> optionalUser = service.find(criteria);

        if (optionalUser.isEmpty() || !BCrypt.checkpw(password, optionalUser.get().getPassword())) {
            return Optional.empty();
        }

        return optionalUser;
    }
}
