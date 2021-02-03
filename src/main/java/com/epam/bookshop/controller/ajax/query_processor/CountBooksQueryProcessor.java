package com.epam.bookshop.controller.ajax.query_processor;

import com.epam.bookshop.command.impl.BooksCommand;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.controller.ajax.BooksController;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Processes request coming to {@code /books} url
 * to count books in database
 */
public class CountBooksQueryProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CountBooksQueryProcessor.class);

    private static final ReentrantLock lock = new ReentrantLock();
    private static CountBooksQueryProcessor instance;

    public static CountBooksQueryProcessor getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new CountBooksQueryProcessor();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }


    public boolean process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String locale = (String) req.getSession().getAttribute(RequestConstants.LOCALE);

        String count = req.getParameter(RequestConstants.COUNT);
        if (Objects.nonNull(count) && !count.isEmpty()) {
            int rows = countBooks(req, locale);
            BooksController.writeResp(resp, UtilStringConstants.TEXT_PLAIN, String.valueOf(rows));
            return true;
        }

        return false;
    }



    /**
     * Counts total number of books in database
     *
     * @param request {@link HttpServletRequest} instance
     * @param locale language for error messages
     * @return total number of books in database
     */
    private int countBooks(HttpServletRequest request, String locale) {
        final HttpSession session = request.getSession();

        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        int rows = 0;
        if (Objects.nonNull(session.getAttribute(RequestConstants.REQUEST_FROM_SEARCH_PAGE))) {
            rows = service.count(BooksController.buildCriteria(session, locale));
        } else if (Objects.nonNull(session.getAttribute(RequestConstants.REQUEST_FROM_SEARCH_INPUT))) {
            rows = service.count((String) session.getAttribute(RequestConstants.SEARCH_STR));
        } else if (Objects.nonNull(request.getParameter(RequestConstants.GENRE)) && !request.getParameter(RequestConstants.GENRE).isEmpty()) {
            String genreName = BooksCommand.decode(request.getParameter(RequestConstants.GENRE));
            Criteria<Genre> genreCriteria = GenreCriteria.builder()
                    .genre(genreName)
                    .build();

            long genreId;
            try {
                Genre genre = EntityFinder.getInstance().find(locale, logger, genreCriteria);
                genreId = genre.getEntityId();
            } catch (EntityNotFoundException e) {
                String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND)
                        + UtilStringConstants.WHITESPACE + genreName;
                logger.error(error, e);
                genreId = BooksController.DEFAULT_GENRE_ID;
            }

            Criteria<Book> criteria = BookCriteria.builder()
                    .genreId(genreId)
                    .build();
            rows = service.count(criteria);
        } else {
            rows = service.count();
        }

        session.removeAttribute(RequestConstants.REQUEST_FROM_SEARCH_PAGE);
        session.removeAttribute(RequestConstants.REQUEST_FROM_SEARCH_INPUT);

        return rows;
    }
}
