package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.GenreService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.JSONWriter;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import static com.epam.bookshop.util.constant.UtilStrings.BOOKS;

/**
 * Fetches books from database
 */
@WebServlet("/books")
public class BooksController extends HttpServlet {
    final static Logger logger = LoggerFactory.getLogger(BooksController.class);

    private static final String  BOOKS_PAGE = "/WEB-INF/jsp/books.jsp";

    private static final int ITEMS_PER_PAGE = 8;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final HttpSession session = req.getSession();
        String locale = (String) session.getAttribute(UtilStrings.LOCALE);

//        if (session.getAttribute("page") != null && session.getAttribute("page").equals("get")) {
//            session.removeAttribute("page");
//            req.getRequestDispatcher(BOOKS_PAGE).forward(req, resp);
//            return;
//        }

        String count = req.getParameter(UtilStrings.COUNT);
        if (Objects.nonNull(count) && !count.isEmpty()) {
            int rows = countBooks(locale);
            resp.setContentType(UtilStrings.TEXT_PLAIN);
            req.setCharacterEncoding(UtilStrings.UTF8);
            resp.getWriter().write(String.valueOf(rows));
            return;
        }

        int start = 1;
        String pageStr = req.getParameter(UtilStrings.PAGE);
        if (Objects.nonNull(pageStr) && !pageStr.isEmpty()) {
            start = Integer.parseInt(pageStr);
        }
        start = --start * ITEMS_PER_PAGE;


//        boolean needPage = false;
//        if ((Objects.isNull(pageStr) || pageStr.isEmpty()) && (Objects.isNull(session.getAttribute("process")) || !((Boolean) session.getAttribute("process")))) {
////            session.removeAttribute("process");
//            needPage = true;
//        }


//        Boolean filtered = (Boolean) session.getAttribute(UtilStrings.FILTERED);
//        if (Objects.isNull(filtered)) {
//            filtered = false;
//        }
//        if (!filtered) {
        String genreName = decode(req.getParameter(UtilStrings.GENRE));
        Collection<Book> books = getBooksByGenre(genreName, start, ITEMS_PER_PAGE, locale);
        resp.setContentType(UtilStrings.APPLICATION_JSON);
        req.setCharacterEncoding(UtilStrings.UTF8);
        String jsonStrings = JSONWriter.getInstance().write(books);
        resp.getWriter().write(jsonStrings);
//        session.setAttribute(BOOKS, books);
//        session.setAttribute(UtilStrings.BOOKS_LEN_ATTR, books.size());
//        }
//        filtered = false;
//        session.setAttribute("process", false);
//        session.setAttribute(UtilStrings.FILTERED, filtered);

//        if (needPage) {
//        req.getRequestDispatcher(BOOKS_PAGE).forward(req, resp);
//        }
    }

    //    @Override
    protected void doGGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpSession session = req.getSession();
        String locale = (String) session.getAttribute(UtilStrings.LOCALE);

        if (session.getAttribute("page") != null && session.getAttribute("page").equals("get")) {
            session.removeAttribute("page");
            req.getRequestDispatcher(BOOKS_PAGE).forward(req, resp);
            return;
        }

        boolean needPage = false;

        String genreName = decode(req.getParameter(UtilStrings.GENRE));
        int page = 1;
        String pageStr = req.getParameter(UtilStrings.PAGE);
        if ((Objects.isNull(pageStr) || pageStr.isEmpty()) && (Objects.isNull(session.getAttribute("process")) || !((Boolean) session.getAttribute("process")))) {
//            session.removeAttribute("process");
            needPage = true;
        }
        if (Objects.nonNull(pageStr) && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }
        int total = 8;
        if (--page > 0) {
            page *= total;
        }

        Boolean filtered = (Boolean) session.getAttribute(UtilStrings.FILTERED);
        if (Objects.isNull(filtered)) {
            filtered = false;
        }
        if (!filtered) {
            Collection<Book> books = getBooksByGenre(genreName, page, total, locale);
            session.setAttribute(BOOKS, books);
            session.setAttribute(UtilStrings.BOOKS_LEN_ATTR, books.size());
        }
        filtered = false;
        session.setAttribute("process", false);
        session.setAttribute(UtilStrings.FILTERED, filtered);

        if (needPage) {
            req.getRequestDispatcher(BOOKS_PAGE).forward(req, resp);
        }
//        if (Objects.nonNull(req.getParameter("action")) && req.getParameter("action").equals("getPage")) {
//            resp.sendRedirect("/home?command=books");
//        }
    }


    private int countBooks(String locale) {
        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        return service.count();
    }


    /**
     * Decodes encoded  string
     * @param encodedString enoded {@link String}
     * @return encoded {@link String}
     */
    private String decode(String encodedString) {
        String decodedString = "";

        if(Objects.nonNull(encodedString)) {
            decodedString = URLDecoder.decode(encodedString, StandardCharsets.UTF_8);

            //        try {
//            dcodedGenre = URLDecoder.decode(encodedGenre, UtilStrings.UTF8);
//        } catch (UnsupportedEncodingException e) {
//            logger.error(e.getMessage(), e);
//        }
        }

        return decodedString;
    }


    /**
     * Finds books for given genre name.
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
                Genre genre = findGenre(locale, genreCriteria);

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
     * Finds genreby criteria
     * @param locale {@link String} language for error messages
     * @param criteria {@link Criteria<Genre>} criteria by which genre is found
     * @return {@link Genre} object if found
     * @throws EntityNotFoundException if no such genre found
     */
    private Genre findGenre(String locale, Criteria<Genre> criteria) throws EntityNotFoundException {
        EntityService<Genre> genreService = (GenreService) ServiceFactory.getInstance().create(EntityType.GENRE);
        genreService.setLocale(locale);
        Optional<Genre> optionalGenre;
        try {
            optionalGenre = genreService.find(criteria);

            if (optionalGenre.isEmpty()) {
                String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND)
                        + UtilStrings.WHITESPACE + ((GenreCriteria) criteria).getGenre();
                throw new EntityNotFoundException(errorMessage);
            }
        } catch (ValidatorException e) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.GENRE_INCORRECT)
                    + UtilStrings.WHITESPACE + ((GenreCriteria) criteria).getGenre();
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }

        return optionalGenre.get();
    }
}
