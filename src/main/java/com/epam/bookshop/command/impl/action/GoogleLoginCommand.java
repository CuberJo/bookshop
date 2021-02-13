package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.impl.page_and_action.RegisterCommand;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RegexConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.DqlException;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.mail.MailSender;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.EntityFinderFacade;
import com.epam.bookshop.util.IdTokenVerifierAndParser;
import com.epam.bookshop.util.PasswordCreator;
import com.epam.bookshop.util.Transliterator;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.util.DqlExceptionMessageProcessor;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Optional;

/**
 * Logins user with its Google account
 */
public class GoogleLoginCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(GoogleLoginCommand.class);

    private static final String TO_CART = "TO_CART";
    private static final String OK = "OK";
    private static final String FAIL = "FAIL";
    private static final String ID_TOKEN = "id_token";
    private static final String GIVEN_NAME = "given_name";

    private static final String LOGIN_USER_SUBJECT = "Signed in with Google account";
    private static final String LOGIN_RESPONSE = "<p>You have successfully signed in with your Google account!</p>";

    @Override
    public CommandResult execute(RequestContext requestContext) {
        HttpSession session = requestContext.getSession();
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);
        try {
            String idToken = requestContext.getParameter(ID_TOKEN);
            GoogleIdToken.Payload payLoad = IdTokenVerifierAndParser.getPayload(idToken);
            String name = (String) payLoad.get(GIVEN_NAME);

            if (Objects.nonNull(name) && name.matches(RegexConstants.CYRILLIC)) {
                name = Transliterator.getInstance().transliterate(name);
            }

            String email = payLoad.getEmail();

            final int len = 30;
            final int randNumOrigin = 48, randNumBound = 122;
            String password = PasswordCreator.getInstance().generateRandomPassword(len, randNumOrigin, randNumBound);

            Optional<User> optionalUser =
                    EntityFinderFacade.getInstance()
                            .findOptional(UserCriteria.builder().login(name).build(), logger, locale);
            User user = null;
            try {
                if (optionalUser.isEmpty()) {
                    user = RegisterCommand.register(name, name, email, password, locale);
                    MailSender.getInstance().send(email, LOGIN_USER_SUBJECT, LOGIN_RESPONSE);
                    session.setAttribute(RequestConstants.NEED_TO_LINK_BANK_ACCOUNT, true);
                } else {
                    user = optionalUser.get();
                }

                session.setAttribute(RequestConstants.LOGIN, name);
                session.setAttribute(RequestConstants.ROLE, RequestConstants.USER_ROLE);
                session.setAttribute(UtilStringConstants.GOOGLE_SIGN_IN, true);

            } catch (ValidatorException e) {
                session.setAttribute(ErrorMessageConstants.ERROR_LOG_MESSAGE, e.getMessage());
                logger.error(e.getMessage(), e);
                return new CommandResult(CommandResult.ResponseType.TEXT_PLAIN, FAIL);
            } catch (MessagingException e) {
                session.setAttribute(ErrorMessageConstants.ERROR_LOG_MESSAGE,
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
                return new CommandResult(CommandResult.ResponseType.TEXT_PLAIN, FAIL);
            } catch (DqlException e) {
                DqlExceptionMessageProcessor dqlExceptionMessageProcessor = new DqlExceptionMessageProcessor();
                dqlExceptionMessageProcessor.setLocale(locale);
                session.setAttribute(ErrorMessageConstants.ERROR_LOG_MESSAGE,
                        dqlExceptionMessageProcessor.process(e));
                logger.error(e.getMessage(), e);
                return new CommandResult(CommandResult.ResponseType.TEXT_PLAIN, FAIL);
            }
        } catch (Exception e) {
            session.setAttribute(ErrorMessageConstants.ERROR_LOG_MESSAGE,
                    ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.FAILED_GOOGLE_SIGN_IN));
            return new CommandResult(CommandResult.ResponseType.TEXT_PLAIN, FAIL);
        }

        if (Objects.nonNull(session.getAttribute(RequestConstants.BACK_TO_CART))) {
            session.removeAttribute(RequestConstants.BACK_TO_CART);
            return new CommandResult(CommandResult.ResponseType.TEXT_PLAIN, TO_CART);
        }

        return new CommandResult(CommandResult.ResponseType.TEXT_PLAIN, OK);
    }
}
