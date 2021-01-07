package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.MailSender;
import com.epam.bookshop.util.PasswordCreator;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.util.manager.MessageManager;
import com.epam.bookshop.validator.Validator;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Optional;

public class ResetPasswordCommand implements Command {

//    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/home.jsp";
//    private static final ResponseContext FORGOT_PASSWORD_PAGE = () -> "/WEB-INF/jsp/forgot_password.jsp";

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext FORGOT_PASSWORD_PAGE = () -> "/home?command=forgot_password";

    private static final String PASSWORD_RESET_SUBJECT = "Password reset";
    private static final String EMAIL = "email";
    private static final String ERROR_MESSAGE = "error_message";
    private static final String LOCALE_ATTR = "locale";
    private static final String FIELDS_CANNOT_BE_EMPTY = "fields_cannot_be_empty";
    private static final String EMAIL_NOT_FOUND_IN_DATABASE = "email_not_found_in_database";
    private static final String EMAIL_RESPONSE = "email_response";
    private static final String INVALID_INPUT_DATA = "invalid_input_data";
    private static final String NEW_LINE = "\n";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final int len = 30;
        final int randNumOrigin = 48, randNumBound = 122;

        final String email = requestContext.getParameter(EMAIL);

        final HttpSession session = requestContext.getSession();
        String locale = (String) requestContext.getSession().getAttribute(LOCALE_ATTR);
        String errorMessage = "";
        Validator validator = new Validator();

        try {
            if (!validator.emptyStringValidator(email)) {
                System.out.println("!validateInput(name, email, password, login)");
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(FIELDS_CANNOT_BE_EMPTY);
                session.setAttribute(ERROR_MESSAGE, errorMessage);
                return FORGOT_PASSWORD_PAGE;
            }

            validator.validateEmail(email);

            EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);

            Optional<User> optionalUser = service.find(UserCriteria.builder().email(email).build());
            if (optionalUser.isEmpty()) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(EMAIL_NOT_FOUND_IN_DATABASE) + NEW_LINE + email;
                session.setAttribute(ERROR_MESSAGE, errorMessage);
                return FORGOT_PASSWORD_PAGE;
            }

            String newPassword = PasswordCreator.getInstance().generateRandomPassword(len, randNumOrigin, randNumBound);
            String response = String.format(MessageManager.valueOf(locale).getMessage(EMAIL_RESPONSE), newPassword);

            optionalUser.get().setPassword(newPassword);
            service.update(optionalUser.get());

            MailSender.getInstance().send(email, PASSWORD_RESET_SUBJECT, response);

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(INVALID_INPUT_DATA);
            session.setAttribute(ERROR_MESSAGE, errorMessage);
            e.printStackTrace();
            return FORGOT_PASSWORD_PAGE;
        } catch (MessagingException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(EMAIL_NOT_FOUND_IN_DATABASE) + NEW_LINE + email;
            session.setAttribute(ERROR_MESSAGE, errorMessage);
            e.printStackTrace();
            return FORGOT_PASSWORD_PAGE;
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        return HOME_PAGE;
    }
}
