package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.mail.MailSender;
import com.epam.bookshop.constant.RegexConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.validator.impl.StringSanitizer;
import com.epam.bookshop.validator.impl.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

/**
 * Processes sending contact form
 */
public class SendContactFormCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(SendContactFormCommand.class);

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext CONTACT_US_PAGE = () -> "/home?command=contact_us";

    private static final String SUBJECT = "subject";
    private static final String AUTO_REPLY_SUBJECT = "Auto reply";
    private static final String RESPONSE = "We have received your message. Our stuff will process it as soon as possible";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final String name = requestContext.getParameter(RequestConstants.NAME);
        final String email = requestContext.getParameter(RequestConstants.EMAIL);
        final String subject = requestContext.getParameter(SUBJECT);

        final HttpSession session = requestContext.getSession();
        String locale = (String) requestContext.getSession().getAttribute(RequestConstants.LOCALE);
        StringSanitizer sanitizer = new StringSanitizer();
        String errorMessage = "";


        try {
            if (StringValidator.getInstance().empty(name, email, subject)) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.FIELDS_CANNOT_BE_EMPTY);
                session.setAttribute(ErrorMessageConstants.ERROR_CONTACT_US_MESSAGE, errorMessage);
                return CONTACT_US_PAGE;
            }
            if(!StringValidator.getInstance().validate(email, RegexConstants.EMAIL_REGEX)) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMAIL_INCORRECT)
                        + UtilStringConstants.WHITESPACE + email;
                session.setAttribute(ErrorMessageConstants.ERROR_CONTACT_US_MESSAGE, errorMessage);
                logger.error(errorMessage);
                return CONTACT_US_PAGE;
            }

            String sanitizedName = sanitizer.sanitize(name);
            String sanitizedEmail = sanitizer.sanitize(email);
            String sanitizedSubject = sanitizer.sanitize(subject);

            MailSender.getInstance().send(email, AUTO_REPLY_SUBJECT, RESPONSE);

            System.out.println("sanitizedName: " + sanitizedName + ", sanitizedEmail: " + sanitizedEmail + ", sanitizedSubject: " + sanitizedSubject);

            //todo make stuff with sanitized data

        } catch (MessagingException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.COULD_NOT_REACH_EMAIL_ADDRESS) + UtilStringConstants.NEW_LINE + email;
            session.setAttribute(ErrorMessageConstants.ERROR_CONTACT_US_MESSAGE, errorMessage);
            logger.error(errorMessage, e);
            return CONTACT_US_PAGE;
        }

        return HOME_PAGE;
    }
}
