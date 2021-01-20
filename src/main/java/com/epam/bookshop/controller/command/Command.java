package com.epam.bookshop.controller.command;

/**
 * interface represents application logic to handle {@link javax.servlet.http.HttpServletRequest}
 * implementation of Command design pattern
 */
@FunctionalInterface
public interface Command {
    ResponseContext execute(RequestContext requestContext);
}
