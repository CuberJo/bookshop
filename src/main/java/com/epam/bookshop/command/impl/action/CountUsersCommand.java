package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.ToJsonConverter;

/**
 * Returns total amout of {@link User} instances in database
 */
public class CountUsersCommand implements Command {

    @Override
    public CommandResult execute(RequestContext requestContext) {
        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale((String) requestContext.getSession().getAttribute(RequestConstants.LOCALE));

        int rows = service.count();

        return new CommandResult(CommandResult.ResponseType.JSON,
                ToJsonConverter.getInstance().write(rows));
    }
}
