package com.epam.bookshop.command.impl.page_and_action;

import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.constant.RouteConstants;
import com.epam.bookshop.exception.DqlException;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.mail.MailSender;
import com.epam.bookshop.validator.impl.DqlExceptionMessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Registers in application
 */
public class RegisterCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(RegisterCommand.class);

    private static final String REGISTER_USER_SUBJECT = "Registration completed";
    private static final String REGISTER_RESPONSE = "You have successfully registered!";

    @Override
    public CommandResult execute(RequestContext requestContext) {

        final String name = requestContext.getParameter(RequestConstants.NAME);
        final String login = requestContext.getParameter(RequestConstants.LOGIN);
        final String email = requestContext.getParameter(RequestConstants.EMAIL);
        final String password = requestContext.getParameter(RequestConstants.PASSWORD);

        final HttpSession session = requestContext.getSession();
        String locale = (String) requestContext.getSession().getAttribute(RequestConstants.LOCALE);

        User user = null;
        try {
            user = register(name, login, email, password, locale);

            MailSender.getInstance().send(email, REGISTER_USER_SUBJECT, REGISTER_RESPONSE);

            session.setAttribute(RequestConstants.LOGIN, login);
            session.setAttribute(RequestConstants.ROLE, RequestConstants.USER_ROLE);
            session.setAttribute(RequestConstants.NEED_TO_LINK_BANK_ACCOUNT, true);

        } catch (ValidatorException e) {
            session.setAttribute(ErrorMessageConstants.ERROR_REG_MESSAGE, e.getMessage());
            logger.error(e.getMessage(), e);
            return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.ACCOUNT.getRoute());
        } catch (MessagingException e) {
            session.setAttribute(ErrorMessageConstants.ERROR_REG_MESSAGE,
                    ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.COULD_NOT_REACH_EMAIL_ADDRESS)
                            + UtilStringConstants.NEW_LINE + email);
            try {
                EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
                service.setLocale(locale);
                service.delete(user);
            } catch (EntityNotFoundException | ValidatorException e1) {
                logger.error(e1.getMessage(), e1);
            }
            logger.error(e.getMessage(), e);
            return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.ACCOUNT.getRoute());
        } catch (DqlException e) {
            DqlExceptionMessageProcessor dqlExceptionMessageProcessor = new DqlExceptionMessageProcessor();
            dqlExceptionMessageProcessor.setLocale(locale);
            session.setAttribute(ErrorMessageConstants.ERROR_REG_MESSAGE,
                    dqlExceptionMessageProcessor.process(e));
            logger.error(e.getMessage(), e);
            return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.ACCOUNT.getRoute());
        }

        if (Objects.nonNull(session.getAttribute(RequestConstants.BACK_TO_CART))) {
            session.removeAttribute(RequestConstants.BACK_TO_CART);
            return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.CART.getRoute());
        }

        return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.HOME.getRoute());
    }


    /**
     * Registers user by given name, login, email, password
     * and create new {@link User} in database
     *
     * @param name {@link String} user name
     * @param login {@link String} user login
     * @param email {@link String} user email
     * @param password {@link String} user password
     * @param locale {@link String} language for error messages
     * @return user if he/she was found in database
     * @throws ValidatorException if {@link User} object's data fails validation
     */
    public static User register(String name, String login, String email, String password, String locale) throws ValidatorException, DqlException {
        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        return service.create(new User(name, login, password, email, false));
    }
}
