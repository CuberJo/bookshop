package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.OrderCriteria;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.strategy.query_creator.FindEntityQueryCreator;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;

public class FindOrderQueryCreator implements FindEntityQueryCreator {

    private static final String ORDER_ID_COLUMN = "Id";
    private static final String LIBRARY_USER_ID_COLUMN = "Library_User_Id";
    private static final String ORDER_TIME_COLUMN = "Order_Time";
    private static final String STATUS_ID_COLUMN = "Status_Id";
    private static final String AND = "AND";
    private static final String WHITESPACE = " ";

    private static String sql_query = "SELECT Id, Library_User_Id, Order_Time, Status_Id " +
            "FROM TEST_LIBRARY.ORDER " +
            "WHERE ";

    private static final String INCOMPATIBLE_TYPE_OF_CRITERIA = "incompatible_type_of_criteria";
    private String locale = "EN";

    FindOrderQueryCreator() {

    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String createQuery(Criteria<? extends Entity> criteria) {
        StringBuffer columnNames = new StringBuffer();
        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof OrderCriteria)) {
            String incompatibleTypeOfCriteria = ErrorMessageManager.EN.getMessage(INCOMPATIBLE_TYPE_OF_CRITERIA);
            throw new UnknownEntityException(incompatibleTypeOfCriteria);
        }

        if (criteria.getEntityId() != null) {
            condition.append(ORDER_ID_COLUMN + " = " + criteria.getEntityId() + WHITESPACE + AND + WHITESPACE);
        }
        if (((OrderCriteria) criteria).getLibraryUserId() != null) {
            condition.append(LIBRARY_USER_ID_COLUMN + " = '" + ((OrderCriteria) criteria).getLibraryUserId() + "'" + WHITESPACE + AND + WHITESPACE);
        }
        if (((OrderCriteria) criteria).getOrderTime() != null) {
            condition.append(ORDER_TIME_COLUMN + " = '" + ((OrderCriteria) criteria).getOrderTime() + "'" + WHITESPACE + AND + WHITESPACE);
        }
        if (((OrderCriteria) criteria).getStatusId() != null) {
            condition.append(STATUS_ID_COLUMN + " = '" + ((OrderCriteria) criteria).getStatusId() + "'" + WHITESPACE + AND + WHITESPACE);
        }

        Validator validator = new Validator();
        validator.setLocale(locale);
        return validator.validatedQuery(condition, sql_query);
    }
}
