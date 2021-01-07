package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class ContactUsCommand implements Command {

//    private static final ResponseContext ABOUT_PAGE = () -> "/WEB-INF/jsp/contact_us.jsp";

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext CONTACT_US_PAGE = () -> "/home?command=contact_us";

    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String SUBJECT = "subject";
    private static final String ERROR_MESSAGE = "error_log_message";
    private static final String EMPTY_STRING = "";
    private static final String EMPTY_STRING_REGEX = "^[\\s]+$";
    private static final String FIELDS_CANNOT_BE_EMPTY = "fields_cannot_be_empty";
    private static final String INCORRECT_LOGIN_OR_PASSWORD = "incorrect_login_or_password";
    private static final String INVALID_INPUT_DATA = "invalid_input_data";
    private static final String LOCALE_ATTR = "locale";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final String name = requestContext.getParameter(NAME);
        final String email = requestContext.getParameter(EMAIL);
        final String subject = requestContext.getParameter(SUBJECT);

        System.out.println("NAME=" + requestContext.getParameter(NAME));
        System.out.println("EMAIL=" + requestContext.getParameter(EMAIL));
        System.out.println("SUBJECT=" + requestContext.getParameter(SUBJECT));

        final HttpSession session = requestContext.getSession();
        String locale = (String) requestContext.getSession().getAttribute(LOCALE_ATTR);
        Validator validator = new Validator();
        String errorMessage = "";


        try {
            if (!validator.emptyStringValidator(name, email, subject)) {
                System.out.println("!validateInput(name, email, subject)");
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(FIELDS_CANNOT_BE_EMPTY);
                session.setAttribute(ERROR_MESSAGE, errorMessage);
                return CONTACT_US_PAGE;
            }

            validator.validateEmail(email);
            String sanitizedName = validator.sanitizeString(name);
            String sanitizedEmail = validator.sanitizeString(email);
            String sanitizedSubject = validator.sanitizeString(subject);



            //todo make stuff with sanitized data

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(INVALID_INPUT_DATA);
            session.setAttribute(ERROR_MESSAGE, errorMessage);
            e.printStackTrace();
            return CONTACT_US_PAGE;
        }

        return HOME_PAGE;
    }
}
