package com.epam.bookshop.controller;

import com.epam.bookshop.constant.UtilStrings;
import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.CommandFactory;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.controller.command.impl.CustomRequestContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/home")
public class Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final ResponseContext responseContext = processRequest(req, resp);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(responseContext.getPage());
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding(UtilStrings.UTF8);
        final ResponseContext responseContext = processRequest(req, resp);
        resp.sendRedirect(req.getContextPath() + responseContext.getPage());
    }

//    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        final String commandParam = req.getParameter(COMMAND_PARAM);
//        Command command = CommandFactory.command(commandParam);
//        final ResponseContext responseContext = command.execute(new CustomRequestContext(req));
//        RequestDispatcher requestDispatcher = req.getRequestDispatcher(responseContext.getPage());
//        requestDispatcher.forward(req, resp);
//    }

    protected ResponseContext processRequest(HttpServletRequest req, HttpServletResponse resp) {
        final String commandParam = req.getParameter(UtilStrings.COMMAND);
        Command command = CommandFactory.command(commandParam);
        return command.execute(new CustomRequestContext(req));
    }
}