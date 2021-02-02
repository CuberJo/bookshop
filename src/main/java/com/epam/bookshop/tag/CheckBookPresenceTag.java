package com.epam.bookshop.tag;

import com.epam.bookshop.domain.impl.Book;

import javax.servlet.jsp.tagext.TagSupport;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This tag checks whether book is presents in user library
 */
public class CheckBookPresenceTag extends TagSupport {

    private Book bookToCheck;
    private ArrayList<Book> library;
    private static final String CONTAINS = "contains";

    public void setBookToCheck(Book bookToCheck) {
        this.bookToCheck = bookToCheck;
    }

    public void setLibrary(ArrayList<Book> library) {
        this.library = library;
    }

    @Override
    public int doStartTag() {
        pageContext.setAttribute(CONTAINS, check());

        return 0;
    }


    /**
     * Checks whether book is presents in user library, if so,
     * sets {@code contains} page attribute to {@code true}
     *
     * @return true, if and only if book in present in user library
     */
    private boolean check() {
        if (Objects.isNull(library) || Objects.isNull(bookToCheck)) {
            return false;
        }

        return Objects.nonNull(library) && library.stream().anyMatch(book -> bookToCheck.equals(book));
    }
}
