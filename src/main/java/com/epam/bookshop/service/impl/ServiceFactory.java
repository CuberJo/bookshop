package com.epam.bookshop.service.impl;

import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.UnknownEntityException;
import com.epam.bookshop.service.EntityService;

import java.util.concurrent.atomic.AtomicBoolean;

public class ServiceFactory {

    private static ServiceFactory instance;
    private static AtomicBoolean isCreated = new AtomicBoolean(false);

    private ServiceFactory() {

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
            default:
                throw new UnknownEntityException("No such service type");
        }

        return serviceToReturn;
    }
}
