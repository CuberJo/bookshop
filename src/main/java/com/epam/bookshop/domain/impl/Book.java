package com.epam.bookshop.domain.impl;

import com.epam.bookshop.context.annotation.Naming;
import com.epam.bookshop.context.annotation.Size;
import com.epam.bookshop.domain.Entity;

import java.io.Serializable;
import java.util.Objects;

public class Book extends Entity implements Serializable {

    @Size(size = 17)
    @Naming(ISBN = true)
    private java.lang.String ISBN;

    @Naming(title = true)
    private java.lang.String title;

    @Naming(author = true)
    private String author;

    private double price;

    @Naming(publisher = true)
    private java.lang.String publisher;

    private Genre genre;
    private java.lang.String preview;

    public Book() {

    }

    public Book(java.lang.String ISBN, java.lang.String title, java.lang.String author, double price, java.lang.String publisher, Genre genre, java.lang.String preview) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.price = price;
        this.publisher = publisher;
        this.genre = new Genre(genre.getEntityId(), genre.getGenre());
        this.preview = preview;
    }

    public Book(Long entityId, java.lang.String ISBN, java.lang.String title, java.lang.String author, double price, java.lang.String publisher, Genre genre, java.lang.String preview) {
        super(entityId);
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.price = price;
        this.publisher = publisher;
        this.genre = new Genre(genre.getEntityId(), genre.getGenre());
        this.preview = preview;
    }

    public java.lang.String getISBN() {
        return ISBN;
    }

    public void setISBN(java.lang.String ISBN) {
        this.ISBN = ISBN;
    }

    public java.lang.String getTitle() {
        return title;
    }

    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    public java.lang.String getAuthor() {
        return author;
    }

    public void setAuthor(java.lang.String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public java.lang.String getPublisher() {
        return publisher;
    }

    public void setPublisher(java.lang.String publisher) {
        this.publisher = publisher;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = new Genre(genre.getEntityId(), genre.getGenre());
    }

    public java.lang.String getPreview() {
        return preview;
    }

    public void setPreview(java.lang.String preview) {
        this.preview = preview;
    }

    @Override
    public java.lang.String toString() {
        return "Book{" +
                "Id='" + entityId + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", publisher='" + publisher + '\'' +
                ", genre=" + genre.getGenre() +
                ", preview=" + preview +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Book book = (Book) o;
        return Double.compare(book.price, price) == 0 &&
                ISBN.equals(book.ISBN) &&
                title.equals(book.title) &&
                author.equals(book.author) &&
                publisher.equals(book.publisher) &&
                genre.equals(book.genre) &&
                preview.equals(book.preview);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ISBN, title, author, price, publisher, genre, preview);
    }
}
