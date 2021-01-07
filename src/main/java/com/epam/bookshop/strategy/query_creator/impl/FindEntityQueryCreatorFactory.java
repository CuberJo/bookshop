package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.strategy.query_creator.FindEntityQueryCreator;
import com.epam.bookshop.util.manager.ErrorMessageManager;

public class FindEntityQueryCreatorFactory {

    private static final String NO_SUCH_SERVICE_TYPE = "no_such_query_creator_type";
    private static final String WHITESPACE = " ";
    private String locale = "EN";

    public static final FindEntityQueryCreatorFactory INSTANCE = new FindEntityQueryCreatorFactory();

    private FindEntityQueryCreatorFactory() {

    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public FindEntityQueryCreator create(EntityType type) {

        FindEntityQueryCreator creatorToReturn;

        switch (type) {
            case BOOK:
                creatorToReturn = new FindBookQueryCreator();
                break;
            case ORDER:
                creatorToReturn = new FindOrderQueryCreator();
                break;
            case USER:
                creatorToReturn = new FindUserQueryCreator();
                break;
            case GENRE:
                creatorToReturn = new FindGenreQueryCreator();
                break;
            case ROLE:
                creatorToReturn = new FindRoleQueryCreator();
                break;
            default:
                String noSuchQueryType = ErrorMessageManager.valueOf(locale).getMessage(NO_SUCH_SERVICE_TYPE) + WHITESPACE + type;
                throw new UnknownEntityException(noSuchQueryType);
        }

        creatorToReturn.setLocale(locale);
        return creatorToReturn;
    }
}
