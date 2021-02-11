package com.epam.bookshop.command.impl.page_and_action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.*;
import com.epam.bookshop.mail.MailSender;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.validator.impl.EmptyStringValidator;
import com.epam.bookshop.validator.impl.RegexValidator;
import com.epam.bookshop.validator.impl.StringSanitizer;
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

    private static final String SUBJECT = "subject";
    private static final String AUTO_REPLY_SUBJECT = "Auto reply";
    private static final String RESPONSE = "We have received your message. Our stuff will process it as soon as possible";

    @Override
    public CommandResult execute(RequestContext requestContext) {
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
                return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.CONTACT_US.getRoute());
            }
            if(!RegexValidator.getInstance().validate(email, RegexConstants.STRONG_EMAIL_REGEX)) {
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMAIL_INCORRECT)
                        + UtilStringConstants.WHITESPACE + email;
                session.setAttribute(ErrorMessageConstants.ERROR_CONTACT_US_MESSAGE, errorMessage);
                logger.error(errorMessage);
                return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.CONTACT_US.getRoute());
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
            return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.CONTACT_US.getRoute());
        }

        return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.HOME.getRoute());
    }
}
