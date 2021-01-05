package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.InvalidStateException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import org.mindrot.jbcrypt.BCrypt;
import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class LoginCommand implements Command {

//    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/home.jsp";
//    private static final ResponseContext ACCOUNT_PAGE = () -> "/WEB-INF/jsp/account.jsp";

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext ACCOUNT_PAGE = () -> "/home?command=account";

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String ROLE = "role";
    private static final String ERROR_MESSAGE = "error_log_message";
    private static final String EMPTY_STRING = "";
    private static final String EMPTY_STRING_REGEX = "^[\\s]+$";


    /**
     * @param requestContext {@link RequestContext} which is wrapper under {@link javax.servlet.http.HttpServletRequest}
     * @return page uri of jsp
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final String login = requestContext.getParameter(LOGIN);
        final String password = requestContext.getParameter(PASSWORD);

        System.out.println("LOGIN=" + requestContext.getParameter(LOGIN));
        System.out.println("PASSWORD=" + requestContext.getParameter(PASSWORD));

        final HttpSession session = requestContext.getSession();

        try {
            if (!validateInput(login, password)) {
                System.out.println("!validateInput(login, password)");
                session.setAttribute(ERROR_MESSAGE, "Fields cannot be empty");
                return ACCOUNT_PAGE;
            }

            Optional<User> optionalUser = authenticate(login, password);
            if (optionalUser.isEmpty()) {
                System.out.println("optionalUser.isEmpty()");
                session.setAttribute(ERROR_MESSAGE, "Incorrect login or password");
                return ACCOUNT_PAGE;
            }


            System.out.println("*****************NOTHING*****************************");
            session.setAttribute(LOGIN, login);
            session.setAttribute(ROLE, optionalUser.get().getRole());

        } catch (ValidatorException e) {
            session.setAttribute(ERROR_MESSAGE, "Invalid input data");
            e.printStackTrace();
            return ACCOUNT_PAGE;
        }

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
    private Optional<User> authenticate(String login, String password) throws ValidatorException {

        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);

        UserCriteria criteria = UserCriteria.builder()
                .login(login)
                .build();

        Optional<User> optionalUser = service.find(criteria);

        if (optionalUser.isEmpty() || !BCrypt.checkpw(password, optionalUser.get().getPassword())) {
            return Optional.empty();
        }


        return optionalUser;
    }
}
