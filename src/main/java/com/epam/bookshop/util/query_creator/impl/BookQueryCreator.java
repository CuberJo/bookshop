package com.epam.bookshop.util.query_creator.impl;

import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.util.query_creator.EntityQueryCreator;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.validator.impl.Validator;

import java.util.concurrent.locks.ReentrantLock;

public class BookQueryCreator implements EntityQueryCreator<Book> {

    private static final String BOOK_ID_COLUMN = "Id";
    private static final String ISBN_COLUMN = "ISBN";
    private static final String TITLE_COLUMN = "Title";
    private static final String AUTHOR_COLUMN = "Author";
    private static final String PRICE_COLUMN = "Price";
    private static final String PUBLISHER_COLUMN = "Publisher";
    private static final String GENRE_ID_COLUMN = "Genre_Id";

    private static final ReentrantLock lock = new ReentrantLock();

    private static BookQueryCreator instance;

    public static BookQueryCreator getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new BookQueryCreator();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    private BookQueryCreator() {

    }

    @Override
    public String createQuery(Criteria<Book> criteria, String operator) {

        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof BookCriteria)) {
            String locale = "US";
            String incompatibleTypeOfCriteria = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INCOMPATIBLE_TYPE_OF_CRITERIA) + UtilStringConstants.NEW_LINE + criteria;
            throw new UnknownEntityException(incompatibleTypeOfCriteria);
        }

        if (criteria.getEntityId() != null) {
            condition.append(BOOK_ID_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(criteria.getEntityId())
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }
        if (((BookCriteria) criteria).getISBN() != null) {
            condition.append(ISBN_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append("'")
                    .append(((BookCriteria) criteria).getISBN())
                    .append("'")
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }
        if (((BookCriteria) criteria).getTitle() != null) {
            condition.append(TITLE_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append("'")
                    .append(((BookCriteria) criteria).getTitle())
                    .append("'")
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }
        if (((BookCriteria) criteria).getAuthor() != null) {
            condition.append(AUTHOR_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append("'")
                    .append(((BookCriteria) criteria).getAuthor())
                    .append("'")
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND).
                    append(UtilStringConstants.WHITESPACE);
        }
        if (((BookCriteria) criteria).getPrice() != null) {
            condition.append(PRICE_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append("'")
                    .append(((BookCriteria) criteria).getPrice())
                    .append("'")
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }
        if (((BookCriteria) criteria).getPublisher() != null) {
            condition.append(PUBLISHER_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append("'")
                    .append(((BookCriteria) criteria).getPublisher())
                    .append("'")
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }
        if (((BookCriteria) criteria).getGenreId() != null) {
            condition.append(GENRE_ID_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append("'")
                    .append(((BookCriteria) criteria).getGenreId())
                    .append("'")
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }

        return new Validator().validatedQuery(condition);
    }
}
