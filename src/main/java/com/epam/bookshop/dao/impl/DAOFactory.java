package com.epam.bookshop.dao.impl;

import com.epam.bookshop.dao.AbstractDao;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;

import java.sql.Connection;

/**
 * Creates DAO classes instances by incoming DAO entity type
 */
public class DAOFactory {

    public static final DAOFactory INSTANCE = new DAOFactory();
    private static final String LOCALE = "US";

    private DAOFactory() {}

    /**
     * Creates DAO by input parameters
     *
     * @param type type of DAO
     * @param connection param for DAO constructor
     * @return new DAO
     */
    public AbstractDao create(EntityType type, Connection connection) {

        AbstractDao daoToReturn;

        switch (type) {
            case BOOK:
                daoToReturn = new BookDao(connection);
                break;
            case USER:
                daoToReturn = new UserDao(connection);
                break;
            case PAYMENT:
                daoToReturn = new PaymentDao(connection);
                break;
            case GENRE:
                daoToReturn = new GenreDao(connection);
                break;
         default:
                String errorMessage = ErrorMessageManager.valueOf(LOCALE).getMessage(ErrorMessageConstants.NO_SUCH_DAO_TYPE)
                        + UtilStringConstants.WHITESPACE + type;
                throw new UnknownEntityException(errorMessage);
        }

        return daoToReturn;
    }
}
