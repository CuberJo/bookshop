package com.epam.bookshop.service.impl;

import com.epam.bookshop.util.ImgToBase64Converter;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.dao.impl.BookDAO;
import com.epam.bookshop.dao.impl.DAOFactory;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.util.validator.impl.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BookService implements EntityService<Book> {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private String locale = "US";

    BookService() {

    }

    /**
     * @param locale service language for error messages
     */
    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public Book create(Book book) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(book);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        dao.create(book);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return book;
    }

    /**
     * @return collection {@link Collection<Book>} found
     */
    @Override
    public Collection<Book> findAll() {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        List<Book> books = dao.findAll();

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return books;
    }

    @Override
    public Collection<Book> findAll(Criteria<Book> criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        Collection<Book> books = dao.findAll(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return books;
    }

    @Override
    public Optional<Book> findById(long id) {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        Optional<Book> optionalBook = dao.findById(id);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalBook;
    }

    @Override
    public Optional<Book> find(Criteria<Book> criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        Optional<Book> optionalBook = dao.find(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalBook;
    }

    @Override
    public Optional<Book> update(Book book) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(book);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        Optional<Book> optionalBook = dao.update(book);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalBook;
    }

    @Override
    public boolean delete(long id) throws EntityNotFoundException {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        boolean isDeleted = dao.delete(id);
        if (!isDeleted) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND) + UtilStrings.WHITESPACE + id;
            throw new EntityNotFoundException(errorMessage + UtilStrings.WHITESPACE + id);
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return isDeleted;
    }

    @Override
    public boolean delete(Book book) throws EntityNotFoundException, ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(book);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        boolean isDeleted = dao.delete(book.getEntityId());
        if (!isDeleted) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND) + UtilStrings.WHITESPACE + book.getEntityId();
            throw new EntityNotFoundException(errorMessage + UtilStrings.WHITESPACE + book.getEntityId());
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return isDeleted;
    }


    /**
     * Creates image in database
     *
     * @param ISBN book's unique identifier
     * @param filePath file system path of image
     */
    public void createImage(String ISBN, String filePath) {

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        dao.createImage(ISBN, filePath);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }


    /**
     * @param book book needed to be set with default img
     */
    private void setDefaultImg(Book book) {
        final String IMAGE_NOT_FOUND_PATH = "C:\\Users\\User\\IdeaProjects\\bookshop\\src\\main\\webapp\\images\\books\\image-not-found.jpg";
        InputStream is = null;
        try {
            is = new FileInputStream(new File(IMAGE_NOT_FOUND_PATH));
//            byte[] bytes = Files.readAllBytes(Paths.get(IMAGE_NOT_FOUND_PATH));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        String base64Image = ImgToBase64Converter.getInstance().convert(is);
        book.setBase64Image(base64Image);
    }


    /**
     * Finds book image in database and set it to book
     *
     * @param book {@link Book} instance
     */
    public void findImageForBook(Book book) {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();

        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);

        Optional<String> optionalImage = dao.findImageByISBN(book.getISBN());
        if (optionalImage.isEmpty()) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IMAGE_NOT_FOUND)
                    + UtilStrings.WHITESPACE + book.getISBN();
            logger.error(errorMessage);
            setDefaultImg(book);
        }

        book.setBase64Image(optionalImage.get());

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }


    /**
     * Finds images for books in database and set it to each of them
     *
     * @param books {@link Collection<Book>} for which images needed be set
     */
    public void findImagesForBooks(Collection<Book> books) {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);

        for (Book book: books) {
            Optional<String> optionalImage = dao.findImageByISBN(book.getISBN());
            if (optionalImage.isEmpty()) {
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IMAGE_NOT_FOUND)
                        + UtilStrings.WHITESPACE + book.getISBN();
                logger.error(errorMessage);
                setDefaultImg(book);
                continue;
            }

            book.setBase64Image(optionalImage.get());
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }


    /**
     * @param ISBN book's unique identifier
     * @param filePath file system path of book
     */
    public void createBookFile(String ISBN, String filePath) {

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        dao.createBookFile(ISBN, filePath);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }


    /**
     * @param book {@link Book} needed to find file for
     * @return book file converted to byte array
     * @throws EntityNotFoundException if file not found
     */
    public byte[] findBookFile(Book book) throws EntityNotFoundException {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();

        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);

        byte[] bookFile = dao.findBookFileByISBN(book.getISBN());
        if (Objects.isNull(bookFile)) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_FILE_NOT_FOUND)
                    + UtilStrings.WHITESPACE + book.getISBN();
            throw new EntityNotFoundException(errorMessage);
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return bookFile;
    }


    /**
     * @param criteria {@link Criteria<com.epam.bookshop.domain.impl.User>} search criteria
     * @param start from where to start limitation
     * @param total how many books to kake
     * @return {@link Collection<Book>} found
     * @throws ValidatorException if book data fails validation
     */
    public Collection<Book> findAll(Criteria<Book> criteria, int start, int total) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        Collection<Book> books = dao.findAll(criteria, start, total);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return books;
    }


    /**
     * @param start from where to start limitation
     * @param total how many books to take
     * @return {@link Collection<Book>} found
     */
    public Collection<Book> findAll(int start, int total) {

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        Collection<Book> books = dao.findAll(start, total);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return books;
    }



    /**
     * Finds random row in table
     *
     * @return found random book
     */
    public Optional<Book> findRand() {

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        Optional<Book> optionalBook = dao.findRand();

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalBook;
    }


    /**
     * Counts total number of books
     *
     * @return number of rows in BOOKS table
     */
    public int count() {

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        int rows = dao.count();

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return rows;
    }


    /**
     * Counts number of rows that are similar
     * to passed as argument {@link Criteria<Book>} instance
     *
     * @param criteria criteria, by which fields rows would be counted
     * @return number of rows in 'BOOKS' table
     */
    public int count(Criteria<Book> criteria) {

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        int rows = dao.count(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return rows;
    }


    /**
     * Counts number of rows in 'BOOK' table
     * that are similar to passed argument
     *
     * @param searchParam string to look for in table
     * @return number of rows in 'BOOKS' table
     */
    public int count(String searchParam) {

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        int rows = dao.count(searchParam);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return rows;
    }



    /**
     * Finds books similar to data fields in criteria
     *
     * @param criteria builded {@link Criteria<Book>} to search by
     * @return collection of books found
     * @throws ValidatorException if criteria data is incorrect
     */
    public Collection<Book> findAllLike(Criteria<Book> criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        Collection<Book> books = dao.findAllLike(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return books;
    }


    /**
     * Finds limited quantity of books similar
     * to data fields in criteria
     *
     * @param criteria builded {@link Criteria<Book>} to search by
     * @param start from where to start limitation
     * @param total how many books to take
     * @return collection of books found
     * @throws ValidatorException if criteria data is incorrect
     */
    public Collection<Book> findAllLike(Criteria<Book> criteria, int start, int total) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        Collection<Book> books = dao.findAllLike(criteria, start, total);

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return books;
    }
}
