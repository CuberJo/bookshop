package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RegexConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.util.EntityFinderFacade;
import com.epam.bookshop.util.ToJsonConverter;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.validator.impl.EmptyStringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Returns books found by live search
 */
public class LiveBookSearchCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(LiveBookSearchCommand.class);

    private static final int START_POINT = 0;
    private static final int TOTAL_ITEMS_PER_PAGE = 8;
    private static final int DEFAULT_GENRE_ID = 1;

    @Override
    public CommandResult execute(RequestContext requestContext) {
        final HttpSession session = requestContext.getSession();
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        if (!isSearchInputCorrect(requestContext.getParameter(RequestConstants.SEARCH_STR))) {
            session.setAttribute(ErrorMessageConstants.ERROR_SEARCH_MESSAGE,
                    ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA));
            return new CommandResult(CommandResult.ResponseType.JSON,
                    UtilStringConstants.EMPTY_STRING);
        }

        Criteria<Book> criteria = buildCriteria(requestContext, locale);
        Collection<Book> books = EntityFinderFacade.getInstance().findBooksLike(criteria, START_POINT, TOTAL_ITEMS_PER_PAGE, locale, logger);

        return new CommandResult(CommandResult.ResponseType.JSON,
                ToJsonConverter.getInstance().write(books));
    }


    /**
     * Checks, if passed string corresponds to correct input
     *
     * @param searchStr string to validate
     * @return returns <b>true</b> if and only if passed
     * string corresponds to correct input, otherwise - <b>false</b>
     */
    private boolean isSearchInputCorrect(String searchStr) {
        Pattern p = Pattern.compile(RegexConstants.MALICIOUS_REGEX);
        Matcher m = p.matcher(searchStr);

        return !EmptyStringValidator.getInstance().empty(searchStr) && !m.matches();
    }


    /**
     * Builds criteria by request params
     *
     * @param requestContext instance of {@link RequestContext} implementor class
     * @param locale language of error messages
     * @return built {@link Criteria < Book >} instance
     */
    private Criteria<Book> buildCriteria(RequestContext requestContext, String locale) {

        String searchStr = requestContext.getParameter(RequestConstants.SEARCH_STR);
        searchStr = searchStr.replaceFirst(RegexConstants.SPACES_BEFORE_STR, UtilStringConstants.EMPTY_STRING)
                .replaceFirst(RegexConstants.SPACES_AFTER_STR, UtilStringConstants.EMPTY_STRING);

        Criteria<Book> criteria;
        switch (requestContext.getParameter(RequestConstants.SEARCH_CRITERIA)) {
            case RequestConstants.GENRE:
                long genreId;
                try {
                    Genre genre = EntityFinderFacade.getInstance().find(locale, logger, GenreCriteria.builder().genre(searchStr).build());
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
