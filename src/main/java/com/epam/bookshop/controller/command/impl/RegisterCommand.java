package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Role;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.InvalidStateException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.util.MailSender;
import com.epam.bookshop.validator.Validator;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

public class RegisterCommand implements Command {

    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/home.jsp";
    private static final ResponseContext ACCOUNT_PAGE = () -> "/WEB-INF/jsp/account.jsp";

    private static final String REGISTER_USER_SUBJECT = "<h4>Registration completed<h4>";
    private static final String REGISTER_RESPONSE = "You have successfully registered!";


    private static final String NAME = "name";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String ROLE = "role";
    private static final String ERROR_MESSAGE = "error_reg_message";
    private static final String USER_ROLE = "USER";


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

        try {
            if (!Validator.getInstance().emptyStringValidator(name, login, email, password)) {
                System.out.println("!validateInput(name, email, password, login)");
                session.setAttribute(ERROR_MESSAGE, "Fields cannot be empty");
                return ACCOUNT_PAGE;
            }

            User user = register(name, login, email, password);

            MailSender.getInstance().send(email, REGISTER_USER_SUBJECT, REGISTER_RESPONSE);

            System.out.println("*****************NOTHING*****************************");
            session.setAttribute(LOGIN, login);
            session.setAttribute(ROLE, user.getRole());

        } catch (ValidatorException e) {
            session.setAttribute(ERROR_MESSAGE, "Invalid input data");
            e.printStackTrace();
            return ACCOUNT_PAGE;
        } catch (MessagingException e) {
            session.setAttribute(ERROR_MESSAGE, "Could not reach email\n" + email);
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
    private User register(String name, String login, String email, String password) throws ValidatorException {

        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);

//        RoleService roleService = (RoleService) ServiceFactory.getInstance().create(EntityType.ROLE);
//        roleService.find(RoleCriteria.builder().role(ROLE).build());

        User user = new User(name, login, password, email, new Role(1L, USER_ROLE));

        return service.create(user);
    }
}
