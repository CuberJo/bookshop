package com.epam.bookshop.dao;

import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.exception.DqlException;
import com.epam.bookshop.util.criteria.Criteria;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Encapsulates common functionality interaction with database.
 */
public abstract class AbstractDao<K, T extends Entity> {
    private final Connection connection;

    public AbstractDao(Connection connection) {
        this.connection = connection;
    }

    public abstract T create(T entity) throws DqlException;
    public abstract List<T> findAll();
    public abstract Optional<T> findById(K id);
    public abstract Collection<T> findAll(Criteria<T> criteria);
    public abstract Collection<T> findAll(int start, int total);
    public abstract Optional<T> find(Criteria<T> criteria);
    public abstract boolean delete(T entity);
    public abstract boolean delete(K key);
    public abstract int count();
    public abstract Optional<T> update(T entity);

    /**
     * Creates {@link PreparedStatement} from {@code sql} string.
     *
     * @param sql sql query string used in {@link PreparedStatement} creation
     * @return {@link PreparedStatement} instance
     */
    protected PreparedStatement getPrepareStatement(String sql) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ps;
    }

    /**
     * Closes {@link ResultSet} instance.
     *
     * @param rs {@link ResultSet} to close
     * @param logger {@link Logger} instance of class where
     * prepare statement is closing
     */
    protected void closeResultSet(ResultSet rs, Logger logger) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
//
//    protected void closePrepareStatement(PreparedStatement ps) {
//        if (ps != null) {
//            try {
//                ps.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
