package com.epam.bookshop.controller;

import com.epam.bookshop.controller.command.RequestContext;
import com.epam.bookshop.controller.command.ResponseContext;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.BookCriteria;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/read_book")
public class ReadBookController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ReadBookController.class);
    private static final int DEFAULT_BUFFER_SIZE = 100;

//    private static final ResponseContext READ_BOOK_COMMAND = () -> "/WEB-INF/jsp/read_book.jsp";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        performTask(request, response);
    }





    public void render(HttpServletRequest request, HttpServletResponse response) throws IOException {


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

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                int inputStreamLength = 0;
                int length = 0;

                byte[] file = service.findBookFile(optionalBook.get());

                while ((length = request.getInputStream().read(buffer)) > 0) {
                    inputStreamLength += length;
                    baos.write(buffer, 0, length);
                }

                if (inputStreamLength > getContentLength()) {
                    setContentLength(inputStreamLength);
                }

                if (response instanceof HttpServletResponse) {
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.reset();
                    httpResponse.setHeader("Content-Type", "inline");//getContentType()
                    httpResponse.setHeader("Content-Length", String.valueOf()));
                    httpResponse.setHeader("Content-Disposition", "\"" + getContentDisposition() + "\"" + ((getFileName() != null && !getFileName().isEmpty()) ? "; filename=\"" + getFileName() + "\"": ""));
                }

                response.getOutputStream().write(baos.toByteArray(), 0, (int)getContentLength());

                //finally
                response.getOutputStream().flush();

                //clear
                baos = null;
            } finally {
                response.getOutputStream().close();
                request.getInputStream().close();
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
