package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.constant.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;
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
        User userToUpdate = findUser(criteria, locale);


        if (!validateEmptyInput(login, email, password, verifyPassword, checkPass, session, locale) ||
            !passesEqual(password, verifyPassword, session, locale) ||
            !checkPassCorrect(checkPass, userToUpdate.getPassword(), session, locale)) {
            return;
        }

        update(login, email, password, userToUpdate, session, locale);
    }


    /**
     * Validates passed strings for emptiness
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

        if (!validator.emptyStringValidator(checkPassword)) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMPTY_CHECK_PASS);
            session.setAttribute(ErrorMessageConstants.ERROR_ACC_SETTINGS, errorMessage);
            return false;
        }

        if (!validator.emptyStringValidator(login) &&
                !validator.emptyStringValidator(email) &&
                !validator.emptyStringValidator(password) &&
                !validator.emptyStringValidator(verifyPassword)) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.FIELDS_CANNOT_BE_EMPTY);
            session.setAttribute(ErrorMessageConstants.ERROR_ACC_SETTINGS, errorMessage);
            return false;
        }

        return true;
    }


    /**
     * Looks for {@link User} by certain {@link Criteria<User>} criteria
     * @param locale language for error messages
     * @return {@link User} object if it is found
     */
    private User findUser(Criteria<User> criteria, String locale) {
        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        User user = null;

        try {
            Optional<User> optionalUser = service.find(criteria);
            if (optionalUser.isEmpty()) {
                String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND)
                        + ((UserCriteria) criteria).getLogin();
                throw new RuntimeException(error);
            }

            user = optionalUser.get();
        } catch (ValidatorException e) {
            logger.error(e.getMessage(), e);
        }

        return user;
    }


    /**
     * Updates {@link User} object by given data
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
