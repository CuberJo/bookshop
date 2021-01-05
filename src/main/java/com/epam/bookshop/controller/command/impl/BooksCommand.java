package com.epam.bookshop.controller.command.impl;

import com.epam.bookshop.criteria.impl.BookCriteria;
import com.epam.bookshop.criteria.impl.GenreCriteria;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.GenreService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.controller.command.Command;
import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.util.manager.ErrorMessageManager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

public class BooksCommand implements Command {

    private static final ResponseContext BOOKS_PAGE = () -> "/WEB-INF/jsp/books.jsp";
    private static final String GENRE_NAME = "genre";
    private static final String UTF8 = "UTF-8";
    private static final String BOOKS_ATTR = "books";
    private static final String BOOKS_LEN_ATTR = "booksLength";
    private static final String LOCALE_ATTR = "locale";
    private static final String NO_SUCH_GENRE_FOUND = "no_such_genre_found";
    private static final String WHITESPACE = " ";


    @Override
    public ResponseContext execute(RequestContext requestContext) {
        BookService bookService = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);

        String genreName = null;
        if(requestContext.getParameter(GENRE_NAME) != null) {
            try {
                genreName = URLDecoder.decode(requestContext.getParameter(GENRE_NAME), UTF8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        Collection<Book> books = null;
        if (genreName != null && !genreName.isEmpty()) {
            try {
                GenreService genreService = (GenreService)  ServiceFactory.getInstance().create(EntityType.GENRE);
                Optional<Genre> optionalGenre = genreService.find(GenreCriteria.builder().genre(genreName).build());
                if (optionalGenre.isEmpty()) {
//                    throw new EntityNotFoundException("No genre with genreName = " + genreName + " found");
                    String locale = (String) requestContext.getSession().getAttribute(LOCALE_ATTR);
                    String errorMessage = ErrorMessageManager.valueOf(locale).getMessage(NO_SUCH_GENRE_FOUND);
                    throw new EntityNotFoundException(errorMessage + WHITESPACE);
                }

                Genre genre = optionalGenre.get();
                books = bookService.findAll(BookCriteria.builder().genreId(genre.getEntityId()).build());
            } catch (EntityNotFoundException | ValidatorException e) {
                e.printStackTrace();
            }
        } else {
            books = bookService.findAll();
        }
        requestContext.setAttribute(BOOKS_ATTR, books);
        requestContext.setAttribute(BOOKS_LEN_ATTR, books.size());


        return BOOKS_PAGE;
    }
}
