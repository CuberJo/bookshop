package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.Command;
import com.epam.bookshop.command.CommandResult;
import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.impl.BookService;
import com.epam.bookshop.service.impl.ServiceFactory;
import com.epam.bookshop.util.BookDataProcessor;
import com.epam.bookshop.util.EntityFinderFacade;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Updates {@link Book} data in database by incoming parameters
 */
public class UpdateBookCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(UpdateBookCommand.class);

    private static final String FILE_TYPE = "file";
    private static final String OLD_ISBN = "oldISBN";


    @Override
    public CommandResult execute(RequestContext requestContext) {
        boolean updated = false;
        try {
            updated = updateBook(requestContext, (String) requestContext.getSession().getAttribute(RequestConstants.LOCALE));
        } catch (IOException | ServletException e) {
            logger.error(e.getMessage(), e);
        } catch (ValidatorException e) {
            logger.error(e.getMessage(), e);
            return new CommandResult(CommandResult.ResponseType.NO_ACTION, e.getMessage());
        }
        if (!updated) {
            String error = ErrorMessageConstants.NO_BOOK_UPDATE_OCCURRED;
            logger.error(error);
            return new CommandResult(CommandResult.ResponseType.TEXT_PLAIN, error);
        }

        return new CommandResult(CommandResult.ResponseType.NO_ACTION, UtilStringConstants.EMPTY_STRING);
    }


    /**
     * Updates {@link Book} instance with passed by client params
     *
     * @param req current {@link RequestContext} request
     * @param locale language for error messages if ones will take place
     * @return true, if and only if book was updated successfully, otherwise - false
     * @throws IOException
     * @throws ServletException
     * @throws ValidatorException
     */
    private boolean updateBook(RequestContext req, String locale) throws IOException, ServletException, ValidatorException {
        String oldISBN = req.getParameter(OLD_ISBN);
        String isbn = req.getParameter(RequestConstants.ISBN);

        Book bookToUpdate = EntityFinderFacade.getInstance().find(BookCriteria.builder().ISBN(oldISBN).build(), locale, logger);
        if(!BookDataProcessor.getInstance().fillBook(bookToUpdate, req, locale)) {
            return false;
        }

        BookService service = (BookService) ServiceFactory.getInstance().create(EntityType.BOOK);
        service.update(bookToUpdate);

        Part filePart = req.getPart(FILE_TYPE);
        if (Objects.nonNull(filePart)) {
            InputStream fileContent = filePart.getInputStream();

            if (Objects.nonNull(fileContent)) {
                if (Objects.nonNull(isbn) && !isbn.isEmpty()) {
                    service.updateImage(isbn, fileContent);
                } else {
                    service.updateImage(oldISBN, fileContent);
                }
            } else {
                AddNewBookCommand.writeEmptyFileDataError(locale, req);
            }
        } else {
            AddNewBookCommand.writeEmptyFileDataError(locale, req);
        }

        return true;
    }
}
