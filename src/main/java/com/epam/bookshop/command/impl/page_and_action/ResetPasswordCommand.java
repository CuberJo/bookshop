package com.epam.bookshop.command.impl.page_and_action;

import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.*;
import com.epam.bookshop.mail.MailSender;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.util.manager.language.MessageManager;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Resets {@link User} user password and sets new one
 */
public class ResetPasswordCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordCommand.class);

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext FORGOT_PASSWORD_PAGE = () -> "/home?command=forgot_password";

    private static final String PASSWORD_RESET_SUBJECT = "Password reset";
    private static final String EMAIL_RESPONSE = "email_response";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();
        final String locale = (String) requestContext.getSession().getAttribute(RequestConstants.LOCALE);
        final String email = requestContext.getParameter(RequestConstants.EMAIL);

        try {
            EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
            service.setLocale(locale);

            Optional<User> optionalUser = service.find(UserCriteria.builder().email(email).build());
            if (optionalUser.isEmpty()) {
                session.setAttribute(ErrorMessageConstants.ERROR_MESSAGE,
                        ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMAIL_NOT_FOUND_IN_DATABASE)
                                + UtilStringConstants.NEW_LINE + email);
                return FORGOT_PASSWORD_PAGE;
            }
            reset(locale, optionalUser.get(), email);

        } catch (ValidatorException | MessagingException e) {
            session.setAttribute(ErrorMessageConstants.ERROR_MESSAGE, e.getMessage());
            logger.error(e.getMessage(), e);
            return FORGOT_PASSWORD_PAGE;
        }

        return HOME_PAGE;
    }


    /**
     * Resets user password by creating new random password
     *
     * @param locale {@link String} language for error messages
     * @param user {@link User} user to update
     * @param email email to send new password
     * @throws ValidatorException if user data fails validation
     * @throws MessagingException if exception occurred while sending email
     */
    private void reset(String locale, User user, String email) throws ValidatorException, MessagingException {
        final int len = 30;
        final int randNumOrigin = 48, randNumBound = 122;

        String newPass = PasswordCreator.getInstance().generateRandomPassword(len, randNumOrigin, randNumBound);
        user.setPassword(BCrypt.hashpw(newPass, BCrypt.gensalt()));

        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);
        service.update(user);

        MailSender.getInstance().send(email, PASSWORD_RESET_SUBJECT,
                String.format(MessageManager.valueOf(locale).getMessage(EMAIL_RESPONSE), newPass));
    }
}
