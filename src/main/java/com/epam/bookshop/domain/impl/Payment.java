package com.epam.bookshop.domain.impl;

import com.epam.bookshop.domain.Entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Payment extends Entity {
    private User user;
    private Book book;
    private LocalDateTime paymentTime;
    private double price;

    public Payment() {

    }

    public Payment(User user, Book book, LocalDateTime paymentTime, double price) {
        this.user = new User(user.getEntityId(), user.getName(), user.getLogin(), user.getPassword(), user.getEmail(), user.isAdmin(), user.getIBANs());
        this.book = new Book(book.getEntityId(), book.getISBN(), book.getTitle(), book.getAuthor(), book.getPrice(), book.getPublisher(), book.getGenre(), book.getPreview());
        this.paymentTime = LocalDateTime.of(paymentTime.getYear(), paymentTime.getMonthValue(), paymentTime.getDayOfMonth(), paymentTime.getHour(), paymentTime.getMinute(), paymentTime.getSecond(), paymentTime.getNano());
        this.price = price;
    }

    public Payment(Long entityId, User user, Book book, LocalDateTime paymentTime, double price) {
        super(entityId);
        this.user = new User(user.getEntityId(), user.getName(), user.getLogin(), user.getPassword(), user.getEmail(), user.isAdmin(), user.getIBANs());
        this.book = new Book(book.getEntityId(), book.getISBN(), book.getTitle(), book.getAuthor(), book.getPrice(), book.getPublisher(), book.getGenre(), book.getPreview());
        this.paymentTime = LocalDateTime.of(paymentTime.getYear(), paymentTime.getMonthValue(), paymentTime.getDayOfMonth(), paymentTime.getHour(), paymentTime.getMinute(), paymentTime.getSecond(), paymentTime.getNano());
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = new User(user.getEntityId(), user.getName(), user.getLogin(), user.getPassword(), user.getEmail(), user.isAdmin(), user.getIBANs());
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = new Book(book.getEntityId(), book.getISBN(), book.getTitle(), book.getAuthor(), book.getPrice(), book.getPublisher(), book.getGenre(), book.getPreview());
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = LocalDateTime.of(paymentTime.getYear(), paymentTime.getMonthValue(), paymentTime.getDayOfMonth(), paymentTime.getHour(), paymentTime.getMinute(), paymentTime.getSecond(), paymentTime.getNano());
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "user=" + user +
                ", book=" + book +
                ", orderTime=" + paymentTime +
                ", price=" + price +
                ", entityId=" + entityId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Payment payment = (Payment) o;
        return Double.compare(payment.price, price) == 0 &&
                user.equals(payment.user) &&
                book.equals(payment.book) &&
                paymentTime.equals(payment.paymentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, book, paymentTime, price);
    }
}
