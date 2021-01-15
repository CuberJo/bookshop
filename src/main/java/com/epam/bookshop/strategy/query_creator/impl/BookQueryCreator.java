package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.BookCriteria;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.strategy.query_creator.EntityQueryCreator;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;

import java.util.concurrent.locks.ReentrantLock;

public class BookQueryCreator implements EntityQueryCreator<Book> {

    private static final String BOOK_ID_COLUMN = "Id";
    private static final String ISBN_COLUMN = "ISBN";
    private static final String TITLE_COLUMN = "Title";
    private static final String AUTHOR_COLUMN = "Author";
    private static final String PRICE_COLUMN = "Price";
    private static final String PUBLISHER_COLUMN = "Publisher";
    private static final String GENRE_ID_COLUMN = "Genre_Id";

    private static final String locale = "US";

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
    public String createQuery(Criteria<Book> criteria) {

        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof BookCriteria)) {
            String incompatibleTypeOfCriteria = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INCOMPATIBLE_TYPE_OF_CRITERIA) + UtilStrings.NEW_LINE + criteria;
            throw new UnknownEntityException(incompatibleTypeOfCriteria);
        }

        if (criteria.getEntityId() != null) {
            condition.append(BOOK_ID_COLUMN + " = ")
                    .append(criteria.getEntityId())
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((BookCriteria) criteria).getISBN() != null) {
            condition.append(ISBN_COLUMN + " = '")
                    .append(((BookCriteria) criteria).getISBN())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((BookCriteria) criteria).getTitle() != null) {
            condition.append(TITLE_COLUMN + " = '")
                    .append(((BookCriteria) criteria).getTitle())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((BookCriteria) criteria).getAuthor() != null) {
            condition.append(AUTHOR_COLUMN + " = '")
                    .append(((BookCriteria) criteria).getAuthor())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND).
                    append(UtilStrings.WHITESPACE);
        }
        if (((BookCriteria) criteria).getPrice() != null) {
            condition.append(PRICE_COLUMN + " = '")
                    .append(((BookCriteria) criteria).getPrice())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((BookCriteria) criteria).getPublisher() != null) {
            condition.append(PUBLISHER_COLUMN + " = '")
                    .append(((BookCriteria) criteria).getPublisher())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((BookCriteria) criteria).getGenreId() != null) {
            condition.append(GENRE_ID_COLUMN + " = '")
                    .append(((BookCriteria) criteria).getGenreId())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }

        return new Validator().validatedQuery(condition);
    }
}
