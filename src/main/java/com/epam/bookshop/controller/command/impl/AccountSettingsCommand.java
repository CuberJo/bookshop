package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.domain.impl.Regex;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class AccountSettingsCommand implements Command {

    private static final ResponseContext PERSONAL_PAGE = () -> "/home?command=personal_page";

    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final HttpSession session = requestContext.getSession();
        String locale = (String) session.getAttribute(UtilStrings.LOCALE);

        updateSettings(requestContext, locale);

        return null;
    }



    private void updateSettings(RequestContext requestContext, String locale) {

        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        User userToUpdate = findUser(requestContext, service, locale);

        Map<String, String> userDataToUpdate = getUserDataToUpdate(requestContext);


        service.update()
    }



    private User findUser(RequestContext requestContext, EntityService service, String locale) {
        User user = null;

        try {
            String login = (String) requestContext.getSession().getAttribute(UtilStrings.LOGIN);
            Optional<User> optionalUser = service.find(UserCriteria.builder().login(login).build());
            if (optionalUser.isEmpty()) {
                String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND) + login;
                throw new RuntimeException(error);
            }

            user = optionalUser.get();
        } catch (ValidatorException e) {
            e.printStackTrace();
        }

        return user;
    }



    private User updateUserData(User userToUpdate, RequestContext requestContext) {

        String login = requestContext.getParameter(UtilStrings.LOGIN);
        String email = requestContext.getParameter(UtilStrings.EMAIL);
        String password = requestContext.getParameter(UtilStrings.PASSWORD);
        String verifyPassword = requestContext.getParameter(UtilStrings.VERIFY_PASSWORD);


        validateUserData();

        if (Objects.nonNull(login)) {
            userToUpdate.setLogin(login);
        }
        if (Objects.nonNull(email)) {
            userToUpdate.setEmail(email);
        }
        if (Objects.nonNull(password)) {
            userToUpdate.setPassword(password);
        }
        if (Objects.nonNull(verifyPassword)) {
            userToUpdate.s verifyPassword);
        }

        return userToUpdate;
    }


    private void validateUserData(String login, String email, String pass, String verifyPass, String locale) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);

        validator.emptyStringValidator(login, email, pass, verifyPass);

        validator.validateString(login, Regex.LOGIN_REGEX, ErrorMessageConstants.LOGIN_INCORRECT);
        validator.validateString(email, EMA, ErrorMessageConstants.EMAIL_INCORRECT);
    }
}
