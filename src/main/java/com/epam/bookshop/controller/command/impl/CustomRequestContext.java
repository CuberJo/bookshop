package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is a wrapper over {@link HttpServletRequest}.
 * Implements {@link RequestContext}
 */
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

    public Map<String, String[]> getParameterMap() {
        return httpServletRequest.getParameterMap();
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

    @Override
    public Object getAttribute(String s) {
        return httpServletRequest.getAttribute(s);
    }

    @Override
    public String getContextPath() {
        return httpServletRequest.getContextPath();
    }

    @Override
    public void removeAttribute(String s) {
        httpServletRequest.removeAttribute(s);
    }
}
