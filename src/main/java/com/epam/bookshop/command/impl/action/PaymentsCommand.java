package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Payment;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.BookDataProcessor;
import com.epam.bookshop.util.ToJsonConverter;

import java.util.Collection;

/**
 * Returns paginated {@link Payment} instances found in database
 */
public class PaymentsCommand implements Command {

    private static final int ITEMS_PER_PAGE = 4;

    @Override
    public CommandResult execute(RequestContext requestContext) {
        EntityService<Payment> service = ServiceFactory.getInstance().create(EntityType.PAYMENT);
        service.setLocale((String) requestContext.getSession().getAttribute(RequestConstants.LOCALE));

        Collection<Payment> payments = service.findAll(
                BookDataProcessor.getInstance().getStartPoint(requestContext, ITEMS_PER_PAGE),
                ITEMS_PER_PAGE);

        return new CommandResult(CommandResult.ResponseType.JSON,
                ToJsonConverter.getInstance().write(payments));
    }
}
