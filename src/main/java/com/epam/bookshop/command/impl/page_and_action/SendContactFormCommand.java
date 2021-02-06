package com.epam.bookshop.command.impl.page_and_action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.mail.MailSender;
import com.epam.bookshop.constant.RegexConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.validator.impl.EmptyStringValidator;
import com.epam.bookshop.validator.impl.StringSanitizer;
import com.epam.bookshop.validator.impl.RegexValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

/**
 * Processes sending contact form with message from user.
 * Actually, nothing is done with user messages, he/she is
 * just informed, that their message has been received
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
        final String locale = (String) requestContext.getSession().getAttribute(RequestConstants.LOCALE);

        String errorMessage = "";
        try {
            if (EmptyStringValidator.getInstance().empty(name, email, subject)) {
                session.setAttribute(ErrorMessageConstants.ERROR_CONTACT_US_MESSAGE,
                        ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.FIELDS_CANNOT_BE_EMPTY));
                return CONTACT_US_PAGE;
            }
            if(!RegexValidator.getInstance().validate(email, RegexConstants.EMAIL_REGEX)) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMAIL_INCORRECT)
                        + UtilStringConstants.WHITESPACE + email;
                session.setAttribute(ErrorMessageConstants.ERROR_CONTACT_US_MESSAGE, errorMessage);
                logger.error(errorMessage);
                return CONTACT_US_PAGE;
            }

            StringSanitizer sanitizer = new StringSanitizer();
            String sanitizedName = sanitizer.sanitize(name);
            String sanitizedEmail = sanitizer.sanitize(email);
            String sanitizedSubject = sanitizer.sanitize(subject);

            MailSender.getInstance().send(email, AUTO_REPLY_SUBJECT, RESPONSE);

            logger.info("Message from: " + sanitizedName + ", Email: " + sanitizedEmail + "\n Subject: " + sanitizedSubject);

        } catch (MessagingException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.COULD_NOT_REACH_EMAIL_ADDRESS)
                    + UtilStringConstants.NEW_LINE + email;
            session.setAttribute(ErrorMessageConstants.ERROR_CONTACT_US_MESSAGE, errorMessage);
            logger.error(errorMessage, e);
            return CONTACT_US_PAGE;
        }

        return HOME_PAGE;
    }
}
