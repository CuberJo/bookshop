package com.epam.bookshop.util;

import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.UtilStrings;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import org.slf4j.Logger;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class UserFinder {
    
    private static UserFinder instance;
    private static final ReentrantLock lock = new ReentrantLock();

    private UserFinder() {
        
    }

    public static UserFinder getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new UserFinder();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    /**
     * Finds user  of current session.
     * @param session current {@link HttpSession} session
     * @return {@link User} object if found
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
}
