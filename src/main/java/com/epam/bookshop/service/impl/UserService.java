package com.epam.bookshop.service.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.dao.impl.DAOFactory;
import com.epam.bookshop.dao.impl.UserDAO;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
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
import java.util.Map;
import java.util.Optional;

public class UserService implements EntityService<User> {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private String locale = "US";

    UserService() {

    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public User create(User user) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(user);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);
        dao.create(user);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return user;
    }

    @Override
    public Collection<User> findAll() {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);
        List<User> users = dao.findAll();

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return users;
    }

    @Override
    public Collection<User> findAll(Criteria<User> criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);

        Collection<User> users = dao.findAll(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return users;
    }

    @Override
    public Optional<User> findById(long id) {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);

        Optional<User> optionalUser = dao.findById(id);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalUser;
    }

    @Override
    public Optional<User> find(Criteria<User> criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);

        Optional<User> optionalUser = dao.find(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalUser;
    }

    @Override
    public Optional<User> update(User user) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(user);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);

        Optional<User> optionalUser = dao.update(user);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalUser;
    }

    @Override
    public boolean delete(long id) throws EntityNotFoundException {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);

        boolean isDeleted = dao.delete(id);
        if (!isDeleted) {
            String userNotFound = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND) + UtilStrings.WHITESPACE + id;
            throw new EntityNotFoundException(userNotFound);
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return isDeleted;
    }

    @Override
    public boolean delete(User user) throws EntityNotFoundException, ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(user);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);

        boolean isDeleted = dao.delete(user.getEntityId());
        if (!isDeleted) {
            String userNotFound = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND) + UtilStrings.WHITESPACE + user.getEntityId();
            throw new EntityNotFoundException(userNotFound);
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return isDeleted;
    }

    public Map<String, Long> createUserBankAccount(String IBAN, Long library_User_Id) {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        UserDAO dao = (UserDAO) DAOFactory.INSTANCE.create(EntityType.USER, conn);
        dao.createUserBankAccount(IBAN, library_User_Id);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return Map.of(IBAN, library_User_Id);
    }

    public Map<String, Long> findUsersBankAccounts() {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        UserDAO dao = (UserDAO) DAOFactory.INSTANCE.create(EntityType.USER, conn);
        Map<String, Long> userIBANs = dao.findUsersBankAccounts();

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return userIBANs;
    }

    public List<String> findUserBankAccounts(long id) {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        UserDAO dao = (UserDAO) DAOFactory.INSTANCE.create(EntityType.USER, conn);
        List<String> userIBANs = dao.findUserBankAccounts(id);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return userIBANs;
    }

    public boolean deleteUserBankAccount(String iban) throws EntityNotFoundException {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        UserDAO dao = (UserDAO) DAOFactory.INSTANCE.create(EntityType.USER, conn);

        boolean isDeleted = dao.deleteUserBankAccount(iban);
        if (!isDeleted) {
            String IBANNotFound = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IBAN_NOT_FOUND) + UtilStrings.WHITESPACE + iban;
            throw new EntityNotFoundException(IBANNotFound);
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return isDeleted;
    }
}
