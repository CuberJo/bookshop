package com.epam.bookshop.controller;

import com.epam.bookshop.command.CommandFactory;
import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.command.impl.CustomRequestContext;
import constant.RequestConstants;
import constant.UtilStringConstants;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Initial entry point for handling all requests
 * implementation of Front FrontController design pattern
 */
@WebServlet("/home")
public class FrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final ResponseContext responseContext = processRequest(req, resp);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(responseContext.getPage());
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding(UtilStringConstants.UTF8);
        final ResponseContext responseContext = processRequest(req, resp);
        resp.sendRedirect(req.getContextPath() + responseContext.getPage());
    }


    protected ResponseContext processRequest(HttpServletRequest req, HttpServletResponse resp) {
        final String commandParam = req.getParameter(RequestConstants.COMMAND);
        Command command = CommandFactory.command(commandParam);
        return command.execute(new CustomRequestContext(req));
    }
}