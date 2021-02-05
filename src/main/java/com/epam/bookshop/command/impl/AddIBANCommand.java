package com.epam.bookshop.command.impl;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

/**
 * Adds IBAN to {@link User} or returns page
 */
public class AddIBANCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(AddIBANCommand.class);

//    private static final ResponseContext ADD_IBAN_PAGE_FORWARD = () -> "/WEB-INF/jsp/add_iban.jsp";
    private static final ResponseContext ADD_IBAN_PAGE_REDIRECT = () -> "/home?command=add_iban";
    private static final ResponseContext CHOOSE_IBAN_PAGE_FORWARD = () -> "/WEB-INF/jsp/choose_iban.jsp";
    private static final ResponseContext PERSONAL_PAGE_PAGE = () -> "/home?command=personal_page";
    private static final ResponseContext CHOOSE_IBAN_PAGE_SEND_REDIRECT = () -> "/home?command=choose_iban";
    private static final ResponseContext CART_PAGE = () -> "/home?command=cart";


    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

//        if (needToReturnPage(requestContext)) {
//            return ADD_IBAN_PAGE_FORWARD;
//        }

        User user = EntityFinder.getInstance().findUserInSession(session, logger);
        List<String> IBANs = EntityFinder.getInstance().findIBANs(user, locale);
        if (Objects.isNull(session.getAttribute(RequestConstants.IBANs))) {
            session.setAttribute(RequestConstants.IBANs, IBANs);
        }

//        if (Objects.isNull(session.getAttribute(RequestConstants.CREATE_ADDITIONAL_IBAN))) {
//            if (IBANs.stream().findAny().isPresent() && Objects.nonNull(session.getAttribute(RequestConstants.BACK_TO_CART))) {
//                session.removeAttribute(RequestConstants.BACK_TO_CART);
//                return CHOOSE_IBAN_PAGE_FORWARD;
//            }
//        }
//
//        if ((IBANs.stream().findAny().isEmpty()
//                /*|| Objects.nonNull(session.getAttribute(RequestConstants.CREATE_ADDITIONAL_IBAN))*/)) {
            try {
                String iban = createIBAN(requestContext, user, locale);
                IBANs.add(iban);
            } catch (ValidatorException e) {
                session.setAttribute(ErrorMessageConstants.ERROR_ADD_IBAN_MESSAGE, e.getMessage());
                session.setAttribute(RequestConstants.GET_ADD_IBAN_PAGE_ATTR, RequestConstants.GET_ADD_IBAN_PAGE_ATTR);
                return ADD_IBAN_PAGE_REDIRECT;
            }

//            if (Objects.nonNull(session.getAttribute(RequestConstants.CREATE_ADDITIONAL_IBAN))) {
//                session.removeAttribute(RequestConstants.CREATE_ADDITIONAL_IBAN);
//            }
//        }

        if (Objects.nonNull(session.getAttribute(RequestConstants.BACK_TO_CART))) {
            session.removeAttribute(RequestConstants.BACK_TO_CART);
            return CART_PAGE;
        }

        if (Objects.nonNull(session.getAttribute(RequestConstants.BACK_TO_CHOOSE_IBAN))) {
            session.removeAttribute(RequestConstants.BACK_TO_CHOOSE_IBAN);
            session.setAttribute(RequestConstants.IBANs, IBANs);
            return CHOOSE_IBAN_PAGE_SEND_REDIRECT;
        }

        return PERSONAL_PAGE_PAGE;
    }


    /**
     * Checks whether it is request for getting page
     *
     * @param requestContext {@link RequestContext} object, which is request wrapper
     * @return true if and only if it is need to return page, otherwise - false
     */
    private boolean needToReturnPage(RequestContext requestContext) {
        HttpSession session = requestContext.getSession();

        if (Objects.nonNull(session.getAttribute(RequestConstants.GET_ADD_IBAN_PAGE_ATTR))
                || Objects.nonNull(requestContext.getParameter(RequestConstants.GET_ADD_IBAN_PAGE_ATTR))) {
            if (Objects.nonNull(requestContext.getParameter(RequestConstants.CREATE_ADDITIONAL_IBAN))) {
                session.setAttribute(RequestConstants.CREATE_ADDITIONAL_IBAN, RequestConstants.CREATE_ADDITIONAL_IBAN);
            }
            session.removeAttribute(RequestConstants.GET_ADD_IBAN_PAGE_ATTR);

            return true;
        }

        return false;
    }


    /**
     * Creates IBAN for user, found by {@link Criteria<User>} criteria
     *
     * @param requestContext {@link RequestContext} object, which is request wrapper
     * @param user to whom account is going to be created
     * @param locale {@link String} language for error messages
     * @return {@link String} object of created IBAN
     */
    private String createIBAN(RequestContext requestContext, User user, String locale) throws ValidatorException {
        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        String iban = requestContext.getParameter(RequestConstants.IBAN);
        service.createUserBankAccount(iban, user.getEntityId());

        return iban;
    }
}
