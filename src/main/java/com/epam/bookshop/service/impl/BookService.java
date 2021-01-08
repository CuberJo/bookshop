package com.epam.bookshop.service.impl;

import com.epam.bookshop.dao.AbstractDAO;
import com.epam.bookshop.dao.impl.BookDAO;
import com.epam.bookshop.dao.impl.DAOFactory;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import com.epam.bookshop.validator.Validator;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BookService implements EntityService<Book> {

    private static final String BOOK_NOT_FOUND = "book_not_found";
    private static final String IMAGE_NOT_FOUND = "image_not_found";
    private static final String WHITESPACE = " ";

    private String locale = "EN";

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
        DAOFactory.INSTANCE.setLocale(locale);
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        dao.setLocale(locale);
        dao.create(book);

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return book;
    }

    @Override
    public Collection findAll() {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        dao.setLocale(locale);
        List<Book> books = dao.findAll();

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return books;
    }

    @Override
    public Collection findAll(Criteria criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        dao.setLocale(locale);
        Collection<Book> books = dao.findAll(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return books;
    }

    @Override
    public Optional<Book> findById(long id) {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        dao.setLocale(locale);
        Optional<Book> optionalBook = dao.findById(id);

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return optionalBook;
    }

    @Override
    public Optional<Book> find(Criteria criteria) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        dao.setLocale(locale);
        Optional<Book> optionalBook = dao.find(criteria);

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return optionalBook;
    }

    @Override
    public Optional<Book> update(Book book) throws ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(book);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        dao.setLocale(locale);
        Optional<Book> optionalBook = dao.update(book);

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return optionalBook;
    }

    @Override
    public boolean delete(long id) throws EntityNotFoundException {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        dao.setLocale(locale);
        boolean isDeleted = dao.delete(id);
        if (!isDeleted) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(BOOK_NOT_FOUND) + WHITESPACE + id;
            throw new EntityNotFoundException(errorMessage + WHITESPACE + id);
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return isDeleted;
    }

    @Override
    public boolean delete(Book book) throws EntityNotFoundException, ValidatorException {
        Validator validator = new Validator();
        validator.setLocale(locale);
        validator.validate(book);

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        AbstractDAO<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        dao.setLocale(locale);
        boolean isDeleted = dao.delete(book.getEntityId());
        if (!isDeleted) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(BOOK_NOT_FOUND) + WHITESPACE + book.getEntityId();
            throw new EntityNotFoundException(errorMessage + WHITESPACE + book.getEntityId());
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return isDeleted;
    }

    public void create(String ISBN, String filePath) {

        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        dao.setLocale(locale);
        dao.createImage(ISBN, filePath);

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Collection<Book> findImagesForBooks(Collection<Book> books) throws EntityNotFoundException {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();
        DAOFactory.INSTANCE.setLocale(locale);
        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        dao.setLocale(locale);

        for (Book book: books) {
            Optional<String> optionalImage = dao.findImageByISBN(book.getISBN());
            if (optionalImage.isEmpty()) {
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(IMAGE_NOT_FOUND) + WHITESPACE + book.getISBN();
                throw new EntityNotFoundException(errorMessage);
            }

            book.setBase64Image(optionalImage.get());
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return books;
    }

    public Optional<String> findImageForBook(Book book) throws EntityNotFoundException {
        Connection conn = ConnectionPool.getInstance().getAvailableConnection();

        DAOFactory.INSTANCE.setLocale(locale);
        BookDAO dao = (BookDAO) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
        dao.setLocale(locale);

        Optional<String> optionalImage = dao.findImageByISBN(book.getISBN());
        if (optionalImage.isEmpty()) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(IMAGE_NOT_FOUND) + WHITESPACE + book.getISBN();
            throw new EntityNotFoundException(errorMessage);
        }

        book.setBase64Image(optionalImage.get());

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return optionalImage;
    }
}
