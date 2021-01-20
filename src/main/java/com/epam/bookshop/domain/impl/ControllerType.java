package com.epam.bookshop.domain.impl;

public enum ControllerType {

    ADD_TO_CART("add_to_cart", true),
    REMOVE_FROM_CART("remove_from_cart", false),
    LOAD_IBANs("load_ibans", false),
    UNBIND_IBAN("unbind_iban", false),
    READ_BOOK("read_book", false);

    private String controller;
    private boolean isVisibleWOLogin;

    ControllerType(String controller, boolean isVisibleWOLogin) {
        this.controller = controller;
        this.isVisibleWOLogin = isVisibleWOLogin;
    }
}
