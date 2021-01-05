package com.epam.bookshop.controller;

import com.epam.bookshop.util.MailThread;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

//@WebServlet("/mail")
public class MailController extends HttpServlet {

    private static final String HOME_PAGE = "/WEB-INF/jsp/home.jsp";

    private final String MAIL = "mail";
    private final String EMAIL = "email";
    private final String RESET_PASSWORD = "reset_password";
    private final String REGISTER_USER = "register_user";
    private final String PASSWORD_RESET_SUBJECT = "Password reset";
    private final String REGISTER_USER_SUBJECT = "Password reset";
    private final String REGISTER_RESPONSE = "You have successfully registered";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Properties properties = new Properties();

        String mailPropertiesFile = getServletContext().getInitParameter(MAIL);

        properties.load(getServletContext().getResourceAsStream(mailPropertiesFile));
        MailThread mailOperator = null;
        if (Objects.nonNull(req.getParameter(RESET_PASSWORD))) {
            mailOperator = new MailThread(req.getParameter(EMAIL), req.getParameter(PASSWORD_RESET_SUBJECT), req.getParameter("body"), properties);
        }
        if (Objects.nonNull(req.getParameter(REGISTER_USER))) {
            mailOperator = new MailThread(req.getParameter(EMAIL), req.getParameter(REGISTER_USER_SUBJECT), req.getParameter("body"), properties);
        }
        // запуск процесса отправки письма в отдельном потоке
        mailOperator.start();

        // переход на страницу с предложением о создании нового письма
        req.getRequestDispatcher(HOME_PAGE).forward(req, resp);
    }
}
