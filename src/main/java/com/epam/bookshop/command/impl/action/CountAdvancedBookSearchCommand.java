package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.BookDataProcessor;
import com.epam.bookshop.util.ToJsonConverter;

import javax.servlet.http.HttpSession;

/**
 * Returns number of books found by {@link AdvancedBookSearchCommand}
 */
public class CountAdvancedBookSearchCommand implements Command {

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();
        final String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        int rows = service.count(BookDataProcessor.getInstance().buildCriteria(session, locale));

        return new CommandResult(CommandResult.ResponseType.JSON,
                ToJsonConverter.getInstance().write(rows));
    }
}
