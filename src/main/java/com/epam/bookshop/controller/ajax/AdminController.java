package com.epam.bookshop.controller.ajax;

import com.epam.bookshop.controller.command.impl.AdminCommand;
import com.epam.bookshop.domain.Entity;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Payment;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.service.EntityService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.JSONWriter;
import com.epam.bookshop.util.constant.UtilStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@WebServlet("/admin")
public class AdminController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AdminCommand.class);

    private static final String REQUEST_PARAM_NOT_FOUND = "Request param not found";
    private static final String COUNT_PAYMENTS = "countPayments";
    private static final String COUNT_USERS = "countUsers";
    private static final String FETCH = "fetch";
    private static final String PAYMENTS = "payments";
    private static final String USERS = "users";

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


    private void convertToDTO(Collection<Entity> entities) {
        if (Objects.nonNull(entities) && !entities.isEmpty()) {
            entities.forEach(entity -> {
                ((User) entity).setPassword(null);
                ((User) entity).getIBANs().forEach(s -> s = null);
            });
        }
    }
}
