package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.JsonConverter;

public class CountUsersCommand implements Command {

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale((String) requestContext.getSession().getAttribute(RequestConstants.LOCALE));

        int rows = service.count();

        return () -> JsonConverter.getInstance().write(rows);
    }
}
