//package org.mycompany.bookshop.fliter;

//
//import org.mindrot.jbcrypt.BCrypt;
//import UserCriteria;
//import EntityType;
//import User;
//import InvalidStateException;
//import EntityService;
//import ServiceFactory;
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
//import java.util.Optional;
//
//@WebFilter("/*")
//public class AuthFilter extends HttpFilter {
//
//    private static final String LOGIN = "login";
//    private static final String PASSWORD = "password";
//    private static final String ROLE = "role";
//    private static final String ERROR_MESSAGE = "error_message";
//    private static final String EMPTY_STRING = "";
//    private static final String EMPTY_STRING_REGEX = "^[\\s]+$";
//    private static final String ACCOUNT_PAGE = "/home?command=account";
//
//    @Override
//    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
////        final String login = req.getParameter(LOGIN);
////        final String password = req.getParameter(PASSWORD);
////
////        System.out.println("LOGIN=" + req.getParameter(LOGIN));
////        System.out.println("PASSWORD=" + req.getParameter(PASSWORD));
//
//        final HttpSession session = req.getSession();
//
//
//        /**
//         * if session is not finished,
//         * then just pass the request
//         * to another filter
//         *
//         * user is authenticated and authorized
//         */
//        if (Objects.nonNull(session)
//                && Objects.nonNull(session.getAttribute(LOGIN))
//                && Objects.nonNull(session.getAttribute(ROLE))) {
//            chain.doFilter(req, res);
////            return;
//        } else {
//            chain.doFilter(req, res);
//        }
//
////        /**
////         * if there is no session,
////         * then just pass the request
////         * to another filter
////         *
////         * user is not authenticated neither authorized
////         */
////        if (Objects.isNull(login) && Objects.isNull(password)) {
////            System.out.println("Objects.isNull(login) && Objects.isNull(password)");
////            chain.doFilter(req, res);
////            return;
////        }
////
////        try {
////            if (!validateInput(login, password)) {
////                System.out.println("!validateInput(login, password)");
////                session.setAttribute(ERROR_MESSAGE, "Fields cannot be empty");
////                res.sendRedirect(ACCOUNT_PAGE);
////                return;
////            }
////
////            Optional<User> optionalUser = authenticate(login, password);
////            if (optionalUser.isEmpty()) {
////                System.out.println("optionalUser.isEmpty()");
////                session.setAttribute(ERROR_MESSAGE, "Incorrect login or password");
////                res.sendRedirect(ACCOUNT_PAGE);
////                return;
////            }
////
////
////            System.out.println("*****************NOTHING*****************************");
////            session.setAttribute(LOGIN, login);
////            session.setAttribute(ROLE, optionalUser.get().getRole());
////
////            chain.doFilter(req, res);
////        } catch (InvalidStateException e) {
////            e.printStackTrace();
////        }
//    }
//
//
//
//
//}
//
