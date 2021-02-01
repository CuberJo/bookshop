package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.FrontCommand;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.mail.MailSender;
import com.epam.bookshop.util.constant.RegexConstant;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.util.validator.impl.StringSanitizer;
import com.epam.bookshop.util.validator.impl.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

/**
 * processes sending contact form
 */
public class SendContactFormFrontCommand implements FrontCommand {
    private static final Logger logger = LoggerFactory.getLogger(SendContactFormFrontCommand.class);

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext CONTACT_US_PAGE = () -> "/home?command=contact_us";

    private static final String SUBJECT = "subject";
    private static final String AUTO_REPLY_SUBJECT = "Auto reply";
    private static final String RESPONSE = "We have received your message. Our stuff will process it as soon as possible";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final String name = requestContext.getParameter(UtilStrings.NAME);
        final String email = requestContext.getParameter(UtilStrings.EMAIL);
        final String subject = requestContext.getParameter(SUBJECT);

        final HttpSession session = requestContext.getSession();
        String locale = (String) requestContext.getSession().getAttribute(UtilStrings.LOCALE);
        Validator validator = new Validator();
        StringSanitizer sanitizer = new StringSanitizer();
        String errorMessage = "";


        try {
            if (validator.empty(name, email, subject)) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.FIELDS_CANNOT_BE_EMPTY);
                session.setAttribute(ErrorMessageConstants.ERROR_CONTACT_US_MESSAGE, errorMessage);
                return CONTACT_US_PAGE;
            }

            validator.validate(email, RegexConstant.EMAIL_REGEX, ErrorMessageConstants.EMAIL_INCORRECT);
            String sanitizedName = sanitizer.sanitize(name);
            String sanitizedEmail = sanitizer.sanitize(email);
            String sanitizedSubject = sanitizer.sanitize(subject);

            MailSender.getInstance().send(email, AUTO_REPLY_SUBJECT, RESPONSE);

            System.out.println("sanitizedName: " + sanitizedName + ", sanitizedEmail: " + sanitizedEmail + ", sanitizedSubject: " + sanitizedSubject);

            //todo make stuff with sanitized data

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
            session.setAttribute(ErrorMessageConstants.ERROR_CONTACT_US_MESSAGE, errorMessage);
            logger.error(errorMessage, e);
            return CONTACT_US_PAGE;
        } catch (MessagingException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.COULD_NOT_REACH_EMAIL_ADDRESS) + UtilStrings.NEW_LINE + email;
            session.setAttribute(ErrorMessageConstants.ERROR_CONTACT_US_MESSAGE, errorMessage);
            logger.error(errorMessage, e);
            return CONTACT_US_PAGE;
        }

        return HOME_PAGE;
    }
}
