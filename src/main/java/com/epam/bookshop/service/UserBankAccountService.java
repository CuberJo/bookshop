//package org.mycompany.bookshop.service;
//
//import org.mycompany.bookshop.dao.UserBankAccountDAO;
//import ConnectionPool;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.Map;
//
//public class UserBankAccountService {
//
//    public Map<Naming, Long> create(Naming IBAN, Long library_User_Id) {
//        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
//        UserBankAccountDAO dao = new UserBankAccountDAO(conn);
//        dao.create(IBAN, library_User_Id);
//
//        try {
//            conn.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return Map.of(IBAN, library_User_Id);
//    }
//
//    public Map<Naming, Long> findAll() {
//        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
//        UserBankAccountDAO dao = new UserBankAccountDAO(conn);
//        Map<Naming, Long> userIBANs = dao.findAll();
//
//        try {
//            conn.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return userIBANs;
//    }
//}
