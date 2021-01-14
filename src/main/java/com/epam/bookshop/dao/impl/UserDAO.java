package com.epam.bookshop.dao.impl;

import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import org.mindrot.jbcrypt.BCrypt;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Role;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.strategy.query_creator.FindEntityQueryCreator;
import com.epam.bookshop.strategy.query_creator.impl.FindEntityQueryCreatorFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserDAO extends AbstractDAO<Long, User> {

    private static final String SQL_INSERT_USER = "INSERT INTO TEST_LIBRARY.LIBRARY_USER (Name, Login, Password, Email, Role_Id) VALUES(?, ?, ?, ?, ?);";
    private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM TEST_LIBRARY.LIBRARY_USER WHERE Id = ?;";
    private static final String SQL_UPDATE_USER_BY_ID = "UPDATE TEST_LIBRARY.LIBRARY_USER SET Name = ?, Login = ?, Password = ?, Email = ?, Role_Id = ? WHERE Id = ?;";

    private static String SQL_SELECT_ALL_USERS = "SELECT Id, Name, Login, Password, Email, Role_Id " +
            "FROM TEST_LIBRARY.LIBRARY_USER ";

    private static String SQL_SELECT_USER_BY_ID = "SELECT Id, Name, Login, Password, Email, Role_Id " +
            "FROM TEST_LIBRARY.LIBRARY_USER u " +
            "WHERE Id = ?";

    private static final String ID_COLUMN = "Id";
    private static final String NAME_COLUMN = "Name";
    private static final String LOGIN_COLUMN = "Login";
    private static final String PASSWORD_COLUMN = "Password";
    private static final String EMAIL_COLUMN = "Email";
    private static final String ROLE_ID_COLUMN = "Role_Id";

    private static final String SQL_INSERT_USER_BANK_ACCOUNT = "INSERT INTO TEST_LIBRARY.USER_BANK_ACCOUNT (Library_User_Id, IBAN) VALUES (?, ?);";
    private static final String SQL_SELECT_ALL_USERs_IBANS = "SELECT Library_User_Id, IBAN FROM TEST_LIBRARY.USER_BANK_ACCOUNT;";
    private static final String SQL_SELECT_ALL_USER_IBANS_BY_LIBRARY_USER_ID = "SELECT Library_User_Id, IBAN FROM TEST_LIBRARY.USER_BANK_ACCOUNT WHERE Library_User_Id = ?;";
    private static final String SQL_DELETE_USER_IBAN_BY_IBAN = "DELETE FROM TEST_LIBRARY.USER_BANK_ACCOUNT WHERE IBAN = ?;";


    private static final String LIBRARY_USER_ID_COLUMN = "Library_User_Id";
    private static final String IBAN_COLUMN = "IBAN";

    private static final String ROLE_NOT_FOUND = "role_not_found";
    private static final String IBANs_NOT_FOUND = "IBANs_not_found";
    private static final String NO_USER_UPDATE_OCCURRED = "no_user_update_occurred";
    private static final String WHITESPACE = " ";

    private static final Integer ZERO_ROWS_AFFECTED = 0;

    private String locale = "EN";

    UserDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public User create(User user) {

        try(PreparedStatement ps = getPrepareStatement(SQL_INSERT_USER)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());

            String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            ps.setString(3, hashpw);

            ps.setString(4, user.getEmail());
            ps.setLong(5, user.getRole().getEntityId());

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_USERS);
             ResultSet rs = ps.executeQuery()) {

            AbstractDAO<Long, Role> roleDAO = DAOFactory.INSTANCE.create(EntityType.ROLE, ConnectionPool.getInstance().getAvailableConnection());
//            UserBankAccountDAO userBankAccountDAO = new UserBankAccountDAO(ConnectionPool.getInstance().getAvailableConnection());

            String errorMessage = "";
            while (rs.next()) {
                User user = new User();
                user.setEntityId(rs.getLong(ID_COLUMN));
                user.setName(rs.getString(NAME_COLUMN));
                user.setLogin(rs.getString(LOGIN_COLUMN));
                user.setPassword(rs.getString(PASSWORD_COLUMN));
                user.setEmail(rs.getString(EMAIL_COLUMN));

                Optional<Role> optionalRole = roleDAO.findById(rs.getLong(ROLE_ID_COLUMN));
                if (optionalRole.isEmpty()) {
                    errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ROLE_NOT_FOUND) + WHITESPACE + rs.getLong(ROLE_ID_COLUMN);
                    throw new RuntimeException(errorMessage + WHITESPACE + rs.getLong(ROLE_ID_COLUMN));
                }
                user.setRole(optionalRole.get());

                List<String> IBANs = findUserBankAccounts(user.getEntityId());
//                Map<String, Long> userIBANS = findAllUserBankAccounts();
//                Iterator<Map.Entry<String, Long>> it = userIBANS.entrySet().iterator();
//                List<String> IBANs = new ArrayList<>();
//                while (it.hasNext()) {
//                    // if User id equals user id from map, then ..
//                    Map.Entry<String, Long> next = it.next();
//                    if (next.getValue().equals(ID_COLUMN)) {
//                        IBANs.add(next.getKey());
//                    }
//                }
//                if (IBANs.isEmpty()) {
//                    errorMessage = ErrorMessageManager.valueOf(locale).getMessage(IBANs_NOT_FOUND);
//                    throw new RuntimeException(errorMessage);
//                }
                user.setIBANs(IBANs);

                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

            AbstractDAO<Long, Role> roleDAO = DAOFactory.INSTANCE.create(EntityType.ROLE, ConnectionPool.getInstance().getAvailableConnection());
//            UserBankAccountDAO userBankAccountDAO = new UserBankAccountDAO(ConnectionPool.getInstance().getAvailableConnection());

            while (rs.next()) {
                user = new User();

                user.setEntityId(rs.getLong(ID_COLUMN));
                user.setName(rs.getString(NAME_COLUMN));
                user.setLogin(rs.getString(LOGIN_COLUMN));
                user.setPassword(rs.getString(PASSWORD_COLUMN));
                user.setEmail(rs.getString(EMAIL_COLUMN));

                Optional<Role> optionalRole = roleDAO.findById(rs.getLong(ROLE_ID_COLUMN));
                if (optionalRole.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ROLE_NOT_FOUND) + WHITESPACE + rs.getLong(ROLE_ID_COLUMN);
                    throw new RuntimeException(errorMessage + WHITESPACE + rs.getLong(ROLE_ID_COLUMN));
                }
                user.setRole(optionalRole.get());

                List<String> IBANs = findUserBankAccounts(user.getEntityId());
//                Map<String, Long> userIBANS = findAllUserBankAccounts();
//                Iterator<Map.Entry<String, Long>> it = userIBANS.entrySet().iterator();
//                List<String> IBANs = new ArrayList<>();
//                while (it.hasNext()) {
//                    // if User id equals user id from map, then ..
//                    Map.Entry<String, Long> next = it.next();
//                    if (next.getValue().equals(ID_COLUMN)) {
//                        IBANs.add(next.getKey());
//                    }
//                }
//                if (IBANs.isEmpty()) {
////                    throw new RuntimeException("No IBANs found");
//                }
                user.setIBANs(IBANs);


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

        return Optional.ofNullable(user);
    }

    @Override
    public Collection<User> findAll(Criteria criteria) {
        FindEntityQueryCreator queryCreator = FindEntityQueryCreatorFactory.INSTANCE.create(EntityType.USER);
        queryCreator.setLocale(locale);
        String query = queryCreator.createQuery(criteria);

        List<User> users = new ArrayList<>();

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            AbstractDAO<Long, Role> roleDAO = DAOFactory.INSTANCE.create(EntityType.ROLE, ConnectionPool.getInstance().getAvailableConnection());
//            UserBankAccountDAO userBankAccountDAO = new UserBankAccountDAO(ConnectionPool.getInstance().getAvailableConnection());

            String errorMessage = "";

            while (rs.next()) {
                User user = new User();
                user.setEntityId(rs.getLong(ID_COLUMN));
                user.setName(rs.getString(NAME_COLUMN));
                user.setLogin(rs.getString(LOGIN_COLUMN));
                user.setPassword(rs.getString(PASSWORD_COLUMN));
                user.setEmail(rs.getString(EMAIL_COLUMN));

                Optional<Role> optionalRole = roleDAO.findById(rs.getLong(ROLE_ID_COLUMN));
                if (optionalRole.isEmpty()) {
                    errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ROLE_NOT_FOUND) + WHITESPACE + rs.getLong(ROLE_ID_COLUMN);
                    throw new RuntimeException(errorMessage + WHITESPACE + rs.getLong(ROLE_ID_COLUMN));
                }
                user.setRole(optionalRole.get());

                List<String> IBANs = findUserBankAccounts(user.getEntityId());
//                Map<String, Long> userIBANS = findAllUserBankAccounts();
//                Iterator<Map.Entry<String, Long>> it = userIBANS.entrySet().iterator();
//                List<String> IBANs = new ArrayList<>();
//                while (it.hasNext()) {
//                    // if User id equals user id from map, then ..
//                    Map.Entry<String, Long> next = it.next();
//                    if (next.getValue().equals(ID_COLUMN)) {
//                        IBANs.add(next.getKey());
//                    }
//                }
//                if (IBANs.isEmpty()) {
//                    errorMessage = ErrorMessageManager.valueOf(locale).getMessage(IBANs_NOT_FOUND);
//                    throw new RuntimeException(errorMessage);
//                }
                user.setIBANs(IBANs);

                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }


    @Override
    public Optional<User> find(Criteria<? extends Entity> criteria) {
        FindEntityQueryCreator queryCreator = FindEntityQueryCreatorFactory.INSTANCE.create(EntityType.USER);
        queryCreator.setLocale(locale);
        String query = queryCreator.createQuery(criteria);

        User user = null;

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            AbstractDAO<Long, Role> roleDAO = DAOFactory.INSTANCE.create(EntityType.ROLE, ConnectionPool.getInstance().getAvailableConnection());
//            UserBankAccountDAO userBankAccountDAO = new UserBankAccountDAO(ConnectionPool.getInstance().getAvailableConnection());

            while (rs.next()) {
                user = new User();

                user.setEntityId(rs.getLong(ID_COLUMN));
                user.setName(rs.getString(NAME_COLUMN));
                user.setLogin(rs.getString(LOGIN_COLUMN));
                user.setPassword(rs.getString(PASSWORD_COLUMN));
                user.setEmail(rs.getString(EMAIL_COLUMN));

                Optional<Role> optionalRole = roleDAO.findById(rs.getLong(ROLE_ID_COLUMN));
                if (optionalRole.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ROLE_NOT_FOUND) + WHITESPACE + rs.getLong(ROLE_ID_COLUMN);
                    throw new RuntimeException(errorMessage + WHITESPACE + rs.getLong(ROLE_ID_COLUMN));
                }
                user.setRole(optionalRole.get());

                List<String> IBANs = findUserBankAccounts(user.getEntityId());
//                Map<String, Long> userIBANS = findAllUserBankAccounts();
//                Iterator<Map.Entry<String, Long>> it = userIBANS.entrySet().iterator();
//                List<String> IBANs = new ArrayList<>();
//                while (it.hasNext()) {
//                    // if User id equals user id from map, then ..
//                    Map.Entry<String, Long> next = it.next();
//                    if (next.getValue().equals(ID_COLUMN)) {
//                        IBANs.add(next.getKey());
//                    }
//                }
//                if (IBANs.isEmpty()) {
////                    throw new RuntimeException("No IBANs found");
//
//                }
                user.setIBANs(IBANs);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

            if (result == ZERO_ROWS_AFFECTED) {
                return false;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

            String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            ps.setString(3, hashpw);

            ps.setString(4, user.getEmail());
            ps.setLong(5, user.getRole().getEntityId());
            ps.setLong(6, user.getEntityId());

            int result = ps.executeUpdate();

            if (result == ZERO_ROWS_AFFECTED) {
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(NO_USER_UPDATE_OCCURRED);
                throw new RuntimeException(errorMessage);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return optionalUserToUpdate;
    }

    public Map<String, Long> createUserBankAccount(String IBAN, Long libraryUserId) {
        try(PreparedStatement ps = getPrepareStatement(SQL_INSERT_USER_BANK_ACCOUNT)) {

            ps.setLong(1, libraryUserId);
            ps.setString(2, IBAN);

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Map.of(IBAN, libraryUserId);
    }

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
            throwables.printStackTrace();
        }

        return userIBANs;
    }

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

        return userIBANs;
    }

    public boolean deleteUserBankAccount(String iban) {

        try (PreparedStatement ps = getPrepareStatement(SQL_DELETE_USER_IBAN_BY_IBAN)) {
            ps.setString(1, iban);
            int result = ps.executeUpdate();

            if (result == ZERO_ROWS_AFFECTED) {
                return false;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }
}
