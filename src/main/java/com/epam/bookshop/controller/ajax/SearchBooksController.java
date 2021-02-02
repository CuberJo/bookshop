package com.epam.bookshop.controller.ajax;

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
import com.epam.bookshop.util.constant.RegexConstants;
import com.epam.bookshop.util.constant.RequestConstants;
import com.epam.bookshop.util.constant.UtilStringConstants;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.util.validator.impl.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Live book search
 */
@WebServlet("/search_books")
public class SearchBooksController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(SearchBooksController.class);

    private static final int DEFAULT_GENRE_ID = 1;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        final HttpSession session = req.getSession();
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);


        if (!isSearchInputCorrect(req.getParameter(RequestConstants.SEARCH_STR))) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA);
            session.setAttribute(ErrorMessageConstants.ERROR_SEARCH_MESSAGE, error);
            return;
        }

        Criteria<Book> criteria = buildCriteria(req, locale);
        Collection<Book> books = findBooksLike(criteria, locale);

        String jsonStrings = JSONWriter.getInstance().write(books);
        resp.getWriter().write(jsonStrings);
    }


    /**
     * Checks, if passed string corresponds to correct input
     *
     * @param searchStr string to validate
     * @return returns <b>true</b> if and only if passed
     * string corresponds to correct input, otherwise - <b>false</b>
     */
    private boolean isSearchInputCorrect(String searchStr) {
        Validator validator = new Validator();

        Pattern p = Pattern.compile(RegexConstants.MALICIOUS_REGEX);
        Matcher m = p.matcher(searchStr);

        return !validator.empty(searchStr) && !m.matches();
    }



    /**
     * Builds criteria by request params
     *
     * @param request instance of {@link HttpServletRequest} class
     * @param locale language of error messages
     * @return built {@link Criteria<Book>} instance
     */
    private Criteria<Book> buildCriteria(HttpServletRequest request, String locale) {

        String searchStr = request.getParameter(RequestConstants.SEARCH_STR);

        Criteria<Book> criteria;
        switch (request.getParameter(RequestConstants.SEARCH_CRITERIA)) {
            case RequestConstants.GENRE:
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
                        .build();
        }

        return criteria;
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
