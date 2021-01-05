package com.epam.bookshop.domain.impl;

import com.epam.bookshop.context.annotation.Naming;
import com.epam.bookshop.domain.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity {

    @Naming(name = true)
    private String name;

    @Naming(login = true)
    private String login;

    private String password;

    @Naming(email = true)
    private String email;

    private Role role;
    private List<String> IBANs = new ArrayList<>();

    public User() {

    }

    public User(String name, String login, String password, String email, Role role) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(Long entityId, String name, String login, String password, String email, Role role) {
        super(entityId);
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(String name, String login, String password, String email, Role role, List<String> IBANs) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = new Role(role.getEntityId(), role.getRole());
        this.IBANs.addAll(IBANs);
    }

    public User(Long entityId, String name, String login, String password, String email, Role role, List<String> IBANs) {
        super(entityId);
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = new Role(role.getEntityId(), role.getRole());
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = new Role(role.getEntityId(), role.getRole());
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
                ", role=" + role.getRole() +
                ", IBAN='" + IBANs + '\'' +
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
                role.equals(user.role) &&
                IBANs.equals(user.IBANs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, login, password, email, role, IBANs);
    }
}
