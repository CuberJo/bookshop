package com.epam.bookshop.service.impl;

import com.epam.bookshop.exception.DqlException;
import com.epam.bookshop.util.convertor.ImgToBase64Converter;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.dao.AbstractDao;
import com.epam.bookshop.dao.impl.BookDao;
import com.epam.bookshop.dao.impl.DAOFactory;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.validator.impl.CriteriaValidator;
import com.epam.bookshop.validator.impl.EntityValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Business logic for {@link Book} instances
 */
public class BookService implements EntityService<Book> {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private String locale = "US";

    BookService() {}

    @Override
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public Book create(Book book) throws ValidatorException, DqlException {
        EntityValidator entityValidator = new EntityValidator();
        entityValidator.setLocale(locale);
        entityValidator.validate(book);

        try (Connection conn =  ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            dao.create(book);
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
        List<Book> books = new ArrayList<>();
        try (Connection conn =  ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            books = dao.findAll();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return books;
    }

    @Override
    public Collection<Book> findAll(Criteria<Book> criteria) throws ValidatorException {
        CriteriaValidator validator = new CriteriaValidator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Collection<Book> books = new ArrayList<>();
        try (Connection conn =  ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            books = dao.findAll(criteria);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return books;
    }

    @Override
    public Optional<Book> findById(long id) {
        Optional<Book> optionalBook = Optional.empty();
        try(Connection conn =  ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            optionalBook = dao.findById(id);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalBook;
    }

    @Override
    public Optional<Book> find(Criteria<Book> criteria) throws ValidatorException {
        CriteriaValidator validator = new CriteriaValidator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Optional<Book> optionalBook = Optional.empty();
        try(Connection conn =  ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            optionalBook = dao.find(criteria);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalBook;
    }

    @Override
    public Optional<Book> update(Book book) throws ValidatorException {
        EntityValidator entityValidator = new EntityValidator();
        entityValidator.setLocale(locale);
        entityValidator.validate(book);

        Optional<Book> optionalBook = Optional.empty();
        try(Connection conn =  ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            optionalBook = dao.update(book);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return optionalBook;
    }

    @Override
    public boolean delete(long id) throws EntityNotFoundException {
        boolean isDeleted = false;

        try(Connection conn =  ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            isDeleted = dao.delete(id);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        if (!isDeleted) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND) + UtilStringConstants.WHITESPACE + id;
            throw new EntityNotFoundException(errorMessage + UtilStringConstants.WHITESPACE + id);
        }

        return isDeleted;
    }

    @Override
    public boolean delete(Book book) throws EntityNotFoundException, ValidatorException {
        EntityValidator entityValidator = new EntityValidator();
        entityValidator.setLocale(locale);
        entityValidator.validate(book);

        boolean isDeleted = false;
        try(Connection conn =  ConnectionPool.getInstance().getAvailableConnection()) {
            AbstractDao<Long, Book> dao = DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            isDeleted = dao.delete(book.getEntityId());
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        if (!isDeleted) {
            String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND) + UtilStringConstants.WHITESPACE + book.getEntityId();
            throw new EntityNotFoundException(errorMessage + UtilStringConstants.WHITESPACE + book.getEntityId());
        }

        return isDeleted;
    }


    /**
     * Creates image in database, by reading it in file system
     *
     * @param ISBN book's unique identifier
     * @param filePath file system path of image
     */
    public void createImage(String ISBN, String filePath) {
        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            dao.createImage(ISBN, filePath);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }


    /**
     * Creates image in database reading it from {@link InputStream}
     *
     * @param ISBN book's unique identifier
     * @param is stream with uploaded image
     */
    public void createImage(String ISBN, InputStream is) {
        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            dao.createImage(ISBN, is);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }


    /**
     * Updates image in database, reading it from {@link InputStream}
     *
     * @param ISBN book's unique identifier
     * @param is stream with uploaded image
     */
    public void updateImage(String ISBN, InputStream is) {
        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            dao.updateImage(ISBN, is);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }



    /**
     * Sets default image from system path
     *
     * @param book book needed to be set with default img
     */
    private void setDefaultImg(Book book) {
        final String image_not_found_path = "C:\\Users\\User\\IdeaProjects\\bookshop\\src\\main\\webapp\\images\\books\\image-not-found.jpg";
        InputStream is = null;
        try {
            is = new FileInputStream(new File(image_not_found_path));
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
        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            Optional<String> optionalImage = dao.findImageByISBN(book.getISBN());

            if (optionalImage.isEmpty()) {
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IMAGE_NOT_FOUND)
                        + UtilStringConstants.WHITESPACE + book.getISBN();
                logger.error(errorMessage);
                setDefaultImg(book);
            }

            book.setBase64Image(optionalImage.get());
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
        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);

            for (Book book: books) {
                Optional<String> optionalImage = dao.findImageByISBN(book.getISBN());
                if (optionalImage.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IMAGE_NOT_FOUND)
                            + UtilStringConstants.WHITESPACE + book.getISBN();
                    logger.error(errorMessage);
                    setDefaultImg(book);
                    continue;
                }
                book.setBase64Image(optionalImage.get());
            }
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }


    /**
     * Creates book file in database, by reading it in system path
     *
     * @param ISBN book's unique identifier
     * @param filePath file system path of book
     */
    public void createBookFile(String ISBN, String filePath) {
        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            dao.createBookFile(ISBN, filePath);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }


    /**
     * Creates book file in database, reading it from {@link InputStream}
     *
     * @param ISBN book's unique identifier
     * @param is stream with uploaded image
     */
    public void createBookFile(String ISBN, InputStream is) {
        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            dao.createBookFile(ISBN, is);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }



    /**
     * Finds book file in database by book ISBN
     *
     * @param book {@link Book} needed to find file for
     * @return book file converted to byte array
     * @throws EntityNotFoundException if file not found
     */
    public byte[] findBookFile(Book book) throws EntityNotFoundException {
        byte[] bookFile = new byte[0];
        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);

            bookFile = dao.findBookFileByISBN(book.getISBN());
            if (Objects.isNull(bookFile)) {
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_FILE_NOT_FOUND)
                        + UtilStringConstants.WHITESPACE + book.getISBN();
                throw new EntityNotFoundException(errorMessage);
            }
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
        CriteriaValidator validator = new CriteriaValidator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Collection<Book> books = new ArrayList<>();
        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            books = dao.findAll(criteria, start, total);
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
    @Override
    public Collection<Book> findAll(int start, int total) {
        Collection<Book> books = new ArrayList<>();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            books = dao.findAll(start, total);
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
        Optional<Book> optionalBook = Optional.empty();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            optionalBook = dao.findRand();
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
    @Override
    public int count() {
        int rows = 0;

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            rows = dao.count();
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
        int rows = 0;

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            rows = dao.count(criteria);
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
        int rows = 0;

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            rows = dao.count(searchParam);
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
        CriteriaValidator validator = new CriteriaValidator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Collection<Book> books = new ArrayList<>();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            books = dao.findAllLike(criteria);
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
        CriteriaValidator validator = new CriteriaValidator();
        validator.setLocale(locale);
        validator.validate(criteria);

        Collection<Book> books = new ArrayList<>();

        try(Connection conn = ConnectionPool.getInstance().getAvailableConnection()) {
            BookDao dao = (BookDao) DAOFactory.INSTANCE.create(EntityType.BOOK, conn);
            books = dao.findAllLike(criteria, start, total);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }

        return books;
    }
}
