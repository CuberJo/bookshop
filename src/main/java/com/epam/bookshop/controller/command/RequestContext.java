package com.epam.bookshop.controller.command;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface RequestContext {

    List<String> getParamList();

    String getParameter(String name);

    void setAttribute(String attr, Object o);

    HttpSession getSession();
}
