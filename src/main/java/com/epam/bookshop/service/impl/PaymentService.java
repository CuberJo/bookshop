package com.epam.bookshop.service.impl;

import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.dao.impl.DAOFactory;
import com.epam.bookshop.dao.impl.PaymentDAO;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Payment;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.util.validator.impl.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PaymentService implements EntityService<Payment> {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private String locale = "US";

    PaymentService() {

    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public Payment create(Payment payment) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(payment);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Payment> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
        dao.create(payment);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return payment;
    }

    @Override
    public Collection<Payment> findAll() {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Payment> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
        List<Payment> payments = dao.findAll();

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return payments;
    }

    @Override
    public Collection<Payment> findAll(Criteria<Payment> criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Payment> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);

        Collection<Payment> payments = dao.findAll(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return payments;
    }

    @Override
    public Optional<Payment> findById(long id) {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Payment> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
        Optional<Payment> optionalOrder = dao.findById(id);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalOrder;
    }

    @Override
    public Optional<Payment> find(Criteria<Payment> criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Payment> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
        Optional<Payment> optionalOrder = dao.find(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalOrder;
    }

    @Override
    public Optional<Payment> update(Payment payment) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(payment);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Payment> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
        Optional<Payment> optionalOrder = dao.update(payment);

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
        AbstractDAO<Long, Payment> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
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
    public boolean delete(Payment payment) throws EntityNotFoundException, ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(payment);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
        boolean isDeleted = dao.delete(payment.getEntityId());
        if (!isDeleted) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.ORDER_NOT_FOUND) + UtilStrings.WHITESPACE + payment.getEntityId();
            throw new EntityNotFoundException(errorMessage + UtilStrings.WHITESPACE + payment.getEntityId());
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return isDeleted;
    }

    public List<Book> findAllBooksInPayment(long userId) {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        PaymentDAO dao = (PaymentDAO) DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
        List<Book> books = dao.findAllBooksInPayment(userId);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return books;
    }

}
