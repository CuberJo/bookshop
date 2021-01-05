package com.epam.bookshop.validator;

import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.domain.impl.Role;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.ValidatorException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NamingTest {


    @Test
    public void validateUser() {
        User user = new User("agw", "age", "adga", "awgwa@gmail.com", new Role(1L, "awe"));
        try {
            Validator.getInstance().validate(user);
        } catch (ValidatorException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void validateBook() {
        Book book = new Book("111-1-11111-111-1", "rag", "ag", 121, "wqg", new Genre(1L, "Adventure"), "awegwa");
        try {
            Validator.getInstance().validate(book);
        } catch (ValidatorException e) {
            e.printStackTrace();
        }
    }
}