package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.util.validator.impl.Validator;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Optional;

/**
 * Updates user account data
 */
@WebServlet("/account_settings")
public class AccountSettingsController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AccountSettingsController.class);


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        final String login = request.getParameter(UtilStrings.LOGIN);
        final String email = request.getParameter(UtilStrings.EMAIL);
        final String password = request.getParameter(UtilStrings.PASSWORD);
        final String verifyPassword = request.getParameter(UtilStrings.VERIFY_PASSWORD);
        final String checkPass = request.getParameter(UtilStrings.CHECK_PASSWORD);

        final HttpSession session = request.getSession();
        String locale = (String) session.getAttribute(UtilStrings.LOCALE);


        UserCriteria criteria = UserCriteria.builder()
                .login((String) session.getAttribute(UtilStrings.LOGIN))
                .build();
        User userToUpdate = EntityFinder.getInstance().find(criteria, logger, locale);


        if (!validateEmptyInput(login, email, password, verifyPassword, checkPass, session, locale) ||
            !passesEqual(password, verifyPassword, session, locale) ||
            !checkPassCorrect(checkPass, userToUpdate.getPassword(), session, locale)) {
            return;
        }

        update(login, email, password, userToUpdate, session, locale);
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
    private boolean validateEmptyInput(String login, String email, String password, String verifyPassword,
                                       String checkPassword, HttpSession session, String locale) {

        Validator validator = new Validator();
        String errorMessage = "";

        if (validator.empty(checkPassword)) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMPTY_CHECK_PASS);
            session.setAttribute(ErrorMessageConstants.ERROR_ACC_SETTINGS, errorMessage);
            return false;
        }

        if (validator.empty(login) &&
                validator.empty(email) &&
                validator.empty(password) &&
                validator.empty(verifyPassword)) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.FIELDS_CANNOT_BE_EMPTY);
            session.setAttribute(ErrorMessageConstants.ERROR_ACC_SETTINGS, errorMessage);
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
            session.setAttribute(UtilStrings.LOGIN, login);
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
        } catch (ValidatorException e) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
            session.setAttribute(ErrorMessageConstants.ERROR_ACC_SETTINGS, error);
            logger.error(error);
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
                    ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.PASSWORDS_NOT_EQUAL));
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
