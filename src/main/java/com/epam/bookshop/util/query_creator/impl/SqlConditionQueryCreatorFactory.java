package com.epam.bookshop.util.query_creator.impl;

import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.util.query_creator.SqlConditionQueryCreator;

/**
 * Factory that produces {@link SqlConditionQueryCreator}
 */
public class SqlConditionQueryCreatorFactory {

    private String locale = "US";

    public static final SqlConditionQueryCreatorFactory INSTANCE = new SqlConditionQueryCreatorFactory();

    private SqlConditionQueryCreatorFactory() {

    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public SqlConditionQueryCreator create(EntityType type) {

        SqlConditionQueryCreator creatorToReturn;

        switch (type) {
            case BOOK:
                creatorToReturn = BookSqlConditionQueryCreator.getInstance();
                break;
            case PAYMENT:
                creatorToReturn = PaymentSqlConditionQueryCreator.getInstance();
                break;
            case USER:
                creatorToReturn = UserSqlConditionQueryCreator.getInstance();
                break;
            case GENRE:
                creatorToReturn = GenreSqlConditionQueryCreator.getInstance();
                break;
            default:
                String noSuchQueryType = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_SERVICE_TYPE)
                        + UtilStringConstants.WHITESPACE + type;
                throw new UnknownEntityException(noSuchQueryType);
        }

        return creatorToReturn;
    }
}
