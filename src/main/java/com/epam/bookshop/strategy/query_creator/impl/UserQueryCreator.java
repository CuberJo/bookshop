package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.strategy.query_creator.EntityQueryCreator;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;

import java.util.concurrent.locks.ReentrantLock;

public class UserQueryCreator implements EntityQueryCreator<User> {

    private static final String LIBRARY_USER_ID_COLUMN = "Id";
    private static final String NAME_COLUMN = "Name";
    private static final String LOGIN_COLUMN = "Login";
    private static final String PASSWORD_COLUMN = "Password";
    private static final String EMAIL_COLUMN = "Email";
    private static final String ROLE_ID_COLUMN = "Role_Id";

    private static final String locale = "EN";

    private static final ReentrantLock lock = new ReentrantLock();

    private static UserQueryCreator instance;

    public static UserQueryCreator getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new UserQueryCreator();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    private UserQueryCreator() {

    }

    @Override
    public String createQuery(Criteria<User> criteria) {
        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof UserCriteria)) {
            String incompatibleTypeOfCriteria = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INCOMPATIBLE_TYPE_OF_CRITERIA)
                    + UtilStrings.WHITESPACE + criteria;
            throw new UnknownEntityException(incompatibleTypeOfCriteria);
        }

        if (criteria.getEntityId() != null) {
            condition.append(LIBRARY_USER_ID_COLUMN + " = ")
                    .append(criteria.getEntityId())
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((UserCriteria) criteria).getName() != null) {
            condition.append(NAME_COLUMN + " = '")
                    .append(((UserCriteria) criteria).getName())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((UserCriteria) criteria).getLogin() != null) {
            condition.append(LOGIN_COLUMN + " = '")
                    .append(((UserCriteria) criteria).getLogin())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((UserCriteria) criteria).getPassword() != null) {
            condition.append(PASSWORD_COLUMN + " = '")
                    .append(((UserCriteria) criteria).getPassword())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((UserCriteria) criteria).getEmail() != null) {
            condition.append(EMAIL_COLUMN + " = '")
                    .append(((UserCriteria) criteria).getEmail())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((UserCriteria) criteria).getRoleId() != null) {
            condition.append(ROLE_ID_COLUMN + " = '")
                    .append(((UserCriteria) criteria).getRoleId())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }

        return new Validator().validatedQuery(condition);
    }
}
