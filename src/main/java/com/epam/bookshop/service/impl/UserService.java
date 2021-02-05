package com.epam.bookshop.service.impl;

import com.epam.bookshop.constant.RegexConstants;
import com.epam.bookshop.dao.AbstractDao;
import com.epam.bookshop.dao.impl.DAOFactory;
import com.epam.bookshop.dao.impl.UserDao;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.validator.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Business logic for {@link User} instances
 */
public class UserService implements EntityService<User> {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private String locale = "US";

    UserService() {}

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public User create(User user) throws ValidatorException {
        EntityValidator entityValidator = new EntityValidator();
        entityValidator.setLocale(locale);
        entityValidator.validate(user);

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);
            dao.create(user);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return user;
    }

    @Override
    public Collection<User> findAll() {
        List<User> users = new ArrayList<>();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);
            users = dao.findAll();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return users;
    }

    @Override
    public Collection<User> findAll(Criteria<User> criteria) throws ValidatorException {
        CriteriaValidator validator = new CriteriaValidator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Collection<User> users = new ArrayList<>();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);
            users = dao.findAll(criteria);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return users;
    }


    /**
     * @param start from where to start limitation
     * @param total how many users to take
     * @return {@link Collection<User>} found
     */
    @Override
    public Collection<User> findAll(int start, int total) {
        Collection<User> books = new ArrayList<>();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);
            books = dao.findAll(start, total);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return books;
    }


    @Override
    public Optional<User> findById(long id) {
        Optional<User> optionalUser = Optional.empty();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);
            optionalUser = dao.findById(id);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalUser;
    }

    @Override
    public Optional<User> find(Criteria<User> criteria) throws ValidatorException {
        CriteriaValidator validator = new CriteriaValidator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Optional<User> optionalUser = Optional.empty();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);
            optionalUser = dao.find(criteria);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalUser;
    }

    @Override
    public Optional<User> update(User user) throws ValidatorException {
        EntityValidator entityValidator = new EntityValidator();
        entityValidator.setLocale(locale);
        entityValidator.validate(user);

        Optional<User> optionalUser = Optional.empty();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);
            optionalUser = dao.update(user);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalUser;
    }

    @Override
    public boolean delete(long id) throws EntityNotFoundException {
        boolean isDeleted = false;

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);
            isDeleted = dao.delete(id);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        if (!isDeleted) {
            String userNotFound = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND) + UtilStringConstants.WHITESPACE + id;
            throw new EntityNotFoundException(userNotFound);
        }

        return isDeleted;
    }

    @Override
    public boolean delete(User user) throws EntityNotFoundException, ValidatorException {
        EntityValidator entityValidator = new EntityValidator();
        entityValidator.setLocale(locale);
        entityValidator.validate(user);

        boolean isDeleted = false;

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);
            isDeleted = dao.delete(user.getEntityId());
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        if (!isDeleted) {
            String userNotFound = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND) + UtilStringConstants.WHITESPACE + user.getEntityId();
            throw new EntityNotFoundException(userNotFound);
        }

        return isDeleted;
    }


    /**
     * Counts total number of users
     *
     * @return number of rows in 'USER' table
     */
    @Override
    public int count() {
        int rows = 0;

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            UserDao dao = (UserDao) DAOFactory.INSTANCE.create(EntityType.USER, conn);
            rows = dao.count();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return rows;
    }


    public Map<String, Long> createUserBankAccount(String IBAN, Long library_User_Id) throws ValidatorException {
        if (EmptyStringValidator.getInstance().empty(IBAN)) {
            throw new ValidatorException(ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMPTY_IBAN));
        }
        if (!RegexValidator.getInstance().validate(IBAN, RegexConstants.IBAN_REGEX)) {
            throw new ValidatorException(ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IBAN_INCORRECT));
        }

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            UserDao dao = (UserDao) DAOFactory.INSTANCE.create(EntityType.USER, conn);
            dao.createUserBankAccount(IBAN, library_User_Id);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return Map.of(IBAN, library_User_Id);
    }

    public Map<String, Long> findUsersBankAccounts() {
        Map<String, Long> userIBANs = new HashMap<>();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            UserDao dao = (UserDao) DAOFactory.INSTANCE.create(EntityType.USER, conn);
            userIBANs = dao.findUsersBankAccounts();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return userIBANs;
    }

    public List<String> findUserBankAccounts(long id) {
        List<String> userIBANs = new ArrayList<>();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            UserDao dao = (UserDao) DAOFactory.INSTANCE.create(EntityType.USER, conn);
            userIBANs = dao.findUserBankAccounts(id);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return userIBANs;
    }

    public boolean deleteUserBankAccount(String iban) throws EntityNotFoundException {
        boolean isDeleted = false;

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            UserDao dao = (UserDao) DAOFactory.INSTANCE.create(EntityType.USER, conn);
            isDeleted = dao.deleteUserBankAccount(iban);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        if (!isDeleted) {
            String IBANNotFound = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IBAN_NOT_FOUND) + UtilStringConstants.WHITESPACE + iban;
            throw new EntityNotFoundException(IBANNotFound);
        }

        return isDeleted;
    }
}
