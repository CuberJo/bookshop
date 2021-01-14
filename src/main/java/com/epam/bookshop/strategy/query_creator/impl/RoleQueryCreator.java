package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.RoleCriteria;
import com.epam.bookshop.domain.impl.Role;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.strategy.query_creator.EntityQueryCreator;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;

import java.util.concurrent.locks.ReentrantLock;

public class RoleQueryCreator implements EntityQueryCreator<Role> {

    private static final String ROLE_ID_COLUMN = "Id";
    private static final String ROLE_COLUMN = "Role";

    private static final String locale = "EN";

    private static final ReentrantLock lock = new ReentrantLock();

    private static RoleQueryCreator instance;

    public static RoleQueryCreator getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new RoleQueryCreator();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    private RoleQueryCreator() {

    }

    @Override
    public String createQuery(Criteria<Role> criteria) {
        StringBuffer condition = new StringBuffer();

        if (!(criteria instanceof RoleCriteria)) {
            String incompatibleTypeOfCriteria = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INCOMPATIBLE_TYPE_OF_CRITERIA)
                    + UtilStrings.WHITESPACE + criteria;
            throw new UnknownEntityException(incompatibleTypeOfCriteria);
        }

        if (criteria.getEntityId() != null) {
            condition.append(ROLE_ID_COLUMN + " = ")
                    .append(criteria.getEntityId())
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }
        if (((RoleCriteria) criteria).getRole() != null) {
            condition.append(ROLE_COLUMN + " = '")
                    .append(((RoleCriteria) criteria).getRole())
                    .append("'")
                    .append(UtilStrings.WHITESPACE)
                    .append(UtilStrings.AND)
                    .append(UtilStrings.WHITESPACE);
        }

        return new Validator().validatedQuery(condition);
    }
}
