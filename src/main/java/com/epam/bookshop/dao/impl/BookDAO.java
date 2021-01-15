package com.epam.bookshop.dao.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.GenreCriteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.strategy.query_creator.impl.EntityQueryCreatorFactory;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.*;

public class BookDAO extends AbstractDAO<Long, Book> {

    private static final Logger logger = LoggerFactory.getLogger(BookDAO.class);

    private static final String SQL_SELECT_ALL_BOOKS_WHERE =  "SELECT Id, ISBN, Title, Author, Price, Publisher, Genre_Id, Preview FROM TEST_LIBRARY.BOOK WHERE ";
    private static final String SQL_UPDATE_BOOK_WHERE =  "UPDATE TEST_LIBRARY.BOOK SET %s WHERE Id = ?";

    private static final String SQL_INSERT_BOOK = "INSERT INTO TEST_LIBRARY.BOOK (ISBN, Title, Author, Price, Publisher, Genre_Id, Preview) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_DELETE_BOOK_BY_ID = "DELETE FROM TEST_LIBRARY.BOOK WHERE Id = ?;";
    private static final String SQL_UPDATE_BOOK_BY_ID = "UPDATE TEST_LIBRARY.BOOK SET ISBN = ?, Title = ?, Author = ?, Price = ?, Publisher = ?, Genre_Id = ?, Preview = ? WHERE Id = ?;";

    private static String SQL_SELECT_ALL_BOOKS = "SELECT Id, ISBN, Title, Author, Price, Publisher, Genre_Id, Preview " +
            "FROM TEST_LIBRARY.BOOK ";

    private static String SQL_SELECT_BOOk_BY_ID = "SELECT Id, ISBN, Title, Author, Price, Publisher, Genre_Id, Preview " +
            "FROM TEST_LIBRARY.BOOK " +
            "WHERE Id = ?";

    private static final String SQL_SELECT_IMG_BY_ISBN = "SELECT Image from TEST_LIBRARY.BOOK_IMAGE WHERE ISBN = ?;";
    private static final String SQL_INSERT_IMG = "INSERT INTO TEST_LIBRARY.BOOK_IMAGE (ISBN, Image) VALUES (?, ?)";

    private static final String ID_COLUMN = "Id";
    private static final String ISBN_COLUMN = "ISBN";
    private static final String TITLE_COLUMN = "Title";
    private static final String AUTHOR_COLUMN = "Author";
    private static final String PRICE_COLUMN = "Price";
    private static final String GENRE_ID_COLUMN = "Genre_Id";;
    private static final String PUBLISHER_COLUMN = "Publisher";
    private static final String PREVIEW_COLUMN = "Preview";
    private static final String IMAGE_COLUMN = "Image";

    private final String locale = "US";

    BookDAO(Connection connection) {
        super(connection);
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
            Optional<Genre> optionalGenre = genreDAO.find(GenreCriteria.builder().genre(book.getGenre().getGenre()).build());
            if (optionalGenre.isEmpty()) {
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND) + UtilStrings.NEW_LINE + book.getGenre();
                throw new RuntimeException(errorMessage + UtilStrings.WHITESPACE + book.getGenre().getGenre());
            }
            ps.setLong(6, optionalGenre.get().getEntityId());

            ps.setString(7, book.getPreview());

            ps.executeUpdate();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return book;
    }



    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_BOOKS);
             ResultSet rs = ps.executeQuery()) {

            AbstractDAO<Long, Genre> genreDAO = DAOFactory.INSTANCE.create(EntityType.GENRE,
                    ConnectionPool.getInstance().getAvailableConnection());

            while (rs.next()) {
                Book book = new Book();
                book.setEntityId(rs.getLong(ID_COLUMN));
                book.setISBN(rs.getString(ISBN_COLUMN));
                book.setTitle(rs.getString(TITLE_COLUMN));
                book.setPrice(rs.getDouble(PRICE_COLUMN));

                Optional<Genre> optionalGenre = genreDAO.findById(rs.getLong(GENRE_ID_COLUMN));
                if (optionalGenre.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND) + UtilStrings.WHITESPACE + rs.getLong(GENRE_ID_COLUMN);
                    throw new RuntimeException(errorMessage + ID_COLUMN + UtilStrings.WHITESPACE + rs.getLong(GENRE_ID_COLUMN));
                }
                book.setGenre(optionalGenre.get());

                book.setAuthor(rs.getString(AUTHOR_COLUMN));
                book.setPublisher(rs.getString(PUBLISHER_COLUMN));
                book.setPreview(rs.getString(PREVIEW_COLUMN));

                books.add(book);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
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
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND) + UtilStrings.WHITESPACE + rs.getLong(GENRE_ID_COLUMN);
                    throw new RuntimeException(errorMessage + ID_COLUMN + UtilStrings.WHITESPACE + rs.getLong(GENRE_ID_COLUMN));
                }
                book.setGenre(optionalGenre.get());

                book.setAuthor(rs.getString(AUTHOR_COLUMN));
                book.setPublisher(rs.getString(PUBLISHER_COLUMN));
                book.setPreview(rs.getString(PREVIEW_COLUMN));
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

        return Optional.ofNullable(book);
    }



    @Override
    public Collection<Book> findAll(Criteria<Book> criteria) {
        String query = SQL_SELECT_ALL_BOOKS_WHERE
                + EntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK).createQuery(criteria);

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
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND) + UtilStrings.WHITESPACE + rs.getLong(GENRE_ID_COLUMN);
                    throw new RuntimeException(errorMessage + ID_COLUMN + UtilStrings.WHITESPACE + rs.getLong(GENRE_ID_COLUMN));
                }
                book.setGenre(optionalGenre.get());

                book.setAuthor(rs.getString(AUTHOR_COLUMN));
                book.setPublisher(rs.getString(PUBLISHER_COLUMN));
                book.setPreview(rs.getString(PREVIEW_COLUMN));

                books.add(book);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return books;
    }



    @Override
    public Optional<Book> find(Criteria<Book> criteria) {
        String query = SQL_SELECT_ALL_BOOKS_WHERE +
                EntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK).createQuery(criteria);

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
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND) + UtilStrings.WHITESPACE + rs.getLong(GENRE_ID_COLUMN);
                    throw new RuntimeException(errorMessage + ID_COLUMN + UtilStrings.WHITESPACE + rs.getLong(GENRE_ID_COLUMN));
                }
                book.setGenre(optionalGenre.get());

                book.setAuthor(rs.getString(AUTHOR_COLUMN));
                book.setPublisher(rs.getString(PUBLISHER_COLUMN));
                book.setPreview(rs.getString(PREVIEW_COLUMN));

            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
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

            if (result == UtilStrings.ZERO_ROWS_AFFECTED) {
                return false;
            }

        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return true;
    }



    @Override
    public Optional<Book> update(Book book) {
//
//        String query = String.format(SQL_UPDATE_BOOK_WHERE,
//                EntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK).createQuery(criteria));

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

            if (result == UtilStrings.ZERO_ROWS_AFFECTED) {
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_BOOK_UPDATE_OCCURRED);
                throw new RuntimeException(errorMessage);
            }

        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalBookToUpdate;
    }



    public void createImage(String ISBN, String filePath) {

        try(PreparedStatement ps = getPrepareStatement(SQL_INSERT_IMG);
            InputStream is = new FileInputStream(new File(filePath))) {

            ps.setString(1, ISBN);
            ps.setBinaryStream(2, is); //   or ps.setBlob(2, is);

            ps.execute();
        } catch (SQLException | FileNotFoundException throwables) {
            logger.error(throwables.getMessage(), throwables);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }



    public Optional<String> findImageByISBN(String ISBN) {
        ResultSet rs = null;

        String base64Image = "";

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_IMG_BY_ISBN)) {

            ps.setString(1, ISBN);
            rs = ps.executeQuery();

            while (rs.next()) {
                Blob blob = rs.getBlob(IMAGE_COLUMN);

                byte[] imageBytes = convertImageToBytes(blob);
                base64Image = Base64.getEncoder().encodeToString(imageBytes);
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

        return Optional.ofNullable(base64Image);
    }



    private byte[] convertImageToBytes(Blob blob) {

        byte[] imageBytes = null;
        try(InputStream inputStream = blob.getBinaryStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while (true) {
                try {
                    if (!((bytesRead = inputStream.read(buffer)) != -1)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                outputStream.write(buffer, 0, bytesRead);
            }

            imageBytes = outputStream.toByteArray();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return imageBytes;
    }
}
