package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.ResponseContext;
import com.epam.bookshop.command.impl.page.BooksPageCommand;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.controller.ajax.BooksController;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.util.JsonConverter;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Returns number of found books by {@link Criteria<Genre>} criteria
 * or total number of books in database
 */
public class CountBooksCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CountBooksCommand.class);

    private static final int DEFAULT_GENRE_ID = 1;

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        final String locale = (String) requestContext.getSession().getAttribute(RequestConstants.LOCALE);

        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);
        int rows;

        String encodedGenre = requestContext.getParameter(RequestConstants.GENRE);
        if (Objects.nonNull(encodedGenre) && !encodedGenre.isEmpty()) {
            String genreName = BooksPageCommand.decode(encodedGenre);
            long genreId;
            try {
                genreId = EntityFinder.getInstance()
                        .find(locale, logger, GenreCriteria.builder().genre(genreName).build())
                        .getEntityId();
            } catch (EntityNotFoundException e) {
                logger.error(ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND)
                        + UtilStringConstants.WHITESPACE + genreName, e);
                genreId = DEFAULT_GENRE_ID;
            }
            rows = service.count(BookCriteria.builder().genreId(genreId).build());
        } else {
            rows = service.count();
        }

        return () -> JsonConverter.getInstance().write(rows);
    }
}
