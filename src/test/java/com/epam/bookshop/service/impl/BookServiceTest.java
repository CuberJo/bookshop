package com.epam.bookshop.service.impl;

import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.BookCriteria;
import com.epam.bookshop.domain.impl.Book;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.Genre;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BookServiceTest {

    EntityService service;

    @BeforeMethod
    public void setUp() {
        service = ServiceFactory.getInstance().create(EntityType.BOOK);
    }

    @Test
    public void testCreate() {
        try {
            service.create(new Book("99-12-321-5", "The book", "Hopwaa", 12, "H&H", new Genre(1L, "FANTASY"), "One day at in one city ..."));
        } catch (ValidatorException e) {
            e.printStackTrace();
        }

//        BookService.INSTANCE.createImage(new Book("978-5-389-1", "Опережая некролог", "J. Miller", 12.3, "F&F", Genre.ADVENTURE));
    }

    @Test
    public void testCreateWithId() {
        try {
            service.create(new Book(3l, "98-65621-5", "The book", "Hopwaa", 12, "H&H", new Genre("FANTASY"), "One day at in one city ..."));
        } catch (ValidatorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateWithIntegerPtice() {
        try {
            service.create(new Book("978-6-389-1", "War and peace", "Tolstoy", 12, "F&F", new Genre("FANTASY"), "One day at in one city ..."));
        } catch (ValidatorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateWithDuplicateISBN() {
        try {
            service.create(new Book("978-6-389-1", "War and peace", "Tolstoy", 12, "F&F", new Genre("FANTASY"), "One day at in one city ..."));
        } catch (ValidatorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindAll() {
        service.findAll().forEach(System.out::println);
    }

    @Test
    public void testTestFindAll() throws ValidatorException {
        Criteria<Book> criteria = BookCriteria.builder()
//                .id(1L)
//                .ISBN("978-6-389-1")
//                .title("War and peace")
//                .author("Tolsoy")
//                .price(12.0)
                .publisher("F&F")
//                .genreId(1L)
                .build();

        service.findAll(criteria).forEach(System.out::println);
    }


    @Test
    public void testFindAllWithInjection() throws ValidatorException {
        BookCriteria criteria = BookCriteria.builder().author("--").build();
        service.findAll(criteria).forEach(System.out::println);
    }

    @Test
    public void testFindById() throws EntityNotFoundException {
        System.out.println(service.findById(2L));
    }

    @Test
    public void testFind() throws EntityNotFoundException, ValidatorException {
        Criteria<Book> criteria = BookCriteria.builder()
//                .id(1L)
//                .ISBN("978-6-389-1")
//                .title("War and peace")
//                .author("Tolsoy")
                .price(12.0)
//                .publisher("F&F")
                .genreId(1L)
                .build();

        System.out.println(service.find(criteria));
    }

    @Test
    public void testUpdate() throws EntityNotFoundException {
        try {
            service.update(new Book(2L,"978-6-389-2", "War and peace", "Tolstoy", 12, "F&F", new Genre(1L,"FANTASY"), "One day at in one city ..."));
        } catch (ValidatorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete() throws EntityNotFoundException, ValidatorException {
        service.delete(new Book(10L, "98-65621-5", "The book", "Hopwaa", 12, "H&H", new Genre("FANTASY"), "One day at in one city ..."));
    }

    @Test
    public void testTestDelete() throws EntityNotFoundException, ValidatorException {
        service.delete(new Book(11l, "98-65621-5", "The book", "Hopwaa", 12, "H&H", new Genre("FANTASY"), "One day at in one city ..."));
    }
}