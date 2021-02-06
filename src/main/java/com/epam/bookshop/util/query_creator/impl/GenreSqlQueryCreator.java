package com.epam.bookshop.util.query_creator.impl;

import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.util.query_creator.SqlQueryCreator;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.validator.impl.SqlQueryValidator;

import java.util.concurrent.locks.ReentrantLock;

public class GenreSqlQueryCreator implements SqlQueryCreator<Genre> {

    private static final String GENRE_ID_COLUMN = "Id";
    private static final String GENRE_COLUMN = "Genre";

    private static final String INCOMPATIBLE_TYPE_OF_CRITERIA = "incompatible_type_of_criteria";

    private static final ReentrantLock lock = new ReentrantLock();
    private static GenreSqlQueryCreator instance;

    public static GenreSqlQueryCreator getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new GenreSqlQueryCreator();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    private GenreSqlQueryCreator() {

    }

    @Override
    public String createQuery(Criteria<Genre> criteria, String operator) {
        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof GenreCriteria)) {
            String locale = "US";
            String incompatibleTypeOfCriteria = ErrorMessageManager.valueOf(locale).getMessage(INCOMPATIBLE_TYPE_OF_CRITERIA)
                    + UtilStringConstants.WHITESPACE + criteria;
            throw new UnknownEntityException(incompatibleTypeOfCriteria);
        }

        if (criteria.getEntityId() != null) {
            condition.append(GENRE_ID_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(criteria.getEntityId())
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }
        if (((GenreCriteria) criteria).getGenre() != null) {
            condition.append(GENRE_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append("'")
                    .append(((GenreCriteria) criteria).getGenre())
                    .append("'")
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }

        return SqlQueryValidator.getInstance().validatedQuery(condition);
    }
}
