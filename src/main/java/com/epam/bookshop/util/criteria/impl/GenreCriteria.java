package com.epam.bookshop.util.criteria.impl;

import com.epam.bookshop.util.annotation.Naming;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.util.criteria.Criteria;

public class GenreCriteria extends Criteria<Genre> {

    @Naming(genre = true)
    private String genre;

    public GenreCriteria(Criteria.Builder<? extends Criteria.Builder> builder) {
        super(builder);
    }

    public String  getGenre() {
        return genre;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Criteria.Builder<BookCriteria.Builder> {

        @Naming(genre = true)
        private String genre;

        private Builder() {

        }

        public GenreCriteria.Builder genre(String genre) {
            this.genre = genre;
            return this;
        }

        @Override
        public GenreCriteria build() {
            GenreCriteria criteria = new GenreCriteria(this);
            criteria.genre = genre;

            return criteria;
        }
    }
}
