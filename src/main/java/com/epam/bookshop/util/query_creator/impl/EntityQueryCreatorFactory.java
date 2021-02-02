package com.epam.bookshop.util.query_creator.impl;

import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.util.query_creator.EntityQueryCreator;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;

public class EntityQueryCreatorFactory {

    private String locale = "US";

    public static final EntityQueryCreatorFactory INSTANCE = new EntityQueryCreatorFactory();

    private EntityQueryCreatorFactory() {

    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public EntityQueryCreator create(EntityType type) {

        EntityQueryCreator creatorToReturn;

        switch (type) {
            case BOOK:
                creatorToReturn = BookQueryCreator.getInstance();
                break;
            case PAYMENT:
                creatorToReturn = PaymentQueryCreator.getInstance();
                break;
            case USER:
                creatorToReturn = UserQueryCreator.getInstance();
                break;
            case GENRE:
                creatorToReturn = GenreQueryCreator.getInstance();
                break;
            default:
                String noSuchQueryType = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_SERVICE_TYPE) + UtilStringConstants.WHITESPACE + type;
                throw new UnknownEntityException(noSuchQueryType);
        }

        return creatorToReturn;
    }
}
