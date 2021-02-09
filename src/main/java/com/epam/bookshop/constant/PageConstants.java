package com.epam.bookshop.constant;

/**
 * Enumeration of web pages used in application to be forwarded
 */
public enum PageConstants {
    ACCOUNT("/WEB-INF/jsp/account.jsp"),
    ADMIN("/WEB-INF/jsp/admin.jsp"),
    BOOKS("/WEB-INF/jsp/books.jsp"),
    BOOK_DETAILS("/WEB-INF/jsp/book_details.jsp"),
    BOOK_SEARCH_RESULTS("/WEB-INF/jsp/book_search_results.jsp"),
    CART("/WEB-INF/jsp/cart.jsp"),
    CHOOSE_IBAN("/WEB-INF/jsp/choose_iban.jsp"),
    CONTACT_US("/WEB-INF/jsp/contact_us.jsp"),
    FINISHED_PURCHASE("/WEB-INF/jsp/finished_purchase.jsp"),
    FORGOT_PASSWORD("/WEB-INF/jsp/forgot_password.jsp"),
    HOME("/WEB-INF/jsp/home.jsp"),
    ADD_IBAN("/WEB-INF/jsp/add_iban.jsp"),
    NOT_FOUND("/404error.jsp"),
    PERSONAL_PAGE("/WEB-INF/jsp/personal_page.jsp"),
    SEARCH("/WEB-INF/jsp/search.jsp"),

    WELCOME_PAGE("/welcome-page.jsp");

    private final String page;

    PageConstants(final String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }
}