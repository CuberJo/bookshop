package com.epam.bookshop.service.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.dao.impl.DAOFactory;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Order;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class OrderService implements EntityService<Order> {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private String locale = "US";

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
        AbstractDAO<Long, Order> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);
        dao.create(order);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return order;
    }

    @Override
    public Collection<Order> findAll() {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Order> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);
        List<Order> orders = dao.findAll();

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return orders;
    }

    @Override
    public Collection<Order> findAll(Criteria<Order> criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Order> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);

        Collection<Order> orders = dao.findAll(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return orders;
    }

    @Override
    public Optional<Order> findById(long id) {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Order> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);
        Optional<Order> optionalOrder = dao.findById(id);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalOrder;
    }

    @Override
    public Optional<Order> find(Criteria<Order> criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Order> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);
        Optional<Order> optionalOrder = dao.find(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalOrder;
    }

    @Override
    public Optional<Order> update(Order order) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(order);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Order> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);
        Optional<Order> optionalOrder = dao.update(order);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalOrder;
    }

    @Override
    public boolean delete(long id) throws EntityNotFoundException {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Order> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);
        boolean isDeleted = dao.delete(id);
        if (!isDeleted) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.ORDER_NOT_FOUND) + UtilStrings.WHITESPACE + id;
            throw new EntityNotFoundException(errorMessage + UtilStrings.WHITESPACE + id);
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return isDeleted;
    }

    @Override
    public boolean delete(Order order) throws EntityNotFoundException, ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(order);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.ORDER, conn);
        boolean isDeleted = dao.delete(order.getEntityId());
        if (!isDeleted) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.ORDER_NOT_FOUND) + UtilStrings.WHITESPACE + order.getEntityId();
            throw new EntityNotFoundException(errorMessage + UtilStrings.WHITESPACE + order.getEntityId());
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return isDeleted;
    }
}
