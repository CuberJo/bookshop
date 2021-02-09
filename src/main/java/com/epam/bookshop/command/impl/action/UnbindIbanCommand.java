package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

/**
 * Deletes selected IBAN for user
 */
public class UnbindIbanCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(UnbindIbanCommand.class);

    @Override
    public CommandResult execute(RequestContext requestContext) {
        HttpSession session = requestContext.getSession();
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        List<String> ibans = (List<String>) session.getAttribute(RequestConstants.IBANs);
        if (Objects.isNull(ibans)) {
            throw new RuntimeException(ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IBANs_NOT_FOUND));
        }

        String ibanToDelete = requestContext.getParameter(RequestConstants.IBAN_TO_DELETE);
        deleteIBAN(ibanToDelete, locale);
        ibans.remove(ibanToDelete);

        if (session.getAttribute(RequestConstants.CHOSEN_IBAN).equals(ibanToDelete)) {
            session.removeAttribute(RequestConstants.CHOSEN_IBAN);
        }

        return new CommandResult(CommandResult.ResponseType.NO_ACTION, UtilStringConstants.EMPTY_STRING);
    }

    /**
     * Deletes user IBAN from database
     *
     * @param iban {@link String} user IBAN to delete
     * @param locale {@link String} language for error messages
     */
    private void deleteIBAN(String iban, String locale) {
        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        try {
            service.deleteUserBankAccount(iban);
        } catch (EntityNotFoundException e) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IBAN_NOT_FOUND)
                    + UtilStringConstants.WHITESPACE + iban;
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }
    }
}
