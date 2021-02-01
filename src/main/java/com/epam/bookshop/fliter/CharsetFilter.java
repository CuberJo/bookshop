package com.epam.bookshop.fliter;

import com.epam.bookshop.util.constant.UtilStrings;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter using for setting encoding of request and response to UTF-8
 */
@WebFilter("/*")
public class CharsetFilter extends HttpFilter {

    private static final String CONCRETE_CONTENT_TYPE = "text/html; charset=UTF-8";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        req.setCharacterEncoding(UtilStrings.UTF8);
        res.setContentType(CONCRETE_CONTENT_TYPE);
        res.setCharacterEncoding(UtilStrings.UTF8);
        chain.doFilter(req, res);
    }
}