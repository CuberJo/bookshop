package com.epam.bookshop.controller.command;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface RequestContext {

    List<String> getParamList();

    Map<String, String[]> getParameterMap();

    String getParameter(String name);

    void setAttribute(String attr, Object o);

    HttpSession getSession();

    Object getAttribute(String s);

    String getContextPath();
}
