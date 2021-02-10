package com.epam.bookshop.util;

import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.GenreService;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import org.slf4j.Logger;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Facade for commonly met operations of entities search
 */
public class EntityFinderFacade {
    
    private static EntityFinderFacade instance;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private EntityFinderFacade() {
        
    }

    public static EntityFinderFacade getInstance() {
        LOCK.lock();
        try {
            if (instance == null) {
                instance = new EntityFinderFacade();
            }
        } finally {
            LOCK.unlock();
        }

        return instance;
    }


    /**
     * Finds user of current session.
     *
     * @param session current {@link HttpSession} session
     * @param logger logger which is used to log error if ones would take place
     * @return {@link User} object if found}
     */
    public User findUserInSession(HttpSession session, Logger logger) {
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        EntityService<User> userService = ServiceFactory.getInstance().create(EntityType.USER);
        userService.setLocale(locale);

        Criteria<User> criteria = UserCriteria.builder()
                .login((String) session.getAttribute(RequestConstants.LOGIN))
                .build();

        Optional<User> optionalUser;
        String error;
        try {
            optionalUser = userService.find(criteria);
        } catch (ValidatorException e) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.LOGIN_INCORRECT);
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }

        if (optionalUser.isEmpty()) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND)
                    + UtilStringConstants.WHITESPACE + session.getAttribute(RequestConstants.LOGIN);
            logger.error(error);
            throw new RuntimeException(error);
        }

        return optionalUser.get();
    }


    /**
     * Looks for {@link User} by certain {@link Criteria<User>} criteria
     *
     * @param criteria {@link Criteria<User>} criteria by which user is found
     * @param logger logger which is used to log error if ones would take place
     * @param locale language for error messages
     * @return {@link User} object if it is found
     */
    public User find(Criteria<User> criteria, Logger logger, String locale) {
        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        User user = null;

        try {
            Optional<User> optionalUser = service.find(criteria);
            if (optionalUser.isEmpty()) {
                String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND)
                        + ((UserCriteria) criteria).getLogin();
                throw new RuntimeException(error);
            }

            user = optionalUser.get();
        } catch (ValidatorException e) {
            logger.error(e.getMessage(), e);
        }

        return user;
    }



    /**
     * Looks for {@link User} by certain {@link Criteria<User>} criteria
     *
     * @param criteria {@link Criteria<User>} criteria by which user is found
     * @param logger logger which is used to log error if ones would take place
     * @param locale language for error messages
     * @return optional object
     */
    public Optional<User> findOptional(Criteria<User> criteria, Logger logger, String locale) {
        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        Optional<User> optionalUser = Optional.empty();
        try {
            optionalUser = service.find(criteria);
        } catch (ValidatorException e) {
            logger.error(e.getMessage(), e);
        }

        return optionalUser;
    }



    /**
     * Finds genre by criteria
     *
     * @param locale {@link String} language for error messages
     * @param logger logger which is used to log error if ones would take place
     * @param criteria {@link Criteria<Genre>} criteria by which genre is found
     * @return {@link Genre} object if found
     * @throws EntityNotFoundException if no such genre found
     */
    public Genre find(String locale, Logger logger, Criteria<Genre> criteria) throws EntityNotFoundException {
        GenreService service = (GenreService) ServiceFactory.getInstance().create(EntityType.GENRE);
        service.setLocale(locale);

        Optional<Genre> optionalGenre;
        String error;
        try {
            optionalGenre = service.findLike(criteria);

            if (optionalGenre.isEmpty()) {
                error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND)
                        + UtilStringConstants.WHITESPACE + ((GenreCriteria) criteria).getGenre();
                logger.error(error);
                throw new EntityNotFoundException(error);
            }
        } catch (ValidatorException e) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA)
                    + UtilStringConstants.WHITESPACE + ((GenreCriteria) criteria).getGenre();
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }

        return optionalGenre.get();
    }


    /**
     * Finds book in database by criteria
     *
     * @param criteria {@link Criteria<Book>} criterai of book search
     * @param locale language of error messages
     * @param logger logger which is used to log error if ones would take place
     * @return book if it was found, otherwise {@link Optional} empty
     */
    public Book find(Criteria<Book> criteria, String locale, Logger logger) {
        EntityService<Book> service = ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        Optional<Book> optionalBook;
        try {
            optionalBook = service.find(criteria);
            if (optionalBook.isEmpty()) {
                String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND)
                        + UtilStringConstants.WHITESPACE + ((BookCriteria) criteria).getISBN();
                logger.error(error);
                throw new RuntimeException(error);
            }
        } catch (ValidatorException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return optionalBook.get();
    }


    /**
     * Find all {@link String} IBANs, associated with the user
     *
     * @param user user for who's IBANs are looking for
     * @param locale {@link String} language for error messages
     * @return all {@link String} IBANs, associated with the user
     */
    public List<String> findIBANs(User user, String locale) {

        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        return service.findUserBankAccounts(user.getEntityId());
    }



    /**
     * Finds books by given criteria
     *
     * @param start start point
     * @param total number of rows
     * @param locale {@link String} language for error messages
     * @return all found books
     */
    public Collection<Book> getBooks(int start, int total, String locale) {

        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        Collection<Book> books = service.findAll(start, total);
        service.findImagesForBooks(books);

        return books;
    }


    /**
     * Finds books by given criteria in range of [start, start + total]
     *
     * @param criteria {@link Criteria<Book>} search criteria
     * @param locale {@link String} language for error messages
     * @param logger logger which is used to log error if ones would take place
     * @return all found books
     */
    public static Collection<Book> findBooksLike(Criteria<Book> criteria, int start, int total, String locale, Logger logger) {
        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        Collection<Book> books = null;

        try {
            books = service.findAllLike(criteria, start, total);
            service.findImagesForBooks(books);

        } catch (ValidatorException e) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
            logger.error(error, e);
        }

        return books;
    }
}
