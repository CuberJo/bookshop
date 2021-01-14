package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.util.UtilStrings;
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@WebServlet("/account_settings")
public class AccountSettingsController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AccountSettingsController.class);

    private static final String ERROR_ACC_SETTINGS = "error_acc_settings";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        final String login = request.getParameter(UtilStrings.LOGIN);
        final String email = request.getParameter(UtilStrings.EMAIL);
        final String password = request.getParameter(UtilStrings.PASSWORD);
        final String verifyPassword = request.getParameter(UtilStrings.VERIFY_PASSWORD);
        final String checkPass = request.getParameter(UtilStrings.CHECK_PASSWORD);

        final HttpSession session = request.getSession();
        String locale = (String) session.getAttribute(UtilStrings.LOCALE);

        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        if (!validateEmptyInput(login, email, password, verifyPassword, checkPass, request, locale)) {
            return;
        }

        if (!password.equals(verifyPassword)) {
            session.setAttribute(ERROR_ACC_SETTINGS,
                    ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.PASSWORDS_NOT_EQUAL));
            return;
        }

        User userToUpdate = findUser(request, service, locale);
        if (!userToUpdate.getPassword().equals(BCrypt.hashpw(checkPass, BCrypt.gensalt()))) {
            session.setAttribute(ERROR_ACC_SETTINGS,
                    ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.PASSWORDS_NOT_EQUAL));
            return;
        }

        updateUserData(login, email, password, userToUpdate, service, request, locale);
    }



    private boolean validateEmptyInput(String login, String email, String password, String verifyPassword,
                                       String checkPassword, HttpServletRequest request, String locale) {

        if (!new Validator().emptyStringValidator(login, email, password, verifyPassword, checkPassword)) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.FIELDS_CANNOT_BE_EMPTY);
            request.getSession().setAttribute(ERROR_ACC_SETTINGS, errorMessage);
            return false;
        }

        return true;
    }



    private User findUser(HttpServletRequest request, EntityService service, String locale) {
        User user = null;

        try {
            String login = (String) request.getSession().getAttribute(UtilStrings.LOGIN);
            Optional<User> optionalUser = service.find(UserCriteria.builder().login(login).build());
            if (optionalUser.isEmpty()) {
                String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND) + login;
                throw new RuntimeException(error);
            }

            user = optionalUser.get();
        } catch (ValidatorException e) {
            logger.error(e.getMessage(), e);
        }

        return user;
    }



    private void updateUserData(String login, String email, String password, User userToUpdate,
                                UserService service, HttpServletRequest request, String locale) {

        if (Objects.nonNull(locale)) {
            userToUpdate.setLogin(login);
        }
        if (Objects.nonNull(email)) {
            userToUpdate.setEmail(email);
        }
        if (Objects.nonNull(password)) {
            userToUpdate.setPassword(password);
        }

        try {
            service.update(userToUpdate);
        } catch (ValidatorException e) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
            request.getSession().setAttribute(ERROR_ACC_SETTINGS, error);
            logger.error(error);
        }
    }
}
