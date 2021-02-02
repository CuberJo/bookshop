package com.epam.bookshop.domain.impl;

import com.epam.bookshop.util.annotation.Naming;
import com.epam.bookshop.domain.Entity;

import java.util.Objects;

/**
 * Book genre representation
 */
public class Genre extends Entity {

    @Naming(genre = true)
    private String genre;

    public Genre() {

    }

    public Genre(java.lang.String genre) {
        this.genre = genre;
    }

    public Genre(Long entityId, java.lang.String genre) {
        super(entityId);
        this.genre = genre;
    }

    public java.lang.String getGenre() {
        return genre;
    }

    public void setGenre(java.lang.String genre) {
        this.genre = genre;
    }

    @Override
    public java.lang.String toString() {
        return "Genre{" +
                "entityId=" + entityId +
                ", genre='" + genre + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Genre genre1 = (Genre) o;
        return genre.equals(genre1.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), genre);
    }
}
