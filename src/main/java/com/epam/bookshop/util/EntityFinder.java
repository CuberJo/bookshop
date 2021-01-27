package com.epam.bookshop.util;

import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.service.impl.GenreService;
import com.epam.bookshop.service.impl.UserService;
import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.constant.UtilStrings;
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

public class EntityFinder {
    
    private static EntityFinder instance;
    private static final ReentrantLock lock = new ReentrantLock();

    private EntityFinder() {
        
    }

    public static EntityFinder getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new EntityFinder();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }


    /**
     * Finds user  of current session.
     *
     * @param session current {@link HttpSession} session
     * @param logger logger which is used to log error if ones would take place
     * @param criteria search criteria
     * @return {@link User} object if found}
     * @throws ValidatorException if {@link User} object data incorrect
     */
    public User find(HttpSession session, Logger logger, Criteria<User> criteria) throws ValidatorException {
        String locale = (String) session.getAttribute(UtilStrings.LOCALE);

        EntityService<User> userService = ServiceFactory.getInstance().create(EntityType.USER);
        userService.setLocale(locale);

        Optional<User> optionalUser = userService.find(criteria);
        if (optionalUser.isEmpty()) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND)
                    + UtilStrings.WHITESPACE + session.getAttribute(UtilStrings.LOGIN);
            logger.error(error);
            throw new RuntimeException(error);
        }

        return optionalUser.get();
    }


    /**
     * Looks for {@link User} by certain {@link Criteria<User>} criteria
     *
     * @param criteria {@link Criteria <User>} criteria by which user is found
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
     * @param criteria {@link Criteria <Genre>} criteria by which genre is found
     * @return {@link Genre} object if found
     * @throws EntityNotFoundException if no such genre found
     */
    public Genre find(String locale, Logger logger, Criteria<Genre> criteria) throws EntityNotFoundException {
        GenreService service = (GenreService) ServiceFactory.getInstance().create(EntityType.GENRE);
        service.setLocale(locale);

        Optional<Genre> optionalGenre = null;
        String error;
        try {
            optionalGenre = service.findLike(criteria);

            if (optionalGenre.isEmpty()) {
                error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND)
                        + UtilStrings.WHITESPACE + ((GenreCriteria) criteria).getGenre();
                logger.error(error);
                throw new EntityNotFoundException(error);
            }
        } catch (ValidatorException e) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA)
                    + UtilStrings.WHITESPACE + ((GenreCriteria) criteria).getGenre();
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }

        return optionalGenre.get();
    }


    /**
     * Find all {@link String} IBANs, associated with the user
     * @param criteria {@link Criteria<User>} criteria by which user is found
     * @param logger logger which is used to log error if ones would take place
     * @param locale {@link String} language for error messages
     * @return all {@link String} IBANs, associated with the user
     * @throws ValidatorException if criteria argument failed validation
     */
    public List<String> findIBANs(Criteria<User> criteria, Logger logger, String locale) throws ValidatorException {

        UserService service = (UserService) ServiceFactory.getInstance().create(EntityType.USER);
        service.setLocale(locale);

        Optional<User> optionalUser = service.find(criteria);
        if (optionalUser.isEmpty()) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.USER_NOT_FOUND);
            logger.error(error);
            throw new RuntimeException(error);
        }

        return service.findUserBankAccounts(optionalUser.get().getEntityId());
    }
}
