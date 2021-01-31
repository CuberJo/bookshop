package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.controller.command.impl.AdminCommand;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.domain.impl.*;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.GenreService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.EntityFinder;
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
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@WebServlet("/admin")
@MultipartConfig
public class AdminController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AdminCommand.class);

    private static final String REQUEST_PARAM_NOT_FOUND = "Request param not found";
    private static final String COUNT_PAYMENTS = "countPayments";
    private static final String COUNT_USERS = "countUsers";
    private static final String FETCH = "fetch";
    private static final String PAYMENTS = "payments";
    private static final String USERS = "users";
    private static final String OLD_ISBN = "oldISBN";

    private static final int ITEMS_PER_PAGE = 4;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        final HttpSession session = req.getSession();
        final String locale = (String) session.getAttribute(UtilStrings.LOCALE);

//        String count = req.getParameter(UtilStrings.COUNT);
        if (Objects.nonNull(req.getParameter(COUNT_PAYMENTS)) ||
            Objects.nonNull(req.getParameter(COUNT_USERS))) {
//        if (Objects.nonNull(count) && !count.isEmpty()) {
            int rows = count(req, locale);
            resp.setContentType(UtilStrings.TEXT_PLAIN);
            req.setCharacterEncoding(UtilStrings.UTF8);
            resp.getWriter().write(String.valueOf(rows));
            return;
        }

        int start = BooksController.getStart(req, ITEMS_PER_PAGE);

        Collection<Entity> entities = findRecords(req, locale, start, ITEMS_PER_PAGE);
        resp.setContentType(UtilStrings.APPLICATION_JSON);
        req.setCharacterEncoding(UtilStrings.UTF8);
        String jsonStrings = JSONWriter.getInstance().write(entities);
        resp.getWriter().write(jsonStrings);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final HttpSession session = req.getSession();
        String locale = (String) session.getAttribute(UtilStrings.LOCALE);

        String oldISBN = req.getParameter(OLD_ISBN);
        String isbn = req.getParameter(UtilStrings.ISBN);
        String title = req.getParameter(UtilStrings.TITLE);
        String author = req.getParameter(UtilStrings.AUTHOR);
        String price = req.getParameter(UtilStrings.PRICE);
        String publisher = req.getParameter(UtilStrings.PUBLISHER);
        String genre = req.getParameter(UtilStrings.GENRE);
//        String base64Image = req.getParameter(UtilStrings.BASE64_IMAGE);
//        String imgFile = req.getParameter("imgFile");

        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        Criteria<Book> criteria = BookCriteria.builder()
                .ISBN(oldISBN)
                .build();
        Book bookToUpdate;
        try {
            Optional<Book> optionalBook = service.find(criteria);
            if (optionalBook.isEmpty()) {
                String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND)
                        + UtilStrings.WHITESPACE + oldISBN;
                logger.error(error);
                throw new RuntimeException(error);
            }
            bookToUpdate = optionalBook.get();
        } catch (ValidatorException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }

        if (Objects.nonNull(isbn) && !isbn.isEmpty()) {
            bookToUpdate.setISBN(isbn);
        }
        if (Objects.nonNull(title) && !title.isEmpty()) {
            bookToUpdate.setTitle(title);
        }
        if (Objects.nonNull(author) && !author.isEmpty()) {
            bookToUpdate.setAuthor(author);
        }
        if (Objects.nonNull(price) && !price.isEmpty()) {
            if (price.contains("$")) {
                price.replace("$", UtilStrings.EMPTY_STRING);
            }
            bookToUpdate.setPrice(Double.parseDouble(price));
        }
        if (Objects.nonNull(publisher) && !publisher.isEmpty()) {
            bookToUpdate.setPublisher(publisher);
        }
        if (Objects.nonNull(genre) && !genre.isEmpty()) {
            GenreService genreService = (GenreService) ServiceFactory.getInstance().create(EntityType.GENRE);
            Criteria<Genre> genreCriteria = GenreCriteria.builder()
                    .genre(genre)
                    .build();
            Optional<Genre> optionalGenre = Optional.empty();
            try {
                optionalGenre = genreService.find(genreCriteria);
                if (optionalGenre.isEmpty()) {
                    String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND)
                            + UtilStrings.WHITESPACE + genre;
                    logger.error(error);
                    throw new RuntimeException(error);
                }
            } catch (ValidatorException e) {
                logger.error(e.getMessage(), e);
            }
            bookToUpdate.setGenre(optionalGenre.get());
        }

        установить атрибут isbnBookToUpdate в сессии и потом..
        try {
            service.update(bookToUpdate);

            Part filePart = req.getPart("file"); // Retrieves <input type="file" name="file">
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
            InputStream fileContent = filePart.getInputStream();

            if (Objects.nonNull(fileContent)) {
                if (Objects.nonNull(isbn) && !isbn.isEmpty()){
                    service.createImage(isbn, fileContent);
                }
            }
        } catch (ValidatorException e) {
            logger.error(e.getMessage(), e);
            resp.setContentType("text/plain");
            req.setCharacterEncoding(UtilStrings.UTF8);
            resp.getWriter().write(e.getMessage());
        }

        System.out.println(isbn);
        System.out.println(title);
        System.out.println(author);
        System.out.println(price);
        System.out.println(publisher);
        System.out.println(genre);
//        System.out.println(base64Image);

    }

    /**
     * Counts total number of books in database
     *
     * @param request {@link HttpServletRequest} instance
     * @param locale language for error messages
     * @return total number of books in database
     */
    private int count(HttpServletRequest request, String locale) {
        String countPayments = request.getParameter(COUNT_PAYMENTS);
        String countUsers = request.getParameter(COUNT_USERS);

        EntityService service;
        int rows;
        if (Objects.nonNull(countPayments)) {
            service = ServiceFactory.getInstance().create(EntityType.PAYMENT);
            service.setLocale(locale);
        } else if (Objects.nonNull(countUsers)) {
            service = ServiceFactory.getInstance().create(EntityType.USER);
            service.setLocale(locale);
        } else {
            logger.error(REQUEST_PARAM_NOT_FOUND);
            throw new RuntimeException(REQUEST_PARAM_NOT_FOUND);
        }
        rows = service.count();

        return rows;
    }


    /**
     * Finds records in database
     *
     * @param request
     * @param locale
     * @param start
     * @param total
     * @return
     */
    private Collection<Entity> findRecords(HttpServletRequest request, String locale, int start, int total) {
        String fetchParam = request.getParameter(FETCH);

        EntityService service;

        switch (fetchParam) {
            case PAYMENTS:
                service = ServiceFactory.getInstance().create(EntityType.PAYMENT);
                service.setLocale(locale);
                break;
            case "users":
                service = ServiceFactory.getInstance().create(EntityType.USER);
                service.setLocale(locale);
                break;
            default:
                logger.error(REQUEST_PARAM_NOT_FOUND);
                throw new RuntimeException(REQUEST_PARAM_NOT_FOUND);
        }

        Collection<Entity> enities = service.findAll(start, total);
        if (USERS.equals(fetchParam)) {
            convertToDTO(enities);
        }

        return enities;
    }


    /**
     * Removes unnecessary data(like pass and IBAN list) from object to pass to client
     *
     * @param entities
     */
    private void convertToDTO(Collection<Entity> entities) {
        if (Objects.nonNull(entities) && !entities.isEmpty()) {
            entities.forEach(entity -> {
                ((User) entity).setPassword(null);
                ((User) entity).getIBANs().forEach(s -> s = null);
            });
        }
    }
}
