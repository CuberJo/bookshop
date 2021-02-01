package com.epam.bookshop.controller.command;

import javax.servlet.http.HttpServletRequest;

/**
 * This interface is a wrapper over {@link javax.servlet.http.HttpServletResponse}.
 */
@FunctionalInterface
public interface ResponseContext {
    String getPage();
}
