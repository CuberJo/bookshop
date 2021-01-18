package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class DeleteAccountCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(DeleteAccountCommand.class);

    private static final ResponseContext ACCOUNT_PAGE = () -> "/home?command=account";

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        String locale = (String) session.getAttribute(UtilStrings.LOCALE);

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


    private void delete(String locale, HttpSession session) throws ValidatorException, EntityNotFoundException {
        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        User user = findUser(session, service, locale);

        service.delete(user);
        session.removeAttribute(UtilStrings.LOGIN);
        session.removeAttribute(UtilStrings.LIBRARY);
        session.removeAttribute(UtilStrings.IBANs);
        session.removeAttribute(UtilStrings.ROLE);
    }


    private User findUser(HttpSession session, EntityService<User> service, String locale) throws ValidatorException {
        String login = (String) session.getAttribute(UtilStrings.LOGIN);
        UserCriteria criteria = UserCriteria.builder().login(login).build();

        Optional<User> optionalUser = service.find(criteria);
        if (optionalUser.isEmpty()) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND);
            throw new RuntimeException(error);
        }

        return optionalUser.get();
    }
}
