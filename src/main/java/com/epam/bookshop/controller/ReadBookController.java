package com.epam.bookshop.controller;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.BookCriteria;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.ErrorMessageConstants;
import com.epam.bookshop.util.UtilStrings;
import com.epam.bookshop.util.manager.ErrorMessageManager;
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
import java.util.Optional;

@WebServlet("/read_book")
public class ReadBookController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ReadBookController.class);

    private static final String HEADER_PARAM = "inline; filename=automatic_start.pdf";


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        render(request, response);
    }


    /**
     * Renders file read from database to a jsp page
     * @param request {@link HttpServletRequest} which comes to a Servlet
     * @param response {@link HttpServletResponse} which comes to a Servlet
     */
    public void render(HttpServletRequest request, HttpServletResponse response) {
        final HttpSession session = request.getSession();
        String locale = (String) session.getAttribute(UtilStrings.LOCALE);

        ByteArrayOutputStream bos = null;
        try {
            BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
            service.setLocale(locale);
            Criteria<Book> criteria = BookCriteria.builder()
                    .ISBN(request.getParameter(UtilStrings.ISBN))
                    .build();
            Book book = findBook(service, criteria, locale);

            bos = getBookByteArray(service, book);

            response.setContentType(UtilStrings.APPLICATION_PDF_CONTENT_TYPE);
            response.setHeader(UtilStrings.CONTENT_DISPOSITION_HEADER, HEADER_PARAM);
            response.setContentLength(bos.size());
            bos.writeTo(response.getOutputStream());

//                httpResponse.setHeader("Content-Disposition", "\"" + getContentDisposition() + "\"" + ((getFileName() != null && !getFileName().isEmpty()) ? "; filename=\"" + getFileName() + "\"": ""));
//            response.addHeader("Content-Disposition", "attachment; filename=" + pdfFileName);

            System.out.println("hi");
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
     * @param service {@link EntityService<Book>} which is used to fetch the book
     * @param criteria {@link Criteria<Book>} criterai of book search
     * @param locale language of error messages
     * @return book if it was found, otherwise {@link Optional} empty
     */
    private Book findBook(EntityService<Book> service, Criteria<Book> criteria, String locale) {
        Optional<Book> optionalBook;
        try {
            optionalBook = service.find(criteria);
            if (optionalBook.isEmpty()) {
                String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND + UtilStrings.WHITESPACE + ((BookCriteria) criteria).getISBN());
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
     * @param service {@link EntityService<Book>} which is used to fetch the book file
     * @param book {@link Book} which is searching parameter
     * @return {@link ByteArrayOutputStream} object with written book file in it
     */
    private ByteArrayOutputStream getBookByteArray(BookService service, Book book) {
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
    private void performTask(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        final HttpSession session = request.getSession();

        String locale = (String) session.getAttribute(UtilStrings.LOCALE);


        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        String isbn = (String) request.getParameter(UtilStrings.ISBN);
        Criteria<Book> criteria = BookCriteria.builder()
                .ISBN(isbn)
                .build();
        try {
            Optional<Book> optionalBook = service.find(criteria);
            if (optionalBook.isEmpty()) {
                String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND + UtilStrings.WHITESPACE + isbn);
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

//    @Override
//    public ResponseContext execute(RequestContext requestContext) {
//        final HttpSession session = requestContext.getSession();
//
//        String locale = (String) requestContext.getAttribute(UtilStrings.LOCALE);
//
//
//        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
//        String isbn = (String) requestContext.getParameter(UtilStrings.ISBN);
//        Criteria<Book> criteria = BookCriteria.builder()
//                .ISBN(isbn)
//                .build();
//        try {
//            Optional<Book> optionalBook = service.find(criteria);
//            if (optionalBook.isEmpty()) {
//                String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND + UtilStrings.WHITESPACE + isbn);
//                logger.error(error);
//                throw new RuntimeException(error);
//            }
//
//            Blob file = service.findBookFile(optionalBook.get());
//
//            session.setAttribute("file", file.getBytes(1, (int) file.length()));
//        } catch (ValidatorException | EntityNotFoundException | SQLException e) {
//            logger.error(e.getMessage(), e);
//            throw new RuntimeException(e);
//        }
//
//        return READ_BOOK_COMMAND;
//    }
}
