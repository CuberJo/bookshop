package com.epam.bookshop.dao.impl;

import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.dao.AbstractDao;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Payment;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.DqlException;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.util.query_creator.impl.SqlConditionQueryCreatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Class that interacts with the database and provides CRUD methods to do with {@link Payment} instance.
 * Implements DAO pattern
 */
public class PaymentDao extends AbstractDao<Long, Payment> {
    private static final Logger logger = LoggerFactory.getLogger(PaymentDao.class);

    private static final String SQL_SELECT_ALL_PAYMENTS_WHERE =  "SELECT Id, Library_User_Id, Book_Id, Payment_Time, Price FROM BOOKSHOP.PAYMENT WHERE ";
    private static final String SQL_SELECT_PAYMENT_BY_ID = "SELECT Id, Library_User_Id, Book_Id, Payment_Time, Price FROM BOOKSHOP.PAYMENT WHERE Id = ?";
    private static final String SQL_SELECT_COUNT_ALL = "SELECT COUNT(*) as Num FROM BOOKSHOP.PAYMENT;";
    private static final String SQL_SELECT_ALL_PAYMENTS_BY_LIMIT = "SELECT Id, Library_User_Id, Book_Id, Payment_Time, Price FROM BOOKSHOP.PAYMENT LIMIT ?, ?";
    private static final String SQL_INSERT_PAYMENT = "INSERT INTO BOOKSHOP.PAYMENT (Library_User_Id, Book_Id, Payment_Time, Price) VALUES (?, ?, ?, ?);";
    private static final String SQL_DELETE_PAYMENT_BY_ID = "DELETE FROM BOOKSHOP.PAYMENT WHERE Id = ?;";
    private static final String SQL_UPDATE_PAYMENT_BY_ID = "UPDATE BOOKSHOP.PAYMENT SET Library_User_Id = ?, Book_Id = ?, Payment_Time = ?, Price = ? WHERE Id = ?;";
    private static final String SQL_SELECT_ALL_PAYMENTS = "SELECT Id, Library_User_Id, Book_Id, Payment_Time, Price FROM BOOKSHOP.PAYMENT ";
    private static final String SQL_SELECT_ALL_BOOKS_IN_PAYMENT = "SELECT Book_Id FROM BOOKSHOP.PAYMENT WHERE Library_User_Id = ?";

    private static final String ID_COLUMN = "Id";
    private static final String LIBRARY_USER_ID_COLUMN = "Library_User_Id";
    private static final String BOOK_ID_COLUMN = "Book_Id";
    private static final String PAYMENT_TIME_COLUMN = "Payment_Time";
    private static final String PRICE_COLUMN = "Price";
    private static final String NUM_COLUMN = "Num";

    private final String locale = "US";

    PaymentDao(Connection connection) {
        super(connection);
    }


    /**
     * @param payment {@link Payment} object to be inserted in database
     * @return inserted {@link Payment} object
     */
    @Override
    public Payment create(Payment payment) throws DqlException {
        try(PreparedStatement ps = getPrepareStatement(SQL_INSERT_PAYMENT)) {
            ps.setLong(1, payment.getUser().getEntityId());
            ps.setLong(2, payment.getBook().getEntityId());
            ps.setTimestamp(3, Timestamp.valueOf(payment.getPaymentTime()));
            ps.setDouble(4, payment.getPrice());

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throw new DqlException(throwables.getMessage(), throwables);
        }

        return payment;
    }


    /**
     * @return {@link List<Payment>} all found {@link Payment} objects
     */
    @Override
    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_PAYMENTS);
             ResultSet rs = ps.executeQuery()) {

            payments = fill(rs);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return payments;
    }


    /**
     * @param id payment id
     * @return {@link Optional<Payment>} object
     */
    @Override
    public Optional<Payment> findById(Long id) {
        Payment payment = null;
        ResultSet rs = null;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_PAYMENT_BY_ID)) {
            ps.setLong(1, id);
            rs = ps.executeQuery();

            List<Payment> payments = fill(rs);
            if (!payments.isEmpty()) {
                payment = payments.get(0);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        } finally {
           closeResultSet(rs, logger);
        }

        return Optional.ofNullable(payment);
    }


    /**
     * @param criteria {@link Criteria<Payment>} object used to find all payents
     * @return {@link Collection<Payment>} all found {@link Payment} objects
     */
    @Override
    public Collection<Payment> findAll(Criteria<Payment> criteria) {
        String query = SQL_SELECT_ALL_PAYMENTS_WHERE
                + SqlConditionQueryCreatorFactory.INSTANCE.create(EntityType.PAYMENT).createQuery(criteria, UtilStringConstants.EQUALS);

        List<Payment> payments = new ArrayList<>();

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            payments = fill(rs);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return payments;
    }


    /**
     * @param criteria {@link Criteria<Payment>} object used to find all payents
     * @return {@link Optional<Payment>} object
     */
    @Override
    public Optional<Payment> find(Criteria<Payment> criteria) {
        String query = SQL_SELECT_ALL_PAYMENTS_WHERE
                + SqlConditionQueryCreatorFactory.INSTANCE.create(EntityType.PAYMENT).createQuery(criteria, UtilStringConstants.EQUALS);

        Payment payment = null;

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            List<Payment> payments = fill(rs);
            if (!payments.isEmpty()) {
                payment = payments.get(0);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return Optional.ofNullable(payment);
    }


    /**
     * @param payment {@link Payment} object to delete
     * @return true if and only if payment was deleted
     */
    @Override
    public boolean delete(Payment payment) {
        return delete(payment.getEntityId());
    }


    /**
     * @param id {@link Payment} object id to delete
     * @return true if and only if payment was deleted
     */
    @Override
    public boolean delete(Long id) {
        try (PreparedStatement ps = getPrepareStatement(SQL_DELETE_PAYMENT_BY_ID)) {
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


    /**
     * @param payment {@link Payment} object to delete
     * @return {@link Optional<Payment>} object to be updated if was found in database
     */
    @Override
    public Optional<Payment> update(Payment payment) {
        Optional<Payment> optionalPaymentToUpdate = findById(payment.getEntityId());
        if (optionalPaymentToUpdate.isEmpty()) {
            return Optional.empty();
        }

        try(PreparedStatement ps = getPrepareStatement(SQL_UPDATE_PAYMENT_BY_ID)) {
            ps.setLong(1, payment.getUser().getEntityId());
            ps.setLong(2, payment.getBook().getEntityId());
            ps.setTimestamp(3, Timestamp.valueOf(payment.getPaymentTime()));
            ps.setDouble(4, payment.getPrice());
            ps.setLong(5, payment.getEntityId());

            int result = ps.executeUpdate();

            if (result == UtilStringConstants.ZERO_ROWS_AFFECTED) {
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_ORDER_UPDATE_OCCURRED);
                throw new RuntimeException(errorMessage);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalPaymentToUpdate;
    }


    /**
     * @param userId user id which is used to find books
     * @return all faound books, which user has bought
     */
    public List<Book> findAllBooksInPayment(long userId) {
        List<Long> bookIds = new ArrayList<>();
        ResultSet rs = null;

        try(PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_BOOKS_IN_PAYMENT)) {
            ps.setLong(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                bookIds.add(rs.getLong(BOOK_ID_COLUMN));
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        } finally {
           closeResultSet(rs, logger);
        }

        List<Book> books = new ArrayList<>();
        try(Connection connection =  ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Book> bookDAO = DAOFactory.INSTANCE.create(EntityType.BOOK, connection);
            for (Long bookId : bookIds) {
                Optional<Book> optionalBook = bookDAO.findById(bookId);
                optionalBook.ifPresent(books::add);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return books;
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


    /**
     * Finds limited by <b>start</b> and <b>end</b> portion of books
     *
     * @param start from where to start search
     * @param total how many row to find
     * @return found {@link List<Book>}
     */
    @Override
    public List<Payment> findAll(int start, int total) {
        List<Payment> payments = new ArrayList<>();
        ResultSet rs = null;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_PAYMENTS_BY_LIMIT)) {
            ps.setInt(1, start);
            ps.setInt(2, total);
            rs = ps.executeQuery();

            payments = fill(rs);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        } finally {
            closeResultSet(rs, logger);
        }

        return payments;
    }


    /**
     * Fills list with data in ResultSet
     *
     * @param rs sql ResultSet from where data is taken
     * @return list of parsed instances
     * @throws SQLException
     */
    private List<Payment> fill(ResultSet rs) throws SQLException {
        List<Payment> payments = new ArrayList<>();

        try (Connection connection1 =  ConnectionPool.getInstance().getAvailableConnection();
             Connection connection2 =  ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, User> userDAO = DAOFactory.INSTANCE.create(EntityType.USER, connection1);
            AbstractDao<Long, Book> bookDAO = DAOFactory.INSTANCE.create(EntityType.BOOK, connection2);

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setEntityId(rs.getLong(ID_COLUMN));

                Optional<User> optionalUser = userDAO.findById(rs.getLong(LIBRARY_USER_ID_COLUMN));
                if (optionalUser.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND)
                            + UtilStringConstants.WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN);
                    throw new RuntimeException(errorMessage + UtilStringConstants.WHITESPACE + rs.getLong(LIBRARY_USER_ID_COLUMN));
                }
                payment.setUser(optionalUser.get());

                Optional<Book> optionalBook = bookDAO.findById(rs.getLong(BOOK_ID_COLUMN));
                if (optionalBook.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND)
                            + UtilStringConstants.WHITESPACE + rs.getLong(BOOK_ID_COLUMN);
                    throw new RuntimeException(errorMessage);
                }
                payment.setBook(optionalBook.get());

                payment.setPaymentTime(rs.getTimestamp(PAYMENT_TIME_COLUMN).toLocalDateTime());
                payment.setPrice(rs.getDouble(PRICE_COLUMN));

                payments.add(payment);
            }
        }

        return payments;
    }
}
