package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.constant.UtilStrings;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.domain.impl.Book;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@WebServlet("/sort")
public class SortController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final HttpSession session = req.getSession();

        List<Book> books = (List<Book>) session.getAttribute(UtilStrings.PAGE_BOOKS);

        sort(books, req);

        session.setAttribute(UtilStrings.BOOKS, books);
        session.setAttribute(UtilStrings.FILTERED, true);
        session.setAttribute("page", "get");

        session.setAttribute("process", true);

//        resp.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
//        resp.setCharacterEncoding("UTF-8"); // You want world domination, huh?
//        resp.getWriter().write("hi");
//        session.removeAttribute(UtilStrings.PAGE_BOOKS);
    }


    /**
     * @param books {@link List<Book>} list of books to order
     * @param req {@link HttpServletRequest} request from where parameters are taken
     */
    private void sort(List<Book> books, HttpServletRequest req) {
        if (Objects.nonNull(req.getParameter(UtilStrings.SORT)) && req.getParameter(UtilStrings.SORT).equals(UtilStrings.BY_AUTHOR)) {
            books.sort((a, b) -> a.getAuthor().compareToIgnoreCase(b.getAuthor()));
            return;
        }
        if (Objects.nonNull(req.getParameter(UtilStrings.SORT)) && req.getParameter(UtilStrings.SORT).equals(UtilStrings.BY_PRICE)) {
            books.sort(Comparator.comparingDouble(Book::getPrice));
            return;
        }
        if (Objects.nonNull(req.getParameter(UtilStrings.DEFAULT)) && !req.getParameter(UtilStrings.DEFAULT).isEmpty()) {
            books.sort(Comparator.comparingLong(Entity::getEntityId));
        }
    }
}
