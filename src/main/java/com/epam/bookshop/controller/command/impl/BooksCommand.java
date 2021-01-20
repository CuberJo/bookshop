package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.BookCriteria;
import com.epam.bookshop.criteria.impl.GenreCriteria;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.GenreService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class BooksCommand implements Command {
    final static Logger logger = LoggerFactory.getLogger(BooksCommand.class);


    private static final ResponseContext BOOKS_PAGE = () -> "/WEB-INF/jsp/books.jsp";


    @Override
    public ResponseContext execute(RequestContext requestContext) {
        String locale = (String) requestContext.getSession().getAttribute(UtilStrings.LOCALE);

        String genreName = decode(requestContext.getParameter(UtilStrings.GENRE));


        Collection<Book> books = getBooksByGenre(genreName, locale);

        requestContext.setAttribute(UtilStrings.BOOKS, books);
        requestContext.setAttribute(UtilStrings.BOOKS_LEN_ATTR, books.size());

        return BOOKS_PAGE;
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
    private Collection<Book> getBooksByGenre(String genreName, String locale) {

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
                books = service.findAll(bookCriteria);

            } catch (EntityNotFoundException | ValidatorException e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            books = service.findAll();
        }

        try {
            service.findImagesForBooks(books);
        } catch (EntityNotFoundException e) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IMAGE_NOT_FOUND)
                    + UtilStrings.WHITESPACE + books;
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }

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
