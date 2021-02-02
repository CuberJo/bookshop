package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.RequestConstants;
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
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.LOGIN_INCORRECT);
            logger.error(error, e);
            throw new RuntimeException(e);
        } catch (EntityNotFoundException e) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND);
            logger.error(error, e);
            throw new RuntimeException(e);
        }

        return ACCOUNT_PAGE;
    }


    /**
     * Deletes user in current session.
     * @param locale {@link String} language for error messages
     * @param session current {@link HttpSession} session
     * @throws ValidatorException if user data fails fvalidation
     * @throws EntityNotFoundException if user not found in database
     */
    private void delete(String locale, HttpSession session) throws ValidatorException, EntityNotFoundException {
        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        User user = getSessionUser(session, service, locale);

        service.delete(user);
        session.removeAttribute(RequestConstants.LOGIN);
        session.removeAttribute(RequestConstants.LIBRARY);
        session.removeAttribute(RequestConstants.IBANs);
        session.removeAttribute(RequestConstants.ROLE);
    }


    /**
     * Returns user from current session.
     * @param session current {@link HttpSession} session
     * @param service {@link EntityService<User>} object used to find user in database
     * @param locale {@link String} language for error messages
     * @return {@link User} user if found
     * @throws ValidatorException if user criteria fails fvalidation
     */
    private User getSessionUser(HttpSession session, EntityService<User> service, String locale) throws ValidatorException {
        String login = (String) session.getAttribute(RequestConstants.LOGIN);
        UserCriteria criteria = UserCriteria.builder().login(login).build();

        Optional<User> optionalUser = service.find(criteria);
        if (optionalUser.isEmpty()) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND);
            throw new RuntimeException(error);
        }

        return optionalUser.get();
    }
}
