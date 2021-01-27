package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
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


/**
 * Loads user IBANs into session
 */
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

        List<String> IBANs;
        try {
            IBANs = EntityFinder.getInstance().findIBANs(criteria, logger, locale);
        } catch (ValidatorException e) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
            logger.error(UtilStrings.EMPTY_STRING, e);
            throw new RuntimeException(error, e);
        }

        addIBANs(session, IBANs);
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
