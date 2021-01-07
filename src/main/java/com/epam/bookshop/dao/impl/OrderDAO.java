package com.epam.bookshop.dao.impl;

import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.domain.impl.*;
import com.epam.bookshop.strategy.query_creator.FindEntityQueryCreator;
import com.epam.bookshop.strategy.query_creator.impl.FindEntityQueryCreatorFactory;
import com.epam.bookshop.util.manager.ErrorMessageManager;

import java.sql.*;
import java.util.*;

public class OrderDAO extends AbstractDAO<Long, Order> {

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

    private static final String NO_ORDER_UPDATE_OCCURRED = "no_order_update_occurred";
    private static final String STATUS_NOT_FOUND = "status_not_found";
    private static final String USER_NOT_FOUND = "user_not_found";
    private static final String WHITESPACE = " ";

    private static final Integer ZERO_ROWS_AFFECTED = 0;

    private String locale = "EN";

    public OrderDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public Order create(Order order) {

        try(PreparedStatement ps = getPrepareStatement(SQL_INSERT_ORDER)) {

            ps.setLong(1, order.getUser().getEntityId());
            ps.setTimestamp(2, Timestamp.valueOf(order.getOrderTime()));
            ps.setLong(3, order.getStatus().getEntityId());

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(USER_NOT_FOUND) + WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN);
                    throw new RuntimeException(errorMessage + WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN));
                }
                order.setUser(optionalUser.get());

                order.setOrderTime(rs.getTimestamp(ORDER_TIME_COLUMN).toLocalDateTime());

                Optional<Status> optionalStatus = statusDAO.findById(rs.getLong(STATUS_ID_COLUMN));
                if (optionalStatus.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(STATUS_NOT_FOUND)  + WHITESPACE + rs.getLong(STATUS_ID_COLUMN);
                    throw new RuntimeException(errorMessage + WHITESPACE + rs.getLong(STATUS_ID_COLUMN));
                }
                order.setStatus(optionalStatus.get());

                List<Book> orderedBooks = findAllBooksInThisOrder(order.getEntityId(), bookDAO);
                order.setOrderedBooks(orderedBooks);

                orders.add(order);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(USER_NOT_FOUND) + WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN);
                    throw new RuntimeException(errorMessage + WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN));
                }
                order.setUser(optionalUser.get());

                order.setOrderTime(rs.getTimestamp(ORDER_TIME_COLUMN).toLocalDateTime());

                Optional<Status> optionalStatus = statusDAO.findById(rs.getLong(STATUS_ID_COLUMN));
                if (optionalStatus.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(STATUS_ID_COLUMN)  + WHITESPACE + rs.getLong(STATUS_ID_COLUMN);
                    throw new RuntimeException(errorMessage + WHITESPACE + rs.getLong(STATUS_ID_COLUMN));
                }
                order.setStatus(optionalStatus.get());

                List<Book> orderedBooks = findAllBooksInThisOrder(order.getEntityId(), bookDAO);
                order.setOrderedBooks(orderedBooks);

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

        return Optional.ofNullable(order);
    }

    @Override
    public Collection<Order> findAll(Criteria criteria) {
        FindEntityQueryCreator queryCreator = FindEntityQueryCreatorFactory.INSTANCE.create(EntityType.ORDER);
        queryCreator.setLocale(locale);
        String query = queryCreator.createQuery(criteria);

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
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(USER_NOT_FOUND)  + WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN);
                    throw new RuntimeException(errorMessage + WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN));
                }
                order.setUser(optionalUser.get());

                order.setOrderTime(rs.getTimestamp(ORDER_TIME_COLUMN).toLocalDateTime());

                Optional<Status> optionalStatus = statusDAO.findById(rs.getLong(STATUS_ID_COLUMN));
                if (optionalStatus.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(STATUS_NOT_FOUND) + WHITESPACE + rs.getLong(STATUS_ID_COLUMN);
                    throw new RuntimeException(errorMessage + WHITESPACE + rs.getLong(STATUS_ID_COLUMN));
                }
                order.setStatus(optionalStatus.get());

                List<Book> orderedBooks = findAllBooksInThisOrder(order.getEntityId(), bookDAO);
                order.setOrderedBooks(orderedBooks);

                orders.add(order);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return orders;
    }

    @Override
    public Optional<Order> find(Criteria<? extends Entity> criteria) {
        FindEntityQueryCreator queryCreator = FindEntityQueryCreatorFactory.INSTANCE.create(EntityType.ORDER);
        queryCreator.setLocale(locale);
        String query = queryCreator.createQuery(criteria);

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
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(STATUS_NOT_FOUND) + WHITESPACE + rs.getLong(STATUS_ID_COLUMN);
                    throw new RuntimeException(errorMessage + WHITESPACE + rs.getLong(STATUS_ID_COLUMN));
                }
                order.setStatus(optionalStatus.get());

                List<Book> orderedBooks = findAllBooksInThisOrder(order.getEntityId(), bookDAO);
                order.setOrderedBooks(orderedBooks);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

            if (result == ZERO_ROWS_AFFECTED) {
                return false;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

    @Override
    public Optional<Order> update(Order order) {

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

            if (result == ZERO_ROWS_AFFECTED) {
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(NO_ORDER_UPDATE_OCCURRED);
                throw new RuntimeException(errorMessage);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

        List<Book> books = new ArrayList<>();
//        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, ConnectionPool.getInstance().getAvailableConnection());
        for (Long bookId : bookIds) {
            Optional<Book> optionalBook = bookDAO.findById(bookId);
            if (optionalBook.isPresent()) {
                books.add(optionalBook.get());
            }
        }

        return books;
    }
}
