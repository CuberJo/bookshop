package com.epam.bookshop.dao;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.domain.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDAO <K, T extends Entity> {

    private Connection connection;

    public AbstractDAO(Connection connection) {
        this.connection = connection;
    }

    public abstract T create(T entity);
    public abstract List<T> findAll();
    public abstract Optional<T> findById(K id);
    public abstract Collection<T> findAll(Criteria criteria);
    public abstract Optional<T> find(Criteria<? extends Entity> criteria);
    public abstract boolean delete(T entity);
    public abstract boolean delete(K key);
    public abstract Optional<T> update(T entity);

    protected PreparedStatement getPrepareStatement(String sql) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ps;
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
