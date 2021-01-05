package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.util.MailSender;
import com.epam.bookshop.util.PasswordCreator;
import com.epam.bookshop.validator.Validator;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;

public class ResetPasswordCommand implements Command {

    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/home.jsp";
    private static final ResponseContext FORGOT_PASSWORD_PAGE = () -> "/WEB-INF/jsp/forgot_password.jsp";

    private static final String PASSWORD_RESET_SUBJECT = "<h4>Password reset<h4>";
    private static final String EMAIL = "email";
    private static final String ERROR_MESSAGE = "error_message";


    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final int len = 30;
        final int randNumOrigin = 48, randNumBound = 122;

        final String email = requestContext.getParameter(EMAIL);

        final HttpSession session = requestContext.getSession();

        try {
            if (!Validator.getInstance().emptyStringValidator(email)) {
                System.out.println("!validateInput(name, email, password, login)");
                session.setAttribute(ERROR_MESSAGE, "Fields cannot be empty");
                return FORGOT_PASSWORD_PAGE;
            }

            Validator.getInstance().validateEmail(email);

            String newPassword = PasswordCreator.getInstance().generateRandomPassword(len, randNumOrigin, randNumBound);
            String response = String.format("<h5>Hello!<h5>\n" +
                    "You are receiving this email because we received a password reset request for your account\n" +
                    "Your new password is <b>%s<b>", newPassword);

            MailSender.getInstance().send(email, PASSWORD_RESET_SUBJECT, response);

        } catch (ValidatorException e) {
            session.setAttribute(ERROR_MESSAGE, "Invalid input data");
            e.printStackTrace();
            return FORGOT_PASSWORD_PAGE;
        } catch (MessagingException e) {
            session.setAttribute(ERROR_MESSAGE, "Email not found in database");
            e.printStackTrace();
            return FORGOT_PASSWORD_PAGE;
        }

        return HOME_PAGE;
    }
}
