package com.epam.bookshop.service.impl;

import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.util.manager.ErrorMessageManager;

import java.util.concurrent.atomic.AtomicBoolean;

public class ServiceFactory {

    private static final String NO_SUCH_SERVICE_TYPE = "no_such_service_type";

    private static ServiceFactory instance;
    private static AtomicBoolean isCreated = new AtomicBoolean(false);

    private ServiceFactory() {

    }

    private String locale = "EN";

    public void setLocale(String locale) {
        this.locale = locale;
    }

    // двойная проверка isCreated, т.к. метод не пустит другой поток, пока
    // объект не будет поностью создан, т.к. объект может ЧАСТИЧНО создаться,
    // но не полностью
    public static ServiceFactory getInstance() {
        if (!isCreated.get()) {
            synchronized (ServiceFactory.class){
                if (instance == null) {
                    if (!isCreated.get()) {
                        instance = new ServiceFactory();
                        isCreated.set(true);
                    }
                }
            }
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
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(NO_SUCH_SERVICE_TYPE);
                throw new UnknownEntityException(errorMessage);
        }

        return serviceToReturn;
    }
}
