package com.epam.bookshop.dao.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.domain.impl.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class RoleDAO extends AbstractDAO<Long, Role> {

    private static final String SQL_SELECT_ALL_ROLES = "SELECT Id, Role FROM TEST_LIBRARY.USER_ROLE;";
    private static final String SQL_SELECT_ROLE_BY_ID = "SELECT Id, Role FROM TEST_LIBRARY.USER_ROLE WHERE Id = ?;";

    private static final String ID_COLUMN = "Id";
    private static final String ROLE_COLUMN = "Role";

    private String locale = "EN";

    public RoleDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public Role create(Role entity) {
        return null;
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();

        try(PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_ROLES);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Role role = new Role();

                role.setEntityId(rs.getLong(ID_COLUMN));
                role.setRole(rs.getString(ROLE_COLUMN));

                roles.add(role);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return roles;
    }

    @Override
    public Optional<Role> findById(Long id) {
        Role role = null;
        ResultSet rs = null;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_ROLE_BY_ID)) {

            ps.setLong(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                role = new Role();
                role.setEntityId(rs.getLong(ID_COLUMN));
                role.setRole(rs.getString(ROLE_COLUMN));
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

        return Optional.ofNullable(role);
    }

    @Override
    public Collection<Role> findAll(Criteria criteria) {
        return null;
    }

    @Override
    public Optional<Role> find(Criteria<? extends Entity> criteria) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Role entity) {
        return false;
    }

    @Override
    public boolean delete(Long key) {
        return false;
    }

    @Override
    public Optional<Role> update(Role entity) {
        return Optional.empty();
    }
}
