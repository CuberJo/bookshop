package com.epam.bookshop.controller;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.EntityNotFoundException;
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


@WebServlet("/unbind_iban")
public class UnbindIBANController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(Command.class);

    private static final String LOCALE_ATTR = "locale";
    private static final String IBAN_TO_DELETE = "iban_to_delete";
    private static final String IBANs_ATR = "ibans";

    private static final String EMPTY_STRING = "";

    private static final ResponseContext PERSONAL_PAGE_PAGE = () -> "/home?command=personal_page";

    private static final String IBANs_NOT_FOUND = "IBANs_not_found";


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        final HttpSession session = request.getSession();

        String locale = (String) session.getAttribute(LOCALE_ATTR);

        List<String> ibans = (List<String>) session.getAttribute(IBANs_ATR);
        if (Objects.isNull(ibans)) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(IBANs_NOT_FOUND);
            throw new RuntimeException(error);
        }

        String ibanToDelete = request.getParameter(IBAN_TO_DELETE);
        deleteIBAN(ibanToDelete, locale);
        ibans.remove(ibanToDelete);
    }


    private void deleteIBAN(String iban, String locale) {

        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        try {
            service.deleteUserBankAccount(iban);
        } catch (EntityNotFoundException e) {
            logger.error(EMPTY_STRING, e);
        }
    }
}
