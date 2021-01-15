package com.epam.bookshop.dao.impl;

import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;

import java.sql.Connection;

public class DAOFactory {

    private String locale = "US";

    public static final DAOFactory INSTANCE = new DAOFactory();

    private DAOFactory() {

    }


    public AbstractDAO create(EntityType type, Connection connection) {

        AbstractDAO daoToReturn;

        switch (type) {
            case BOOK:
                daoToReturn = new BookDAO(connection);
                break;
            case USER:
                daoToReturn = new UserDAO(connection);
                break;
            case ORDER:
                daoToReturn = new OrderDAO(connection);
                break;
            case STATUS:
                daoToReturn = new StatusDAO(connection);
                break;
            case GENRE:
                daoToReturn = new GenreDAO(connection);
                break;
            case ROLE:
                daoToReturn = new RoleDAO(connection);
                break;
            default:
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_DAO_TYPE) + UtilStrings.WHITESPACE + type;
                throw new UnknownEntityException(errorMessage);
        }

        return daoToReturn;
    }
}
