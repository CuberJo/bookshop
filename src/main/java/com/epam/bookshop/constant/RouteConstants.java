package com.epam.bookshop.constant;

/**
 * Enumeration of web pages used in application to be redirected
 */
public enum RouteConstants {
    ACCOUNT("/home?command=account"),
    ADD_IBAN("/home?command=add_iban_page"),
    BOOK_SEARCH_RESULTS("/home?command=book_search_results"),
    CART("/home?command=cart"),
    CHOOSE_IBAN("/home?command=choose_iban"),
    CONTACT_US("/home?command=contact_us"),
    FINISHED_PURCHASE("/home?command=finished_purchase"),
    FORGOT_PASSWORD("/home?command=forgot_password"),
    HOME("/home"),
    PERSONAL_PAGE("/home?command=personal_page");

    private final String route;

    RouteConstants(final String route) {
        this.route = route;
    }

    public String getRoute() {
        return route;
    }
}