package com.epam.bookshop.controller.ajax.query_processor;

import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.controller.ajax.BooksController;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.JsonWriter;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class SearchBooksQueryProcessor {
    private static final Logger logger = LoggerFactory.getLogger(SearchBooksQueryProcessor.class);

    private static final ReentrantLock lock = new ReentrantLock();
    private static SearchBooksQueryProcessor instance;

    public static SearchBooksQueryProcessor getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new SearchBooksQueryProcessor();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }


    public boolean process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final HttpSession session = req.getSession();
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        int start;
        int total = BooksController.ITEMS_PER_PAGE;
        Collection<Book> books = null;

        start = BooksController.getStartPoint(req, total);

        if (Objects.nonNull(session.getAttribute(RequestConstants.NOT_ADVANCED_SEARCH))) {
            String searchStr = (String) session.getAttribute(RequestConstants.SEARCH_STR);

            Criteria<Book> criteria = BookCriteria.builder()
                    .title(searchStr)
                    .author(searchStr)
                    .publisher(searchStr)
                    .build();

            books = findBooksLike(criteria, start, total, locale);

            session.removeAttribute(RequestConstants.NOT_ADVANCED_SEARCH);
            session.removeAttribute(RequestConstants.SEARCH_STR);

            String jsonStrings = JsonWriter.getInstance().write(books);
            BooksController.writeResp(resp, UtilStringConstants.APPLICATION_JSON, jsonStrings);

            return true;
        } else if (Objects.nonNull(session.getAttribute(RequestConstants.SEARCH_CRITERIA)) ) {
            Criteria<Book> criteria = BooksController.buildCriteria(session, locale);

            books = findBooksLike(criteria, start, total, locale);

            session.removeAttribute(RequestConstants.SEARCH_CRITERIA);
            session.removeAttribute(RequestConstants.CUSTOMIZED_SEARCH);
            session.removeAttribute(RequestConstants.SEARCH_STR);

            String jsonStrings = JsonWriter.getInstance().write(books);
            BooksController.writeResp(resp, UtilStringConstants.APPLICATION_JSON, jsonStrings);

            return true;
        }

//        if (Objects.nonNull(books) && !books.stream().allMatch(Objects::isNull)) {
//            String jsonStrings = JsonWriter.getInstance().write(books);
//            BooksController.writeResp(resp, UtilStringConstants.APPLICATION_JSON, jsonStrings);
//
//            return true;
//        }

        return false;
    }


    /**
     * Finds books by given criteria in range of [start, start + total]
     *
     * @param criteria {@link Criteria<Book>} search criteria
     * @param locale {@link String} language for error messages
     * @return all found books
     */
    public static Collection<Book> findBooksLike(Criteria<Book> criteria, int start, int total, String locale) {
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
