package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.controller.command.impl.BooksFrontCommand;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.util.JSONWriter;
import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * Fetches books from database
 */
@WebServlet("/books")
public class BooksController extends HttpServlet {
    final static Logger logger = LoggerFactory.getLogger(BooksController.class);

    private static final String  BOOKS_PAGE = "/WEB-INF/jsp/books.jsp";

    private static final int ITEMS_PER_PAGE = 8;
    private static final int DEFAULT_GENRE_ID = 1;

    private static final String RELATED_BOOKS = "relatedBooks";
    private static final String BESTSELLERS = "bestsellers";
    private static final String EXCLUSIVE = "exclusive";
    private static final String LATEST_PRODUCTS = "latestProducts";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final HttpSession session = req.getSession();
        String locale = (String) session.getAttribute(UtilStrings.LOCALE);

        String relatedBooks = req.getParameter(RELATED_BOOKS);
        if (Objects.nonNull(relatedBooks)) {
            Collection<Book> books = getBooks(23, 4, locale);
            resp.setContentType(UtilStrings.APPLICATION_JSON);
            req.setCharacterEncoding(UtilStrings.UTF8);
            String jsonStrings = JSONWriter.getInstance().write(books);
            resp.getWriter().write(jsonStrings);
            return;
        }

        String bestsellers = req.getParameter(BESTSELLERS);
        if (Objects.nonNull(bestsellers)) {
            Collection<Book> books = getBooks(0, 12, locale);
            resp.setContentType(UtilStrings.APPLICATION_JSON);
            req.setCharacterEncoding(UtilStrings.UTF8);
            String jsonStrings = JSONWriter.getInstance().write(books);
            resp.getWriter().write(jsonStrings);
            return;
        }

        String exclusive = req.getParameter(EXCLUSIVE);
        if (Objects.nonNull(exclusive)) {
            Book book = getRandomBook(locale);
            resp.setContentType(UtilStrings.APPLICATION_JSON);
            req.setCharacterEncoding(UtilStrings.UTF8);
            String jsonStrings = JSONWriter.getInstance().write(book);
            resp.getWriter().write(jsonStrings);
            return;
        }

        String latestProducts = req.getParameter(LATEST_PRODUCTS);
        if (Objects.nonNull(latestProducts)) {
            Collection<Book> books = getBooks(8, 16, locale);
            resp.setContentType(UtilStrings.APPLICATION_JSON);
            req.setCharacterEncoding(UtilStrings.UTF8);
            String jsonStrings = JSONWriter.getInstance().write(books);
            resp.getWriter().write(jsonStrings);
            return;
        }

        String count = req.getParameter(UtilStrings.COUNT);
        if (Objects.nonNull(count) && !count.isEmpty()) {
            int rows = countBooks(req, locale);
            resp.setContentType(UtilStrings.TEXT_PLAIN);
            req.setCharacterEncoding(UtilStrings.UTF8);
            resp.getWriter().write(String.valueOf(rows));
            return;
        }

        int start = getStart(req, ITEMS_PER_PAGE);

        Collection<Book> books;


        if (Objects.nonNull(session.getAttribute(UtilStrings.NOT_ADVANCED_SEARCH))) {
            String searchStr = (String) session.getAttribute(UtilStrings.SEARCH_STR);

            Criteria<Book> criteria = BookCriteria.builder()
                    .title(searchStr)
                    .author(searchStr)
                    .publisher(searchStr)
                    .build();

            books = getBooksLike(criteria, start, ITEMS_PER_PAGE, locale);

            session.removeAttribute(UtilStrings.NOT_ADVANCED_SEARCH);
            session.removeAttribute(UtilStrings.SEARCH_STR);
        } else
//        String searchCriteria = (String) session.getAttribute(UtilStrings.SEARCH_CRITERIA);
        if (Objects.nonNull(session.getAttribute(UtilStrings.SEARCH_CRITERIA)) /*&& !searchCriteria.isEmpty()*/) {
            Criteria<Book> criteria = buildCriteria(session, locale);

            books = getBooksLike(criteria, start, ITEMS_PER_PAGE, locale);

            session.removeAttribute(UtilStrings.SEARCH_CRITERIA);
            session.removeAttribute(UtilStrings.CUSTOMIZED_SEARCH);
            session.removeAttribute(UtilStrings.SEARCH_STR);

//            req.setAttribute();
        } else {
            String genreName = BooksFrontCommand.decode(req.getParameter(UtilStrings.GENRE));
            books = getBooksByGenre(genreName, start, ITEMS_PER_PAGE, locale);
        }


        resp.setContentType(UtilStrings.APPLICATION_JSON);
        req.setCharacterEncoding(UtilStrings.UTF8);
        String jsonStrings = JSONWriter.getInstance().write(books);
        resp.getWriter().write(jsonStrings);
    }


    /**
     * Counts serial number of page from
     * where to start search in database
     *
     * @param req request to this servlet
     * @return counted start page number
     */
    public static int getStart(HttpServletRequest req, int itemsPerPage) {
        int start = 1;
        String pageStr = req.getParameter(UtilStrings.PAGE);
        if (Objects.nonNull(pageStr) && !pageStr.isEmpty()) {
            start = Integer.parseInt(pageStr);
        }
        start = --start * itemsPerPage;

        return start;
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
        if (Objects.nonNull(session.getAttribute(UtilStrings.REQUEST_FROM_SEARCH_PAGE))) {
            rows = service.count(buildCriteria(session, locale));
        } else if (Objects.nonNull(session.getAttribute(UtilStrings.REQUEST_FROM_SEARCH_INPUT))) {
            rows = service.count((String) session.getAttribute(UtilStrings.SEARCH_STR));
        } else if (Objects.nonNull(request.getParameter(UtilStrings.GENRE)) && !request.getParameter(UtilStrings.GENRE).isEmpty()) {
            String genreName = BooksFrontCommand.decode(request.getParameter(UtilStrings.GENRE));
            Criteria<Genre> genreCriteria = GenreCriteria.builder()
                    .genre(genreName)
                    .build();
            Genre genre;
            long genreId;
            try {
                genre = EntityFinder.getInstance().find(locale, logger, genreCriteria);
                genreId = genre.getEntityId();
            } catch (EntityNotFoundException e) {
                String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND)
                        + UtilStrings.WHITESPACE + genreName;
                logger.error(error, e);
                genreId = DEFAULT_GENRE_ID;
            }

            Criteria<Book> criteria = BookCriteria.builder()
                    .genreId(genreId)
                    .build();
            rows = service.count(criteria);
        } else {
            rows = service.count();
        }

//        int rows = Objects.nonNull(session.getAttribute(UtilStrings.REQUEST_FROM_SEARCH_PAGE))
//                ? service.count(buildCriteria(session, locale)) : service.count();

        session.removeAttribute(UtilStrings.REQUEST_FROM_SEARCH_PAGE);
        session.removeAttribute(UtilStrings.REQUEST_FROM_SEARCH_INPUT);

        return rows;
    }



    /**
     * Finds books by given criteria
     *
     * @param start start point
     * @param total number of rows
     * @param locale {@link String} language for error messages
     * @return {@link Collection<Book>} books genre
     */
    private Collection<Book> getBooks(int start, int total, String locale) {

        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        Collection<Book> books = service.findAll(start, total);
        service.findImagesForBooks(books);

        return books;
    }



    /**
     * Finds random row in table
     *
     * @return found random book
     */
    private Book getRandomBook(String locale) {

        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        String error;

        Optional<Book> optionalBook = service.findRand();
        if (optionalBook.isEmpty()) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND);
            logger.error(error);
            throw new RuntimeException(error);
        }

        service.findImageForBook(optionalBook.get());


        return optionalBook.get();
    }



    /**
     * Finds books by given criteria
     *
     * @param criteria {@link Criteria<Book>} search criteria
     * @param locale {@link String} language for error messages
     * @return {@link Collection<Book>} books genre
     */
    private Collection<Book> getBooksLike(Criteria<Book> criteria, int start, int total, String locale) {

        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        Collection<Book> books = null;

        try {
            books = service.findAllLike(criteria, start, total);
            service.findImagesForBooks(books);

        } catch (ValidatorException e) {
            logger.error(e.getMessage(), e);
        }

        return books;
    }


    /**
     * Finds books by given genre name.
     *
     * @param genreName {@link String} name of book to be found
     * @param locale {@link String} language for error messages
     * @return {@link Collection<Book>} books genre
     */
    private Collection<Book> getBooksByGenre(String genreName, int start, int total, String locale) {

        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        Collection<Book> books = null;

        if (Objects.nonNull(genreName) && !genreName.isEmpty()) {
            try {
                GenreCriteria genreCriteria = GenreCriteria.builder()
                        .genre(genreName)
                        .build();
                Genre genre = EntityFinder.getInstance().find(locale, logger, genreCriteria);

                BookCriteria bookCriteria = BookCriteria.builder()
                        .genreId(genre.getEntityId())
                        .build();
                books = service.findAll(bookCriteria, start, total);

            } catch (EntityNotFoundException | ValidatorException e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            books = service.findAll(start, total);
        }

        service.findImagesForBooks(books);

        return books;
    }


    /**
     * Builds criteria by request params
     *
     * @param session instance of {@link HttpSession} class
     * @param locale language of error messages
     * @return built {@link Criteria<Book>} instance
     */
    private Criteria<Book> buildCriteria(HttpSession session, String locale) {

        String searchStr = (String) session.getAttribute(UtilStrings.SEARCH_STR);

        Criteria<Book> criteria;
        switch ((String) session.getAttribute(UtilStrings.SEARCH_CRITERIA)) {
            case UtilStrings.GENRE:
                Criteria<Genre> genreCriteria = GenreCriteria.builder()
                        .genre(searchStr)
                        .build();
                Genre genre;
                long genreId;
                try {
                    genre = EntityFinder.getInstance().find(locale, logger, genreCriteria);
                    genreId = genre.getEntityId();
                } catch (EntityNotFoundException e) {
                    String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND)
                            + UtilStrings.WHITESPACE + searchStr;
                    logger.error(error, e);
                    genreId = DEFAULT_GENRE_ID;
                }

                criteria = BookCriteria.builder()
                        .genreId(genreId)
                        .build();
                break;
            case UtilStrings.PUBLISHER:
                criteria = BookCriteria.builder()
                        .publisher(searchStr)
                        .build();
                break;
            case UtilStrings.AUTHOR:
                criteria = BookCriteria.builder()
                        .author(searchStr)
                        .build();
                break;
            case UtilStrings.BOOK:
            default:
                criteria = BookCriteria.builder()
                        .title(searchStr)
                        .build();
        }

        return criteria;
    }
}
