package com.epam.bookshop.service.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.dao.impl.DAOFactory;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Role;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class RoleService implements EntityService<Role> {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    private String locale = "US";

    RoleService() {

    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public Role create(Role role) throws ValidatorException {
        return null;
    }

    @Override
    public Collection<Role> findAll() {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Role> dao = DAOFactory.INSTANCE.create(EntityType.ROLE, conn);
        List<Role> roles = dao.findAll();

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return roles;
    }

    @Override
    public Collection<Role> findAll(Criteria<Role> criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Role> dao = DAOFactory.INSTANCE.create(EntityType.ROLE, conn);
        Collection<Role> roles = dao.findAll(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return roles;
    }

    @Override
    public Optional<Role> findById(long id) {
        return null;
    }

    @Override
    public Optional<Role> find(Criteria<Role> criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Role> dao = DAOFactory.INSTANCE.create(EntityType.ROLE, conn);
        Optional<Role> optionalRole = dao.find(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalRole;
    }

    @Override
    public Optional<Role> update(Role role) throws ValidatorException {
        return null;
    }

    @Override
    public boolean delete(long id) throws EntityNotFoundException {
        return false;
    }

    @Override
    public boolean delete(Role role) throws EntityNotFoundException {
        return false;
    }
}
