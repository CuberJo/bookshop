package com.epam.bookshop.service.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.dao.impl.DAOFactory;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
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

public class GenreService implements EntityService<Genre> {

    private static final Logger logger = LoggerFactory.getLogger(GenreService.class);

    GenreService() {

    }

    private String locale = "EN";

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public Genre create(Genre entity) throws ValidatorException {
        return null;
    }

    @Override
    public Collection<Genre> findAll() {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Genre> dao = DAOFactory.INSTANCE.create(EntityType.GENRE, conn);
        List<Genre> genres = dao.findAll();

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return genres;
    }

    @Override
    public Collection<Genre> findAll(Criteria<Genre> criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Genre> dao = DAOFactory.INSTANCE.create(EntityType.GENRE, conn);
        Collection<Genre> genres = dao.findAll(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return genres;
    }

    @Override
    public Optional<Genre> findById(long id) {
        return null;
    }

    @Override
    public Optional<Genre> find(Criteria<Genre> criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Genre> dao = DAOFactory.INSTANCE.create(EntityType.GENRE, conn);
        Optional<Genre> optionalGenre = dao.find(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalGenre;
    }

    @Override
    public Optional<Genre> update(Genre genre) throws ValidatorException {
        return null;
    }

    @Override
    public boolean delete(long id) throws EntityNotFoundException {
        return false;
    }

    @Override
    public boolean delete(Genre entity) throws EntityNotFoundException {
        return false;
    }
}
