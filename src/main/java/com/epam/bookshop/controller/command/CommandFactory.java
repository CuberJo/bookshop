package com.epam.bookshop.controller.command;

import com.epam.bookshop.controller.command.impl.*;

import java.util.Objects;

public class CommandFactory {

    private static final String BOOKS = "books";
    private static final String ACCOUNT = "account";
    private static final String CART = "cart";
    private static final String BOOK_DETAILS = "book-details";
    private static final String SEARCH = "search";
    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String REGISTER = "register";
    private static final String FORGOT_PASSWORD = "forgot_password";
    private static final String RESET_PASSWORD = "reset_password";
    private static final String CONTACT_US = "contact_us";
    private static final String CHANGE_LOCALE = "change_locale";



    public static Command command(String command) {
        if (Objects.isNull(command)) {
            return new HomeCommand();
        }

        switch (command) {
            case BOOKS:
                return new BooksCommand();
            case ACCOUNT:
                return new AccountCommand();
            case CART:
                return new CartCommand();
            case BOOK_DETAILS:
                return new BookDetailsCommand();
            case SEARCH:
                return new SearchCommand();
            case LOGIN:
                return new LoginCommand();
            case LOGOUT:
                return new LogoutCommand();
            case REGISTER:
                return new RegisterCommand();
            case FORGOT_PASSWORD:
                return new ForgotPasswordCommand();
            case RESET_PASSWORD:
                return new ResetPasswordCommand();
            case CONTACT_US:
                return new ContactUsCommand();
            case CHANGE_LOCALE:
                return new ChangeLocaleCommand();
            default:
                return new HomeCommand();
        }
    }
}
