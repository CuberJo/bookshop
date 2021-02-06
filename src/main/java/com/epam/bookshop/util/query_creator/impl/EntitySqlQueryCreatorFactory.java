package com.epam.bookshop.util.query_creator.impl;

import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.util.query_creator.SqlQueryCreator;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;

public class EntitySqlQueryCreatorFactory {

    private String locale = "US";

    public static final EntitySqlQueryCreatorFactory INSTANCE = new EntitySqlQueryCreatorFactory();

    private EntitySqlQueryCreatorFactory() {

    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public SqlQueryCreator create(EntityType type) {

        SqlQueryCreator creatorToReturn;

        switch (type) {
            case BOOK:
                creatorToReturn = BookSqlQueryCreator.getInstance();
                break;
            case PAYMENT:
                creatorToReturn = PaymentSqlQueryCreator.getInstance();
                break;
            case USER:
                creatorToReturn = UserSqlQueryCreator.getInstance();
                break;
            case GENRE:
                creatorToReturn = GenreSqlQueryCreator.getInstance();
                break;
            default:
                String noSuchQueryType = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_SERVICE_TYPE)
                        + UtilStringConstants.WHITESPACE + type;
                throw new UnknownEntityException(noSuchQueryType);
        }

        return creatorToReturn;
    }
}
