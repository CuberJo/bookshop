package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.util.EntityFinderFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Retrieves user IBANs from database and loads them into session
 */
public class LoadIbansCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(LoadIbansCommand.class);

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        User user = EntityFinderFacade.getInstance().findUserInSession(session, logger);
        List<String> IBANs = EntityFinderFacade.getInstance().findIBANs(user, (String) session.getAttribute(RequestConstants.LOCALE));

        addIBANs(session, IBANs);

        return new CommandResult(CommandResult.ResponseType.NO_ACTION, UtilStringConstants.EMPTY_STRING);
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
