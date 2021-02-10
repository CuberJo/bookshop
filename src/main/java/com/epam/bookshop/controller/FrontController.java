package com.epam.bookshop.controller;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandFactory;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.impl.CustomRequestContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Initial entry point for handling all requests
 * implementation of Front FrontController design pattern
 */
@MultipartConfig
@WebServlet("/home")
public class FrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        processRequest(req, resp);
    }


    public static void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        final String commandParam = req.getParameter(RequestConstants.COMMAND);
        Command command = CommandFactory.command(commandParam);
        CommandResult result = command.execute(new CustomRequestContext(req));

        switch (result.getResponseType()) {
            case FORWARD:
                RequestDispatcher requestDispatcher = req.getRequestDispatcher(result.getResp());
                requestDispatcher.forward(req, resp);
                return;
            case REDIRECT:
                req.setCharacterEncoding(UtilStringConstants.UTF8);
                resp.sendRedirect(req.getContextPath() + result.getResp());
                return;
            case JSON:
                resp.setContentType(UtilStringConstants.APPLICATION_JSON);
                resp.setCharacterEncoding(UtilStringConstants.UTF8);
                resp.getWriter().write(result.getResp());
                return;
            case TEXT_PLAIN:
                resp.setContentType(UtilStringConstants.TEXT_PLAIN);
                resp.setCharacterEncoding(UtilStringConstants.UTF8);
                resp.getWriter().write(result.getResp());
                return;
            case NO_ACTION:
                return;
            default:
                throw new RuntimeException(ErrorMessageConstants.NO_SUCH_COMMAND_RESULT
                    + result.getResponseType());
        }
    }
}