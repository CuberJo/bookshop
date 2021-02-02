package com.epam.bookshop.fliter;

import com.epam.bookshop.util.constant.RequestConstants;
import com.epam.bookshop.util.constant.UtilStringConstants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@WebFilter("/home")
public class AuthFilter extends HttpFilter {

    private static final String HOME_PAGE = "/home";

    private static final String ADD_IBAN_COMMAND = "add_iban";
    private static final String ADMIN_COMMAND = "admin";
    private static final String CART_COMMAND = "cart";
    private static final String CHOOSE_IBAN_COMMAND = "choose_iban";
    private static final String DELETE_ACCOUNT_COMMAND = "delete_account";
    private static final String FINISHED_PURCHASE_COMMAND = "finished_purchase";
    private static final String LOGOUT_COMMAND = "logout";
    private static final String PERSONAL_COMMAND = "personal_page";
    private static final String PURCHASE_COMMAND = "purchase";


    private static final String ACCOUNT_SETTINGS_CONTROLLER = "account_settings";
    private static final String ADMIN_CONTROLLER = "admin";
    private static final String LOAD_IBANs_CONTROLLER = "load_ibans";
    private static final String READ_BOOK_CONTROLLER = "read_book";
    private static final String UNBIND_IBAN_CONTROLLER = "unbind_iban";


    /**
     * Command parameter need authorization
     */
    private static final List<String> COMMANDS_NEED_AUTHORIZATION = Arrays.asList(
            ADD_IBAN_COMMAND,
            ADMIN_COMMAND,
            CART_COMMAND,
            CHOOSE_IBAN_COMMAND,
            DELETE_ACCOUNT_COMMAND,
            FINISHED_PURCHASE_COMMAND,
            LOGOUT_COMMAND,
            PERSONAL_COMMAND,
            PURCHASE_COMMAND
    );

    /**
     * FrontController path need authorization
     */
    private static final List<String> CONTROLLERS_NEED_AUTHORIZATION = Arrays.asList(
            ACCOUNT_SETTINGS_CONTROLLER,
            ADMIN_CONTROLLER,
            LOAD_IBANs_CONTROLLER,
            READ_BOOK_CONTROLLER,
            UNBIND_IBAN_CONTROLLER
    );

    /**
     * Checks whether user is allowed to visit this page,
     * otherwise, redirects to home page
     *
     * @param req {@link HttpServletRequest} object
     * @param res {@link HttpServletResponse} object
     * @param chain {@link FilterChain} object
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        final HttpSession session = req.getSession();

        String login = (String) session.getAttribute(RequestConstants.LOGIN);
        String role = (String) session.getAttribute(RequestConstants.ROLE);

        if (Objects.isNull(login) || Objects.isNull(role)) {

            long equalCommands = COMMANDS_NEED_AUTHORIZATION.stream().
                    filter(s -> s.equals(req.getParameter(RequestConstants.COMMAND)))
                    .count();

            if (equalCommands > 0) {
                res.sendRedirect(req.getContextPath() + HOME_PAGE);
                return;
            }

            long equalControllers = CONTROLLERS_NEED_AUTHORIZATION.stream().
                    filter(s -> s.equals(req.getServletPath()))
                    .count();

            if (equalControllers > 0) {
                res.sendRedirect(req.getContextPath() + HOME_PAGE);
                return;
            }
        }

        if (Objects.nonNull(login) && Objects.nonNull(role)
                && role.equals(RequestConstants.ADMIN_ROLE) && PERSONAL_COMMAND.equals(req.getParameter(RequestConstants.COMMAND))) {

            res.sendRedirect(req.getContextPath() + HOME_PAGE);
            return;
        }

        chain.doFilter(req, res);
    }
}



