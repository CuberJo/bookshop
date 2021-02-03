package com.epam.bookshop.util.query_creator.impl;

import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.util.query_creator.EntityQueryCreator;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.validator.impl.SqlQueryValidator;

import java.util.concurrent.locks.ReentrantLock;

public class UserQueryCreator implements EntityQueryCreator<User> {

    private static final String LIBRARY_USER_ID_COLUMN = "Id";
    private static final String NAME_COLUMN = "Name";
    private static final String LOGIN_COLUMN = "Login";
    private static final String PASSWORD_COLUMN = "Password";
    private static final String EMAIL_COLUMN = "Email";
    private static final String ADMIN_COLUMN = "Admin";

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
    public String createQuery(Criteria<User> criteria, String operator) {
        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof UserCriteria)) {
            String locale = "US";
            String incompatibleTypeOfCriteria = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INCOMPATIBLE_TYPE_OF_CRITERIA)
                    + UtilStringConstants.WHITESPACE + criteria;
            throw new UnknownEntityException(incompatibleTypeOfCriteria);
        }

        if (criteria.getEntityId() != null) {
            condition.append(LIBRARY_USER_ID_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(criteria.getEntityId())
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }
        if (((UserCriteria) criteria).getName() != null) {
            condition.append(NAME_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append("'")
                    .append(((UserCriteria) criteria).getName())
                    .append("'")
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }
        if (((UserCriteria) criteria).getLogin() != null) {
            condition.append(LOGIN_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append("'")
                    .append(((UserCriteria) criteria).getLogin())
                    .append("'")
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }
        if (((UserCriteria) criteria).getPassword() != null) {
            condition.append(PASSWORD_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append("'")
                    .append(((UserCriteria) criteria).getPassword())
                    .append("'")
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }
        if (((UserCriteria) criteria).getEmail() != null) {
            condition.append(EMAIL_COLUMN)
                    .append(UtilStringConstants.WHITESPACE)
                    .append(operator)
                    .append(UtilStringConstants.WHITESPACE)
                    .append("'")
                    .append(((UserCriteria) criteria).getEmail())
                    .append("'")
                    .append(UtilStringConstants.WHITESPACE)
                    .append(UtilStringConstants.AND)
                    .append(UtilStringConstants.WHITESPACE);
        }
//        if (((UserCriteria) criteria).isAdmin() != null) {
//            condition.append(ADMIN_COLUMN + " = '")
//                    .append(((UserCriteria) criteria).isAdmin())
//                    .append("'")
//                    .append(UtilStrings.WHITESPACE)
//                    .append(UtilStrings.AND)
//                    .append(UtilStrings.WHITESPACE);
//        }

        return SqlQueryValidator.getInstance().validatedQuery(condition);
    }
}
