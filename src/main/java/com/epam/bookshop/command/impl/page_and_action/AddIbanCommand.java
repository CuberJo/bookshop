package com.epam.bookshop.command.impl.page_and_action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.RouteConstants;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.DqlException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.util.EntityFinderFacade;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.processor.DqlExceptionMessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

/**
 * Adds IBAN to {@link User} or returns page
 */
public class AddIbanCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(AddIbanCommand.class);

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        User user = EntityFinderFacade.getInstance().findUserInSession(session, logger);
        List<String> IBANs = EntityFinderFacade.getInstance().findIBANs(user, locale);
        if (Objects.isNull(session.getAttribute(RequestConstants.IBANs))) {
            session.setAttribute(RequestConstants.IBANs, IBANs);
        }

        try {
            String iban = createIBAN(requestContext, user, locale);
            IBANs.add(iban);
        } catch (ValidatorException e) {
            logger.error(e.getMessage(), e);
            session.setAttribute(ErrorMessageConstants.ERROR_ADD_IBAN_MESSAGE, e.getMessage());
            return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.ADD_IBAN.getRoute());
        } catch (DqlException e) {
            logger.error(e.getMessage(), e);
            DqlExceptionMessageProcessor dqlExceptionMessageProcessor = new DqlExceptionMessageProcessor();
            dqlExceptionMessageProcessor.setLocale(locale);
            session.setAttribute(ErrorMessageConstants.ERROR_ADD_IBAN_MESSAGE,
                    dqlExceptionMessageProcessor.process(e));
            return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.ADD_IBAN.getRoute());
        }

        if (Objects.nonNull(session.getAttribute(RequestConstants.BACK_TO_CART))) {
            session.removeAttribute(RequestConstants.BACK_TO_CART);
            return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.CART.getRoute());
        }

        if (Objects.nonNull(session.getAttribute(RequestConstants.BACK_TO_CHOOSE_IBAN))) {
            session.removeAttribute(RequestConstants.BACK_TO_CHOOSE_IBAN);
            session.setAttribute(RequestConstants.IBANs, IBANs);
            return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.CHOOSE_IBAN.getRoute());
        }

        return new CommandResult(CommandResult.ResponseType.REDIRECT, RouteConstants.PERSONAL_PAGE.getRoute());
    }


    /**
     * Creates IBAN for user, found by {@link Criteria<User>} criteria
     *
     * @param requestContext {@link RequestContext} object, which is request wrapper
     * @param user to whom account is going to be created
     * @param locale {@link String} language for error messages
     * @return {@link String} object of created IBAN
     */
    private String createIBAN(RequestContext requestContext, User user, String locale) throws ValidatorException, DqlException {
        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        String iban = requestContext.getParameter(RequestConstants.IBAN);
        service.createUserBankAccount(iban, user.getEntityId());

        return iban;
    }
}
