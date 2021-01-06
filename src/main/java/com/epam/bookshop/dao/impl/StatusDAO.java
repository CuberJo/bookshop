package com.epam.bookshop.dao.impl;

import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.domain.impl.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class StatusDAO extends AbstractDAO<Long, Status> {

    private static final String SQL_SELECT_ALL_STATUSES = "SELECT Id, Status FROM TEST_LIBRARY.ORDER_STATUS;";
    private static final String SQL_SELECT_STATUS_BY_ID = "SELECT Id, Status FROM TEST_LIBRARY.ORDER_STATUS WHERE Id = ?;";

    private static final String STATUS_COLUMN = "Status";
    private static final String ID_COLUMN = "Id";

    private String locale = "EN";

    public StatusDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public Status create(Status entity) {
        return null;
    }

    @Override
    public List<Status> findAll() {
        List<Status> statuses = new ArrayList<>();

        try(PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_STATUSES);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Status status = new Status();

                status.setEntityId(rs.getLong(ID_COLUMN));
                status.setStatus(rs.getString(STATUS_COLUMN));

                statuses.add(status);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return statuses;
    }


    @Override
    public Optional<Status> findById(Long id) {
        Status  status = null;
        ResultSet rs = null;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_STATUS_BY_ID)) {

            ps.setLong(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                status = new Status();
                status.setEntityId(rs.getLong(ID_COLUMN));
                status.setStatus(rs.getString(STATUS_COLUMN));
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

        return Optional.ofNullable(status);
    }

    @Override
    public Collection<Status> findAll(Criteria criteria) {
        return null;
    }

    @Override
    public Optional<Status> find(Criteria<? extends Entity> criteria) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Status entity) {
        return false;
    }

    @Override
    public boolean delete(Long key) {
        return false;
    }

    @Override
    public Optional<Status> update(Status entity) {
        return Optional.empty();
    }
}
