package com.epam.bookshop.domain.impl;

import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.context.annotation.Naming;

import java.util.Objects;

public class Status extends Entity {

    @Naming(status = true)
    private java.lang.String status;

    public Status() {

    }

    public Status(java.lang.String status) {
        this.status = status;
    }

    public Status(Long entityId, java.lang.String status) {
        super(entityId);
        this.status = status;
    }

    @Override
    public java.lang.String toString() {
        return "Status{" +
                "entityId=" + entityId +
                ", status='" + status + '\'' +
                '}';
    }

    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Status status1 = (Status) o;
        return status.equals(status1.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), status);
    }
}
