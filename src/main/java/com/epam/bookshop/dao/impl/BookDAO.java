package com.epam.bookshop.dao.impl;

import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.strategy.query_creator.FindEntityQueryCreator;
import com.epam.bookshop.strategy.query_creator.impl.FindEntityQueryCreatorFactory;
import com.epam.bookshop.util.manager.ErrorMessageManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class BookDAO extends AbstractDAO<Long, Book> {

    private static final String SQL_INSERT_BOOK = "INSERT INTO TEST_LIBRARY.BOOK (ISBN, Title, Author, Price, Publisher, Genre_Id, Preview) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_DELETE_BOOK_BY_ID = "DELETE FROM TEST_LIBRARY.BOOK WHERE Id = ?;";
    private static final String SQL_UPDATE_BOOK_BY_ID = "UPDATE TEST_LIBRARY.BOOK SET ISBN = ?, Title = ?, Author = ?, Price = ?, Publisher = ?, Genre_Id = ?, Preview = ? WHERE Id = ?;";

    private static String SQL_SELECT_ALL_BOOKS = "SELECT Id, ISBN, Title, Author, Price, Publisher, Genre_Id, Preview " +
            "FROM TEST_LIBRARY.BOOK ";

    private static String SQL_SELECT_BOOk_BY_ID = "SELECT Id, ISBN, Title, Author, Price, Publisher, Genre_Id, Preview " +
            "FROM TEST_LIBRARY.BOOK " +
            "WHERE Id = ?";

    private static final String ID_COLUMN = "Id";
    private static final String ISBN_COLUMN = "ISBN";
    private static final String TITLE_COLUMN = "Title";
    private static final String AUTHOR_COLUMN = "Author";
    private static final String PRICE_COLUMN = "Price";
    private static final String GENRE_ID_COLUMN = "Genre_Id";;
    private static final String PUBLISHER_COLUMN = "Publisher";
    private static final String PREVIEW_COLUMN = "Preview";

    private static final String NO_SUCH_GENRE_FOUND = "no_such_genre_found";
    private static final String WHITESPACE = " ";
    private static final String NO_BOOK_UPDATE_OCCURRED = "no_book_update_occurred";

    private static final Integer ZERO_ROWS_AFFECTED = 0;

    private String locale = "EN";

    BookDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public Book create(Book book) {

        try(PreparedStatement ps = getPrepareStatement(SQL_INSERT_BOOK)) {
            ps.setString(1, book.getISBN());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setDouble(4, book.getPrice());
            ps.setString(5, book.getPublisher());

            AbstractDAO<Long, Genre> genreDAO = DAOFactory.INSTANCE.create(EntityType.GENRE, ConnectionPool.getInstance().getAvailableConnection());
            List<Genre> genres = genreDAO.findAll();
            Optional<Genre> optionalGenre = genres.stream().filter(genre -> genre.getGenre().equals(book.getGenre().getGenre())).findAny();
            if (optionalGenre.isEmpty()) {
                String errorMessage = ErrorMessageManager.EN.getMessage(NO_SUCH_GENRE_FOUND);
                throw new RuntimeException(errorMessage + WHITESPACE + book.getGenre().getGenre());
            }
            ps.setLong(6, optionalGenre.get().getEntityId());

            ps.setString(7, book.getPreview());

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return book;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_BOOKS);
             ResultSet rs = ps.executeQuery()) {

            AbstractDAO<Long, Genre> genreDAO = DAOFactory.INSTANCE.create(EntityType.GENRE, ConnectionPool.getInstance().getAvailableConnection());

            while (rs.next()) {
                Book book = new Book();
                book.setEntityId(rs.getLong(ID_COLUMN));
                book.setISBN(rs.getString(ISBN_COLUMN));
                book.setTitle(rs.getString(TITLE_COLUMN));
                book.setPrice(rs.getDouble(PRICE_COLUMN));

                Optional<Genre> optionalGenre = genreDAO.findById(rs.getLong(GENRE_ID_COLUMN));
                if (optionalGenre.isEmpty()) {
                    String errorMessage = ErrorMessageManager.EN.getMessage(NO_SUCH_GENRE_FOUND);
//                    throw new RuntimeException("Genre with id " + rs.getLong(GENRE_ID_COLUMN));
                    throw new RuntimeException(errorMessage + ID_COLUMN + WHITESPACE + rs.getLong(GENRE_ID_COLUMN));
                }
                book.setGenre(optionalGenre.get());

                book.setAuthor(rs.getString(AUTHOR_COLUMN));
                book.setPublisher(rs.getString(PUBLISHER_COLUMN));
                book.setPreview(rs.getString(PREVIEW_COLUMN));


                books.add(book);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        Book book = null;
        ResultSet rs = null;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_BOOk_BY_ID)) {

            ps.setLong(1, id);
            rs = ps.executeQuery();

            AbstractDAO<Long, Genre> genreDAO = DAOFactory.INSTANCE.create(EntityType.GENRE, ConnectionPool.getInstance().getAvailableConnection());

            while (rs.next()) {
                book = new Book();
                book.setEntityId(rs.getLong(ID_COLUMN));
                book.setISBN(rs.getString(ISBN_COLUMN));
                book.setTitle(rs.getString(TITLE_COLUMN));
                book.setPrice(rs.getDouble(PRICE_COLUMN));

                Optional<Genre> optionalGenre = genreDAO.findById(rs.getLong(GENRE_ID_COLUMN));
                if (optionalGenre.isEmpty()) {
                    String errorMessage = ErrorMessageManager.EN.getMessage(NO_SUCH_GENRE_FOUND);
//                    throw new RuntimeException("Genre with id " + rs.getLong(GENRE_ID_COLUMN));
                    throw new RuntimeException(errorMessage + ID_COLUMN + WHITESPACE + rs.getLong(GENRE_ID_COLUMN));
                }
                book.setGenre(optionalGenre.get());

                book.setAuthor(rs.getString(AUTHOR_COLUMN));
                book.setPublisher(rs.getString(PUBLISHER_COLUMN));
                book.setPreview(rs.getString(PREVIEW_COLUMN));
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

        return Optional.ofNullable(book);
    }

    @Override
    public Collection<Book> findAll(Criteria criteria) {
        FindEntityQueryCreator queryCreator = FindEntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK);
        queryCreator.setLocale(locale);
        String query = queryCreator.createQuery(criteria);

        List<Book> books = new ArrayList<>();

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            AbstractDAO<Long, Genre> genreDAO = DAOFactory.INSTANCE.create(EntityType.GENRE, ConnectionPool.getInstance().getAvailableConnection());

            while (rs.next()) {
                Book book = new Book();
                book.setEntityId(rs.getLong(ID_COLUMN));
                book.setISBN(rs.getString(ISBN_COLUMN));
                book.setTitle(rs.getString(TITLE_COLUMN));
                book.setPrice(rs.getDouble(PRICE_COLUMN));

                Optional<Genre> optionalGenre = genreDAO.findById(rs.getLong(GENRE_ID_COLUMN));
                if (optionalGenre.isEmpty()) {
                    String errorMessage = ErrorMessageManager.EN.getMessage(NO_SUCH_GENRE_FOUND);
//                    throw new RuntimeException("Genre with id " + rs.getLong(GENRE_ID_COLUMN));
                    throw new RuntimeException(errorMessage + ID_COLUMN + WHITESPACE + rs.getLong(GENRE_ID_COLUMN));
                }
                book.setGenre(optionalGenre.get());

                book.setAuthor(rs.getString(AUTHOR_COLUMN));
                book.setPublisher(rs.getString(PUBLISHER_COLUMN));
                book.setPreview(rs.getString(PREVIEW_COLUMN));

                books.add(book);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return books;
    }

    @Override
    public Optional<Book> find(Criteria<? extends Entity> criteria) {
        FindEntityQueryCreator queryCreator = FindEntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK);
        queryCreator.setLocale(locale);
        String query = queryCreator.createQuery(criteria);

        Book book = null;

        AbstractDAO<Long, Genre> genreDAO = DAOFactory.INSTANCE.create(EntityType.GENRE, ConnectionPool.getInstance().getAvailableConnection());

        try (PreparedStatement ps = getPrepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                book = new Book();

                book.setEntityId(rs.getLong(ID_COLUMN));
                book.setISBN(rs.getString(ISBN_COLUMN));
                book.setTitle(rs.getString(TITLE_COLUMN));
                book.setPrice(rs.getDouble(PRICE_COLUMN));

                Optional<Genre> optionalGenre = genreDAO.findById(rs.getLong(GENRE_ID_COLUMN));
                if (optionalGenre.isEmpty()) {
                    String errorMessage = ErrorMessageManager.EN.getMessage(NO_SUCH_GENRE_FOUND);
//                    throw new RuntimeException("Genre with id " + rs.getLong(GENRE_ID_COLUMN));
                    throw new RuntimeException(errorMessage + ID_COLUMN + WHITESPACE + rs.getLong(GENRE_ID_COLUMN));
                }
                book.setGenre(optionalGenre.get());

                book.setAuthor(rs.getString(AUTHOR_COLUMN));
                book.setPublisher(rs.getString(PUBLISHER_COLUMN));
                book.setPreview(rs.getString(PREVIEW_COLUMN));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Optional.ofNullable(book);
    }

    @Override
    public boolean delete(Book entity) {
        return delete(entity.getEntityId());
    }

    @Override
    public boolean delete(Long id) {

        try (PreparedStatement ps = getPrepareStatement(SQL_DELETE_BOOK_BY_ID)) {
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
    public Optional<Book> update(Book book) {

        Optional<Book> optionalBookToUpdate = findById(book.getEntityId());
        if (optionalBookToUpdate.isEmpty()) {
            return Optional.empty();
        }

        try(PreparedStatement ps = getPrepareStatement(SQL_UPDATE_BOOK_BY_ID)) {
            ps.setString(1, book.getISBN());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setDouble(4, book.getPrice());
            ps.setString(5, book.getPublisher());
            ps.setLong(6, book.getGenre().getEntityId());
            ps.setString(7, book.getPreview());
            ps.setLong(8, book.getEntityId());

            int result = ps.executeUpdate();

            if (result == ZERO_ROWS_AFFECTED) {
                String errorMessage = ErrorMessageManager.EN.getMessage(NO_BOOK_UPDATE_OCCURRED);
                throw new RuntimeException(errorMessage);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return optionalBookToUpdate;
    }
}
