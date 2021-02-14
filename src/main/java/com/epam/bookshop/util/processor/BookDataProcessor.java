package com.epam.bookshop.util.processor;

import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.GenreService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.EntityFinderFacade;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.validator.impl.BookFormDataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Process incoming data for {@link Book}
 */
public class BookDataProcessor {
    private static final Logger logger = LoggerFactory.getLogger(BookDataProcessor.class);

    private static final int DEFAULT_GENRE_ID = 1;

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static BookDataProcessor instance;

    public static BookDataProcessor getInstance() {
        LOCK.lock();
        try {
            if (instance == null) {
                instance = new BookDataProcessor();
            }
        } finally {
            LOCK.unlock();
        }

        return instance;
    }

    /**
     * Fills book with new data
     *
     * @param book book to fill with data passed from client
     * @param req current {@link RequestContext} request
     * @param locale language for error messages if ones will take place
     * @return true, if and only if books data passed the validation and book was filled
     * by this data
     */
    public boolean fillBook(Book book, RequestContext req, String locale) {
        String isbn = req.getParameter(RequestConstants.ISBN);
        String title = req.getParameter(RequestConstants.TITLE);
        String author = req.getParameter(RequestConstants.AUTHOR);
        String price = req.getParameter(RequestConstants.PRICE);
        String publisher = req.getParameter(RequestConstants.PUBLISHER);
        String genre = req.getParameter(RequestConstants.GENRE);
        String preview = req.getParameter(RequestConstants.PREVIEW);

        if (!BookFormDataValidator.getInstance().validateInput(isbn, title, author, price, publisher, genre, preview, req.getSession())) {
            return false;
        }

        if (Objects.nonNull(isbn) && !isbn.isEmpty()) {
            book.setISBN(isbn);
        }
        if (Objects.nonNull(title) && !title.isEmpty()) {
            book.setTitle(title);
        }
        if (Objects.nonNull(author) && !author.isEmpty()) {
            book.setAuthor(author);
        }
        if (Objects.nonNull(price) && !price.isEmpty()) {
            if (price.contains("$")) {
                price = price.replace("$", UtilStringConstants.EMPTY_STRING);
            }
            book.setPrice(Double.parseDouble(price));
        }
        if (Objects.nonNull(publisher) && !publisher.isEmpty()) {
            book.setPublisher(publisher);
        }
        if (Objects.nonNull(genre) && !genre.isEmpty()) {
            GenreService genreService = (GenreService) ServiceFactory.getInstance().create(EntityType.GENRE);
            Criteria<Genre> genreCriteria = GenreCriteria.builder()
                    .genre(genre)
                    .build();
            Optional<Genre> optionalGenre;
            try {
                optionalGenre = genreService.find(genreCriteria);
                if (optionalGenre.isEmpty()) {
                    String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND)
                            + UtilStringConstants.WHITESPACE + genre;
                    logger.error(error);
                    throw new RuntimeException(error);
                }
            } catch (ValidatorException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
            book.setGenre(optionalGenre.get());
        }
        if (Objects.nonNull(preview) && !preview.isEmpty()) {
            book.setPreview(preview);
        }

        return true;
    }


    /**
     * Counts serial number of row from
     * where to start search in database
     *
     * @param requestContext request to this servlet
     * @return counted start page number
     */
    public int getStartPoint(RequestContext requestContext, int itemsPerPage) {
        int start = 1;
        String pageStr = requestContext.getParameter(RequestConstants.PAGE);
        if (Objects.nonNull(pageStr) && !pageStr.isEmpty()) {
            start = Integer.parseInt(pageStr);
        }
        start = --start * itemsPerPage;

        return start;
    }


    /**
     * Builds criteria by incoming request params
     *
     * @param session instance of {@link HttpSession} class
     * @param locale language of error messages
     * @return built {@link Criteria<Book>} instance
     */
    public Criteria<Book> buildCriteria(HttpSession session, String locale) {

        String searchStr = (String) session.getAttribute(RequestConstants.SEARCH_STR);

        Criteria<Book> criteria;
        switch ((String) session.getAttribute(RequestConstants.SEARCH_CRITERIA)) {
            case RequestConstants.GENRE:
                Criteria<Genre> genreCriteria = GenreCriteria.builder()
                        .genre(searchStr)
                        .build();
                Genre genre;
                long genreId;
                try {
                    genre = EntityFinderFacade.getInstance().find(locale, logger, genreCriteria);
                    genreId = genre.getEntityId();
                } catch (EntityNotFoundException e) {
                    String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND)
                            + UtilStringConstants.WHITESPACE + searchStr;
                    logger.error(error, e);
                    genreId = DEFAULT_GENRE_ID;
                }

                criteria = BookCriteria.builder()
                        .genreId(genreId)
                        .build();
                break;
            case RequestConstants.PUBLISHER:
                criteria = BookCriteria.builder()
                        .publisher(searchStr)
                        .build();
                break;
            case RequestConstants.AUTHOR:
                criteria = BookCriteria.builder()
                        .author(searchStr)
                        .build();
                break;
            case RequestConstants.BOOK:
            default:
                criteria = BookCriteria.builder()
                        .title(searchStr)
                        .author(searchStr)
                        .publisher(searchStr)
                        .build();
        }

        return criteria;
    }
}
