package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.EntityNotFoundException;
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
import java.util.List;
import java.util.Objects;


@WebServlet("/unbind_iban")
public class UnbindIBANController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(Command.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        final HttpSession session = request.getSession();

        String locale = (String) session.getAttribute(UtilStrings.LOCALE);

        List<String> ibans = (List<String>) session.getAttribute(UtilStrings.IBANs);
        if (Objects.isNull(ibans)) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IBANs_NOT_FOUND);
            throw new RuntimeException(error);
        }

        String ibanToDelete = request.getParameter(UtilStrings.IBAN_TO_DELETE);
        deleteIBAN(ibanToDelete, locale);
        ibans.remove(ibanToDelete);
    }


    /**
     * Deletes user IBAN
     * @param iban {@link String} user IBAN to delete
     * @param locale {@link String} language for error messages
     */
    private void deleteIBAN(String iban, String locale) {
        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        try {
            service.deleteUserBankAccount(iban);
        } catch (EntityNotFoundException e) {
            logger.error(UtilStrings.EMPTY_STRING, e);
        }
    }
}
