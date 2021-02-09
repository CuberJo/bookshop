package com.epam.bookshop.validator.impl;

import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RegexConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Validates input data, that comes when admin creates new {@link com.epam.bookshop.domain.impl.Book}
 * or updates existing one
 */
public class BookFormDataValidator {

    private static final String ERROR_MESSAGE= "erAddBookMes";

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static BookFormDataValidator instance;

    public static BookFormDataValidator getInstance() {
        LOCK.lock();
        try {
            if (instance == null) {
                instance = new BookFormDataValidator();
            }
        } finally {
            LOCK.unlock();
        }

        return instance;
    }


    /**
     * Validates passed strings for emptiness and correspondent to regex patterns
     *
     * @param isbn ISBN {@link String} to validate
     * @param title title {@link String} to validate
     * @param author author {@link String} to validate
     * @param price price {@link String} to validate
     * @param publisher publisher {@link String} to validate
     * @param genre genre {@link String} to validate
     * @param preview preview {@link String} to validate
     * @param session current {@link HttpSession} session used to set attributes
     * @return true if and only if strings passed validation, otherwise - false
     */
    public boolean validateInput(String isbn, String title, String author, String price, String publisher, String genre, String preview, HttpSession session) {

        RegexValidator validator = RegexValidator.getInstance();
        EmptyStringValidator emptyStringValidator = EmptyStringValidator.getInstance();
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        String error = "";

        if (emptyStringValidator.empty(isbn) || !validator.validate(isbn, RegexConstants.ISBN_REGEX)) {
            error += ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.ISBN_INCORRECT)
                    + UtilStringConstants.WHITESPACE + isbn + UtilStringConstants.SEMICOLON + UtilStringConstants.WHITESPACE;
        }
        if (emptyStringValidator.empty(title) || !validator.validate(title, RegexConstants.TITLE_REGEX)) {
            error += ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.TITLE_INCORRECT)
                    + UtilStringConstants.WHITESPACE + title + UtilStringConstants.SEMICOLON + UtilStringConstants.WHITESPACE;
        }
        if (emptyStringValidator.empty(author) || !validator.validate(author, RegexConstants.AUTHOR_REGEX)) {
            error += ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.AUTHOR_INCORRECT)
                    + UtilStringConstants.WHITESPACE + author + UtilStringConstants.SEMICOLON + UtilStringConstants.WHITESPACE;
        }
        if (price.contains("$")) {
            price = price.replace("$", UtilStringConstants.EMPTY_STRING);
        }
        if (emptyStringValidator.empty(price) || !validator.validate(price, RegexConstants.PRICE_REGEX)) {
            error += ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.PRICE_INCORRECT)
                    + UtilStringConstants.WHITESPACE + price + UtilStringConstants.SEMICOLON + UtilStringConstants.WHITESPACE;
        }
        if (emptyStringValidator.empty(publisher) || !validator.validate(publisher, RegexConstants.PUBLISHER_REGEX)) {
            error += ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.PUBLISHER_INCORRECT)
                    + UtilStringConstants.WHITESPACE + publisher + UtilStringConstants.SEMICOLON + UtilStringConstants.WHITESPACE;
        }
        if (emptyStringValidator.empty(genre) || !validator.validate(genre, RegexConstants.GENRE_REGEX)) {
            error += ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.GENRE_INCORRECT)
                    + UtilStringConstants.WHITESPACE + genre + UtilStringConstants.SEMICOLON + UtilStringConstants.WHITESPACE;
        }
        if (Objects.nonNull(preview)) {
            preview = new StringSanitizer().sanitize(preview);
        }

        if (!error.isEmpty()) {
            session.setAttribute(ERROR_MESSAGE, error);
            return false;
        }

        return true;
    }
}
