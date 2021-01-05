package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.GenreCriteria;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.strategy.query_creator.FindEntityQueryCreator;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.validator.Validator;

public class FindGenreQueryCreator implements FindEntityQueryCreator {

    FindGenreQueryCreator() {

    }

    private static final String GENRE_ID_COLUMN = "Id";
    private static final String GENRE_COLUMN = "Genre";
    private static final String AND = "AND";
    private static final String WHITESPACE = " ";

    private static String sql_query = "SELECT Id, Genre " +
            "FROM TEST_LIBRARY.GENRE " +
            "WHERE ";


    @Override
    public String createQuery(Criteria<? extends Entity> criteria) {
        StringBuffer columnNames = new StringBuffer();
        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof GenreCriteria)) {
            throw new UnknownEntityException("Incompatible type of criteria");
        }

        if (criteria.getEntityId() != null) {
            condition.append(GENRE_ID_COLUMN + " = " + criteria.getEntityId() + WHITESPACE + AND + WHITESPACE);
        }
        if (((GenreCriteria) criteria).getGenre() != null) {
            condition.append(GENRE_COLUMN + " = '" + ((GenreCriteria) criteria).getGenre() +  "'" + WHITESPACE + AND + WHITESPACE);
        }

        return new Validator().validatedQuery(condition, sql_query);
    }
}
