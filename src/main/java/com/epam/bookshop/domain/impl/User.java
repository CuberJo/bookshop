package com.epam.bookshop.domain.impl;

import com.epam.bookshop.util.annotation.Naming;
import com.epam.bookshop.domain.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User representation
 */
public class User extends Entity {

    @Naming(name = true)
    private String name;

    @Naming(login = true)
    private String login;

    private String password;

    @Naming(email = true)
    private String email;

    private boolean admin;
    private List<String> IBANs = new ArrayList<>();

    public User() {

    }

    public User(String name, String login, String password, String email, boolean admin) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.admin = admin;
    }

    public User(Long entityId, String name, String login, String password, String email, boolean admin) {
        super(entityId);
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.admin = admin;
    }

    public User(String name, String login, String password, String email, boolean admin, List<String> IBANs) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.admin = admin;
        this.IBANs.addAll(IBANs);
    }

    public User(Long entityId, String name, String login, String password, String email, boolean admin, List<String> IBANs) {
        super(entityId);
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.admin = admin;
        this.IBANs.addAll(IBANs);
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getLogin() {
        return login;
    }

    public void setLogin(java.lang.String login) {
        this.login = login;
    }

    public java.lang.String getPassword() {
        return password;
    }

    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    public java.lang.String getEmail() {
        return email;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<java.lang.String> getIBANs() {
        return IBANs;
    }

    public void setIBANs(List<java.lang.String> IBANs) {
        this.IBANs.addAll(IBANs);
    }

    @Override
    public java.lang.String toString() {
        return "User{" +
                "entityId=" + entityId +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", admin=" + admin + '\'' +
                ", IBANs='" + IBANs + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return name.equals(user.name) &&
                login.equals(user.login) &&
                password.equals(user.password) &&
                email.equals(user.email) &&
                admin == user.admin &&
                IBANs.equals(user.IBANs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, login, password, email, admin, IBANs);
    }
}
