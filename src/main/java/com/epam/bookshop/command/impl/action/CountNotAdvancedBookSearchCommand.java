package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.ToJsonConverter;

import javax.servlet.http.HttpSession;

/**
 * Returns number of books found by usual search
 */
public class CountNotAdvancedBookSearchCommand implements Command {

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();

        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale((String) session.getAttribute(RequestConstants.LOCALE));

        int rows = service.count((String) session.getAttribute(RequestConstants.SEARCH_STR));

        return new CommandResult(CommandResult.ResponseType.JSON,
                ToJsonConverter.getInstance().write(rows));
    }
}
