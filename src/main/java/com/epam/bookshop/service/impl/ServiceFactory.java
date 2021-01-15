package com.epam.bookshop.service.impl;

import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.util.manager.ErrorMessageManager;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class ServiceFactory {

    private String locale = "US";

    private static ServiceFactory instance;

    private static final ReentrantLock LOCK = new ReentrantLock();

    private ServiceFactory() {

    }

    public static ServiceFactory getInstance() {
        LOCK.lock();

        try {
            if (Objects.isNull(instance)) {
                instance = new ServiceFactory();
            }
        } finally {
            LOCK.unlock();
        }

        return instance;
    }


    public EntityService create(EntityType type) {

        EntityService serviceToReturn;

        switch (type) {
            case BOOK:
                serviceToReturn = new BookService();
                break;
            case USER:
                serviceToReturn = new UserService();
                break;
            case ORDER:
                serviceToReturn = new OrderService();
                break;
            case GENRE:
                serviceToReturn = new GenreService();
                break;
            case ROLE:
                serviceToReturn = new RoleService();
                break;
            default:
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_SERVICE_TYPE);
                throw new UnknownEntityException(errorMessage);
        }

        return serviceToReturn;
    }
}
