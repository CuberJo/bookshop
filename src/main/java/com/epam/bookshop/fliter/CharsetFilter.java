package com.epam.bookshop.fliter;

import com.epam.bookshop.constant.UtilStrings;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CharsetFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        res.setCharacterEncoding(UtilStrings.UTF8);
        res.setContentType("text/html; charset=UTF-8");
        res.setCharacterEncoding(UtilStrings.UTF8);
        chain.doFilter(req, res);
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