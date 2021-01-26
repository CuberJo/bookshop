package com.epam.bookshop.dao.impl;

import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;

import java.sql.Connection;

/**
 * creates DAO
 */
public class DAOFactory {

    /**
     * {@value #INSTANCE} DAO instance
     */
    public static final DAOFactory INSTANCE = new DAOFactory();

    /**
     * no-arg constructor
     */
    private DAOFactory() {

    }


    /**
     * Creates DAO by input parameters
     *
     * @param type type of DAO
     * @param connection param for DAO constructor
     * @return new DAO
     */
    public AbstractDAO create(EntityType type, Connection connection) {

        AbstractDAO daoToReturn;

        String locale = "US";
        switch (type) {
            case BOOK:
                daoToReturn = new BookDAO(connection);
                break;
            case USER:
                daoToReturn = new UserDAO(connection);
                break;
            case PAYMENT:
                daoToReturn = new PaymentDAO(connection);
                break;
            case GENRE:
                daoToReturn = new GenreDAO(connection);
                break;
         default:
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_DAO_TYPE) + UtilStrings.WHITESPACE + type;
                throw new UnknownEntityException(errorMessage);
        }

        return daoToReturn;
    }
}
