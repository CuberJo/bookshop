package com.epam.bookshop.dao.impl;

import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.util.manager.ErrorMessageManager;

import java.sql.Connection;

public class DAOFactory {

    private static final String NO_SUCH_DAO_TYPE = "no_such_DAO_type";
    private static final String WHITESPACE = " ";
    private String locale = "EN";

    public static final DAOFactory INSTANCE = new DAOFactory();

    private DAOFactory() {

    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public AbstractDAO create(EntityType type, Connection connection) {

        AbstractDAO daoToReturn = null;

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
//            case USER_BANK_ACCOUNT:
//                daoToReturn = new UserBankAccountDAO(co)
            default:
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(NO_SUCH_DAO_TYPE) + WHITESPACE + type;
                throw new UnknownEntityException(errorMessage);
        }

        return daoToReturn;
    }
}
