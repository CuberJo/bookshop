package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStringConstants;
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


/**
 * Retrieves user IBANs from database and loads them into session
 */
@WebServlet("/load_ibans")
public class LoadIBANsController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LoadIBANsController.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final HttpSession session = req.getSession();
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        User user = EntityFinder.getInstance().findUserInSession(session, logger);
        List<String> IBANs = EntityFinder.getInstance().findIBANs(user, locale);

        addIBANs(session, IBANs);
    }


    /**
     * Adds user IBANs to {@link HttpSession} object
     *
     * @param session current {@link HttpSession} session used to set attributes
     * @param IBANs {@link List<String>} IBANs to add
     */
    private void addIBANs(HttpSession session, List<String> IBANs) {
        List<String> sessionIBANs = (List<String>) session.getAttribute(RequestConstants.IBANs);
        if (Objects.isNull(sessionIBANs)) {
            sessionIBANs = new ArrayList<>();
            session.setAttribute(RequestConstants.IBANs, IBANs);
        }

        for (String iban : IBANs) {
            if (!sessionIBANs.contains(iban)) {
                sessionIBANs.add(iban);
            }
        }
    }
}
