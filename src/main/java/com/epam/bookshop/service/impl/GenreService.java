package com.epam.bookshop.service.impl;

import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.dao.impl.DAOFactory;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.validator.Validator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class GenreService implements EntityService<Genre> {

    GenreService() {

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
            throwables.printStackTrace();
        }

        return genres;
    }

    @Override
    public Collection<Genre> findAll(Criteria criteria) throws ValidatorException {
        new Validator().validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Genre> dao = DAOFactory.INSTANCE.create(EntityType.GENRE, conn);

        Collection<Genre> genres = dao.findAll(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return genres;
    }

    @Override
    public Optional<Genre> findById(long id) {
        return null;
    }

    @Override
    public Optional<Genre> find(Criteria criteria) throws ValidatorException {
        new Validator().validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Genre> dao = DAOFactory.INSTANCE.create(EntityType.GENRE, conn);

        Optional<Genre> optionalGenre = dao.find(criteria);
//        if (optionalGenre.isEmpty()) {
//            throw new EntityNotFoundException("No genre with genreName = " + ((GenreCriteria)criteria).getGenre() + " found");
//        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//        return optionalGenre.get();
        return optionalGenre;
    }

    @Override
    public Optional<Genre> update(Genre entity) throws ValidatorException {
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
