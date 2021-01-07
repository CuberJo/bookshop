package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.GenreCriteria;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.strategy.query_creator.FindEntityQueryCreator;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;

public class FindGenreQueryCreator implements FindEntityQueryCreator {

    private static final String GENRE_ID_COLUMN = "Id";
    private static final String GENRE_COLUMN = "Genre";
    private static final String AND = "AND";
    private static final String WHITESPACE = " ";

    private static String sql_query = "SELECT Id, Genre " +
            "FROM TEST_LIBRARY.GENRE " +
            "WHERE ";

    private static final String INCOMPATIBLE_TYPE_OF_CRITERIA = "incompatible_type_of_criteria";
    private String locale = "EN";

    FindGenreQueryCreator() {

    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }


    @Override
    public String createQuery(Criteria<? extends Entity> criteria) {
        StringBuffer columnNames = new StringBuffer();
        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof GenreCriteria)) {
            String incompatibleTypeOfCriteria = ErrorMessageManager.valueOf(locale).getMessage(INCOMPATIBLE_TYPE_OF_CRITERIA) + WHITESPACE + criteria;
            throw new UnknownEntityException(incompatibleTypeOfCriteria);
        }

        if (criteria.getEntityId() != null) {
            condition.append(GENRE_ID_COLUMN + " = " + criteria.getEntityId() + WHITESPACE + AND + WHITESPACE);
        }
        if (((GenreCriteria) criteria).getGenre() != null) {
            condition.append(GENRE_COLUMN + " = '" + ((GenreCriteria) criteria).getGenre() +  "'" + WHITESPACE + AND + WHITESPACE);
        }

        Validator validator = new Validator();
        validator.setLocale(locale);
        return validator.validatedQuery(condition, sql_query);
    }
}
