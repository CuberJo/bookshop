package com.epam.bookshop.controller;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.CommandFactory;
import com.epam.bookshop.controller.command.impl.CustomRequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.util.UtilStrings;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/home")
public class Controller extends HttpServlet {

    private static final String COMMAND_PARAM = "command";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        processRequest(req, resp);
        final String commandParam = req.getParameter(COMMAND_PARAM);
        Command command = CommandFactory.command(commandParam);
        final ResponseContext responseContext = command.execute(new CustomRequestContext(req));
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(responseContext.getPage());
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(UtilStrings.UTF8);
//        processRequest(req, resp);
        final String commandParam = req.getParameter(COMMAND_PARAM);
        Command command = CommandFactory.command(commandParam);
        final ResponseContext responseContext = command.execute(new CustomRequestContext(req));
        resp.sendRedirect(req.getContextPath() + responseContext.getPage());
    }

//    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        final String commandParam = req.getParameter(COMMAND_PARAM);
//        Command command = CommandFactory.command(commandParam);
//        final ResponseContext responseContext = command.execute(new CustomRequestContext(req));
//        RequestDispatcher requestDispatcher = req.getRequestDispatcher(responseContext.getPage());
//        requestDispatcher.forward(req, resp);
//    }
}