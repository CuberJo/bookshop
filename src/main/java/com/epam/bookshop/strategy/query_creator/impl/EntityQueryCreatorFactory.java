package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.strategy.query_creator.EntityQueryCreator;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;

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
            case ORDER:
                creatorToReturn = OrderQueryCreator.getInstance();
                break;
            case USER:
                creatorToReturn = UserQueryCreator.getInstance();
                break;
            case GENRE:
                creatorToReturn = GenreQueryCreator.getInstance();
                break;
            case ROLE:
                creatorToReturn = RoleQueryCreator.getInstance();
                break;
            default:
                String noSuchQueryType = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_SERVICE_TYPE) + UtilStrings.WHITESPACE + type;
                throw new UnknownEntityException(noSuchQueryType);
        }

        return creatorToReturn;
    }
}
