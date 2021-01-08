//package com.epam.bookshop.fliter;
//
//import org.jsoup.Jsoup;
//import org.jsoup.safety.Whitelist;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Collections;
//import java.util.Enumeration;
//import java.util.Map;
//
//@WebFilter("/*")
//public class  XSSFilter extends HttpFilter {
//
//    @Override
//    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest adjustedRequest = adjustParams(req);
//        chain.doFilter(adjustedRequest, res);
//    }
//
//
//    private HttpServletRequest adjustParams(final HttpServletRequest req) {
//        req.getParameterMap().forEach((s, strings) -> {
//                s = Jsoup.clean(s, Whitelist.none());
//                for (int i = 0; i < strings.length; i++) {
//                    strings[i] = Jsoup.clean(strings[i], Whitelist.none());
//                }
//            });
//        return new HttpServletRequestWrapper(req) {
//            public String getParameter(String name) {
//                return req.getParameter(name);
//            }
//
//            public Map<String, String[]> getParameterMap() {
//                return req.getParameterMap();
//            }
//
//            public Enumeration<String> getParameterNames() {
//                return Collections.enumeration(req.getParameterMap().keySet());
//            }
//
//            public String[] getParameterValues(String name) {
//                return req.getParameterMap().get(name);
//            }
//
//            @Override
//            public String getRequestURI() {
//                return super.getRequestURI();
//            }
//
//            @Override
//            public StringBuffer getRequestURL() {
//                return super.getRequestURL();
//            }
//
//            @Override
//            public String getServletPath() {
//                return super.getServletPath();
//            }
//
//            @Override
//            public String getPathInfo() {
//                return super.getPathInfo();
//            }
//
//            @Override
//            public String getPathTranslated() {
//                return super.getPathTranslated();
//            }
//
//            @Override
//            public String getContextPath() {
//                return super.getContextPath();
//            }
//
//            @Override
//            public String getQueryString() {
//                return super.getQueryString();
//            }
//
//            @Override
//            public String getHeader(String name) {
//                return super.getHeader(name);
//            }
//
//            @Override
//            public Enumeration<String> getHeaders(String name) {
//                return super.getHeaders(name);
//            }
//
//            @Override
//            public Enumeration<String> getHeaderNames() {
//                return super.getHeaderNames();
//            }
//
//            @Override
//            public int getIntHeader(String name) {
//                return super.getIntHeader(name);
//            }
//
//            @Override
//            public ServletContext getServletContext() {
//                return super.getServletContext();
//            }
//        };
//    }
//}
