package com.epam.bookshop.service.impl;

import com.epam.bookshop.dao.AbstractDao;
import com.epam.bookshop.dao.impl.DAOFactory;
import com.epam.bookshop.dao.impl.PaymentDao;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Payment;
import com.epam.bookshop.exception.DqlException;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.validator.impl.CriteriaValidator;
import com.epam.bookshop.validator.impl.EntityValidator;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for {@link Payment} instances
 */
public class PaymentService implements EntityService<Payment> {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private String locale = "US";

    PaymentService() {}

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public Payment create(Payment payment) throws ValidatorException, DqlException {
        EntityValidator entityValidator = new EntityValidator();
        entityValidator.setLocale(locale);
        entityValidator.validate(payment);

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Payment> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
            dao.create(payment);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return payment;
    }

    @Override
    public Collection<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Payment> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
            payments = dao.findAll();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return payments;
    }

    @Override
    public Collection<Payment> findAll(Criteria<Payment> criteria) throws ValidatorException {
        CriteriaValidator validator = new CriteriaValidator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Collection<Payment> payments = new ArrayList<>();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Payment> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
            payments = dao.findAll(criteria);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return payments;
    }

    @Override
    public Optional<Payment> findById(long id) {
        Optional<Payment> optionalOrder = Optional.empty();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Payment> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
            optionalOrder = dao.findById(id);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalOrder;
    }

    @Override
    public Optional<Payment> find(Criteria<Payment> criteria) throws ValidatorException {
        CriteriaValidator validator = new CriteriaValidator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Optional<Payment> optionalOrder = Optional.empty();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Payment> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
            optionalOrder = dao.find(criteria);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalOrder;
    }

    @Override
    public Optional<Payment> update(Payment payment) throws ValidatorException {
        EntityValidator entityValidator = new EntityValidator();
        entityValidator.setLocale(locale);
        entityValidator.validate(payment);

        Optional<Payment> optionalOrder = Optional.empty();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Payment> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
            optionalOrder = dao.update(payment);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalOrder;
    }

    @Override
    public boolean delete(long id) throws EntityNotFoundException {
        boolean isDeleted = false;

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection();) {
            AbstractDao<Long, Payment> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
            isDeleted = dao.delete(id);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        if (!isDeleted) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.PAYMENT_NOT_FOUND)
                    + UtilStringConstants.WHITESPACE + id;
            throw new EntityNotFoundException(errorMessage + UtilStringConstants.WHITESPACE + id);
        }

        return isDeleted;
    }

    @Override
    public boolean delete(Payment payment) throws EntityNotFoundException, ValidatorException {
        EntityValidator entityValidator = new EntityValidator();
        entityValidator.setLocale(locale);
        entityValidator.validate(payment);

        boolean isDeleted = false;

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
            isDeleted = dao.delete(payment.getEntityId());
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        if (!isDeleted) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.PAYMENT_NOT_FOUND)
                    + UtilStringConstants.WHITESPACE + payment.getEntityId();
            throw new EntityNotFoundException(errorMessage + UtilStringConstants.WHITESPACE + payment.getEntityId());
        }

        return isDeleted;
    }


    /**
     * Counts total number of payment rows
     *
     * @return number of rows in PAYMENT table
     */
    @Override
    public int count() {
        int rows = 0;

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            PaymentDao dao = (PaymentDao) DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
            rows = dao.count();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return rows;
    }


    /**
     * @param start from where to start limitation
     * @param total how many books to take
     * @return {@link Collection<Book>} found
     */
    @Override
    public Collection<Payment> findAll(int start, int total) {
        Collection<Payment> books = new ArrayList<>();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            PaymentDao dao = (PaymentDao) DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
            books = dao.findAll(start, total);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return books;
    }


    public List<Book> findAllBooksInPayment(long userId) {
        List<Book> books = new ArrayList<>();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            PaymentDao dao = (PaymentDao) DAOFactory.INSTANCE.create(EntityType.PAYMENT, conn);
            books = dao.findAllBooksInPayment(userId);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return books;
    }

}
