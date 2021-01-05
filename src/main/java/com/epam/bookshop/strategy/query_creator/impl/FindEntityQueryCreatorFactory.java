package com.epam.bookshop.strategy.query_creator.impl;

import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.strategy.query_creator.FindEntityQueryCreator;
import com.epam.bookshop.exception.UnknownEntityException;

public class FindEntityQueryCreatorFactory {

    public static final FindEntityQueryCreatorFactory INSTANCE = new FindEntityQueryCreatorFactory();

    private FindEntityQueryCreatorFactory() {

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
            default:
                throw new UnknownEntityException("No such query creator type");
        }

        return creatorToReturn;
    }
}
