package com.epam.bookshop.service.impl;

import com.epam.bookshop.dao.AbstractDAO;
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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BookService implements EntityService<Book> {

    private static final String BOOK_NOT_FOUND = "book_not_found";
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
//        if (optionalBook.isEmpty()) {
//            throw new EntityNotFoundException("No book with id = " + id + " found");
//        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//        return optionalBook.get();
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
//        if (optionalBook.isEmpty()) {
//            throw new EntityNotFoundException("No book with ISBN = " + ((BookCriteria)criteria).getISBN() + " found");
//        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//        return optionalBook.get();
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
//        if (optionalBook.isEmpty()) {
//            throw new EntityNotFoundException("No book with id = " + book.getEntityId() + " found");
//        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//        return optionalBook.get();
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
            String errorMessage = ErrorMessageManager.EN.getMessage(BOOK_NOT_FOUND);
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
            String errorMessage = ErrorMessageManager.EN.getMessage(BOOK_NOT_FOUND);
            throw new EntityNotFoundException(errorMessage + WHITESPACE + book.getEntityId());
        }

        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return isDeleted;
    }
}
