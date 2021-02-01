package com.epam.bookshop.controller;

import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.util.EntityFinder;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.util.criteria.impl.PaymentCriteria;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Payment;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.constant.ErrorMessageConstants;
import com.epam.bookshop.util.constant.UtilStrings;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Optional;

/**
 * Renders book in browser
 */
@WebServlet("/read_book")
public class ReadBookController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ReadBookController.class);

    private static final String HEADER_PARAM = "inline; filename=automatic_start.pdf";
    private static final String HOME_PAGE = "/home";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (checkBookPurchase(request) || (Objects.nonNull(request.getSession().getAttribute(UtilStrings.LOGIN)) && UtilStrings.ADMIN_ROLE.equals(request.getSession().getAttribute(UtilStrings.ROLE)))) {
            renderBook(request, response);
        } else {
            request.getRequestDispatcher(HOME_PAGE).forward(request, response);
        }
    }

    /**
     * Checks, whether user, requesting this book, purchased it
     *
     * @param request {@link HttpServletRequest} which comes to a {@link HttpServletRequest}
     * @return true if and only if requested book was purchased
     */
    private boolean checkBookPurchase(HttpServletRequest request) {
        final String locale = (String) request.getSession().getAttribute(UtilStrings.LOCALE);

        EntityService<Payment> service = ServiceFactory.getInstance().create(EntityType.PAYMENT);
        service.setLocale(locale);

        Criteria<Book> criteria = BookCriteria.builder()
                .ISBN(request.getParameter(UtilStrings.ISBN))
                .build();
        Book book = findBook(criteria, locale);
        try {
            Criteria<User> userCriteria = UserCriteria.builder()
                    .login((String) request.getSession().getAttribute(UtilStrings.LOGIN))
                    .build();
            User user = EntityFinder.getInstance().find(userCriteria, logger, locale);
            Criteria<Payment> paymentCriteria = PaymentCriteria.builder()
                    .bookId(book.getEntityId())
                    .libraryUserId(user.getEntityId())
                    .build();
            Optional<Payment> optionalPayment = service.find(paymentCriteria);
            if (optionalPayment.isEmpty()) {
                return false;
            }
        } catch (ValidatorException e) {
            String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.INVALID_INPUT_DATA)
                    + UtilStrings.WHITESPACE + ((BookCriteria) criteria).getISBN();
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }

        return true;
    }


    /**
     * Renders book on screen
     *
     * Renders file read from database to a jsp page
     * @param request {@link HttpServletRequest} which comes to a {@link HttpServletRequest}
     * @param response {@link HttpServletResponse} which comes to a Servlet
     */
    public void renderBook(HttpServletRequest request, HttpServletResponse response) {
        final HttpSession session = request.getSession();
        String locale = (String) session.getAttribute(UtilStrings.LOCALE);

        ByteArrayOutputStream bos = null;
        try {
            Criteria<Book> criteria = BookCriteria.builder()
                    .ISBN(request.getParameter(UtilStrings.ISBN))
                    .build();
            Book book = findBook(criteria, locale);

            bos = getBookByteArrayStram(book, locale);

            response.setContentType(UtilStrings.APPLICATION_PDF_CONTENT_TYPE);
            response.setHeader(UtilStrings.CONTENT_DISPOSITION_HEADER, HEADER_PARAM);
            response.setContentLength(bos.size());
            bos.writeTo(response.getOutputStream());

//                httpResponse.setHeader("Content-Disposition", "\"" + getContentDisposition() + "\"" + ((getFileName() != null && !getFileName().isEmpty()) ? "; filename=\"" + getFileName() + "\"": ""));
//            response.addHeader("Content-Disposition", "attachment; filename=" + pdfFileName);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }


    /**
     * Finds book in database by criteria
     *
     * @param criteria {@link Criteria<Book>} criterai of book search
     * @param locale language of error messages
     * @return book if it was found, otherwise {@link Optional} empty
     */
    private Book findBook(Criteria<Book> criteria, String locale) {
        EntityService<Book> service = ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        Optional<Book> optionalBook;
        try {
            optionalBook = service.find(criteria);
            if (optionalBook.isEmpty()) {
                String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND)
                        + UtilStrings.WHITESPACE + ((BookCriteria) criteria).getISBN();
                logger.error(error);
                throw new RuntimeException(error);
            }
        } catch (ValidatorException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return optionalBook.get();
    }


    /**
     * Converts found book in database to ByteArrayOutputStream
     *
     * @param book {@link Book} which is searching parameter
     * @param locale language of error messages
     * @return {@link ByteArrayOutputStream} object with written book file in it
     */
    private ByteArrayOutputStream getBookByteArrayStram(Book book, String locale) {
        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.setLocale(locale);

        byte[] file;
        try {
            file = service.findBookFile(book);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream(file.length);
        bos.write(file, 0, file.length);

        return bos;
    }


    /**
     * for download
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final HttpSession session = request.getSession();

        String locale = (String) session.getAttribute(UtilStrings.LOCALE);


        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        String isbn = request.getParameter(UtilStrings.ISBN);
        Criteria<Book> criteria = BookCriteria.builder()
                .ISBN(isbn)
                .build();
        try {
            Optional<Book> optionalBook = service.find(criteria);
            if (optionalBook.isEmpty()) {
                String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND)
                        + UtilStrings.WHITESPACE + isbn;
                logger.error(error);
                throw new RuntimeException(error);
            }

            byte[] file = service.findBookFile(optionalBook.get());

            String pdfFileName = "pdf-test.pdf";
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + pdfFileName);
            response.setContentLength(file.length);


//            session.setAttribute("file", file.getBytes(1, (int) file.length()));
            OutputStream responseOutputStream = response.getOutputStream();
//            int bytes;
            for (byte b : file) {
                responseOutputStream.write(b);
            }
//            while ((bytes = file.read()) != -1) {
//                responseOutputStream.write(bytes);
//            }
            System.out.println("hi");
        } catch (ValidatorException | EntityNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
