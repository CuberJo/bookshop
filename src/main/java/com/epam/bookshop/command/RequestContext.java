package com.epam.bookshop.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * This interface is a wrapper over {@link HttpServletRequest}.
 */
public interface RequestContext {

    List<String> getParamList();

    Map<String, String[]> getParameterMap();

    String getParameter(String name);

    void setAttribute(String attr, Object o);

    HttpSession getSession();

    Object getAttribute(String s);

    String getContextPath();

    void removeAttribute(String s);

    Part getPart(String var1) throws IOException, ServletException;
}
