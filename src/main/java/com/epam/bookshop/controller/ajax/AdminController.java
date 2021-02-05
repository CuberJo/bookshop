package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.command.impl.AdminCommand;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.domain.impl.*;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.GenreService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.util.JsonConverter;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RegexConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.criteria.impl.GenreCriteria;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import com.epam.bookshop.validator.impl.EmptyStringValidator;
import com.epam.bookshop.validator.impl.StringSanitizer;
import com.epam.bookshop.validator.impl.RegexValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * Processes admin requests
 */
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
    private static final String ADD_NEW_BOOK = "addNewBook";
    private static final String ERROR_MESSAGE= "erAddBookMes";
    private static final String FILE_TYPE = "file";
    private static final String FILE_IMG_TYPE = "file-img";

    private static final int ITEMS_PER_PAGE = 4;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final HttpSession session = req.getSession();
        final String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        if (Objects.nonNull(req.getParameter(COUNT_PAYMENTS)) || Objects.nonNull(req.getParameter(COUNT_USERS))) {
            int rows = count(req, locale);
            resp.setContentType(UtilStringConstants.TEXT_PLAIN);
            req.setCharacterEncoding(UtilStringConstants.UTF8);
            resp.getWriter().write(String.valueOf(rows));
            return;
        }

        int start = BooksController.getStartPoint(req, ITEMS_PER_PAGE);

        Collection<Entity> entities = findRecords(req, locale, start, ITEMS_PER_PAGE);
        resp.setContentType(UtilStringConstants.APPLICATION_JSON);
        req.setCharacterEncoding(UtilStringConstants.UTF8);
        String jsonStrings = JsonConverter.getInstance().write(entities);
        resp.getWriter().write(jsonStrings);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpSession session = req.getSession();
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        try {
            if (Objects.nonNull(req.getParameter(ADD_NEW_BOOK))) {
                boolean created = createNewBook(req, locale);
                if (!created) {
                    String error = ErrorMessageConstants.BOOK_CREATION_FAILED;
                    logger.error(error);
                    resp.setContentType(UtilStringConstants.TEXT_PLAIN);
                    req.setCharacterEncoding(UtilStringConstants.UTF8);
                    resp.getWriter().write(error);
                }
            } else {
                boolean updated = updateBook(req, locale);
                if (!updated) {
                    String error = ErrorMessageConstants.NO_BOOK_UPDATE_OCCURRED;
                    logger.error(error);
                    resp.setContentType(UtilStringConstants.TEXT_PLAIN);
                    req.setCharacterEncoding(UtilStringConstants.UTF8);
                    resp.getWriter().write(error);
                }
            }
        } catch (ValidatorException e) {
            logger.error(e.getMessage(), e);
            resp.setContentType(UtilStringConstants.TEXT_PLAIN);
            req.setCharacterEncoding(UtilStringConstants.UTF8);
            resp.getWriter().write(e.getMessage());
        }
    }


    /**
     * Creates new {@link Book} instance in database
     *
     * @param req current {@link HttpServletRequest} request
     * @param locale language for error messages if ones will take place
     * @return true, if and only if book was created successfully, otherwise - false
     * @throws IOException
     * @throws ServletException
     * @throws ValidatorException
     */
    private boolean createNewBook(HttpServletRequest req, String locale) throws IOException, ServletException, ValidatorException {
        Book book = new Book();
        if (!fillBook(book, req, locale)) {
            return false;
        }

        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.create(book);

        createBookFile(req.getParameter(RequestConstants.ISBN), locale, req);
        createBookImage(req.getParameter(RequestConstants.ISBN), locale, req);

        return true;
    }


    /**
     * Updates {@link Book} instance with passed by client params
     *
     * @param req current {@link HttpServletRequest} request
     * @param locale language for error messages if ones will take place
     * @return true, if and only if book was updated successfully, otherwise - false
     * @throws IOException
     * @throws ServletException
     * @throws ValidatorException
     */
    private boolean updateBook(HttpServletRequest req, String locale) throws IOException, ServletException, ValidatorException {
        String oldISBN = req.getParameter(OLD_ISBN);
        String isbn = req.getParameter(RequestConstants.ISBN);

        Criteria<Book> criteria = BookCriteria.builder()
                .ISBN(oldISBN)
                .build();
        Book bookToUpdate = EntityFinder.getInstance().find(criteria, locale, logger);
        if(!fillBook(bookToUpdate, req, locale)) {
            return false;
        }

        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.update(bookToUpdate);

        updateImg(isbn, oldISBN, locale, req);

        return true;
    }


    /**
     * Updates book image. Uses {@code isbn}, passed by client, if  book has changed it's ISBN,
     * otherwise - {@code oldISBN}
     *
     * @param isbn isbn which client has sent
     * @param oldISBN previous ISBN before client sent new one
     * @param locale language for error messages if ones will take place
     * @param req current {@link HttpServletRequest} request
     * @throws IOException
     * @throws ServletException
     */
    private void updateImg(String isbn, String oldISBN, String locale, HttpServletRequest req) throws IOException, ServletException {
        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        Part filePart = req.getPart(FILE_TYPE);
        if (Objects.nonNull(filePart)) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            InputStream fileContent = filePart.getInputStream();

            if (Objects.nonNull(fileContent)) {
                if (Objects.nonNull(isbn) && !isbn.isEmpty()) {
                    service.updateImage(isbn, fileContent);
                } else {
                    service.updateImage(oldISBN, fileContent);
                }
            } else {
                writeEmptyFileDataError(locale, req);
            }
        } else {
            writeEmptyFileDataError(locale, req);
        }
    }



    /**
     * Creates book file in database. Uses {@code isbn}, passed by client, if  book has changed it's ISBN,
     * otherwise - {@code oldISBN}
     *
     * @param isbn isbn which client has sent
     * @param locale language for error messages if ones will take place
     * @param req current {@link HttpServletRequest} request
     * @throws IOException
     * @throws ServletException
     */
    private void createBookFile(String isbn, String locale, HttpServletRequest req) throws IOException, ServletException {
        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        Part filePart = req.getPart(FILE_TYPE);
        if (Objects.nonNull(filePart)) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            InputStream fileContent = filePart.getInputStream();

            if (Objects.nonNull(fileContent)) {
                service.createBookFile(isbn, fileContent);
            } else {
                writeEmptyFileDataError(locale, req);
            }
        } else {
            writeEmptyFileDataError(locale, req);
        }
    }



    /**
     * Creates book image in database. Uses {@code isbn}, passed by client, if book has changed it's ISBN,
     * otherwise - {@code oldISBN}
     *
     * @param isbn isbn which client has sent
     * @param locale language for error messages if ones will take place
     * @param req current {@link HttpServletRequest} request
     * @throws IOException
     * @throws ServletException
     */
    private void createBookImage(String isbn, String locale, HttpServletRequest req) throws IOException, ServletException {
        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        Part filePart = req.getPart(FILE_IMG_TYPE);
        if (Objects.nonNull(filePart)) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            InputStream fileContent = filePart.getInputStream();

            if (Objects.nonNull(fileContent)) {
                service.createImage(isbn, fileContent);
            } else {
                writeEmptyFileDataError(locale, req);
            }
        } else {
            writeEmptyFileDataError(locale, req);
        }
    }


    /**
     * Writes errors to session attribute {@code erAddBookMes}
     *
     * @param locale language for error messages if ones will take place
     * @param req current {@link HttpServletRequest} request
     */
    private void writeEmptyFileDataError(String locale, HttpServletRequest req) {
        String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMPTY_FILE_DATA);
        req.getSession().setAttribute(ERROR_MESSAGE, error);
        logger.error(error);
    }


    /**
     * Fills book with new data
     *
     * @param book book to fill with data passed from client
     * @param req current {@link HttpServletRequest} request
     * @param locale language for error messages if ones will take place
     * @return true, if and only if books data passed the validation and book was filled
     * by this data
     */
    private boolean fillBook(Book book, HttpServletRequest req, String locale) {
        String isbn = req.getParameter(RequestConstants.ISBN);
        String title = req.getParameter(RequestConstants.TITLE);
        String author = req.getParameter(RequestConstants.AUTHOR);
        String price = req.getParameter(RequestConstants.PRICE);
        String publisher = req.getParameter(RequestConstants.PUBLISHER);
        String genre = req.getParameter(RequestConstants.GENRE);
        String preview = req.getParameter(RequestConstants.PREVIEW);

        if (!validateInput(isbn, title, author, price, publisher, genre, preview, req.getSession())) {
            return false;
        }

        if (Objects.nonNull(isbn) && !isbn.isEmpty()) {
            book.setISBN(isbn);
        }
        if (Objects.nonNull(title) && !title.isEmpty()) {
            book.setTitle(title);
        }
        if (Objects.nonNull(author) && !author.isEmpty()) {
            book.setAuthor(author);
        }
        if (Objects.nonNull(price) && !price.isEmpty()) {
            if (price.contains("$")) {
                price = price.replace("$", UtilStringConstants.EMPTY_STRING);
            }
            book.setPrice(Double.parseDouble(price));
        }
        if (Objects.nonNull(publisher) && !publisher.isEmpty()) {
            book.setPublisher(publisher);
        }
        if (Objects.nonNull(genre) && !genre.isEmpty()) {
            GenreService genreService = (GenreService) ServiceFactory.getInstance().create(EntityType.GENRE);
            Criteria<Genre> genreCriteria = GenreCriteria.builder()
                    .genre(genre)
                    .build();
            Optional<Genre> optionalGenre;
            try {
                optionalGenre = genreService.find(genreCriteria);
                if (optionalGenre.isEmpty()) {
                    String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.NO_SUCH_GENRE_FOUND)
                            + UtilStringConstants.WHITESPACE + genre;
                    logger.error(error);
                    throw new RuntimeException(error);
                }
            } catch (ValidatorException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
            book.setGenre(optionalGenre.get());
        }
        if (Objects.nonNull(preview) && !preview.isEmpty()) {
            book.setPreview(preview);
        }

        return true;
    }



    /**
     * Validates passed strings for emptiness and correspondent to regex patterns
     *
     * @param isbn ISBN {@link String} to validate
     * @param title title {@link String} to validate
     * @param author author {@link String} to validate
     * @param price price {@link String} to validate
     * @param publisher publisher {@link String} to validate
     * @param genre genre {@link String} to validate
     * @param preview preview {@link String} to validate
     * @param session current {@link HttpSession} session used to set attributes
     * @return true if and only if strings passed validation, otherwise - false
     */
    private boolean validateInput(String isbn, String title, String author, String price, String publisher, String genre, String preview, HttpSession session) {

        RegexValidator validator = RegexValidator.getInstance();
        EmptyStringValidator emptyStringValidator = EmptyStringValidator.getInstance();
        String locale = (String) session.getAttribute(RequestConstants.LOCALE);

        String error = "";

        if (emptyStringValidator.empty(isbn) || !validator.validate(isbn, RegexConstants.ISBN_REGEX)) {
            error += ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.ISBN_INCORRECT)
                    + UtilStringConstants.WHITESPACE + isbn + UtilStringConstants.SEMICOLON + UtilStringConstants.WHITESPACE;
        }
        if (emptyStringValidator.empty(title) || !validator.validate(title, RegexConstants.TITLE_REGEX)) {
            error += ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.TITLE_INCORRECT)
                    + UtilStringConstants.WHITESPACE + title + UtilStringConstants.SEMICOLON + UtilStringConstants.WHITESPACE;
        }
        if (emptyStringValidator.empty(author) || !validator.validate(author, RegexConstants.AUTHOR_REGEX)) {
            error += ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.AUTHOR_INCORRECT)
                    + UtilStringConstants.WHITESPACE + author + UtilStringConstants.SEMICOLON + UtilStringConstants.WHITESPACE;
        }
        if (price.contains("$")) {
            price = price.replace("$", UtilStringConstants.EMPTY_STRING);
        }
        if (emptyStringValidator.empty(price) || !validator.validate(price, RegexConstants.PRICE_REGEX)) {
            error += ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.PRICE_INCORRECT)
                    + UtilStringConstants.WHITESPACE + price + UtilStringConstants.SEMICOLON + UtilStringConstants.WHITESPACE;
        }
        if (emptyStringValidator.empty(publisher) || !validator.validate(publisher, RegexConstants.PUBLISHER_REGEX)) {
            error += ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.PUBLISHER_INCORRECT)
                    + UtilStringConstants.WHITESPACE + publisher + UtilStringConstants.SEMICOLON + UtilStringConstants.WHITESPACE;
        }
        if (emptyStringValidator.empty(genre) || !validator.validate(genre, RegexConstants.GENRE_REGEX)) {
            error += ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.GENRE_INCORRECT)
                    + UtilStringConstants.WHITESPACE + genre + UtilStringConstants.SEMICOLON + UtilStringConstants.WHITESPACE;
        }
        if (Objects.nonNull(preview)) {
            preview = new StringSanitizer().sanitize(preview);
        }

        if (!error.isEmpty()) {
            session.setAttribute(ERROR_MESSAGE, error);
            return false;
        }

        return true;
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

        EntityService service = null;
        int rows;
        if (Objects.nonNull(countPayments)) {
            service = ServiceFactory.getInstance().create(EntityType.PAYMENT);
            service.setLocale(locale);
        } else if (Objects.nonNull(countUsers)) {
            service = ServiceFactory.getInstance().create(EntityType.USER);
            service.setLocale(locale);
        }

        rows = service.count();

        return rows;
    }


    /**
     * Finds records in database
     *
     * @param request @link HttpServletRequest} instance
     * @param locale language for error messages
     * @param start from which row to search in database
     * @param total total number of rows to fetch from database
     * @return found collection of entities
     */
    private Collection<Entity> findRecords(HttpServletRequest request, String locale, int start, int total) {
        String fetchParam = request.getParameter(FETCH);

        EntityService service;

        switch (fetchParam) {
            case PAYMENTS:
                service = ServiceFactory.getInstance().create(EntityType.PAYMENT);
                service.setLocale(locale);
                break;
            case USERS:
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
     * @param entities user entities to find passwords and IBANs
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
