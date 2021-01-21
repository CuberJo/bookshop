package com.epam.bookshop.dao.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.*;
import com.epam.bookshop.strategy.query_creator.impl.EntityQueryCreatorFactory;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class OrderDAO extends AbstractDAO<Long, Payment> {
    private static final Logger logger = LoggerFactory.getLogger(OrderDAO.class);

    private static final String SQL_SELECT_ALL_ORDERS_WHERE =  "SELECT Id, Library_User_Id, Order_Time, Status_Id FROM TEST_LIBRARY.ORDER_BOOK WHERE ";
    private static final String SQL_UPDATE_ORDER_WHERE =  "UPDATE TEST_LIBRARY.ORDER SET %s WHERE Id = ?";

    private static final String SQL_INSERT_ORDER = "INSERT INTO TEST_LIBRARY.ORDER (Library_User_Id, Order_Time, Status_Id) VALUES (?, ?, ?);";
    private static final String SQL_DELETE_ORDER_BY_ID = "DELETE FROM TEST_LIBRARY.ORDER WHERE Id = ?;";
    private static final String SQL_UPDATE_ORDER_BY_ID = "UPDATE TEST_LIBRARY.ORDER SET Library_User_Id = ?, Order_Time = ?, Status_Id = ? WHERE Id = ?;";

    private static final String SQL_SELECT_ALL_ORDERS = "SELECT Id, Library_User_Id, Order_Time, Status_Id " +
            "FROM TEST_LIBRARY.ORDER ";

    private static final String SQL_SELECT_ALL_BOOKS_IN_THIS_ORDER = "SELECT Order_Id, Book_Id " +
            "FROM TEST_LIBRARY.ORDER_BOOK " +
            "WHERE Order_Id = ?";

    private static final String SQL_SELECT_ORDER_BY_ID = "SELECT Id, Library_User_Id, Order_Time, Status_Id " +
            "FROM TEST_LIBRARY.ORDER " +
            "WHERE Id = ?";

    private static final String ID_COLUMN = "Id";
    private static final String LIBRARY_USER_ID_COLUMN = "Library_User_Id";
    private static final String ORDER_TIME_COLUMN = "Order_Time";
    private static final String STATUS_ID_COLUMN = "Status_Id";

    private static final String BOOK_ID_COLUMN = "Book_Id";

    private final String locale = "US";

    public OrderDAO(Connection connection) {
        super(connection);
    }



    @Override
    public Payment create(Payment payment) {

        try(PreparedStatement ps = getPrepareStatement(SQL_INSERT_ORDER)) {

            ps.setLong(1, payment.getUser().getEntityId());
            ps.setTimestamp(2, Timestamp.valueOf(payment.getPaymentTime()));

            AbstractDAO<Long, Status> statusDAO = DAOFactory.INSTANCE.create(EntityType.STATUS, ConnectionPool.getInstance().getAvailableConnection());
            Optional<Status> optionalStatus = statusDAO.find(StatusCriteria.builder().status(payment.getStatus().getStatus()).build());
            if (optionalStatus.isEmpty()) {
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_STATUS_FOUND) + UtilStrings.NEW_LINE + payment.getStatus();
                throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + payment.getStatus().getStatus());
            }
            ps.setLong(3, optionalStatus.get().getEntityId());

            ps.executeUpdate();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return payment;
    }



    @Override
    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_ORDERS);
             ResultSet rs = ps.executeQuery()) {

            AbstractDAO<Long, User> userDAO = DAOFactory.INSTANCE.create(EntityType.USER, ConnectionPool.getInstance().getAvailableConnection());
            AbstractDAO<Long, Status> statusDAO = DAOFactory.INSTANCE.create(EntityType.STATUS, ConnectionPool.getInstance().getAvailableConnection());

            AbstractDAO<Long, Book> bookDAO = DAOFactory.INSTANCE.create(EntityType.BOOK, ConnectionPool.getInstance().getAvailableConnection());

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setEntityId(rs.getLong(ID_COLUMN));

                Optional<User> optionalUser = userDAO.findById(rs.getLong(LIBRARY_USER_ID_COLUMN));
                if (optionalUser.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND) + UtilStrings.WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN);
                    throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN));
                }
                payment.setUser(optionalUser.get());

                payment.setPaymentTime(rs.getTimestamp(ORDER_TIME_COLUMN).toLocalDateTime());

                Optional<Status> optionalStatus = statusDAO.findById(rs.getLong(STATUS_ID_COLUMN));
                if (optionalStatus.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.STATUS_NOT_FOUND)  + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN);
                    throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN));
                }
                payment.setStatus(optionalStatus.get());

                List<Book> orderedBooks = findAllBooksInThisOrder(payment.getEntityId(), bookDAO);
                payment.setOrderedBooks(orderedBooks);

                payments.add(payment);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return payments;
    }



    @Override
    public Optional<Payment> findById(Long id) {
        Payment payment = null;
        ResultSet rs = null;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_ORDER_BY_ID)) {

            ps.setLong(1, id);
            rs = ps.executeQuery();

            AbstractDAO<Long, User> userDAO = DAOFactory.INSTANCE.create(EntityType.USER, ConnectionPool.getInstance().getAvailableConnection());
            AbstractDAO<Long, Status> statusDAO = DAOFactory.INSTANCE.create(EntityType.STATUS, ConnectionPool.getInstance().getAvailableConnection());

            AbstractDAO<Long, Book> bookDAO = DAOFactory.INSTANCE.create(EntityType.BOOK, ConnectionPool.getInstance().getAvailableConnection());

            while (rs.next()) {
                payment = new Payment();
                payment.setEntityId(rs.getLong(ID_COLUMN));

                Optional<User> optionalUser = userDAO.findById(rs.getLong(LIBRARY_USER_ID_COLUMN));
                if (optionalUser.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND) + UtilStrings.WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN);
                    throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN));
                }
                payment.setUser(optionalUser.get());

                payment.setPaymentTime(rs.getTimestamp(ORDER_TIME_COLUMN).toLocalDateTime());

                Optional<Status> optionalStatus = statusDAO.findById(rs.getLong(STATUS_ID_COLUMN));
                if (optionalStatus.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(STATUS_ID_COLUMN)  + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN);
                    throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN));
                }
                payment.setStatus(optionalStatus.get());

                List<Book> orderedBooks = findAllBooksInThisOrder(payment.getEntityId(), bookDAO);
                payment.setOrderedBooks(orderedBooks);

            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException throwables) {
                logger.error(throwables.getMessage(), throwables);
            }
        }

        return Optional.ofNullable(payment);
    }



    @Override
    public Collection<Payment> findAll(Criteria criteria) {
        String query = SQL_SELECT_ALL_ORDERS_WHERE
                + EntityQueryCreatorFactory.INSTANCE.create(EntityType.ORDER).createQuery(criteria);

        List<Payment> payments = new ArrayList<>();

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            AbstractDAO<Long, User> userDAO = DAOFactory.INSTANCE.create(EntityType.USER, ConnectionPool.getInstance().getAvailableConnection());
            AbstractDAO<Long, Status> statusDAO = DAOFactory.INSTANCE.create(EntityType.STATUS, ConnectionPool.getInstance().getAvailableConnection());

            AbstractDAO<Long, Book> bookDAO = DAOFactory.INSTANCE.create(EntityType.BOOK, ConnectionPool.getInstance().getAvailableConnection());

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setEntityId(rs.getLong(ID_COLUMN));

                Optional<User> optionalUser = userDAO.findById(rs.getLong(LIBRARY_USER_ID_COLUMN));
                if (optionalUser.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND)  + UtilStrings.WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN);
                    throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN));
                }
                payment.setUser(optionalUser.get());

                payment.setPaymentTime(rs.getTimestamp(ORDER_TIME_COLUMN).toLocalDateTime());

                Optional<Status> optionalStatus = statusDAO.findById(rs.getLong(STATUS_ID_COLUMN));
                if (optionalStatus.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.STATUS_NOT_FOUND) + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN);
                    throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN));
                }
                payment.setStatus(optionalStatus.get());

                List<Book> orderedBooks = findAllBooksInThisOrder(payment.getEntityId(), bookDAO);
                payment.setOrderedBooks(orderedBooks);

                payments.add(payment);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return payments;
    }



    @Override
    public Optional<Payment> find(Criteria<Payment> criteria) {
        String query = SQL_SELECT_ALL_ORDERS_WHERE
                + EntityQueryCreatorFactory.INSTANCE.create(EntityType.ORDER).createQuery(criteria);

        Payment payment = null;

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            AbstractDAO<Long, User> userDAO = DAOFactory.INSTANCE.create(EntityType.USER, ConnectionPool.getInstance().getAvailableConnection());
            AbstractDAO<Long, Status> statusDAO = DAOFactory.INSTANCE.create(EntityType.STATUS, ConnectionPool.getInstance().getAvailableConnection());

            AbstractDAO<Long, Book> bookDAO = DAOFactory.INSTANCE.create(EntityType.BOOK, ConnectionPool.getInstance().getAvailableConnection());

            while (rs.next()) {
                payment = new Payment();

                payment.setEntityId(rs.getLong(ID_COLUMN));

                Optional<User> optionalUser = userDAO.findById(rs.getLong(LIBRARY_USER_ID_COLUMN));
                if (optionalUser.isEmpty()) {
                    throw new RuntimeException("User with id " + rs.getLong(LIBRARY_USER_ID_COLUMN));
                }
                payment.setUser(optionalUser.get());

                payment.setPaymentTime(rs.getTimestamp(ORDER_TIME_COLUMN).toLocalDateTime());

                Optional<Status> optionalStatus = statusDAO.findById(rs.getLong(STATUS_ID_COLUMN));
                if (optionalStatus.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.STATUS_NOT_FOUND) + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN);
                    throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN));
                }
                payment.setStatus(optionalStatus.get());

                List<Book> orderedBooks = findAllBooksInThisOrder(payment.getEntityId(), bookDAO);
                payment.setOrderedBooks(orderedBooks);

            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return Optional.ofNullable(payment);
    }



    @Override
    public boolean delete(Payment payment) {
        return delete(payment.getEntityId());
    }



    @Override
    public boolean delete(Long id) {

        try (PreparedStatement ps = getPrepareStatement(SQL_DELETE_ORDER_BY_ID)) {
            ps.setLong(1, id);
            int result = ps.executeUpdate();

            if (result == UtilStrings.ZERO_ROWS_AFFECTED) {
                return false;
            }

        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return true;
    }



    @Override
    public Optional<Payment> update(Payment payment) {
//
//        String query = String.format(SQL_UPDATE_ORDER_WHERE,
//                EntityQueryCreatorFactory.INSTANCE.create(EntityType.ORDER).createQuery(criteria));

        Optional<Payment> optionalOrderToUpdate = findById(payment.getEntityId());
        if (optionalOrderToUpdate.isEmpty()) {
            return Optional.empty();
        }

        try(PreparedStatement ps = getPrepareStatement(SQL_UPDATE_ORDER_BY_ID)) {

            ps.setLong(1, payment.getUser().getEntityId());
            ps.setTimestamp(2, Timestamp.valueOf(payment.getPaymentTime()));
            ps.setLong(3, payment.getStatus().getEntityId());
            ps.setLong(4, payment.getEntityId());

            int result = ps.executeUpdate();

            if (result == UtilStrings.ZERO_ROWS_AFFECTED) {
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_ORDER_UPDATE_OCCURRED);
                throw new RuntimeException(errorMessage);
            }

        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalOrderToUpdate;
    }



    private List<Book> findAllBooksInThisOrder(long orderId, AbstractDAO<Long, Book> bookDAO) {
        List<Long> bookIds = new ArrayList<>();

        ResultSet rs = null;

        try(PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_BOOKS_IN_THIS_ORDER)) {

            ps.setLong(1, orderId);
            rs = ps.executeQuery();

            while (rs.next()) {
                bookIds.add(rs.getLong(BOOK_ID_COLUMN));
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException throwables) {
                logger.error(throwables.getMessage(), throwables);
            }
        }

        List<Book> books = new ArrayList<>();
        for (Long bookId : bookIds) {
            Optional<Book> optionalBook = bookDAO.findById(bookId);
            optionalBook.ifPresent(books::add);
        }

        return books;
    }
}
