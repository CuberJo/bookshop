package com.epam.bookshop.util.query_creator.impl;

import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.PaymentCriteria;
import com.epam.bookshop.domain.impl.Payment;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.util.query_creator.SqlConditionQueryCreator;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.validator.impl.SqlQueryValidator;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Creates sql query condition for {@link Payment} by incoming {@link Criteria}
 */
public class PaymentSqlConditionQueryCreator implements SqlConditionQueryCreator<Payment> {

    private static final String PAYMENT_ID_COLUMN = "Id";
    private static final String LIBRARY_USER_ID_COLUMN = "Library_User_Id";
    private static final String BOOK_ID_COLUMN = "Book_Id";
    private static final String PAYMENT_TIME_COLUMN = "Payment_Time";
    private static final String PRICE_COLUMN = "Price";

    private static final ReentrantLock lock = new ReentrantLock();
    private static PaymentSqlConditionQueryCreator instance;

    public static PaymentSqlConditionQueryCreator getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new PaymentSqlConditionQueryCreator();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    private PaymentSqlConditionQueryCreator() {

    }

    @Override
    public String createQuery(Criteria<Payment> criteria, String operator) {
        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof PaymentCriteria)) {
            String locale = "US";
            String incompatibleTypeOfCriteria = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INCOMPATIBLE_TYPE_OF_CRITERIA)
                    + UtilStringConstants.WHITESPACE + criteria;
            throw new UnknownEntityException(incompatibleTypeOfCriteria);
        }

        if (criteria.getEntityId() != null) {
            condition.append(PAYMENT_ID_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(criteria.getEntityId())
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }
        if (((PaymentCriteria) criteria).getLibraryUserId() != null) {
            condition.append(LIBRARY_USER_ID_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append("'")
                    .append(((PaymentCriteria) criteria).getLibraryUserId())
                    .append("'")
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }
        if (((PaymentCriteria) criteria).getBookId() != null) {
            condition.append(BOOK_ID_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append("'")
                    .append(((PaymentCriteria) criteria).getBookId())
                    .append("'")
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }
        if (((PaymentCriteria) criteria).getPaymentTime() != null) {
            condition.append(PAYMENT_TIME_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append("'")
                    .append(((PaymentCriteria) criteria).getPaymentTime())
                    .append("'")
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }
        if (((PaymentCriteria) criteria).getPrice() != null) {
            condition.append(PRICE_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append("'")
                    .append(((PaymentCriteria) criteria).getPrice())
                    .append("'")
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }

        return SqlQueryValidator.getInstance().validatedQuery(condition);
    }
}
