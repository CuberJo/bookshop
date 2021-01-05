package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomRequestContext implements RequestContext {

    private HttpServletRequest httpServletRequest;

    public CustomRequestContext(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public List<String> getParamList() {
        return httpServletRequest.getParameterMap().values().stream()
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    @Override
    public String getParameter(String name) {
        return httpServletRequest.getParameter(name);
    }

    @Override
    public void setAttribute(String attrName, Object attr) {
        httpServletRequest.setAttribute(attrName, attr);
    }


    @Override
    public HttpSession getSession() {
        return httpServletRequest.getSession();
    }
}
