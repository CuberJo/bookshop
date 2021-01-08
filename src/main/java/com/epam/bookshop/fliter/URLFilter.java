package com.epam.bookshop.fliter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class URLFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(req, res);
//        req.getRequestDispatcher(getFullURL(req))
    }

//    public static String getFullURL(HttpServletRequest request) {
//        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
//        String queryString = request.getQueryString();
//
//        if (queryString == null) {
//            return requestURL.toString();
//        } else {
//            return requestURL.append('?').append(queryString).toString();
//        }
//    }
}