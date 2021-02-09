package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.EntityFinderFacade;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.validator.impl.EmptyStringValidator;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Updates user account data
 */
public class AccountSettingsCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(AccountSettingsCommand.class);

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final String login = requestContext.getParameter(RequestConstants.LOGIN);
        final String email = requestContext.getParameter(RequestConstants.EMAIL);
        final String password = requestContext.getParameter(RequestConstants.PASSWORD);
        final String verifyPassword = requestContext.getParameter(RequestConstants.VERIFY_PASSWORD);
        final String checkPass = requestContext.getParameter(RequestConstants.CHECK_PASSWORD);

        final HttpSession session = requestContext.getSession();
        final String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        User userToUpdate = EntityFinderFacade.getInstance().findUserInSession(session, logger);

        if (!validateEmptyInput(login, email, password, verifyPassword, checkPass, session, locale)
                || !passesEqual(password, verifyPassword, session, locale)
                || !checkPassCorrect(checkPass, userToUpdate.getPassword(), session, locale)) {
            return new CommandResult(CommandResult.ResponseType.TEXT_PLAIN, ErrorMessageConstants.INVALID_INPUT_DATA);
        }
        update(login, email, password, userToUpdate, session, locale);

        return new CommandResult(CommandResult.ResponseType.NO_ACTION, UtilStringConstants.EMPTY_STRING);
    }


    /**
     * Validates passed strings for emptiness
     *
     * @param login login string to validate
     * @param email email string to validate
     * @param password password string to validate
     * @param verifyPassword verify string to validate
     * @param checkPassword checkPassword string to validate
     * @param session current {@link HttpSession} session used to set attributes
     * @param locale {@link String} language for error messages
     * @return true if and only if strings passed validation, otherwise - false
     */
    private boolean validateEmptyInput(String login, String email, String password,
                                       String verifyPassword, String checkPassword, HttpSession session, String locale) {

        EmptyStringValidator validator = EmptyStringValidator.getInstance();

        if (validator.empty(checkPassword)) {
            session.setAttribute(ErrorMessageConstants.ERROR_ACC_SETTINGS,
                    ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMPTY_CHECK_PASS));
            return false;
        }

        if (validator.empty(login)
                && validator.empty(email)
                && validator.empty(password)
                && validator.empty(verifyPassword)) {
            session.setAttribute(ErrorMessageConstants.ERROR_ACC_SETTINGS,
                    ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.FIELDS_CANNOT_BE_EMPTY));
            return false;
        }

        return true;
    }


    /**
     * Updates {@link User} object by given data
     *
     * @param login {@link String} login to be set if not null
     * @param email {@link String} email to be set if not null
     * @param password {@link String} password to be set if not null
     * @param userToUpdate {@link User} to be updated
     * @param session current {@link HttpSession} session used to set attributes
     * @param locale {@link String} language for error messages
     */
    private void update(String login, String email, String password, User userToUpdate,
                        HttpSession session, String locale) {
        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        if (Objects.nonNull(login) && !login.isEmpty()) {
            userToUpdate.setLogin(login);
        }
        if (Objects.nonNull(email) && !email.isEmpty()) {
            userToUpdate.setEmail(email);
        }
        if (Objects.nonNull(password) && !password.isEmpty()) {
            String hashpw = BCrypt.hashpw(password, BCrypt.gensalt());
            userToUpdate.setPassword(hashpw);
        }

        try {
            service.update(userToUpdate);
            if (Objects.nonNull(login) && !login.isEmpty()) {
                session.setAttribute(RequestConstants.LOGIN, login);
            }
        } catch (ValidatorException e) {
            session.setAttribute(ErrorMessageConstants.ERROR_ACC_SETTINGS, e.getMessage());
            logger.error(e.getMessage());
        }
    }


    /**
     * Checks whether entered check password is correct, so we can update {@link User} user
     *
     * @param checkPass {@link String} password from user input
     * @param userPass {@link String} hashed {@link User} password stored in database
     * @param session current {@link HttpSession} session used to set attributes
     * @param locale {@link String} language for error messages
     * @return true if and only if passwords are equal, otherwise - false
     */
    private boolean checkPassCorrect(String checkPass, String userPass, HttpSession session, String locale) {
        if (!BCrypt.checkpw(checkPass, userPass)) {
            session.setAttribute(ErrorMessageConstants.ERROR_ACC_SETTINGS,
                    ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.ACCOUNT_PASSWORD_INCORRECT));
            return false;
        }

        return true;
    }


    /**
     * Checks whether passwords are equal
     *
     * @param password {@link String} first not hashed password
     * @param verifyPassword {@link String} second not hashed password
     * @param session current {@link HttpSession} session used to set attributes
     * @param locale {@link String} language for error messages
     * @return true if and only if passwords are equal, otherwise - false
     */
    private boolean passesEqual(String password, String verifyPassword, HttpSession session, String locale) {
        if ((!password.isEmpty() && !verifyPassword.isEmpty()) && !password.equals(verifyPassword)) {
            session.setAttribute(ErrorMessageConstants.ERROR_ACC_SETTINGS,
                    ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.PASSWORDS_NOT_EQUAL));
            return false;
        }

        return true;
    }
}
