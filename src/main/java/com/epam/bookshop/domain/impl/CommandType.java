package com.epam.bookshop.domain.impl;

import java.util.List;

public enum  CommandType {
    SHOW("show", false, "");

    private String commandName;
    private boolean isRedirect;
    private String beutyUrl;
    private String isVisibleWOLogin;
    //or
    private List<Role> availableRoles;

    CommandType(String commandName, boolean isRedirect, String beutyUrl) {
        this.commandName = commandName;
        this.isRedirect = isRedirect;
        this.beutyUrl = beutyUrl;
    }
}
