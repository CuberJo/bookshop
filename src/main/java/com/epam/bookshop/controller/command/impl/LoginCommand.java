package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.InvalidStateException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Optional;

public class LoginCommand implements Command {

//    private static final ResponseContext HOME_PAGE = () -> "/WEB-INF/jsp/home.jsp";
//    private static final ResponseContext ACCOUNT_PAGE = () -> "/WEB-INF/jsp/account.jsp";

    private static final ResponseContext HOME_PAGE = () -> "/home";
    private static final ResponseContext ACCOUNT_PAGE = () -> "/home?command=account";
    private static final ResponseContext CART_PAGE = () -> "/home?command=cart";

    private static final String ERROR_MESSAGE = "error_log_message";
    private static final String BACK_TO_CART_ATTR = "back_to_cart";



    /**
     * @param requestContext {@link RequestContext} which is wrapper under {@link javax.servlet.http.HttpServletRequest}
     * @return page uri of jsp
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final String login = requestContext.getParameter(UtilStrings.LOGIN);
        final String password = requestContext.getParameter(UtilStrings.PASSWORD);

        System.out.println("LOGIN=" + requestContext.getParameter(UtilStrings.LOGIN));
        System.out.println("PASSWORD=" + requestContext.getParameter(UtilStrings.PASSWORD));

        final HttpSession session = requestContext.getSession();
        String locale = (String) requestContext.getSession().getAttribute(UtilStrings.LOCALE);
        String errorMessage = "";


        try {
            if (!new Validator().emptyStringValidator(login, password)) {
                System.out.println("!validateInput(login, password)");
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.FIELDS_CANNOT_BE_EMPTY);
                session.setAttribute(ERROR_MESSAGE, errorMessage);
                return ACCOUNT_PAGE;
            }

            Optional<User> optionalUser = authenticate(login, password, locale);
            if (optionalUser.isEmpty()) {
                System.out.println("optionalUser.isEmpty()");
                errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INCORRECT_LOGIN_OR_PASSWORD);
                session.setAttribute(ERROR_MESSAGE, errorMessage);
                return ACCOUNT_PAGE;
            }


            System.out.println("*****************NOTHING*****************************");
            session.setAttribute(UtilStrings.LOGIN, login);
            session.setAttribute(UtilStrings.ROLE, optionalUser.get().getRole().getRole());

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
            session.setAttribute(ERROR_MESSAGE, e.getMessage());
            e.printStackTrace();
            return ACCOUNT_PAGE;
        }

        if (Objects.nonNull(requestContext.getSession().getAttribute(BACK_TO_CART_ATTR))) {
            requestContext.getSession().removeAttribute(BACK_TO_CART_ATTR);
            return CART_PAGE;
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
    private Optional<User> authenticate(String login, String password, String locale) throws ValidatorException {

        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

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
