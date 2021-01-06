package com.epam.bookshop.service.impl;

import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.dao.impl.DAOFactory;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Order;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class OrderService implements EntityService<Order> {

    private static final String ORDER_NOT_FOUND = "order_not_found";
    private static final String WHITESPACE = " ";

    private String locale = "EN";

    OrderService() {

    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public Order create(Order order) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(order);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        AbstractDAO<Long, Order> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);
        dao.setLocale(locale);
        dao.create(order);

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return order;
    }

    @Override
    public Collection findAll() {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        AbstractDAO<Long, Order> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);
        dao.setLocale(locale);
        List<Order> orders = dao.findAll();

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return orders;
    }

    @Override
    public Collection findAll(Criteria criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Order> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);

        Collection<Order> orders = dao.findAll(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return orders;
    }

    @Override
    public Optional<Order> findById(long id) {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        AbstractDAO<Long, Order> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);
        dao.setLocale(locale);
        Optional<Order> optionalOrder = dao.findById(id);
//        if (optionalOrder.isEmpty()) {
//            throw new EntityNotFoundException("No order with id = " + id + " found");
//        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//        return optionalOrder.get();
        return optionalOrder;
    }

    @Override
    public Optional<Order> find(Criteria criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        AbstractDAO<Long, Order> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);
        dao.setLocale(locale);
        Optional<Order> optionalOrder = dao.find(criteria);
//        if (optionalOrder.isEmpty()) {
//            throw new EntityNotFoundException("No order for user with id = " + ((OrderCriteria) criteria).getLibraryUserId() + " found");
//        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//        return optionalOrder.get();
        return optionalOrder;
    }

    @Override
    public Optional<Order> update(Order order) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(order);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        AbstractDAO<Long, Order> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);
        dao.setLocale(locale);
        Optional<Order> optionalOrder = dao.update(order);
//        if (optionalOrder.isEmpty()) {
//            throw new EntityNotFoundException("No order with id = " + order.getEntityId() + " found");
//        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//        return optionalOrder.get();
        return optionalOrder;
    }

    @Override
    public boolean delete(long id) throws EntityNotFoundException {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        AbstractDAO<Long, Order> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);
        dao.setLocale(locale);
        boolean isDeleted = dao.delete(id);
        if (!isDeleted) {
            String errorMessage = ErrorMessageManager.EN.getMessage(ORDER_NOT_FOUND);
            throw new EntityNotFoundException(errorMessage + WHITESPACE + id);
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return isDeleted;
    }

    @Override
    public boolean delete(Order order) throws EntityNotFoundException, ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(order);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);
        dao.setLocale(locale);
        boolean isDeleted = dao.delete(order.getEntityId());
        if (!isDeleted) {
            String errorMessage = ErrorMessageManager.EN.getMessage(ORDER_NOT_FOUND);
            throw new EntityNotFoundException(errorMessage + WHITESPACE + order.getEntityId());
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return isDeleted;
    }
}
