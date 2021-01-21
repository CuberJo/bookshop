package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.PaymentCriteria;
import com.epam.bookshop.domain.impl.Payment;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.strategy.query_creator.EntityQueryCreator;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;

import java.util.concurrent.locks.ReentrantLock;

public class OrderQueryCreator implements EntityQueryCreator<Payment> {

    private static final String ORDER_ID_COLUMN = "Id";
    private static final String LIBRARY_USER_ID_COLUMN = "Library_User_Id";
    private static final String ORDER_TIME_COLUMN = "Order_Time";
    private static final String STATUS_ID_COLUMN = "Status_Id";

    private static final String locale = "US";

    private static final ReentrantLock lock = new ReentrantLock();

    private static OrderQueryCreator instance;

    public static OrderQueryCreator getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new OrderQueryCreator();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    private OrderQueryCreator() {

    }

    @Override
    public String createQuery(Criteria<Payment> criteria) {
        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof PaymentCriteria)) {
            String incompatibleTypeOfCriteria = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INCOMPATIBLE_TYPE_OF_CRITERIA)
                    + UtilStrings.WHITESPACE + criteria;
            throw new UnknownEntityException(incompatibleTypeOfCriteria);
        }

        if (criteria.getEntityId() != null) {
            condition.append(ORDER_ID_COLUMN + " = ")
                    .append(criteria.getEntityId())
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((PaymentCriteria) criteria).getLibraryUserId() != null) {
            condition.append(LIBRARY_USER_ID_COLUMN + " = '")
                    .append(((PaymentCriteria) criteria).getLibraryUserId())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((PaymentCriteria) criteria).getOrderTime() != null) {
            condition.append(ORDER_TIME_COLUMN + " = '")
                    .append(((PaymentCriteria) criteria).getOrderTime())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((PaymentCriteria) criteria).getStatusId() != null) {
            condition.append(STATUS_ID_COLUMN + " = '")
                    .append(((PaymentCriteria) criteria).getStatusId())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }

        return new Validator().validatedQuery(condition);
    }
}
