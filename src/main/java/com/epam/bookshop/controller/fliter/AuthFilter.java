package com.epam.bookshop.controller.fliter;

import com.epam.bookshop.constant.RequestConstants;

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

/**
 * Checks whether user is authorized to go on certain page
 */
@WebFilter("/home")
public class AuthFilter extends HttpFilter {

    private static final String HOME_PAGE = "/home";

    private static final String ADD_IBAN_COMMAND = "add_iban";
    private static final String ADMIN_COMMAND = "admin";
    private static final String CART_COMMAND = "cart";
    private static final String CHOOSE_IBAN_PAGE_COMMAND = "choose_iban";
    private static final String DELETE_ACCOUNT_COMMAND = "delete_account";
    private static final String FINISHED_PURCHASE_COMMAND = "finished_purchase";
    private static final String LOGOUT_COMMAND = "logout";
    private static final String PERSONAL_PAGE_COMMAND = "personal_page";
    private static final String PURCHASE_COMMAND = "purchase";
    private static final String ACCOUNT_SETTINGS_COMMAND = "account_settings";
    private static final String ADD_TO_CART_COMMAND = "add_to_cart";
    private static final String REMOVE_FROM_CART_COMMAND = "remove_from_cart";
    private static final String LOAD_IBANs_COMMAND = "load_ibans";
    private static final String UNBIND_IBAN_COMMAND = "unbind_iban";
    private static final String ADD_IBAN_PAGE_COMMAND = "add_iban_page";

    private static final String READ_BOOK_CONTROLLER = "read_book";

    private static final String COUNT_USERS_COMMAND = "countUsers";
    private static final String COUNT_PAYMENTS_COMMAND = "countPayments";
    private static final String PAYMENTS_COMMAND = "payments";
    private static final String USERS_COMMAND = "users";
    private static final String ADD_NEW_BOOK_COMMAND = "addNewBook";
    private static final String UPDATE_BOOK_COMMAND = "updateBook";


    /**
     * Command parameter need authorization
     */
    private static final List<String> COMMANDS_NEED_AUTHORIZATION = Arrays.asList(
            ADD_IBAN_COMMAND,
            ADD_IBAN_PAGE_COMMAND,
            ADD_TO_CART_COMMAND,
            ACCOUNT_SETTINGS_COMMAND,
            ADMIN_COMMAND,
            CART_COMMAND,
            CHOOSE_IBAN_PAGE_COMMAND,
            DELETE_ACCOUNT_COMMAND,
            FINISHED_PURCHASE_COMMAND,
            LOAD_IBANs_COMMAND,
            LOGOUT_COMMAND,
            PERSONAL_PAGE_COMMAND,
            PURCHASE_COMMAND,
            REMOVE_FROM_CART_COMMAND,
            UNBIND_IBAN_COMMAND
    );


    /**
     * Command parameter available only for admin
     */
    private static final List<String> ADMIN_COMMANDS = Arrays.asList(
            COUNT_USERS_COMMAND,
            COUNT_PAYMENTS_COMMAND,
            PAYMENTS_COMMAND,
            USERS_COMMAND,
            ADD_NEW_BOOK_COMMAND,
            UPDATE_BOOK_COMMAND
    );


    /**
     * Controller path need authorization
     */
    private static final List<String> CONTROLLERS_NEED_AUTHORIZATION = Arrays.asList(
            READ_BOOK_CONTROLLER
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

        final String login = (String) session.getAttribute(RequestConstants.LOGIN);
        final String role = (String) session.getAttribute(RequestConstants.ROLE);

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

            long equalAdminCommands = ADMIN_COMMANDS.stream().
                    filter(s -> s.equals(req.getServletPath()))
                    .count();
            if (equalAdminCommands > 0) {
                res.sendRedirect(req.getContextPath() + HOME_PAGE);
                return;
            }
        }

        if (Objects.nonNull(login) && Objects.nonNull(role)
                && role.equals(RequestConstants.ADMIN_ROLE) && PERSONAL_PAGE_COMMAND.equals(req.getParameter(RequestConstants.COMMAND))) {
            res.sendRedirect(req.getContextPath() + HOME_PAGE);
            return;
        }

        chain.doFilter(req, res);
    }
}



