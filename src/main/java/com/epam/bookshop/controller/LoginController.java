package com.epam.bookshop.controller;

import com.epam.bookshop.util.validator.impl.VerifyReCaptcha;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private static final String G_RECAPTCHA_RESPONSE = "g-recaptcha-response";


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getParameterMap().values().stream().forEach(System.out::println);

        System.out.println("hi");
        if (!VerifyReCaptcha.getInstance().verify(req.getParameter(G_RECAPTCHA_RESPONSE))) {
            req.getRequestDispatcher("login").forward(req, resp);
        }

        if (req.getParameter("login") == null) {
            System.out.println("*****************************login-null******************************");
        }
        if (req.getParameter("password") == null) {
            System.out.println("*****************************password-null******************************");
        }
        if (req.getParameter("login").equals("")) {
            System.out.println("*****************************login-empty******************************");
        }
        if (req.getParameter("password").equals("")) {
            System.out.println("*****************************password-empty******************************");
        }
        if (req.getParameter("login").matches("\\s")) {
            System.out.println("*****************************login-regex******************************");
        }
        if (req.getParameter("password").matches("\\s")) {
            System.out.println("*****************************password-regix******************************");
        }

        resp.getWriter().write("------------------------------------------");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("login") == null) {
            System.out.println("*****************************login+null******************************");
        }
        if (req.getParameter("password") == null) {
            System.out.println("*****************************password+null******************************");
        }
        if (req.getParameter("login").equals("")) {
            System.out.println("*****************************login+empty******************************");
        }
        if (req.getParameter("password").equals("")) {
            System.out.println("*****************************password+empty******************************");
        }
        if (req.getParameter("login").matches("\\s")) {
            System.out.println("*****************************login+regex******************************");
        }
        if (req.getParameter("password").matches("\\s")) {
            System.out.println("*****************************password+regix******************************");
        }

        resp.getWriter().write("++++++++++++++++++++++++++++++++++++++");
    }
}
