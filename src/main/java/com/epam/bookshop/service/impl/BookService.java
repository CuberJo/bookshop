package com.epam.bookshop.service.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.dao.impl.BookDAO;
import com.epam.bookshop.dao.impl.DAOFactory;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void findImagesForBooks(Collection<Book> books) throws EntityNotFoundException {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);

        for (Book book: books) {
            Optional<String> optionalImage = dao.findImageByISBN(book.getISBN());
            if (optionalImage.isEmpty()) {
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IMAGE_NOT_FOUND) + UtilStrings.WHITESPACE + book.getISBN();
                throw new EntityNotFoundException(errorMessage);
            }

            book.setBase64Image(optionalImage.get());
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }

    public void findImageForBook(Book book) throws EntityNotFoundException {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();

        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);

        Optional<String> optionalImage = dao.findImageByISBN(book.getISBN());
        if (optionalImage.isEmpty()) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IMAGE_NOT_FOUND) + UtilStrings.WHITESPACE + book.getISBN();
            throw new EntityNotFoundException(errorMessage);
        }

        book.setBase64Image(optionalImage.get());

        try {
            conn.close();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }

//    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

//    public void findImagesForBooks(Collection<Book> books) throws EntityNotFoundException {
//        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
//        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.createImage(EntityType.BOOK, conn);
//
//        for (Book book: books) {
//            Optional<String> optionalImage = dao.findImageByISBN(book.getISBN());
//            if (optionalImage.isEmpty()) {
//                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IMAGE_NOT_FOUND) + UtilStrings.WHITESPACE + book.getISBN();
//                throw new EntityNotFoundException(errorMessage);
//            }
//
//            book.setBase64Image(optionalImage.get());
//        }
//
//        try {
//            conn.close();
//        } catch (SQLException throwables) {
//            logger.error(throwables.getMessage(), throwables);
//        }
//    }

    public byte[] findBookFile(Book book) throws EntityNotFoundException {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();

        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);

//        Blob bookFile = dao.findBookFileByISBN(book.getISBN());
        byte[] bookFile = dao.findBookFileByISBN(book.getISBN());
        if (Objects.isNull(bookFile)) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_FILE_NOT_FOUND) + UtilStrings.WHITESPACE + book.getISBN();
            throw new EntityNotFoundException(errorMessage);
        }

//        book.setBase64Image(optionalImage.get());

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
     * @param total how many books to kake
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
}
