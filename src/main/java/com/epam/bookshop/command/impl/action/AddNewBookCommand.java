package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.DqlException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.BookDataProcessor;
import com.epam.bookshop.util.manager.language.ErrorMessageManager;
import com.epam.bookshop.validator.impl.DqlExceptionMessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Creates new {@link Book} in database by incoming parameters
 */
public class AddNewBookCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(AddNewBookCommand.class);

    private static final String FILE_TYPE = "file";
    private static final String FILE_IMG_TYPE = "file-img";
    private static final String ERROR_MESSAGE= "erAddBookMes";


    @Override
    public CommandResult execute(RequestContext requestContext) {
        HttpSession session = requestContext.getSession();
        boolean created = false;
        try {
            created = createNewBook(requestContext, (String) session.getAttribute(RequestConstants.LOCALE));
        } catch (IOException | ServletException e) {
            logger.error(e.getMessage(), e);
        } catch (ValidatorException e) {
            session.setAttribute(ERROR_MESSAGE, e.getMessage());
            logger.error(e.getMessage(), e);
            return new CommandResult(CommandResult.ResponseType.NO_ACTION, e.getMessage());
        } catch (DqlException e) {
            session.setAttribute(ERROR_MESSAGE, DqlExceptionMessageProcessor.getInstance().process(e));
            logger.error(e.getMessage(), e);
            return new CommandResult(CommandResult.ResponseType.NO_ACTION, e.getMessage());
        }
        if (!created) {
            String error = ErrorMessageConstants.BOOK_CREATION_FAILED;
            logger.error(error);
            return new CommandResult(CommandResult.ResponseType.TEXT_PLAIN, error);
        }

        return new CommandResult(CommandResult.ResponseType.NO_ACTION, UtilStringConstants.EMPTY_STRING);
    }


    /**
     * Creates new {@link Book} instance in database
     *
     * @param req current {@link RequestContext} request
     * @param locale language for error messages if ones will take place
     * @return true, if and only if book was created successfully, otherwise - false
     * @throws IOException
     * @throws ServletException
     * @throws ValidatorException
     */
    private boolean createNewBook(RequestContext req, String locale) throws IOException, ServletException, ValidatorException, DqlException {
        Book book = new Book();
        if (!BookDataProcessor.getInstance().fillBook(book, req, locale)) {
            return false;
        }

        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.create(book);

        String isbn = req.getParameter(RequestConstants.ISBN);
        Part img = req.getPart(FILE_IMG_TYPE);
        Part file = req.getPart(FILE_TYPE);
        if (Objects.nonNull(img) && Objects.nonNull(file)) {
            InputStream imgContent = img.getInputStream();
            InputStream fileContent = file.getInputStream();

            if (Objects.nonNull(imgContent) && Objects.nonNull(fileContent)) {
                service.createImage(isbn, imgContent);
                service.createBookFile(isbn, fileContent);
            } else {
                writeEmptyFileDataError(locale, req);
            }
        } else {
            writeEmptyFileDataError(locale, req);
        }
        return true;
    }


    /**
     * Writes errors to session attribute {@code erAddBookMes}
     *
     * @param locale language for error messages if ones will take place
     * @param req current {@link HttpServletRequest} request
     */
    public static void writeEmptyFileDataError(String locale, RequestContext req) {
        String error = ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMPTY_FILE_DATA);
        req.getSession().setAttribute(ERROR_MESSAGE, error);
        logger.error(error);
    }
}
