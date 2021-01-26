package com.epam.bookshop.util.criteria.impl;

import com.epam.bookshop.context.annotation.Naming;
import com.epam.bookshop.context.annotation.Size;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.util.criteria.Criteria;

public class BookCriteria extends Criteria<Book> {

    @Size(size = 17)
    @Naming(ISBN = true)
    private String ISBN;

    @Naming(title = true)
    private String title;

    @Naming(author = true)
    private String author;
    private Double price;

    @Naming(publisher = true)
    private String publisher;

    private Long genreId;

    public BookCriteria(Criteria.Builder<? extends Criteria.Builder> builder) {
        super(builder);
    }

    public String getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Double getPrice() {
        return price;
    }

    public String getPublisher() {
        return publisher;
    }

    public Long getGenreId() {
        return genreId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Criteria.Builder<BookCriteria.Builder> {

        @Size(size = 17)
        @Naming(ISBN = true)
        private String ISBN;

        @Naming(title = true)
        private String title;

        @Naming(author = true)
        private String author;
        private Double price;

        @Naming(publisher = true)
        private String publisher;

        private Long genreId;

        private Builder() {

        }

        public Builder ISBN(String ISBN) {
            this.ISBN = ISBN;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public Builder genreId(Long genreId) {
            this.genreId = genreId;
            return this;
        }

        @Override
        public BookCriteria build() {
            BookCriteria criteria = new BookCriteria(this);
            criteria.ISBN = ISBN;
            criteria.title = title;
            criteria.author = author;
            criteria.price = price;
            criteria.publisher = publisher;
            criteria.genreId = genreId;

            return criteria;
        }
    }
}
