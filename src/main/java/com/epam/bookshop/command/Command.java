package com.epam.bookshop.command;

/**
 * interface represents application logic to handle {@link javax.servlet.http.HttpServletRequest}
 * implementation of Command design pattern
 */
public interface Command {
    CommandResult execute(RequestContext requestContext);
}
