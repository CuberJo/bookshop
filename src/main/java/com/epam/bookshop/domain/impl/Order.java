package com.epam.bookshop.domain.impl;

import com.epam.bookshop.domain.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order extends Entity {
    private User user;
    private LocalDateTime orderTime;
    private Status status;
    List<Book> orderedBooks = new ArrayList<>();

    public Order() {

    }

    public Order(User user, LocalDateTime orderTime, Status status, List<Book> orderedBooks) {
        this.user = new User(user.getEntityId(), user.getName(), user.getLogin(), user.getPassword(), user.getEmail(), user.getRole(), user.getIBANs());
        this.orderTime = LocalDateTime.of(orderTime.getYear(), orderTime.getMonthValue(), orderTime.getDayOfMonth(), orderTime.getHour(), orderTime.getMinute(), orderTime.getSecond(), orderTime.getNano());
        this.status = new Status(status.getEntityId(), status.getStatus());
        this.orderedBooks.addAll(orderedBooks);
    }

    public Order(Long entityId, User user, LocalDateTime orderTime, Status  status, List<Book> orderedBooks) {
        super(entityId);
        this.user = new User(user.getEntityId(), user.getName(), user.getLogin(), user.getPassword(), user.getEmail(), user.getRole(), user.getIBANs());
        this.orderTime = LocalDateTime.of(orderTime.getYear(), orderTime.getMonthValue(), orderTime.getDayOfMonth(), orderTime.getHour(), orderTime.getMinute(), orderTime.getSecond(), orderTime.getNano());
        this.status = new Status(status.getEntityId(), status.getStatus());
        this.orderedBooks.addAll(orderedBooks);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = new User(user.getEntityId(), user.getName(), user.getLogin(), user.getPassword(), user.getEmail(), user.getRole(), user.getIBANs());
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = LocalDateTime.of(orderTime.getYear(), orderTime.getMonthValue(), orderTime.getDayOfMonth(), orderTime.getHour(), orderTime.getMinute(), orderTime.getSecond(), orderTime.getNano());
    }

    public Status  getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = new Status(status.getEntityId(), status.getStatus());
    }

    public List<Book> getOrderedBooks() {
        return orderedBooks;
    }

    public void setOrderedBooks(List<Book> orderedBooks) {
        this.orderedBooks.addAll(orderedBooks);
    }

    @Override
    public String toString() {
        return "Order{" +
                "entityId=" + entityId +
                ", user=" + user +
                ", orderTime=" + orderTime +
                ", status=" + status.getStatus() +
                ", orderedBooks=" + orderedBooks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;
        return user.equals(order.user) &&
                orderTime.equals(order.orderTime) &&
                status.equals(order.status) &&
                orderedBooks.equals(order.orderedBooks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, orderTime, status, orderedBooks);
    }
}
