package com.epam.bookshop.service.impl;

import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.dao.impl.GenreDao;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.Payment;
import com.epam.bookshop.exception.DqlException;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDao;
import com.epam.bookshop.dao.impl.DAOFactory;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.validator.impl.CriteriaValidator;
import com.epam.bookshop.validator.impl.EntityValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Business logic for {@link Genre} instances
 */
public class GenreService implements EntityService<Genre> {
    private static final Logger logger = LoggerFactory.getLogger(GenreService.class);

    GenreService() {}

    private String locale = "US";

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public Genre create(Genre genre) throws ValidatorException, DqlException {
        EntityValidator entityValidator = new EntityValidator();
        entityValidator.setLocale(locale);
        entityValidator.validate(genre);

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Genre> dao = DAOFactory.INSTANCE.create(EntityType.GENRE, conn);
            dao.create(genre);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return genre;
    }

    @Override
    public Collection<Genre> findAll() {
        List<Genre> genres = new ArrayList<>();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Genre> dao = DAOFactory.INSTANCE.create(EntityType.GENRE, conn);
            genres = dao.findAll();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return genres;
    }

    @Override
    public Collection<Genre> findAll(Criteria<Genre> criteria) throws ValidatorException {
        CriteriaValidator validator = new CriteriaValidator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Collection<Genre> genres = new ArrayList<>();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Genre> dao = DAOFactory.INSTANCE.create(EntityType.GENRE, conn);
            genres = dao.findAll(criteria);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return genres;
    }

    @Override
    public Optional<Genre> findById(long id) {
        Optional<Genre> optionalGenre = Optional.empty();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Genre> dao = DAOFactory.INSTANCE.create(EntityType.GENRE, conn);
            optionalGenre = dao.findById(id);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalGenre;
    }

    @Override
    public Optional<Genre> find(Criteria<Genre> criteria) throws ValidatorException {
        CriteriaValidator validator = new CriteriaValidator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Optional<Genre> optionalGenre = Optional.empty();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Genre> dao = DAOFactory.INSTANCE.create(EntityType.GENRE, conn);
            optionalGenre = dao.find(criteria);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalGenre;
    }

    @Override
    public Optional<Genre> update(Genre genre) throws ValidatorException {
        EntityValidator entityValidator = new EntityValidator();
        entityValidator.setLocale(locale);
        entityValidator.validate(genre);

        Optional<Genre> optionalGenre = Optional.empty();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Genre> dao = DAOFactory.INSTANCE.create(EntityType.GENRE, conn);
            optionalGenre = dao.update(genre);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalGenre;
    }

    @Override
    public boolean delete(long id) throws EntityNotFoundException {
        boolean isDeleted = false;

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection();) {
            AbstractDao<Long, Genre> dao = DAOFactory.INSTANCE.create(EntityType.GENRE, conn);
            isDeleted = dao.delete(id);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        if (!isDeleted) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND)
                    + UtilStringConstants.WHITESPACE + id;
            throw new EntityNotFoundException(errorMessage + UtilStringConstants.WHITESPACE + id);
        }

        return isDeleted;
    }

    @Override
    public boolean delete(Genre genre) throws EntityNotFoundException, ValidatorException {
        EntityValidator entityValidator = new EntityValidator();
        entityValidator.setLocale(locale);
        entityValidator.validate(genre);

        boolean isDeleted = false;

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Genre> dao = DAOFactory.INSTANCE.create(EntityType.GENRE, conn);
            isDeleted = dao.delete(genre.getEntityId());
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        if (!isDeleted) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND)
                    + UtilStringConstants.WHITESPACE + genre.getEntityId();
            throw new EntityNotFoundException(errorMessage + UtilStringConstants.WHITESPACE + genre.getEntityId());
        }

        return isDeleted;
    }


    /**
     * Counts total number of genres
     *
     * @return number of rows in 'GENRE' table
     */
    @Override
    public int count() {
        int rows = 0;

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            GenreDao dao = (GenreDao) DAOFactory.INSTANCE.create(EntityType.GENRE, conn);
            rows = dao.count();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return rows;
    }

    @Override
    public Collection<Genre> findAll(int start, int total) {
        return null;
    }

    /**
     * Finds {@link Genre} genre which genre name is similar to criteria's genre name
     *
     * @param criteria {@link Criteria<Genre>} criteria by which genre is found
     * @return {@link Optional<Genre>} instance
     */
    public Optional<Genre> findLike(Criteria<Genre> criteria) throws ValidatorException {
        CriteriaValidator validator = new CriteriaValidator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Optional<Genre> optionalGenre = Optional.empty();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            GenreDao dao = (GenreDao) DAOFactory.INSTANCE.create(EntityType.GENRE, conn);
            optionalGenre = dao.findLike(criteria);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalGenre;
    }
}
