package com.epam.bookshop.dao.impl;

import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.util.query_creator.impl.EntityQueryCreatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Class that interacts with the database and provides CRUD methods to do with {@link Genre} instance
 */
public class GenreDAO extends AbstractDAO<Long, Genre> {
    private static final Logger logger = LoggerFactory.getLogger(GenreDAO.class);

    private static final String SQL_SELECT_ALL_GENRES_WHERE =  "SELECT Id, Genre FROM TEST_LIBRARY.GENRE WHERE ";
    private static final String SQL_SELECT_ALL_GENRES = "SELECT Id, Genre FROM TEST_LIBRARY.GENRE;";
    private static final String SQL_SELECT_GENRE_BY_ID = "SELECT Id, Genre FROM TEST_LIBRARY.GENRE WHERE Id = ?;";

    private static final String ID_COLUMN = "Id";
    private static final String GENRE_COLUMN = "Genre";

    private final String locale = "US";

    GenreDAO(Connection connection) {
        super(connection);
    }


    @Override
    public Genre create(Genre entity) {
        return null;
    }


    @Override
    public List<Genre> findAll() {
        List<Genre> genres = new ArrayList<>();

        try(PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_GENRES);
            ResultSet rs = ps.executeQuery()) {

            genres = fill(rs);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return genres;
    }



    @Override
    public Optional<Genre> findById(Long id) {
        Genre genre = null;
        ResultSet rs = null;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_GENRE_BY_ID)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();

            List<Genre> genres = fill(rs);
            if (!genres.isEmpty()) {
                genre  = genres.get(0);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        } finally {
            closeResultSet(rs, logger);
        }

        return Optional.ofNullable(genre);
    }

    @Override
    public Collection<Genre> findAll(Criteria<Genre> criteria) {
        String query = SQL_SELECT_ALL_GENRES_WHERE
                + EntityQueryCreatorFactory.INSTANCE.create(EntityType.GENRE).createQuery(criteria, UtilStringConstants.EQUALS);

        List<Genre> genres = new ArrayList<>();

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            genres = fill(rs);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return genres;
    }



    @Override
    public Optional<Genre> find(Criteria<Genre> criteria) {
        String query = SQL_SELECT_ALL_GENRES_WHERE
                + EntityQueryCreatorFactory.INSTANCE.create(EntityType.GENRE).createQuery(criteria, UtilStringConstants.EQUALS);

        Genre genre = null;

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            List<Genre> genres = fill(rs);
            if (!genres.isEmpty()) {
                genre  = genres.get(0);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return Optional.ofNullable(genre);
    }



    @Override
    public boolean delete(Genre entity) {
        return false;
    }



    @Override
    public boolean delete(Long key) {
        return false;
    }



    @Override
    public Optional<Genre> update(Genre entity) {
        return Optional.empty();
    }


    /**
     * Finds {@link Genre} genre which genre name is similar to criteria's genre name
     *
     * @param criteria {@link Criteria<Genre>} criteria by which genre is found
     * @return {@link Optional<Genre>} if found, otherwise - empty
     */
    public Optional<Genre> findLike(Criteria<Genre> criteria) {
        String query = SQL_SELECT_ALL_GENRES_WHERE
                + EntityQueryCreatorFactory.INSTANCE.create(EntityType.GENRE).createQuery(criteria, UtilStringConstants.LIKE);

        Genre genre = null;

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            List<Genre> genres = fill(rs);
            if (!genres.isEmpty()) {
                genre  = genres.get(0);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return Optional.ofNullable(genre);
    }


    @Override
    public int count() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Genre> findAll(int start, int total) {
        throw new UnsupportedOperationException();
    }


    /**
     * Fills list with data in ResultSet
     *
     * @param rs sql ResultSet from where data is taken
     * @return list of parsed instances
     * @throws SQLException
     */
    private List<Genre> fill(ResultSet rs) throws SQLException {
        List<Genre> genres = new ArrayList<>();

        while (rs.next()) {
            Genre genre = new Genre();
            genre.setEntityId(rs.getLong(ID_COLUMN));
            genre.setGenre(rs.getString(GENRE_COLUMN));
            genres.add(genre);
        }

        return genres;
    }
}
