//package com.epam.bookshop.fliter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.util.Objects;
//
//@WebFilter("/home")
//public class AuthFilter extends HttpFilter {
//
//    private static final String HOME_PAGE = "/home";
//
//    private static final String ROLE = "role";
//    private static final String LOGIN = "login";
//    private static final String COMMAND = "command";
//    private static final String CART = "cart";
//
//    @Override
//    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
//
//        final HttpSession session = req.getSession();
//
//        String login = (String) session.getAttribute(LOGIN);
//        String role = (String) session.getAttribute(ROLE);
//
//        if (Objects.isNull(login) || Objects.isNull(role)) {
//            if (Objects.nonNull(req.getParameter(COMMAND)) && req.getParameter(COMMAND).equals(CART)) {
//                res.sendRedirect(req.getContextPath() + HOME_PAGE);
//                return;
//            }
//        }
//
//        chain.doFilter(req, res);
//    }
//}
//
//
