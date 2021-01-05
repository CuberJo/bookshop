package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.BookCriteria;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.strategy.query_creator.FindEntityQueryCreator;
import com.epam.bookshop.validator.Validator;

public class FindBookQueryCreator implements FindEntityQueryCreator {

    FindBookQueryCreator() {

    }

    private static final String BOOK_ID_COLUMN = "Id";
    private static final String ISBN_COLUMN = "ISBN";
    private static final String TITLE_COLUMN = "Title";
    private static final String AUTHOR_COLUMN = "Author";
    private static final String PRICE_COLUMN = "Price";
    private static final String PUBLISHER_COLUMN = "Publisher";
    private static final String GENRE_ID_COLUMN = "Genre_Id";
    private static final String AND = "AND";
    private static final String WHITESPACE = " ";

    private static String sql_query = "SELECT Id, ISBN, Title, Author, Price, Publisher, Genre_Id, Preview " +
            "FROM TEST_LIBRARY.BOOK " +
            "WHERE ";

    @Override
    public String createQuery(Criteria<? extends Entity> criteria) {

        StringBuffer columnNames = new StringBuffer();
        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof BookCriteria)) {
            throw new UnknownEntityException("Incompatible type of criteria");
        }

        if (criteria.getEntityId() != null) {
            condition.append(BOOK_ID_COLUMN + " = " + criteria.getEntityId() + WHITESPACE + AND + WHITESPACE);
        }
        if (((BookCriteria) criteria).getISBN() != null) {
            condition.append(ISBN_COLUMN + " = '" + ((BookCriteria) criteria).getISBN() + "'" + WHITESPACE + AND + WHITESPACE);
        }
        if (((BookCriteria) criteria).getTitle() != null) {
            condition.append(TITLE_COLUMN + " = '" + ((BookCriteria) criteria).getTitle() + "'" + WHITESPACE + AND + WHITESPACE);
        }
        if (((BookCriteria) criteria).getAuthor() != null) {
            condition.append(AUTHOR_COLUMN + " = '" + ((BookCriteria) criteria).getAuthor() + "'" + WHITESPACE + AND + WHITESPACE);
        }
        if (((BookCriteria) criteria).getPrice() != null) {
            condition.append(PRICE_COLUMN + " = '" + ((BookCriteria) criteria).getPrice() + "'" + WHITESPACE + AND + WHITESPACE);
        }
        if (((BookCriteria) criteria).getPublisher() != null) {
            condition.append(PUBLISHER_COLUMN + " = '" + ((BookCriteria) criteria).getPublisher() + "'" + WHITESPACE + AND + WHITESPACE);
        }
        if (((BookCriteria) criteria).getGenreId() != null) {
            condition.append(GENRE_ID_COLUMN + " = '" + ((BookCriteria) criteria).getGenreId() + "'" + WHITESPACE + AND + WHITESPACE);
        }

        return Validator.getInstance().validatedQuery(condition, sql_query);
    }
}
