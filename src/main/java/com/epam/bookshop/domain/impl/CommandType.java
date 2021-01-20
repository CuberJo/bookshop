package com.epam.bookshop.domain.impl;

public enum CommandType {

    BOOKS("books", true),
    ACCOUNT("account", false),
    CART("cart", false),
    BOOK_DETAILS("book_details", false),
    SEARCH("search", true),
    LOGIN("login", true),
    LOGOUT("logout", false),
    REGISTER("register", true),
    FORGOT_PASSWORD("forgot_password", true),
    RESET_PASSWORD("reset_password", true),
    CONTACT_US("contact_us", true),
    CHANGE_LOCALE("change_locale", true),
    ADD_IBAN("add_iban", false),
    SEND_CONTACT_FORM("send_contact_form", true),
    PURCHASE("purchase", false),
    CHOOSE_IBAN("choose_iban", false),
    FINISHED_PURCHASE("finished_purchase", false),
    PERSONAL_PAGE("personal_page", false),
    DELETE_ACCOUNT("delete_account", false),
    READ_BOOK("read_book", false);

    private String command;
    private boolean isVisibleWOLogin;

    CommandType(String command, boolean isVisibleWOLogin) {
        this.command = command;
        this.isVisibleWOLogin = isVisibleWOLogin;
    }
}
