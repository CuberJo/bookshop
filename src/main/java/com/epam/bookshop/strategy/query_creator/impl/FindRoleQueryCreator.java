package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.RoleCriteria;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.strategy.query_creator.FindEntityQueryCreator;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;

public class FindRoleQueryCreator implements FindEntityQueryCreator {

    private static final String ROLE_ID_COLUMN = "Id";
    private static final String ROLE_COLUMN = "Role";
    private static final String AND = "AND";
    private static final String WHITESPACE = " ";

    private static String sql_query = "SELECT Id, Role " +
            "FROM TEST_LIBRARY.USER_ROLE " +
            "WHERE ";

    private static final String INCOMPATIBLE_TYPE_OF_CRITERIA = "incompatible_type_of_criteria";
    private String locale = "EN";

    FindRoleQueryCreator() {

    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String createQuery(Criteria<? extends Entity> criteria) {
        StringBuffer columnNames = new StringBuffer();
        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof RoleCriteria)) {
            String incompatibleTypeOfCriteria = ErrorMessageManager.EN.getMessage(INCOMPATIBLE_TYPE_OF_CRITERIA);
            throw new UnknownEntityException(incompatibleTypeOfCriteria);
        }

        if (criteria.getEntityId() != null) {
            condition.append(ROLE_ID_COLUMN + " = " + criteria.getEntityId() + WHITESPACE + AND + WHITESPACE);
        }
        if (((RoleCriteria) criteria).getRole() != null) {
            condition.append(ROLE_COLUMN + " = '" + ((RoleCriteria) criteria).getRole() +  "'" + WHITESPACE + AND + WHITESPACE);
        }

        Validator validator = new Validator();
        validator.setLocale(locale);
        return validator.validatedQuery(condition, sql_query);
    }
}
