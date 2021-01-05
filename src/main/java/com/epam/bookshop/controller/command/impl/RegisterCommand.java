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
//import org.mycompany.bookshop.util.MailSender;

import javax.servlet.http.HttpSession;

public class RegisterCommand implements Command {

    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/home.jsp";
    private static final ResponseContext ACCOUNT_PAGE = () -> "/WEB-INF/jsp/account.jsp";

    private static final String NAME = "name";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String ROLE = "role";
    private static final String ERROR_MESSAGE = "error_reg_message";
    private static final String EMPTY_STRING = "";
    private static final String EMPTY_STRING_REGEX = "^[\\s]+$";

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
            if (!validateInput(name, login, email, password)) {
                System.out.println("!validateInput(name, email, password, login)");
                session.setAttribute(ERROR_MESSAGE, "Fields cannot be empty");
                return ACCOUNT_PAGE;
            }

            User user = register(name, login, email, password);

//            MailSender.getInstance().send(requestContext);

            System.out.println("*****************NOTHING*****************************");
            session.setAttribute(LOGIN, login);
            session.setAttribute(ROLE, user.getRole());

        } catch (ValidatorException e) {
            session.setAttribute(ERROR_MESSAGE, "Invalid input data");
            e.printStackTrace();
            return ACCOUNT_PAGE;
        }
//        } catch (IOException e) {
//            session.setAttribute(ERROR_MESSAGE, "Email does not exist");
//            System.err.print("Ошибка при отправлении сообщения" + e);
//            e.printStackTrace();
//            return ACCOUNT_PAGE;
//        }
//        } catch () {
//            System.err.print("Некорректный адрес:" + sendToEmail + " " + e);
//        }

        return HOME_PAGE;
    }


    /**
     * Checks strings for not being empty and correspond to special pattern
     *
     * @param strings - Strings to be validated
     * @return <b>true</b> if, and only if, passed strings not empty and correspond
     * to special pattern
     */
    private boolean validateInput(String ... strings) {
        for (String string : strings) {
            if (string.equals(EMPTY_STRING) || string.matches(EMPTY_STRING_REGEX)) {
                return false;
            }
        }

        return true;
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

        final String ROLE = "USER";
//        RoleService roleService = (RoleService) ServiceFactory.getInstance().create(EntityType.ROLE);
//        roleService.find(RoleCriteria.builder().role(ROLE).build());

        User user = new User(name, login, password, email, new Role(1L, ROLE));

        return service.create(user);
    }
}
