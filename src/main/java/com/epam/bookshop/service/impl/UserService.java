package com.epam.bookshop.service.impl;

import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.dao.impl.DAOFactory;
import com.epam.bookshop.dao.impl.UserDAO;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.validator.Validator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserService implements EntityService<User> {

    UserService() {

    }

    @Override
    public User create(User user) throws ValidatorException {
        Validator.getInstance().validate(user);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);
        dao.create(user);


//        UserBankAccountDAO userBankAccountDAO = new UserBankAccountDAO(ConnectionPool.getInstance().getAvailableConnection());
//        userBankAccountDAO.create(user.getIBANs().get(0), user.getEntityId());

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
            throwables.printStackTrace();
        }

        return users;
    }

    @Override
    public Collection<User> findAll(Criteria criteria) throws ValidatorException {
        Validator.getInstance().validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);

        Collection<User> users = dao.findAll(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }

    @Override
    public Optional<User> findById(long id) {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);

        Optional<User> optionalUser = dao.findById(id);
//        if (optionalUser.isEmpty()) {
//            throw new EntityNotFoundException("No user with id = " + id + " found");
//        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//        return optionalUser;
        return optionalUser;
    }

    @Override
    public Optional<User> find(Criteria criteria) throws ValidatorException {
        Validator.getInstance().validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);

        Optional<User> optionalUser = dao.find(criteria);
//        if (optionalUser.isEmpty()) {
//            throw new EntityNotFoundException("No user with email = " + ((UserCriteria)criteria).getEmail() + " found");
//        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//        return optionalUser.get();
        return optionalUser;
    }

    @Override
    public Optional<User> update(User user) throws ValidatorException {
        Validator.getInstance().validate(user);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);

        Optional<User> optionalUser = dao.update(user);
//        if (optionalUser.isEmpty()) {
//            throw new EntityNotFoundException("No user with id = " + user.getEntityId() + " found");
//        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//        return optionalUser.get();
        return optionalUser;
    }

    @Override
    public boolean delete(long id) throws EntityNotFoundException {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);

        boolean isDeleted = dao.delete(id);
        if (!isDeleted) {
            throw new EntityNotFoundException("No user with id = " + id + " found");
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return isDeleted;
    }

    @Override
    public boolean delete(User user) throws EntityNotFoundException, ValidatorException {
        Validator.getInstance().validate(user);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, User> dao = DAOFactory.INSTANCE.create(EntityType.USER, conn);

        boolean isDeleted = dao.delete(user.getEntityId());
        if (!isDeleted) {
            throw new EntityNotFoundException("No user with id = " + user.getEntityId() + " found");
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
            throwables.printStackTrace();
        }

        return Map.of(IBAN, library_User_Id);
    }

    public Map<String, Long> findAllUserBankAccount() {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        UserDAO dao = (UserDAO) DAOFactory.INSTANCE.create(EntityType.USER, conn);
        Map<String, Long> userIBANs = dao.findAllUserBankAccounts();

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userIBANs;
    }
}
