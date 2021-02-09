package com.epam.bookshop.command.impl.page;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.PageConstants;

/**
 * Returns 'forgot_password.jsp' page that allows user
 * to reset his/her password
 */
public class ForgotPasswordPageCommand implements Command {

    @Override
    public CommandResult execute(RequestContext requestContext) {
        return new CommandResult(CommandResult.ResponseType.FORWARD, PageConstants.FORGOT_PASSWORD.getPage());
    }
}
