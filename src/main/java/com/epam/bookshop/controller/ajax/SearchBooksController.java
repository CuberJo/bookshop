package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.GenreService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.JSONWriter;
import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
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
import java.util.Collection;
import java.util.Optional;

@WebServlet("/search_books")
public class SearchBooksController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(SearchBooksController.class);

    private static final String SEARCH_CRITERIA = "searchCriteria";
    private static final String SEARCH_STR = "str";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final HttpSession session = req.getSession();
        String locale = (String) session.getAttribute(UtilStrings.LOCALE);

        Criteria<Book> criteria = buildCriteria(req, locale);
        Collection<Book> books = findBooksLike(criteria, locale);

        String jsonStrings = JSONWriter.getInstance().write(books);
        resp.getWriter().write(jsonStrings);
    }


    /**
     * Builds criteria by request params
     *
     * @param request instance of {@link HttpServletRequest} class
     * @param locale language of error messages
     * @return built {@link Criteria<Book>} instance
     */
    private Criteria<Book> buildCriteria(HttpServletRequest request, String locale) {

        String searchStr = request.getParameter(SEARCH_STR);

        Criteria<Book> criteria;
        switch (request.getParameter(SEARCH_CRITERIA)) {
            case UtilStrings.GENRE:
                long genreId = findGenreId(locale, searchStr);

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


    /**
     * Finds genre id by it's name
     *
     * @param locale language of error messages
     * @param genreName {@link String} genre name
     * @return genre id
     */
    private long findGenreId(String locale, String genreName) {
        GenreService service = (GenreService) ServiceFactory.getInstance().create(EntityType.GENRE);
        service.setLocale(locale);

        Criteria<Genre> criteria = GenreCriteria.builder()
                .genre(genreName)
                .build();

        Optional<Genre> optionalGenre;
        String error;
        try {
            optionalGenre = service.findLike(criteria);

            if (optionalGenre.isEmpty()) {
                error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND)
                        + UtilStrings.WHITESPACE + genreName;
                logger.error(error);
                throw new RuntimeException(error);
            }
        } catch (ValidatorException e) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA)
                    + UtilStrings.WHITESPACE + genreName;
            logger.error(error);
            throw new RuntimeException(error, e);
        }

        return optionalGenre.get().getEntityId();
    }


    /**
     * Finds books by criteria
     *
     * @param criteria {@link Criteria<Book>} criteria used to find books
     * @return found collection
     */
    private Collection<Book> findBooksLike(Criteria<Book> criteria, String locale) {
        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        Collection<Book> books;
        String error;
        try {
            books = service.findAllLike(criteria);
            service.findImagesForBooks(books);
        } catch (ValidatorException e) {
            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
            logger.error(error);
            throw new RuntimeException(error, e);
        }

        return books;
    }
}
