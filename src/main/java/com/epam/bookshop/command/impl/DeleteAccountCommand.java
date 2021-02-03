package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Deletes user account both from session and from database
 */
public class DeleteAccountCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(DeleteAccountCommand.class);

    private static final ResponseContext ACCOUNT_PAGE = () -> "/home?command=account";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        String error = "";
        try {
            delete(locale, session);
        } catch (ValidatorException e) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.LOGIN_INCORRECT)
                    + UtilStringConstants.WHITESPACE + session.getAttribute(RequestConstants.LOGIN);
            logger.error(error, e);
            throw new RuntimeException(e);
        } catch (EntityNotFoundException e) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND)
                    + UtilStringConstants.WHITESPACE + session.getAttribute(RequestConstants.LOGIN);
            logger.error(error, e);
            throw new RuntimeException(e);
        }

        return ACCOUNT_PAGE;
    }


    /**
     * Deletes user in current session.
     *
     * @param locale {@link String} language for error messages
     * @param session current {@link HttpSession} session
     * @throws ValidatorException if user data fails fvalidation
     * @throws EntityNotFoundException if user not found in database
     */
    private void delete(String locale, HttpSession session) throws ValidatorException, EntityNotFoundException {
        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        User user = EntityFinder.getInstance().findUserInSession(session, logger);

        service.delete(user);
        session.removeAttribute(RequestConstants.LOGIN);
        session.removeAttribute(RequestConstants.LIBRARY);
        session.removeAttribute(RequestConstants.IBANs);
        session.removeAttribute(RequestConstants.ROLE);
    }
}
