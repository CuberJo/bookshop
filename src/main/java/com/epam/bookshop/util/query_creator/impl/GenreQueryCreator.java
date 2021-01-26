package com.epam.bookshop.util.query_creator.impl;

import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.util.query_creator.EntityQueryCreator;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.util.validator.impl.Validator;

import java.util.concurrent.locks.ReentrantLock;

public class GenreQueryCreator implements EntityQueryCreator<Genre> {

    private static final String GENRE_ID_COLUMN = "Id";
    private static final String GENRE_COLUMN = "Genre";

    private static final String INCOMPATIBLE_TYPE_OF_CRITERIA = "incompatible_type_of_criteria";

    private static final ReentrantLock lock = new ReentrantLock();

    private static GenreQueryCreator instance;

    public static GenreQueryCreator getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new GenreQueryCreator();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    private GenreQueryCreator() {

    }

    @Override
    public String createQuery(Criteria<Genre> criteria, String operator) {
        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof GenreCriteria)) {
            String locale = "US";
            String incompatibleTypeOfCriteria = ErrorMessageManager.valueOf(locale).getMessage(INCOMPATIBLE_TYPE_OF_CRITERIA)
                    + UtilStrings.WHITESPACE + criteria;
            throw new UnknownEntityException(incompatibleTypeOfCriteria);
        }

        if (criteria.getEntityId() != null) {
            condition.append(GENRE_ID_COLUMN)
                    .append(UtilStrings.WHITESPACE)
                    .append(operator)
                    .append(UtilStrings.WHITESPACE)
                    .append(criteria.getEntityId())
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((GenreCriteria) criteria).getGenre() != null) {
            condition.append(GENRE_COLUMN)
                    .append(UtilStrings.WHITESPACE)
                    .append(operator)
                    .append(UtilStrings.WHITESPACE)
                    .append("'")
                    .append(((GenreCriteria) criteria).getGenre())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }

        return new Validator().validatedQuery(condition);
    }
}
