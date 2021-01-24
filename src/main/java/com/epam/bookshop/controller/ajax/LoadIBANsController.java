package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@WebServlet("/load_ibans")
public class LoadIBANsController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LoadIBANsController.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final HttpSession session = req.getSession();
        String locale = (String) session.getAttribute(UtilStrings.LOCALE);


        UserCriteria criteria = UserCriteria.builder()
                .login((String) session.getAttribute(UtilStrings.LOGIN))
                .build();
        List<String> IBANs = findIBANs(criteria, locale);

        addIBANs(session, IBANs);
    }

    /**
     * Looks for IBANs associated with user
     * @param criteria user search {@link Criteria<User>} criteria
     * @param locale {@link String} language for error messages
     * @return {@link List<String>} of found IBANs
     */
    private List<String> findIBANs(Criteria<User> criteria, String locale) {
        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);

        String error = "";

        Optional<User> optionalUser;
        try {
            optionalUser = service.find(criteria);
            if (optionalUser.isEmpty()) {
                error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND);
                throw new RuntimeException(error);
            }
        } catch (ValidatorException e) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
            logger.error(UtilStrings.EMPTY_STRING, e);
            throw new RuntimeException(error, e);
        }

        return service.findUserBankAccounts(optionalUser.get().getEntityId());
    }


    /**
     * Adds user IBANs to session
     * @param session current {@link HttpSession} session used to set attributes
     * @param IBANs {@link List<String>} IBANs to add
     */
    private void addIBANs(HttpSession session, List<String> IBANs) {
        List<String> sessionIBANs = (List<String>) session.getAttribute(UtilStrings.IBANs);
        if (Objects.isNull(sessionIBANs)) {
            sessionIBANs = new ArrayList<>();
            session.setAttribute(UtilStrings.IBANs, IBANs);
        }

        for (String iban : IBANs) {
            if (!sessionIBANs.contains(iban)) {
                sessionIBANs.add(iban);
            }
        }
    }
}
