package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.strategy.query_creator.FindEntityQueryCreator;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;

public class FindUserQueryCreator implements FindEntityQueryCreator {

    private static final String LIBRARY_USER_ID_COLUMN = "Id";
    private static final String NAME_COLUMN = "Name";
    private static final String LOGIN_COLUMN = "Login";
    private static final String PASSWORD_COLUMN = "Password";
    private static final String EMAIL_COLUMN = "Email";
    private static final String ROLE_ID_COLUMN = "Role_Id";

    private static final String AND = "AND";
    private static final String WHITESPACE = " ";

    private static String sql_query = "SELECT Id, Name, Login, Password, Email, Role_Id " +
            "FROM TEST_LIBRARY.LIBRARY_USER " +
            "WHERE ";

    private static final String INCOMPATIBLE_TYPE_OF_CRITERIA = "incompatible_type_of_criteria";
    private String locale = "EN";

    FindUserQueryCreator() {

    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String createQuery(Criteria<? extends Entity> criteria) {

        StringBuffer columnNames = new StringBuffer();
        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof UserCriteria)) {
            String incompatibleTypeOfCriteria = ErrorMessageManager.valueOf(locale).getMessage(INCOMPATIBLE_TYPE_OF_CRITERIA) + WHITESPACE + criteria;
            throw new UnknownEntityException(incompatibleTypeOfCriteria);
        }

        if (criteria.getEntityId() != null) {
            condition.append(LIBRARY_USER_ID_COLUMN + " = " + criteria.getEntityId() + WHITESPACE + AND + WHITESPACE);
        }
        if (((UserCriteria) criteria).getName() != null) {
            condition.append(NAME_COLUMN + " = '" + ((UserCriteria) criteria).getName() + "'" + WHITESPACE + AND + WHITESPACE);
        }
        if (((UserCriteria) criteria).getLogin() != null) {
            condition.append(LOGIN_COLUMN + " = '" + ((UserCriteria) criteria).getLogin() + "'" + WHITESPACE + AND + WHITESPACE);
        }
        if (((UserCriteria) criteria).getPassword() != null) {
            condition.append(PASSWORD_COLUMN + " = '" + ((UserCriteria) criteria).getPassword() + "'" + WHITESPACE + AND + WHITESPACE);
        }
        if (((UserCriteria) criteria).getEmail() != null) {
            condition.append(EMAIL_COLUMN + " = '" + ((UserCriteria) criteria).getEmail() + "'" + WHITESPACE + AND + WHITESPACE);
        }
        if (((UserCriteria) criteria).getRoleId() != null) {
            condition.append(ROLE_ID_COLUMN + " = '" + ((UserCriteria) criteria).getRoleId() + "'" + WHITESPACE + AND + WHITESPACE);
        }

        Validator validator = new Validator();
        validator.setLocale(locale);
        return validator.validatedQuery(condition, sql_query);
    }
}
