package com.epam.bookshop.command.impl.page_and_action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.RouteConstants;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.EntityFinderFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

/**
 * Deletes user account both from session and from database
 */
public class DeleteAccountCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(DeleteAccountCommand.class);

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        delete(session);

        session.removeAttribute(RequestConstants.LOGIN);
        session.removeAttribute(RequestConstants.LIBRARY);
        session.removeAttribute(RequestConstants.IBANs);
        session.removeAttribute(RequestConstants.ROLE);
        session.removeAttribute(RequestConstants.CART);

        session.removeAttribute(RequestConstants.BACK_TO_CHOOSE_IBAN);
        session.removeAttribute(RequestConstants.FROM_CART_PAGE);

        return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.ACCOUNT.getRoute());
    }


    /**
     * Deletes user in current session.
     *
     * @param session current {@link HttpSession} session
     */
    public static void delete(HttpSession session)  {
        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale((String) session.getAttribute(RequestConstants.LOCALE));
        try {
            service.delete(EntityFinderFacade.getInstance().findUserInSession(session, logger));
        } catch (ValidatorException | EntityNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
