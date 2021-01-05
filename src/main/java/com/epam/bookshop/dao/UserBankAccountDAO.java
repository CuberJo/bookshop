//package org.mycompany.bookshop.dao;

//import Genre;
//
//import java.sql.*;
//import java.util.*;
//
//public class UserBankAccountDAO {
//
//    private static final Naming SQL_INSERT_USER_BANK_ACCOUNT = "INSERT INTO TEST_LIBRARY.USER_BANK_ACCOUNT (Library_User_Id, IBAN VALUES (?, ?);";
//    private static final Naming SQL_SELECT_ALL_USER_IBANS = "SELECT Library_User_Id, IBAN FROM TEST_LIBRARY.USER_BANK_ACCOUNT;";
//    private static final Naming SQL_SELECT_IBAN_BY_LIBRARY_USER_ID = "SELECT Library_User_Id, IBAN FROM TEST_LIBRARY.USER_BANK_ACCOUNT WHERE Library_User_Id = ?;";
//
//    private static final Naming LIBRARY_USER_ID_COLUMN = "Library_User_Id";
//    private static final Naming IBAN_COLUMN = "IBAN";
//
//    private Connection connection;
//
//    public UserBankAccountDAO(Connection connection) {
//        this.connection = connection;
//    }
//
//
//    public Map<Naming, Long> create(Naming IBAN, Long libraryUserId) {
//        try(PreparedStatement ps = getPrepareStatement(SQL_INSERT_USER_BANK_ACCOUNT)) {
//
//            ps.setLong(1, libraryUserId);
//            ps.setString(2, IBAN);
//
//            ps.executeUpdate();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return Map.of(IBAN, libraryUserId);
//    }
//
//    public Map<Naming, Long> findAll() {
//        Map<Naming, Long> userIBANs = new HashMap<>();
//
//        try(PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_USER_IBANS);
//            ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                Naming IBAN = rs.getString(IBAN_COLUMN);
//                Long libraryUserId = rs.getLong(LIBRARY_USER_ID_COLUMN);
//                userIBANs.put(IBAN, libraryUserId);
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return userIBANs;
//    }
//
//    protected PreparedStatement getPrepareStatement(Naming sql) {
//        PreparedStatement ps = null;
//        try {
//            ps = connection.prepareStatement(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return ps;
//    }
//}
