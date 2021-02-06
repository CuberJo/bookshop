package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandFactory;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.command.impl.CustomRequestContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RegexConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * Entry point for handling all requests coming to /books servlet url.
 * Fetches books from database by given parameters and returns
 * response in JSON format
 */
@WebServlet("/books")
public class BooksController extends HttpServlet {
    final static Logger logger = LoggerFactory.getLogger(BooksController.class);

    private static final int DEFAULT_GENRE_ID = 1;
    private static final String GET_bOOKS_COMMAND = "get_books";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String commandParam = req.getParameter(RequestConstants.COMMAND);
        if (Objects.isNull(commandParam)) {
            commandParam = GET_bOOKS_COMMAND;
        }
        Command command = CommandFactory.command(commandParam);

        ResponseContext execute = command.execute(new CustomRequestContext(req));
        if (Objects.nonNull(execute) && !execute.getResp().isEmpty()) {
            writeResp(resp, UtilStringConstants.APPLICATION_JSON, execute.getResp());
        }
    }


    /**
     * Writes response message {@code respMsg} string of {@code contentType} and sends it
     * in {@link HttpServletResponse} response
     *
     * @param resp {@link HttpServletResponse} instance
     * @param contentType type of response content
     * @param respMsg message to response with
     * @throws IOException
     */
    public static void writeResp(HttpServletResponse resp, String contentType, String respMsg) throws IOException {
        resp.setContentType(contentType);
        resp.setCharacterEncoding(UtilStringConstants.UTF8);
        resp.getWriter().write(respMsg);
    }



    /**
     * Counts serial number of row from
     * where to start search in database
     *
     * @param req request to this servlet
     * @return counted start page number
     */
    public static int getStartPoint(HttpServletRequest req, int itemsPerPage) {
        int start = 1;
        String pageStr = req.getParameter(RequestConstants.PAGE);
        if (Objects.nonNull(pageStr) && !pageStr.isEmpty()) {
            start = Integer.parseInt(pageStr);
        }
        start = --start * itemsPerPage;

        return start;
    }



    /**
     * Counts serial number of row from
     * where to start search in database
     *
     * @param requestContext request to this servlet
     * @return counted start page number
     */
    public static int getStartPoint(RequestContext requestContext, int itemsPerPage) {
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
    public static Criteria<Book> buildCriteria(HttpSession session, String locale) {

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
}
