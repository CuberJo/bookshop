package com.epam.bookshop.controller;

import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@WebServlet("/load_ibans")
public class LoadIBANsController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(LoadIBANsController.class);

    private static final String LOGIN_ATTR = "login";
    private static final String ROLE_ATTR = "role";

    private static final String IBANs_ATR = "ibans";
    private static final String LOCALE_ATTR = "locale";

    private static final ResponseContext DEFAULT_PAGE = () -> "/home";
    private static final String USER_NOT_FOUND = "user_not_found";
    private static final String INVALID_INPUT_DATA = "invalid_input_data";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final HttpSession session = req.getSession();

        String locale = (String) session.getAttribute(LOCALE_ATTR);

        String role = (String) session.getAttribute(ROLE_ATTR);
        String login = (String) session.getAttribute(LOGIN_ATTR);

        if (Objects.isNull(role) || Objects.isNull(login)) {
            return;
        }

        String errorMessage = "";

        try {

            List<String> IBANs = findIBANs(login, locale);

            if (Objects.isNull(session.getAttribute(IBANs_ATR))) {
                session.setAttribute(IBANs_ATR, IBANs);
            }
            List<String> sessionIBANs = (List<String>) session.getAttribute(IBANs_ATR);
//            for (String iban : IBANs) {
//                sessionIBANs.add(iban);
//            }
            for (int i = 0; i < IBANs.size(); i++) {
                if(!sessionIBANs.contains(IBANs.get(i))) {
                    sessionIBANs.add(IBANs.get(i));
                }
            }

        } catch (ValidatorException e) {
            errorMessage = ErrorMessageManager.valueOf(locale).getMessage(INVALID_INPUT_DATA);
            throw new RuntimeException(errorMessage, e);
        }
    }

    private List<String> findIBANs(String login, String locale) throws ValidatorException {

        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);

        String error = "";

        Optional<User> optionalUser = service.find(UserCriteria.builder().login(login).build());
        if (optionalUser.isEmpty()) {
            error = ErrorMessageManager.valueOf(locale).getMessage(USER_NOT_FOUND);
            throw new RuntimeException(error);
        }

        return service.findUserBankAccounts(optionalUser.get().getEntityId());
    }
}
