//package com.epam.bookshop.controller.ajax.query_processor;
//
//import com.epam.bookshop.constant.ErrorMessageConstants;
//import com.epam.bookshop.constant.RequestConstants;
//import com.epam.bookshop.constant.UtilStringConstants;
//import com.epam.bookshop.controller.ajax.BooksController;
//import com.epam.bookshop.domain.impl.Book;
//import com.epam.bookshop.domain.impl.EntityType;
//import com.epam.bookshop.service.impl.BookService;
//import com.epam.bookshop.service.impl.ServiceFactory;
//import com.epam.bookshop.util.JsonConverter;
//import com.epam.bookshop.util.locale_manager.ErrorMessageManager;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.concurrent.locks.ReentrantLock;
//
//public class HomepageBooksQueryProcessor {
//    private static final Logger logger = LoggerFactory.getLogger(HomepageBooksQueryProcessor.class);
//
//    private static final String RELATED_BOOKS = "relatedBooks";
//    private static final String BESTSELLERS = "bestsellers";
//    private static final String EXCLUSIVE = "exclusive";
//    private static final String LATEST_PRODUCTS = "latestProducts";
//
//    private static final ReentrantLock lock = new ReentrantLock();
//    private static HomepageBooksQueryProcessor instance;
//
//    public static HomepageBooksQueryProcessor getInstance() {
//        lock.lock();
//        try {
//            if (instance == null) {
//                instance = new HomepageBooksQueryProcessor();
//            }
//        } finally {
//            lock.unlock();
//        }
//
//        return instance;
//    }
//
//
//    public boolean process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        final HttpSession session = req.getSession();
//        String locale = (String) session.getAttribute(RequestConstants.LOCALE);
//
//        int start, total;
//        Collection<Book> books;
//
//        String relatedBooks = req.getParameter(RELATED_BOOKS);
//        if (Objects.nonNull(relatedBooks)) {
//            start = 23;
//            total = 4;
//            books = getBooks(start, total, locale);
//            String jsonStrings = JsonConverter.getInstance().write(books);
//            BooksController.writeResp(resp, UtilStringConstants.APPLICATION_JSON, jsonStrings);
//            return true;
//        }
//
//        String bestsellers = req.getParameter(BESTSELLERS);
//        if (Objects.nonNull(bestsellers)) {
//            start = 0;
//            total = 12;
//            books = getBooks(start, total, locale);
//            String jsonStrings = JsonConverter.getInstance().write(books);
//            BooksController.writeResp(resp, UtilStringConstants.APPLICATION_JSON, jsonStrings);
//            return true;
//        }
//
//        String exclusive = req.getParameter(EXCLUSIVE);
//        if (Objects.nonNull(exclusive)) {
//            Book book = getRandomBook(locale);
//            String jsonStrings = JsonConverter.getInstance().write(book);
//            BooksController.writeResp(resp, UtilStringConstants.APPLICATION_JSON, jsonStrings);
//            return true;
//        }
//
//        String latestProducts = req.getParameter(LATEST_PRODUCTS);
//        if (Objects.nonNull(latestProducts)) {
//            start = 8;
//            total = 16;
//            books = getBooks(start, total, locale);
//            String jsonStrings = JsonConverter.getInstance().write(books);
//            BooksController.writeResp(resp, UtilStringConstants.APPLICATION_JSON, jsonStrings);
//            return true;
//        }
//
//        return false;
//    }
//
//
//    /**
//     * Finds random row in table
//     *
//     * @return found random book
//     */
//    private Book getRandomBook(String locale) {
//
//        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
//        service.setLocale(locale);
//
//        String error;
//
//        Optional<Book> optionalBook = service.findRand();
//        if (optionalBook.isEmpty()) {
//            error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.BOOK_NOT_FOUND);
//            logger.error(error);
//            throw new RuntimeException(error);
//        }
//        service.findImageForBook(optionalBook.get());
//
//        return optionalBook.get();
//    }
//
//
//    /**
//     * Finds books by given criteria
//     *
//     * @param start start point
//     * @param total number of rows
//     * @param locale {@link String} language for error messages
//     * @return all found books
//     */
//    private Collection<Book> getBooks(int start, int total, String locale) {
//
//        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
//        service.setLocale(locale);
//
//        Collection<Book> books = service.findAll(start, total);
//        service.findImagesForBooks(books);
//
//        return books;
//    }
//}
