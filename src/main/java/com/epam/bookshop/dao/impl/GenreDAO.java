package com.epam.bookshop.dao.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.strategy.query_creator.FindEntityQueryCreator;
import com.epam.bookshop.strategy.query_creator.impl.FindEntityQueryCreatorFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class GenreDAO extends AbstractDAO<Long, Genre> {

    private static final String SQL_SELECT_ALL_GENRES = "SELECT Id, Genre FROM TEST_LIBRARY.GENRE;";
    private static final String SQL_SELECT_GENRE_BY_ID = "SELECT Id, Genre FROM TEST_LIBRARY.GENRE WHERE Id = ?;";

    private static final String ID_COLUMN = "Id";
    private static final String GENRE_COLUMN = "Genre";

    private String locale = "EN";

    public GenreDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
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

            while (rs.next()) {
                Genre genre = new Genre();

                genre.setEntityId(rs.getLong(ID_COLUMN));
                genre.setGenre(rs.getString(GENRE_COLUMN));

                genres.add(genre);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

            while (rs.next()) {
                genre = new Genre();
                genre.setEntityId(rs.getLong(ID_COLUMN));
                genre.setGenre(rs.getString(GENRE_COLUMN));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return Optional.ofNullable(genre);
    }

    @Override
    public Collection<Genre> findAll(Criteria criteria) {
        FindEntityQueryCreator queryCreator = FindEntityQueryCreatorFactory.INSTANCE.create(EntityType.GENRE);
        queryCreator.setLocale(locale);
        String query = queryCreator.createQuery(criteria);

        List<Genre> genres = new ArrayList<>();

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Genre genre = new Genre();
                genre.setEntityId(rs.getLong(ID_COLUMN));
                genre.setGenre(rs.getString(GENRE_COLUMN));

                genres.add(genre);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return genres;
    }

    @Override
    public Optional<Genre> find(Criteria<? extends Entity> criteria) {
        FindEntityQueryCreator queryCreator = FindEntityQueryCreatorFactory.INSTANCE.create(EntityType.GENRE);
        queryCreator.setLocale(locale);
        String query = queryCreator.createQuery(criteria);

        Genre genre = null;

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                genre = new Genre();
                genre.setEntityId(rs.getLong(ID_COLUMN));
                genre.setGenre(rs.getString(GENRE_COLUMN));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
}
