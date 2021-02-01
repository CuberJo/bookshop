package com.epam.bookshop.controller.command;

import com.epam.bookshop.controller.command.impl.*;

import java.util.Objects;


/**
 * Factory to create instance of {@link FrontCommand} by query parameter <b>command</b>
 */
public class CommandFactory {

    private static final String ACCOUNT = "account";
    private static final String ADMIN = "admin";
    private static final String BOOKS = "books";
    private static final String CART = "cart";
    private static final String BOOK_DETAILS = "book_details";
    private static final String SEARCH = "search";
    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String REGISTER = "register";
    private static final String FORGOT_PASSWORD = "forgot_password";
    private static final String RESET_PASSWORD = "reset_password";
    private static final String CONTACT_US = "contact_us";
    private static final String CHANGE_LOCALE = "change_locale";
    private static final String ADD_IBAN = "add_iban";
    private static final String SEND_CONTACT_FORM = "send_contact_form";
    private static final String ADD_TO_CART = "add_to_cart";
    private static final String REMOVE_FROM_CART = "remove_from_cart";
    private static final String PURCHASE = "purchase";
    private static final String CHOOSE_IBAN = "choose_iban";
    private static final String LOAD_IBANs = "load_ibans";
    private static final String FINISHED_PURCHASE = "finished_purchase";
    private static final String PERSONAL_PAGE = "personal_page";
    private static final String UNBIND_IBAN = "unbind_iban";
    private static final String DELETE_ACCOUNT = "delete_account";
    private static final String READ_BOOK = "read_book";


    public static FrontCommand command(String command) {

        if (Objects.isNull(command)) {
            return new HomeFrontCommand();
        }

        switch (command) {
            case ACCOUNT:
                return new AccountFrontCommand();
            case ADMIN:
                return new AdminFrontCommand();
            case BOOKS:
                return new BooksFrontCommand();
            case CART:
                return new CartFrontCommand();
            case BOOK_DETAILS:
                return new BookDetailsFrontCommand();
            case SEARCH:
                return new SearchFrontCommand();
            case LOGIN:
                return new LoginFrontCommand();
            case LOGOUT:
                return new LogoutFrontCommand();
            case REGISTER:
                return new RegisterFrontCommand();
            case FORGOT_PASSWORD:
                return new ForgotPasswordFrontCommand();
            case RESET_PASSWORD:
                return new ResetPasswordFrontCommand();
            case CONTACT_US:
                return new ContactUsFrontCommand();
            case CHANGE_LOCALE:
                return new ChangeLocaleFrontCommand();
            case SEND_CONTACT_FORM:
                return new SendContactFormFrontCommand();
//            case REMOVE_FROM_CART:
//                return new RemoveFromCartController();
            case PURCHASE:
                return new PurchaseFrontCommand();
            case CHOOSE_IBAN:
                return new ChooseIBAN();
            case ADD_IBAN:
                return new AddIBANFrontCommand();
            case FINISHED_PURCHASE:
                return new FinishedPurchaseFrontCommand();
            case PERSONAL_PAGE:
                return new PersonalPageFrontCommand();
            case DELETE_ACCOUNT:
                return new DeleteAccountFrontCommand();
//            case READ_BOOK:
//                return new ReadBookController();
//            case LOAD_IBANs:
//                return new LoadIBANsController();
            default:
                return new HomeFrontCommand();
        }
    }
}
