package com.epam.bookshop.dao.impl;

import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.util.ImgToBase64Converter;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.util.query_creator.impl.EntityQueryCreatorFactory;
import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.*;

public class BookDAO extends AbstractDAO<Long, Book> {

    private static final Logger logger = LoggerFactory.getLogger(BookDAO.class);

    private static final String SQL_SELECT_ALL_BOOKS_WHERE =  "SELECT Id, ISBN, Title, Author, Price, Publisher, Genre_Id, Preview FROM TEST_LIBRARY.BOOK WHERE ";
    private static final String SQL_SELECT_ALL_BOOKS_WHERE_LIKE_OR = "SELECT Id, ISBN, Title, Author, Price, Publisher, Genre_Id, Preview FROM TEST_LIBRARY.BOOK " +
            "WHERE Title LIKE ? OR Author LIKE ? OR Publisher LIKE ? ;";

    private static final String SQL_UPDATE_BOOK_WHERE =  "UPDATE TEST_LIBRARY.BOOK SET %s WHERE Id = ?";

    private static final String SQL_INSERT_BOOK = "INSERT INTO TEST_LIBRARY.BOOK (ISBN, Title, Author, Price, Publisher, Genre_Id, Preview) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_DELETE_BOOK_BY_ID = "DELETE FROM TEST_LIBRARY.BOOK WHERE Id = ?;";
    private static final String SQL_UPDATE_BOOK_BY_ID = "UPDATE TEST_LIBRARY.BOOK SET ISBN = ?, Title = ?, Author = ?, Price = ?, Publisher = ?, Genre_Id = ?, Preview = ? WHERE Id = ?;";

    private static final String SQL_SELECT_ALL_BOOKS = "SELECT Id, ISBN, Title, Author, Price, Publisher, Genre_Id, Preview " +
            "FROM TEST_LIBRARY.BOOK ";

    private static final String SQL_SELECT_ALL_BOOKS_BY_LIMIT = "SELECT Id, ISBN, Title, Author, Price, Publisher, Genre_Id, Preview " +
            "FROM TEST_LIBRARY.BOOK " +
            "LIMIT ?, ?";

    private static final String SQL_SELECT_BOOk_BY_ID = "SELECT Id, ISBN, Title, Author, Price, Publisher, Genre_Id, Preview " +
            "FROM TEST_LIBRARY.BOOK " +
            "WHERE Id = ?";

    private static final String SQL_SELECT_RAND = "SELECT Id, ISBN, Title, Author, Price, Publisher, Genre_Id, Preview FROM BOOK ORDER BY RAND() LIMIT 1;";

    private static final String SQL_SELECT_COUNT_ALL = "SELECT COUNT(*) as Num FROM TEST_LIBRARY.BOOK;";
    private static final String SQL_SELECT_COUNT_ALL_WHERE = "SELECT COUNT(*) as Num FROM TEST_LIBRARY.BOOK WHERE ";
    private static final String SQL_SELECT_COUNT_ALL_WHERE_LIKE_OR = "SELECT COUNT(*) as Num FROM TEST_LIBRARY.BOOK " +
            "WHERE Title LIKE ? OR Author LIKE ? OR Publisher LIKE ? ;";


    private static final String SQL_SELECT_IMG_BY_ISBN = "SELECT Image from TEST_LIBRARY.BOOK_IMAGE WHERE ISBN = ?;";
    private static final String SQL_INSERT_IMG = "INSERT INTO TEST_LIBRARY.BOOK_IMAGE (ISBN, Image) VALUES (?, ?)";

    private static final String SQL_SELECT_BOOK_FILE_BY_ISBN = "SELECT File from TEST_LIBRARY.BOOK_FILE WHERE ISBN = ?;";
    private static final String SQL_INSERT_BOOK_FILE = "INSERT INTO TEST_LIBRARY.BOOK_FILE (ISBN, File) VALUES (?, ?)";

    private static final String ID_COLUMN = "Id";
    private static final String ISBN_COLUMN = "ISBN";
    private static final String TITLE_COLUMN = "Title";
    private static final String AUTHOR_COLUMN = "Author";
    private static final String PRICE_COLUMN = "Price";
    private static final String GENRE_ID_COLUMN = "Genre_Id";;
    private static final String PUBLISHER_COLUMN = "Publisher";
    private static final String PREVIEW_COLUMN = "Preview";
    private static final String IMAGE_COLUMN = "Image";
    private static final String FILE_COLUMN = "FIle";
    private static final String NUM_COLUMN = "Num";


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
                + EntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK).createQuery(criteria, UtilStrings.EQUALS);

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
                EntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK).createQuery(criteria, UtilStrings.EQUALS);

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


    /**
     * Creates image in database
     *
     * @param ISBN book's unique identifier
     * @param filePath file system path of image
     */
    public void createImage(String ISBN, String filePath) {

        try(PreparedStatement ps = getPrepareStatement(SQL_INSERT_IMG);
            InputStream is = new FileInputStream(new File(filePath))) {

            ps.setString(1, ISBN);
            ps.setBinaryStream(2, is); //   or ps.setBlob(2, is);

            ps.execute();
        } catch (SQLException | IOException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }


    /**
     * Finds image by ISBN in database
     *
     * @param ISBN book's unique identifier
     * @return {@link Optional<String>} decoded image
     */
    public Optional<String> findImageByISBN(String ISBN) {
        ResultSet rs = null;

        String base64Image = null;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_IMG_BY_ISBN)) {

            ps.setString(1, ISBN);
            rs = ps.executeQuery();

            while (rs.next()) {
                Blob blob = rs.getBlob(IMAGE_COLUMN);

                InputStream is = blob.getBinaryStream();
                base64Image = ImgToBase64Converter.getInstance().convert(is);
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


    /**
     * @param ISBN book's unique identifier
     * @param filePath file system path of book
     */
    public void createBookFile(String ISBN, String filePath) {

        try(PreparedStatement ps = getPrepareStatement(SQL_INSERT_BOOK_FILE);
            InputStream is = new FileInputStream(new File(filePath))) {

            ps.setString(1, ISBN);
            ps.setBlob(2, is);

            ps.execute();
        } catch (SQLException | IOException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }



//    private byte[] getArrayFromInputStream(InputStream inputStream) throws IOException {
//        byte[] bytes;
//        byte[] buffer = new byte[1024];
//        try(BufferedInputStream is = new BufferedInputStream(inputStream)){
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            int length;
//            while ((length = is.read(buffer)) > -1 ) {
//                bos.write(buffer, 0, length);
//            }
//            bos.flush();
//            bytes = bos.toByteArray();
//        }
//        return bytes;
//    }
//
//    private static void writeContent(byte[] content, String fileToWriteTo) throws IOException {
//        File file = new File(fileToWriteTo);
//        try(BufferedOutputStream salida = new BufferedOutputStream(new FileOutputStream(file))){
//            salida.write(content);
//            salida.flush();
//        }
//    }


    /**
     * @param ISBN book's unique identifier
     * @return book file converted to byte array
     */
    public byte[] findBookFileByISBN(String ISBN) {
        ResultSet rs = null;

        byte[] bytes = null;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_BOOK_FILE_BY_ISBN)) {

            ps.setString(1, ISBN);
            rs = ps.executeQuery();

            while (rs.next()) {
                bytes = rs.getBytes(FILE_COLUMN);
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

        return bytes;
    }


    /**
     * Finds limited by <b>start</b> and <b>end</b> portion of books
     *
     * @param start from where to start search
     * @param total how many row to find
     * @return found {@link List<Book>}
     */
    public List<Book> findAll(int start, int total) {
        List<Book> books = new ArrayList<>();
        ResultSet rs = null;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_ALL_BOOKS_BY_LIMIT)) {

            ps.setInt(1, start);
            ps.setInt(2, total);
            rs = ps.executeQuery();


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
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException throwables) {
                logger.error(throwables.getMessage(), throwables);
            }
        }

        return books;
    }


    /**
     * Finds limited by <b>start</b> and <b>end</b> portion of books
     * by criteria
     *
     * @param criteria {@link Criteria<Book>} by which search happens
     * @param start from where to start search
     * @param total how many row to find
     * @return found {@link List<Book>}
     */
    public List<Book> findAll(Criteria<Book> criteria, int start, int total) {
        List<Book> books = new ArrayList<>();
        ResultSet rs = null;

        String query = SQL_SELECT_ALL_BOOKS_WHERE +
                EntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK).createQuery(criteria, UtilStrings.EQUALS)
                        .replace(UtilStrings.SEMICOLON, UtilStrings.WHITESPACE) + "LIMIT ?, ?";

        try (PreparedStatement ps = getPrepareStatement(query)) {

            ps.setInt(1, start);
            ps.setInt(2, total);
            rs = ps.executeQuery();


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
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException throwables) {
                logger.error(throwables.getMessage(), throwables);
            }
        }

        return books;
    }


    /**
     * Finds random row in table
     *
     * @return found random book
     */
    public Optional<Book> findRand() {

        Book book = null;

        AbstractDAO<Long, Genre> genreDAO = DAOFactory.INSTANCE.create(EntityType.GENRE, ConnectionPool.getInstance().getAvailableConnection());

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_RAND);
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




    /**
     * Counts number of rows in 'BOOK' table
     *
     * @return number of rows in BOOKS table
     */
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
     * Counts number of rows in 'BOOK' table
     * that are similar to passed as argument
     * {@link Criteria<Book>} instance
     *
     * @param criteria criteria, by which fields rows would be counted
     * @return number of rows in 'BOOKS' table
     */
    public int count(Criteria<Book> criteria) {
        String query;

        if (((BookCriteria) criteria).getGenreId() != null) {
            query = SQL_SELECT_COUNT_ALL_WHERE
                    + EntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK).createQuery(criteria, UtilStrings.EQUALS);
        } else {
            query = SQL_SELECT_COUNT_ALL_WHERE
                    + EntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK).createQuery(criteria, UtilStrings.LIKE);
        }

        int rows = 0;

        try (PreparedStatement ps = getPrepareStatement(query);
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
     * Counts number of rows in 'BOOK' table
     * that are similar to argument
     *
     * @param searchParam string to look for in table
     * @return number of rows in 'BOOKS' table
     */
    public int count(String searchParam) {
        int rows = 0;

        ResultSet rs = null;

        try (PreparedStatement ps = getPrepareStatement(SQL_SELECT_COUNT_ALL_WHERE_LIKE_OR)) {

            ps.setString(1, "%" + searchParam + "%");
            ps.setString(2, "%" + searchParam + "%");
            ps.setString(3, "%" + searchParam + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                rows = rs.getInt(NUM_COLUMN);
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException throwables) {
                    logger.error(throwables.getMessage(), throwables);
                }
            }
        }

        return rows;
    }


    /**
     * Finds collection of books by criteria params, similar to
     * passed as argument {@link Criteria<Book>} instance
     *
     * @param criteria {@link Criteria<Book>} by which search happens
     * @return found {@link Collection<Book>}
     */
    public Collection<Book> findAllLike(Criteria<Book> criteria) {
        String query = SQL_SELECT_ALL_BOOKS_WHERE
                + EntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK).createQuery(criteria, UtilStrings.LIKE);

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



    /**
     * Finds collection of books by criteria params, similar to
     * passed as argument {@link Criteria<Book>} instance
     *
     * @param criteria {@link Criteria<Book>} by which search happens
     * @return found {@link Collection<Book>}
     */
    public Collection<Book> findAllLike(Criteria<Book> criteria, int start, int total) {
        List<Book> books = new ArrayList<>();

        ResultSet rs = null;

        String query;

        if (((BookCriteria) criteria).getAuthor() != null
                && ((BookCriteria) criteria).getTitle() != null
                && ((BookCriteria) criteria).getPublisher() != null) {
            query = SQL_SELECT_ALL_BOOKS_WHERE_LIKE_OR
                    .replace(UtilStrings.SEMICOLON, UtilStrings.WHITESPACE) + "LIMIT ?, ?";
        } else {
            query = SQL_SELECT_ALL_BOOKS_WHERE
                    + EntityQueryCreatorFactory.INSTANCE.create(EntityType.BOOK).createQuery(criteria, UtilStrings.LIKE)
                    .replace(UtilStrings.SEMICOLON, UtilStrings.WHITESPACE) + "LIMIT ?, ?";
        }


        try (PreparedStatement ps = getPrepareStatement(query)) {

            if (((BookCriteria) criteria).getAuthor() != null
                    && ((BookCriteria) criteria).getTitle() != null
                    && ((BookCriteria) criteria).getPublisher() != null) {
                ps.setString(1, "%" + ((BookCriteria) criteria).getTitle() + "%");
                ps.setString(2, "%" + ((BookCriteria) criteria).getAuthor() + "%");
                ps.setString(3, "%" + ((BookCriteria) criteria).getPublisher() + "%");
                ps.setInt(4, start);
                ps.setInt(5, total);
            } else {
                ps.setInt(1, start);
                ps.setInt(2, total);
            }

            rs = ps.executeQuery();

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
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException throwables) {
                logger.error(throwables.getMessage(), throwables);
            }
        }

        return books;
    }
}
