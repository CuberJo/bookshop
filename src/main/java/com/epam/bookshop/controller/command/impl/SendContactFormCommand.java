package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.util.MailSender;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

public class SendContactFormCommand implements Command {

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext CONTACT_US_PAGE = () -> "/home?command=contact_us";

    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String SUBJECT = "subject";
    private static final String ERROR_MESSAGE = "error_contact_us_message";
    private static final String FIELDS_CANNOT_BE_EMPTY = "fields_cannot_be_empty";
    private static final String INVALID_INPUT_DATA = "invalid_input_data";
    private static final String LOCALE_ATTR = "locale";
    private static final String AUTO_REPLY_SUBJECT = "Auto reply";
    private static final String RESPONSE = "We have received your message. Our stuff will process it as soon as possible";
    private static final String COULD_NOT_REACH_EMAIL_ADDRESS = "could_not_reach_email_address";
    private static final String NEW_LINE = "\n";

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

            MailSender.getInstance().send(email, AUTO_REPLY_SUBJECT, RESPONSE);

            System.out.println("sanitizedName: " + sanitizedName + ", sanitizedEmail: " + sanitizedEmail + ", sanitizedSubject: " + sanitizedSubject);

            //todo make stuff with sanitized data

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(INVALID_INPUT_DATA);
            session.setAttribute(ERROR_MESSAGE, errorMessage);
            e.printStackTrace();
            return CONTACT_US_PAGE;
        } catch (MessagingException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(COULD_NOT_REACH_EMAIL_ADDRESS) + NEW_LINE + email;
            session.setAttribute(ERROR_MESSAGE, errorMessage);
            e.printStackTrace();
            return CONTACT_US_PAGE;
        }

        return HOME_PAGE;
    }
}
