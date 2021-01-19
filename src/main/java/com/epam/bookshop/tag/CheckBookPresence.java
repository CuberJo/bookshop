package com.epam.bookshop.tag;

import com.epam.bookshop.domain.impl.Book;

import javax.servlet.jsp.tagext.TagSupport;
import java.util.ArrayList;
import java.util.Objects;

public class CheckBookPresence extends TagSupport {

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


    private boolean check() {
        if (Objects.isNull(library) || Objects.isNull(bookToCheck)) {
            return false;
        }

        return Objects.nonNull(library) && library.stream().anyMatch(book -> bookToCheck.equals(book));
    }
}
