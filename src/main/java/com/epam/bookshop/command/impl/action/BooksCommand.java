package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.command.impl.page.BooksPageCommand;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.processor.BookDataProcessor;
import com.epam.bookshop.util.EntityFinderFacade;
import com.epam.bookshop.util.convertor.ToJsonConverter;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;

/**
 * Returns books found by genre, or, if no genre request present,
 * returns all books. Fetching books in done with pagination
 */
public class BooksCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(BooksCommand.class);

    private static final int TOTAL_ITEMS_PER_PAGE = 8;

    @Override
    public CommandResult execute(RequestContext requestContext) {
        String genreName = BooksPageCommand.decode(requestContext.getParameter(RequestConstants.GENRE));

        Collection<Book> books = getBooksByGenre(genreName,
                BookDataProcessor.getInstance().getStartPoint(requestContext, TOTAL_ITEMS_PER_PAGE), TOTAL_ITEMS_PER_PAGE,
                (String) requestContext.getSession().getAttribute(RequestConstants.LOCALE));

        requestContext.setAttribute(RequestConstants.GENRE, genreName);

        return new CommandResult(CommandResult.ResponseType.JSON, ToJsonConverter.getInstance().write(books));
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
                Genre genre = EntityFinderFacade.getInstance().find(locale, logger, GenreCriteria.builder().genre(genreName).build());
                books = service.findAll(BookCriteria.builder().genreId(genre.getEntityId()).build(), start, total);

            } catch (EntityNotFoundException | ValidatorException e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            books = service.findAll(start, total);
        }

        if (Objects.nonNull(books)) {
            service.findImagesForBooks(books);
        }

        return books;
    }
}
