package com.epam.bookshop.dao.impl;

import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.dao.AbstractDao;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.DqlException;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.util.query_creator.impl.SqlConditionQueryCreatorFactory;
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
public class GenreDao extends AbstractDao<Long, Genre> {
    private static final Logger logger = LoggerFactory.getLogger(GenreDao.class);

    private static final String SQL_INSERT_GENRE = "INSERT INTO BOOKSHOP.GENRE (Genre) VALUES (?);";
    private static final String SQL_DELETE_GENRE_BY_ID = "DELETE FROM BOOKSHOP.GENRE WHERE Id = ?;";
    private static final String SQL_UPDATE_GENRE_BY_ID = "UPDATE BOOKSHOP.GENRE SET Genre = ? WHERE Id = ?;";
    private static final String SQL_SELECT_COUNT_ALL = "SELECT COUNT(*) as Num FROM BOOKSHOP.GENRE;";
    private static final String SQL_SELECT_ALL_GENRES_BY_LIMIT = "SELECT Id, Genre FROM BOOKSHOP.GENRE LIMIT ?, ?";
    private static final String SQL_SELECT_ALL_GENRES_WHERE =  "SELECT Id, Genre FROM BOOKSHOP.GENRE WHERE ";
    private static final String SQL_SELECT_ALL_GENRES = "SELECT Id, Genre FROM BOOKSHOP.GENRE;";
    private static final String SQL_SELECT_GENRE_BY_ID = "SELECT Id, Genre FROM BOOKSHOP.GENRE WHERE Id = ?;";

    private static final String ID_COLUMN = "Id";
    private static final String GENRE_COLUMN = "Genre";
    private static final String NUM_COLUMN = "Num";

    private final String locale = "US";

    GenreDao(Connection connection) {
        super(connection);
    }


    @Override
    public Genre create(Genre genre) throws DqlException {
        try(PreparedStatement ps = getPrepareStatement(SQL_INSERT_GENRE)) {
            ps.setString(1, genre.getGenre());

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throw new DqlException(throwables.getMessage(), throwables);
        }

        return genre;
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
                + SqlConditionQueryCreatorFactory.INSTANCE.create(EntityType.GENRE).createQuery(criteria, UtilStringConstants.EQUALS);

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
                + SqlConditionQueryCreatorFactory.INSTANCE.create(EntityType.GENRE).createQuery(criteria, UtilStringConstants.EQUALS);

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
    public boolean delete(Genre genre) {
        return delete(genre.getEntityId());
    }



    @Override
    public boolean delete(Long id) {
        try (PreparedStatement ps = getPrepareStatement(SQL_DELETE_GENRE_BY_ID)) {
            ps.setLong(1, id);
            int result = ps.executeUpdate();

            if (result == UtilStringConstants.ZERO_ROWS_AFFECTED) {
                return false;
            }

        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return true;
    }



    @Override
    public Optional<Genre> update(Genre genre) {
        Optional<Genre> optionalGenreToUpdate = findById(genre.getEntityId());
        if (optionalGenreToUpdate.isEmpty()) {
            return Optional.empty();
        }

        try(PreparedStatement ps = getPrepareStatement(SQL_UPDATE_GENRE_BY_ID)) {
            ps.setString(1, genre.getGenre());
            ps.setLong(2, genre.getEntityId());

            int result = ps.executeUpdate();

            if (result == UtilStringConstants.ZERO_ROWS_AFFECTED) {
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_BOOK_UPDATE_OCCURRED);
                throw new RuntimeException(errorMessage);
            }

        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalGenreToUpdate;
    }


    /**
     * Finds {@link Genre} genre which genre name is similar to criteria's genre name
     *
     * @param criteria {@link Criteria<Genre>} criteria by which genre is found
     * @return {@link Optional<Genre>} if found, otherwise - empty
     */
    public Optional<Genre> findLike(Criteria<Genre> criteria) {
        String query = SQL_SELECT_ALL_GENRES_WHERE
                + SqlConditionQueryCreatorFactory.INSTANCE.create(EntityType.GENRE).createQuery(criteria, UtilStringConstants.LIKE);

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


    /**
     * Counts number of rows in 'GENRE' table
     *
     * @return number of rows in GENRE table
     */
    @Override
    public int count() {
        int rows = 0;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_COUNT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows = rs.getInt(NUM_COLUMN);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return rows;
    }


    /**
     * Finds limited by <b>start</b> and <b>end</b> portion of genre names
     *
     * @param start from where to start search
     * @param total how many rows to find
     * @return found {@link List<Genre>}
     */
    @Override
    public List<Genre> findAll(int start, int total) {
        List<Genre> genres = new ArrayList<>();
        ResultSet rs = null;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_GENRES_BY_LIMIT)) {
            ps.setInt(1, start);
            ps.setInt(2, total);
            rs = ps.executeQuery();

            genres = fill(rs);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        } finally {
            closeResultSet(rs, logger);
        }

        return genres;
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
