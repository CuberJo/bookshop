package com.epam.bookshop.controller.command.impl;

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
import com.epam.bookshop.util.MailSender;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

public class RegisterCommand implements Command {

//    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/home.jsp";
//    private static final ResponseContext ACCOUNT_PAGE = () -> "/WEB-INF/jsp/account.jsp";

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext ACCOUNT_PAGE = () -> "/home?command=account";

    private static final String REGISTER_USER_SUBJECT = "Registration completed";
    private static final String REGISTER_RESPONSE = "You have successfully registered!";


    private static final String NAME = "name";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String ROLE = "role";
    private static final String ERROR_MESSAGE = "error_reg_message";
    private static final String USER_ROLE = "USER";
    private static final String FIELDS_CANNOT_BE_EMPTY = "fields_cannot_be_empty";
    private static final String INVALID_INPUT_DATA = "invalid_input_data";
    private static final String COULD_NOT_REACH_EMAIL_ADDRESS = "could_not_reach_email_address";
    private static final String LOCALE_ATTR = "locale";
    private static final String NEW_LINE = "\n";


    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final String name = requestContext.getParameter(NAME);
        final String login = requestContext.getParameter(LOGIN);
        final String email = requestContext.getParameter(EMAIL);
        final String password = requestContext.getParameter(PASSWORD);

        System.out.println("NAME=" + requestContext.getParameter(NAME));
        System.out.println("LOGIN=" + requestContext.getParameter(LOGIN));
        System.out.println("PASSWORD=" + requestContext.getParameter(PASSWORD));
        System.out.println("EMAIL=" + requestContext.getParameter(EMAIL));

        final HttpSession session = requestContext.getSession();
        String locale = (String) requestContext.getSession().getAttribute(LOCALE_ATTR);
        String errorMessage = "";

        User user = null;

        try {
            if (!new Validator().emptyStringValidator(name, login, email, password)) {
                System.out.println("!validateInput(name, email, password, login)");
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(FIELDS_CANNOT_BE_EMPTY);
                session.setAttribute(ERROR_MESSAGE, errorMessage);
                return ACCOUNT_PAGE;
            }

            user = register(name, login, email, password, locale);

            MailSender.getInstance().send(email, REGISTER_USER_SUBJECT, REGISTER_RESPONSE);

            System.out.println("*****************NOTHING*****************************");
            session.setAttribute(LOGIN, login);
            session.setAttribute(ROLE, user.getRole());

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(INVALID_INPUT_DATA);
            session.setAttribute(ERROR_MESSAGE, errorMessage);
            e.printStackTrace();
            return ACCOUNT_PAGE;
        } catch (MessagingException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(COULD_NOT_REACH_EMAIL_ADDRESS + NEW_LINE + email);
            session.setAttribute(ERROR_MESSAGE, errorMessage);
            try {
                EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
                service.setLocale(locale);
                service.delete(user);
            } catch (EntityNotFoundException e1) {
                e1.printStackTrace();
            } catch (ValidatorException e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
            return ACCOUNT_PAGE;
        }

        return HOME_PAGE;
    }


    /**
     * Authenticates user by given login and password
     *
     * @param login login of user to be authenticated
     * @param password password of user to be authenticated
     * @return user if he/she was found in database
     * @throws InvalidStateException
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
