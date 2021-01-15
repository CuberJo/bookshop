package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.criteria.impl.BookCriteria;
import com.epam.bookshop.criteria.impl.GenreCriteria;
import com.epam.bookshop.dao.impl.BookDAO;
import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.GenreService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class BooksCommand implements Command {

    final static Logger logger = LoggerFactory.getLogger(BooksCommand.class);

    private static final String BOOKS_LEN_ATTR = "booksLength";

    private static final ResponseContext BOOKS_PAGE = () -> "/WEB-INF/jsp/books.jsp";

    private static final String NO_SUCH_GENRE_FOUND = "no_such_genre_found";
    private static final String WHITESPACE = " ";


    @Override
    public ResponseContext execute(RequestContext requestContext) {

        logger.info("hi");
        String genreName = getDecodedGenre(requestContext.getParameter(UtilStrings.GENRE));

        Collection<Book> books = getBooksByGenre(genreName, requestContext);

        requestContext.setAttribute(UtilStrings.BOOKS, books);
        requestContext.setAttribute(BOOKS_LEN_ATTR, books.size());

        return BOOKS_PAGE;
    }



    private String getDecodedGenre(String encodedGenre) {
        if(Objects.nonNull(encodedGenre)) {
            try {
                return URLDecoder.decode(encodedGenre, UtilStrings.UTF8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return null;
    }



    private Collection<Book> getBooksByGenre(String genreName, RequestContext requestContext) {

        String locale = (String) requestContext.getSession().getAttribute(UtilStrings.LOCALE);

        BookService bookService = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        bookService.setLocale(locale);

        Collection<Book> books = null;

        if (genreName != null && !genreName.isEmpty()) {
            try {
                GenreService genreService = (GenreService) ServiceFactory.getInstance().create(EntityType.GENRE);
                genreService.setLocale(locale);
                Optional<Genre> optionalGenre = genreService.find(GenreCriteria.builder().genre(genreName).build());

                if (optionalGenre.isEmpty()) {
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(NO_SUCH_GENRE_FOUND) + WHITESPACE + genreName;
                    throw new EntityNotFoundException(errorMessage);
                }

                Genre genre = optionalGenre.get();

                books = bookService.findAll(BookCriteria.builder().genreId(genre.getEntityId()).build());

            } catch (EntityNotFoundException | ValidatorException e) {
                e.printStackTrace();
            }
        } else {
            books = bookService.findAll();
        }

        try {
            bookService.findImagesForBooks(books);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        return books;
    }
}
