package com.epam.bookshop.dao.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.*;
import com.epam.bookshop.strategy.query_creator.impl.EntityQueryCreatorFactory;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class OrderDAO extends AbstractDAO<Long, Order> {

    private static final Logger logger = LoggerFactory.getLogger(OrderDAO.class);

    private static final String SQL_SELECT_ALL_ORDERS_WHERE =  "SELECT Id, Library_User_Id, Order_Time, Status_Id FROM TEST_LIBRARY.ORDER_BOOK WHERE ";
    private static final String SQL_UPDATE_ORDER_WHERE =  "UPDATE TEST_LIBRARY.ORDER SET %s WHERE Id = ?";

    private static final String SQL_INSERT_ORDER = "INSERT INTO TEST_LIBRARY.ORDER (Library_User_Id, Order_Time, Status_Id) VALUES (?, ?, ?);";
    private static final String SQL_DELETE_ORDER_BY_ID = "DELETE FROM TEST_LIBRARY.ORDER WHERE Id = ?;";
    private static final String SQL_UPDATE_ORDER_BY_ID = "UPDATE TEST_LIBRARY.ORDER SET Library_User_Id = ?, Order_Time = ?, Status_Id = ? WHERE Id = ?;";

    private static String SQL_SELECT_ALL_ORDERS = "SELECT Id, Library_User_Id, Order_Time, Status_Id " +
            "FROM TEST_LIBRARY.ORDER ";

    private static String SQL_SELECT_ALL_BOOKS_IN_THIS_ORDER = "SELECT Order_Id, Book_Id " +
            "FROM TEST_LIBRARY.ORDER_BOOK " +
            "WHERE Order_Id = ?";

    private static String SQL_SELECT_ORDER_BY_ID = "SELECT Id, Library_User_Id, Order_Time, Status_Id " +
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
    public Order create(Order order) {

        try(PreparedStatement ps = getPrepareStatement(SQL_INSERT_ORDER)) {

            ps.setLong(1, order.getUser().getEntityId());
            ps.setTimestamp(2, Timestamp.valueOf(order.getOrderTime()));
            ps.setLong(3, order.getStatus().getEntityId());

            ps.executeUpdate();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return order;
    }



    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_ORDERS);
             ResultSet rs = ps.executeQuery()) {

            AbstractDAO<Long, User> userDAO = DAOFactory.INSTANCE.create(EntityType.USER, ConnectionPool.getInstance().getAvailableConnection());
            AbstractDAO<Long, Status> statusDAO = DAOFactory.INSTANCE.create(EntityType.STATUS, ConnectionPool.getInstance().getAvailableConnection());

            AbstractDAO<Long, Book> bookDAO = DAOFactory.INSTANCE.create(EntityType.BOOK, ConnectionPool.getInstance().getAvailableConnection());

            while (rs.next()) {
                Order order = new Order();
                order.setEntityId(rs.getLong(ID_COLUMN));

                Optional<User> optionalUser = userDAO.findById(rs.getLong(LIBRARY_USER_ID_COLUMN));
                if (optionalUser.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND) + UtilStrings.WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN);
                    throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN));
                }
                order.setUser(optionalUser.get());

                order.setOrderTime(rs.getTimestamp(ORDER_TIME_COLUMN).toLocalDateTime());

                Optional<Status> optionalStatus = statusDAO.findById(rs.getLong(STATUS_ID_COLUMN));
                if (optionalStatus.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.STATUS_NOT_FOUND)  + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN);
                    throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN));
                }
                order.setStatus(optionalStatus.get());

                List<Book> orderedBooks = findAllBooksInThisOrder(order.getEntityId(), bookDAO);
                order.setOrderedBooks(orderedBooks);

                orders.add(order);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return orders;
    }



    @Override
    public Optional<Order> findById(Long id) {
        Order order = null;
        ResultSet rs = null;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_ORDER_BY_ID)) {

            ps.setLong(1, id);
            rs = ps.executeQuery();

            AbstractDAO<Long, User> userDAO = DAOFactory.INSTANCE.create(EntityType.USER, ConnectionPool.getInstance().getAvailableConnection());
            AbstractDAO<Long, Status> statusDAO = DAOFactory.INSTANCE.create(EntityType.STATUS, ConnectionPool.getInstance().getAvailableConnection());

            AbstractDAO<Long, Book> bookDAO = DAOFactory.INSTANCE.create(EntityType.BOOK, ConnectionPool.getInstance().getAvailableConnection());

            while (rs.next()) {
                order = new Order();
                order.setEntityId(rs.getLong(ID_COLUMN));

                Optional<User> optionalUser = userDAO.findById(rs.getLong(LIBRARY_USER_ID_COLUMN));
                if (optionalUser.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND) + UtilStrings.WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN);
                    throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN));
                }
                order.setUser(optionalUser.get());

                order.setOrderTime(rs.getTimestamp(ORDER_TIME_COLUMN).toLocalDateTime());

                Optional<Status> optionalStatus = statusDAO.findById(rs.getLong(STATUS_ID_COLUMN));
                if (optionalStatus.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(STATUS_ID_COLUMN)  + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN);
                    throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN));
                }
                order.setStatus(optionalStatus.get());

                List<Book> orderedBooks = findAllBooksInThisOrder(order.getEntityId(), bookDAO);
                order.setOrderedBooks(orderedBooks);

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

        return Optional.ofNullable(order);
    }



    @Override
    public Collection<Order> findAll(Criteria criteria) {
        String query = SQL_SELECT_ALL_ORDERS_WHERE
                + EntityQueryCreatorFactory.INSTANCE.create(EntityType.ORDER).createQuery(criteria);

        List<Order> orders = new ArrayList<>();

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            AbstractDAO<Long, User> userDAO = DAOFactory.INSTANCE.create(EntityType.USER, ConnectionPool.getInstance().getAvailableConnection());
            AbstractDAO<Long, Status> statusDAO = DAOFactory.INSTANCE.create(EntityType.STATUS, ConnectionPool.getInstance().getAvailableConnection());

            AbstractDAO<Long, Book> bookDAO = DAOFactory.INSTANCE.create(EntityType.BOOK, ConnectionPool.getInstance().getAvailableConnection());

            while (rs.next()) {
                Order order = new Order();
                order.setEntityId(rs.getLong(ID_COLUMN));

                Optional<User> optionalUser = userDAO.findById(rs.getLong(LIBRARY_USER_ID_COLUMN));
                if (optionalUser.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND)  + UtilStrings.WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN);
                    throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN));
                }
                order.setUser(optionalUser.get());

                order.setOrderTime(rs.getTimestamp(ORDER_TIME_COLUMN).toLocalDateTime());

                Optional<Status> optionalStatus = statusDAO.findById(rs.getLong(STATUS_ID_COLUMN));
                if (optionalStatus.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.STATUS_NOT_FOUND) + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN);
                    throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN));
                }
                order.setStatus(optionalStatus.get());

                List<Book> orderedBooks = findAllBooksInThisOrder(order.getEntityId(), bookDAO);
                order.setOrderedBooks(orderedBooks);

                orders.add(order);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return orders;
    }



    @Override
    public Optional<Order> find(Criteria<Order> criteria) {
        String query = SQL_SELECT_ALL_ORDERS_WHERE
                + EntityQueryCreatorFactory.INSTANCE.create(EntityType.ORDER).createQuery(criteria);

        Order order = null;

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            AbstractDAO<Long, User> userDAO = DAOFactory.INSTANCE.create(EntityType.USER, ConnectionPool.getInstance().getAvailableConnection());
            AbstractDAO<Long, Status> statusDAO = DAOFactory.INSTANCE.create(EntityType.STATUS, ConnectionPool.getInstance().getAvailableConnection());

            AbstractDAO<Long, Book> bookDAO = DAOFactory.INSTANCE.create(EntityType.BOOK, ConnectionPool.getInstance().getAvailableConnection());

            while (rs.next()) {
                order = new Order();

                order.setEntityId(rs.getLong(ID_COLUMN));

                Optional<User> optionalUser = userDAO.findById(rs.getLong(LIBRARY_USER_ID_COLUMN));
                if (optionalUser.isEmpty()) {
                    throw new RuntimeException("User with id " + rs.getLong(LIBRARY_USER_ID_COLUMN));
                }
                order.setUser(optionalUser.get());

                order.setOrderTime(rs.getTimestamp(ORDER_TIME_COLUMN).toLocalDateTime());

                Optional<Status> optionalStatus = statusDAO.findById(rs.getLong(STATUS_ID_COLUMN));
                if (optionalStatus.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.STATUS_NOT_FOUND) + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN);
                    throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + rs.getLong(STATUS_ID_COLUMN));
                }
                order.setStatus(optionalStatus.get());

                List<Book> orderedBooks = findAllBooksInThisOrder(order.getEntityId(), bookDAO);
                order.setOrderedBooks(orderedBooks);

            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return Optional.ofNullable(order);
    }



    @Override
    public boolean delete(Order order) {
        return delete(order.getEntityId());
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
    public Optional<Order> update(Order order) {
//
//        String query = String.format(SQL_UPDATE_ORDER_WHERE,
//                EntityQueryCreatorFactory.INSTANCE.create(EntityType.ORDER).createQuery(criteria));

        Optional<Order> optionalOrderToUpdate = findById(order.getEntityId());
        if (optionalOrderToUpdate.isEmpty()) {
            return Optional.empty();
        }

        try(PreparedStatement ps = getPrepareStatement(SQL_UPDATE_ORDER_BY_ID)) {

            ps.setLong(1, order.getUser().getEntityId());
            ps.setTimestamp(2, Timestamp.valueOf(order.getOrderTime()));
            ps.setLong(3, order.getStatus().getEntityId());
            ps.setLong(4, order.getEntityId());

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
