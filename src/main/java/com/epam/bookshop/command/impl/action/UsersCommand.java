package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.dto.converter.impl.ToUserDtoConverter;
import com.epam.bookshop.dto.impl.UserDto;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.processor.BookDataProcessor;
import com.epam.bookshop.util.convertor.ToJsonConverter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Returns paginated list of users
 */
public class UsersCommand implements Command {

    private static final int ITEMS_PER_PAGE = 4;

    @Override
    public CommandResult execute(RequestContext requestContext) {
        EntityService<User> service = ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale((String) requestContext.getSession().getAttribute(RequestConstants.LOCALE));

        List<UserDto> userDtos = service.findAll(BookDataProcessor.getInstance().getStartPoint(requestContext, ITEMS_PER_PAGE), ITEMS_PER_PAGE).stream()
                .map(user -> new ToUserDtoConverter().convert(user))
                .collect(Collectors.toList());


        return new CommandResult(CommandResult.ResponseType.JSON,
                ToJsonConverter.getInstance().write(userDtos));
    }
}
