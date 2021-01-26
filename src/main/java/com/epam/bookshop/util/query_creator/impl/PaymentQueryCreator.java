package com.epam.bookshop.util.query_creator.impl;

import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.PaymentCriteria;
import com.epam.bookshop.domain.impl.Payment;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.util.query_creator.EntityQueryCreator;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.util.validator.impl.Validator;

import java.util.concurrent.locks.ReentrantLock;

public class PaymentQueryCreator implements EntityQueryCreator<Payment> {

    private static final String PAYMENT_ID_COLUMN = "Id";
    private static final String LIBRARY_USER_ID_COLUMN = "Library_User_Id";
    private static final String BOOK_ID_COLUMN = "Book_Id";
    private static final String PAYMENT_TIME_COLUMN = "Payment_Time";
    private static final String PRICE_COLUMN = "Price";

    private static final ReentrantLock lock = new ReentrantLock();

    private static PaymentQueryCreator instance;

    public static PaymentQueryCreator getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new PaymentQueryCreator();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    private PaymentQueryCreator() {

    }

    @Override
    public String createQuery(Criteria<Payment> criteria, String operator) {
        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof PaymentCriteria)) {
            String locale = "US";
            String incompatibleTypeOfCriteria = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INCOMPATIBLE_TYPE_OF_CRITERIA)
                    + UtilStrings.WHITESPACE + criteria;
            throw new UnknownEntityException(incompatibleTypeOfCriteria);
        }

        if (criteria.getEntityId() != null) {
            condition.append(PAYMENT_ID_COLUMN)
                    .append(UtilStrings.WHITESPACE)
                    .append(operator)
                    .append(UtilStrings.WHITESPACE)
                    .append(criteria.getEntityId())
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((PaymentCriteria) criteria).getLibraryUserId() != null) {
            condition.append(LIBRARY_USER_ID_COLUMN)
                    .append(UtilStrings.WHITESPACE)
                    .append(operator)
                    .append(UtilStrings.WHITESPACE)
                    .append("'")
                    .append(((PaymentCriteria) criteria).getLibraryUserId())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((PaymentCriteria) criteria).getBookId() != null) {
            condition.append(BOOK_ID_COLUMN)
                    .append(UtilStrings.WHITESPACE)
                    .append(operator)
                    .append(UtilStrings.WHITESPACE)
                    .append("'")
                    .append(((PaymentCriteria) criteria).getBookId())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((PaymentCriteria) criteria).getPaymentTime() != null) {
            condition.append(PAYMENT_TIME_COLUMN)
                    .append(UtilStrings.WHITESPACE)
                    .append(operator)
                    .append(UtilStrings.WHITESPACE)
                    .append("'")
                    .append(((PaymentCriteria) criteria).getPaymentTime())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((PaymentCriteria) criteria).getPrice() != null) {
            condition.append(PRICE_COLUMN)
                    .append(UtilStrings.WHITESPACE)
                    .append(operator)
                    .append(UtilStrings.WHITESPACE)
                    .append("'")
                    .append(((PaymentCriteria) criteria).getPrice())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }

        return new Validator().validatedQuery(condition);
    }
}
