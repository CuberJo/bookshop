package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Role;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.InvalidStateException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.util.MailSender;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

public class RegisterCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(RegisterCommand.class);

//    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/home.jsp";
//    private static final ResponseContext ACCOUNT_PAGE = () -> "/WEB-INF/jsp/account.jsp";

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext ACCOUNT_PAGE = () -> "/home?command=account";
    private static final ResponseContext CART_PAGE = () -> "/home?command=cart";

    private static final String REGISTER_USER_SUBJECT = "Registration completed";
    private static final String REGISTER_RESPONSE = "You have successfully registered!";

    private static final String ERROR_MESSAGE = "error_reg_message";
    private static final String USER_ROLE = "USER";
    private static final String BACK_TO_CART_ATTR = "back_to_cart";
    private static final String NEED_TO_LINK_BANK_ACCOUNT = "need_to_link_bank_account";


    @Override
    public ResponseContext execute(RequestContext requestContext) {

        final String name = requestContext.getParameter(UtilStrings.NAME);
        final String login = requestContext.getParameter(UtilStrings.LOGIN);
        final String email = requestContext.getParameter(UtilStrings.EMAIL);
        final String password = requestContext.getParameter(UtilStrings.PASSWORD);

        final HttpSession session = requestContext.getSession();
        String locale = (String) requestContext.getSession().getAttribute(UtilStrings.LOCALE);
        String errorMessage = "";

        User user = null;

        try {
            if (!validateEmptyInput(name, login, email, password, locale, requestContext)) {
                return ACCOUNT_PAGE;
            }

            user = register(name, login, email, password, locale);

//            MailSender.getInstance().send(email, REGISTER_USER_SUBJECT, REGISTER_RESPONSE);

            session.setAttribute(UtilStrings.LOGIN, login);
            session.setAttribute(UtilStrings.ROLE, user.getRole().getRole());
            session.setAttribute(NEED_TO_LINK_BANK_ACCOUNT, true);

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
            session.setAttribute(ERROR_MESSAGE, errorMessage);
            logger.error(e.getMessage(), e);
            return ACCOUNT_PAGE;
        } /*catch (MessagingException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.COULD_NOT_REACH_EMAIL_ADDRESS) + UtilStrings.NEW_LINE + email;
            session.setAttribute(ERROR_MESSAGE, errorMessage);
            try {
                EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
                service.setLocale(locale);
                long userId = service.find(UserCriteria.builder().login(user.getLogin()).build()).get().getEntityId();
                service.delete(userId);
            } catch (EntityNotFoundException | ValidatorException e1) {
                logger.error(e1.getMessage(), e1);
            }
            logger.error(e.getMessage(), e);
            return ACCOUNT_PAGE;
        }*/

        if (Objects.nonNull(requestContext.getSession().getAttribute(BACK_TO_CART_ATTR))) {
            requestContext.getSession().removeAttribute(BACK_TO_CART_ATTR);
            return CART_PAGE;
        }

        return HOME_PAGE;
    }


    private boolean validateEmptyInput(String name, String login, String email, String password,
                                       String locale, RequestContext requestContext) {

        if (!new Validator().emptyStringValidator(name, login, email, password)) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.FIELDS_CANNOT_BE_EMPTY);
            requestContext.getSession().setAttribute(ERROR_MESSAGE, errorMessage);
            return false;
        }

        return true;
    }


    /**
     * Registers user by given name, login, email, password
     *
     * @param name
     * @param login
     * @param email
     * @param password
     * @param locale
     * @return user if he/she was found in database
     * @throws ValidatorException
     */
    private User register(String name, String login, String email, String password, String locale) throws ValidatorException {

        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

//        RoleService roleService = (RoleService) ServiceFactory.getInstance().create(EntityType.ROLE);
//        roleService.find(RoleCriteria.builder().role(ROLE).build());

        User user = new User(name, login, password, email, new Role(1L, USER_ROLE));

        return service.create(user);
    }
}
