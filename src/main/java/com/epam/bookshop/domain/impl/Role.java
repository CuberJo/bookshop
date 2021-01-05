package com.epam.bookshop.domain.impl;

import com.epam.bookshop.context.annotation.Naming;
import com.epam.bookshop.domain.Entity;

import java.util.Objects;

public class Role extends Entity {

    @Naming(role = true)
    private java.lang.String role;

    public Role() {

    }

    public Role(java.lang.String role) {
        this.role = role;
    }

    public Role(Long entityId, java.lang.String role) {
        super(entityId);
        this.role = role;
    }

    public java.lang.String getRole() {
        return role;
    }

    public void setRole(java.lang.String role) {
        this.role = role;
    }

    @Override
    public java.lang.String toString() {
        return "Role{" +
                "entityId=" + entityId +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Role role1 = (Role) o;
        return role.equals(role1.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), role);
    }
}
