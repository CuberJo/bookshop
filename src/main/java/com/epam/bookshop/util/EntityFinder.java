package com.epam.bookshop.util;

import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
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
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import org.slf4j.Logger;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Facade for commonly meet operations of entities search
 */
public class EntityFinder {
    
    private static EntityFinder instance;
    private static final ReentrantLock LOCK = new ReentrantLock();

    private EntityFinder() {
        
    }

    public static EntityFinder getInstance() {
        LOCK.lock();
        try {
            if (instance == null) {
                instance = new EntityFinder();
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
}
