package com.epam.bookshop.dao.impl;

import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import constant.ErrorMessageConstants;
import constant.UtilStringConstants;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.util.query_creator.impl.EntityQueryCreatorFactory;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Class that interacts with the database and provides CRUD methods to do with {@link User} instance.
 * Implements DAO pattern
 */
public class UserDAO extends AbstractDAO<Long, User> {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    private static final String SQL_SELECT_ALL_USERS_WHERE =  "SELECT Id, Name, Login, Password, Email, Admin FROM TEST_LIBRARY.LIBRARY_USER WHERE ";
    private static final String SQL_INSERT_USER = "INSERT INTO TEST_LIBRARY.LIBRARY_USER (Name, Login, Password, Email, Admin) VALUES(?, ?, ?, ?, ?);";
    private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM TEST_LIBRARY.LIBRARY_USER WHERE Id = ?;";
    private static final String SQL_UPDATE_USER_BY_ID = "UPDATE TEST_LIBRARY.LIBRARY_USER SET Name = ?, Login = ?, Password = ?, Email = ?, Admin = ? WHERE Id = ?;";
    private static final String SQL_SELECT_ALL_USERS = "SELECT Id, Name, Login, Password, Email, Admin FROM TEST_LIBRARY.LIBRARY_USER ";
    private static final String SQL_SELECT_USER_BY_ID = "SELECT Id, Name, Login, Password, Email, Admin FROM TEST_LIBRARY.LIBRARY_USER u WHERE Id = ?";
    private static final String SQL_SELECT_COUNT_ALL = "SELECT COUNT(*) as Num FROM TEST_LIBRARY.LIBRARY_USER;";
    private static final String SQL_INSERT_USER_BANK_ACCOUNT = "INSERT INTO TEST_LIBRARY.USER_BANK_ACCOUNT (Library_User_Id, IBAN) VALUES (?, ?);";
    private static final String SQL_SELECT_ALL_USERs_IBANS = "SELECT Library_User_Id, IBAN FROM TEST_LIBRARY.USER_BANK_ACCOUNT;";
    private static final String SQL_SELECT_ALL_USER_IBANS_BY_LIBRARY_USER_ID = "SELECT Library_User_Id, IBAN FROM TEST_LIBRARY.USER_BANK_ACCOUNT WHERE Library_User_Id = ?;";
    private static final String SQL_DELETE_USER_IBAN_BY_IBAN = "DELETE FROM TEST_LIBRARY.USER_BANK_ACCOUNT WHERE IBAN = ?;";
    private static final String SQL_SELECT_ALL_USERS_BY_LIMIT = "SELECT Id, Name, Login, Password, Email, Admin FROM TEST_LIBRARY.LIBRARY_USER LIMIT ?, ?";

    private static final String ID_COLUMN = "Id";
    private static final String NAME_COLUMN = "Name";
    private static final String LOGIN_COLUMN = "Login";
    private static final String PASSWORD_COLUMN = "Password";
    private static final String EMAIL_COLUMN = "Email";
    private static final String ADMIN_COLUMN = "Admin";
    private static final String NUM_COLUMN = "Num";
    private static final String LIBRARY_USER_ID_COLUMN = "Library_User_Id";
    private static final String IBAN_COLUMN = "IBAN";

    UserDAO(Connection connection) { super(connection); }


    @Override
    public User create(User user) {
        try(PreparedStatement ps = getPrepareStatement(SQL_INSERT_USER)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());

            String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            ps.setString(3, hashpw);

            ps.setString(4, user.getEmail());
            ps.setBoolean(5, user.isAdmin());

            ps.executeUpdate();

        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return user;
    }



    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_USERS);
             ResultSet rs = ps.executeQuery()) {

            users = fill(rs);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return users;
    }



    @Override
    public Optional<User> findById(Long id) {
        User user = null;
        ResultSet rs = null;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_USER_BY_ID)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();

            List<User> users = fill(rs);
            if (!users.isEmpty()) {
                user = users.get(0);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        } finally {
           closeResultSet(rs, logger);
        }

        return Optional.ofNullable(user);
    }



    @Override
    public Collection<User> findAll(Criteria<User> criteria) {
        String query = SQL_SELECT_ALL_USERS_WHERE
                + EntityQueryCreatorFactory.INSTANCE.create(EntityType.USER).createQuery(criteria, UtilStringConstants.EQUALS);

        List<User> users = new ArrayList<>();

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            users = fill(rs);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return users;
    }



    @Override
    public Optional<User> find(Criteria<User> criteria) {
        String query = SQL_SELECT_ALL_USERS_WHERE
                + EntityQueryCreatorFactory.INSTANCE.create(EntityType.USER).createQuery(criteria, UtilStringConstants.EQUALS);

        User user = null;

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            List<User> users = fill(rs);
            if (!users.isEmpty()) {
                user = users.get(0);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return Optional.ofNullable(user);
    }


    @Override
    public boolean delete(User user) {
        return delete(user.getEntityId());
    }


    @Override
    public boolean delete(Long id) {
        try (PreparedStatement ps = getPrepareStatement(SQL_DELETE_USER_BY_ID)) {
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
    public Optional<User> update(User user) {
        Optional<User> optionalUserToUpdate = findById(user.getEntityId());
        if (optionalUserToUpdate.isEmpty()) {
            return Optional.empty();
        }

        try(PreparedStatement ps = getPrepareStatement(SQL_UPDATE_USER_BY_ID)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
            ps.setBoolean(5, user.isAdmin());
            ps.setLong(6, user.getEntityId());

            int result = ps.executeUpdate();

            if (result == UtilStringConstants.ZERO_ROWS_AFFECTED) {
                String locale = "US";
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_USER_UPDATE_OCCURRED);
                throw new RuntimeException(errorMessage);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return optionalUserToUpdate;
    }



    /**
     * Counts number of rows in 'BOOK' table
     *
     * @return number of rows in BOOKS table
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


    @Override
    public Collection<User> findAll(int start, int total) {
        List<User> users = new ArrayList<>();
        ResultSet rs = null;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_USERS_BY_LIMIT)) {
            ps.setInt(1, start);
            ps.setInt(2, total);
            rs = ps.executeQuery();

            users = fill(rs);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        } finally {
            closeResultSet(rs, logger);
        }

        return users;
    }


    /**
     * Created user bank account by IBAN and user id
     *
     * @param IBAN to create in database
     * @param libraryUserId  to whom IBAN is created
     */
    public void createUserBankAccount(String IBAN, Long libraryUserId) {
        try(PreparedStatement ps = getPrepareStatement(SQL_INSERT_USER_BANK_ACCOUNT)) {
            ps.setLong(1, libraryUserId);
            ps.setString(2, IBAN);

            ps.executeUpdate();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }


    /**
     * Fetches all IBANs of all users
     *
     * @return all founded IBANs in database
     */
    public Map<String, Long> findUsersBankAccounts() {
        Map<String, Long> userIBANs = new HashMap<>();

        try(PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_USERs_IBANS);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String IBAN = rs.getString(IBAN_COLUMN);
                Long libraryUserId = rs.getLong(LIBRARY_USER_ID_COLUMN);
                userIBANs.put(IBAN, libraryUserId);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return userIBANs;
    }


    /**
     * Look for user IBANs
     *
     * @param id finds all IBANs that belong to user with {@code id}
     * @return list of founded IBANs
     */
    public List<String> findUserBankAccounts(long id) {
        List<String> userIBANs = new ArrayList<>();
        ResultSet rs = null;

        try(PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_USER_IBANS_BY_LIBRARY_USER_ID)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                String IBAN = rs.getString(IBAN_COLUMN);
                userIBANs.add(IBAN);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        } finally {
            closeResultSet(rs,logger);
        }

        return userIBANs;
    }


    /**
     * Deletes user bank account
     *
     * @param iban user IBAN to delete
     * @return true, if and only if, IBAN was deleted successfully
     * from database
     */
    public boolean deleteUserBankAccount(String iban) {
        try (PreparedStatement ps = getPrepareStatement(SQL_DELETE_USER_IBAN_BY_IBAN)) {
            ps.setString(1, iban);
            int result = ps.executeUpdate();

            if (result == UtilStringConstants.ZERO_ROWS_AFFECTED) {
                return false;
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return true;
    }


    /**
     * Fills list with data in ResultSet
     *
     * @param rs sql ResultSet from where data is taken
     * @return list of parsed instances
     * @throws SQLException
     */
    private List<User> fill(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();

        while (rs.next()) {
            User user = new User();
            user.setEntityId(rs.getLong(ID_COLUMN));
            user.setName(rs.getString(NAME_COLUMN));
            user.setLogin(rs.getString(LOGIN_COLUMN));
            user.setPassword(rs.getString(PASSWORD_COLUMN));
            user.setEmail(rs.getString(EMAIL_COLUMN));
            user.setAdmin(rs.getBoolean(ADMIN_COLUMN));

            List<String> IBANs = findUserBankAccounts(user.getEntityId());
            user.setIBANs(IBANs);

            users.add(user);
        }

        return users;
    }
}
