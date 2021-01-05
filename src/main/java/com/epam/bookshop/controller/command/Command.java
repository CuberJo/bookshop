package com.epam.bookshop.controller.command;

public interface Command {
    ResponseContext execute(RequestContext requestContext);
}
