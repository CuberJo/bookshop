package com.epam.bookshop.service.impl;

import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Creates Service classes instances by incoming service entity type
 */
public class ServiceFactory {

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


    /**
     * Creates DAO by service entity type
     *
     * @param type type of entity service
     * @return new {@code EntityService} instance
     */
    public EntityService create(EntityType type) {

        EntityService serviceToReturn;

        String locale = "US";
        switch (type) {
            case BOOK:
                serviceToReturn = new BookService();
                break;
            case USER:
                serviceToReturn = new UserService();
                break;
            case PAYMENT:
                serviceToReturn = new PaymentService();
                break;
            case GENRE:
                serviceToReturn = new GenreService();
                break;
            default:
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_SERVICE_TYPE);
                throw new UnknownEntityException(errorMessage);
        }

        return serviceToReturn;
    }
}
