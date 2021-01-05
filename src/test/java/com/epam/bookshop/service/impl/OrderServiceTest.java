package com.epam.bookshop.service.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.criteria.impl.OrderCriteria;
import com.epam.bookshop.domain.impl.*;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

public class OrderServiceTest {

    EntityService service;

    @BeforeMethod
    public void setUp() {
        service = ServiceFactory.getInstance().create(EntityType.ORDER);
    }

    @Test
    public void testCreate() {
//        service.create(new Order("Kevin", "IE29 AIBK 9311 5212 3456 78", "fsa@gmail.com", LocalDateTime.now(), Status.IN_PROGRESS));
//        service.create(new Order(1L, new User(3L, "Гоша", "гоша", "123", "gosha@gmail.com", new Role("USER"), "214-2523"), LocalDateTime.now(), new Status(1L, "IN_PROGRESS"), Arrays.asList(new Book("98-12-321-5", "The book", "Hopwaa", 12, "H&H", new Genre("FANTASY"), "One day at in one city ..."))));
        try {
            service.create(new Order(1L, new User(6L, "Samanda", "samanda_", "123", "samanda_@gmail.com", new Role("USER"), Arrays.asList("214-2523")), LocalDateTime.now(), new Status(1L, "IN_PROGRESS"), Arrays.asList(new Book("98-12-321-5", "The book", "Hopwaa", 12, "H&H", new Genre("FANTASY"), "One day at in one city ..."))));
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
        Criteria<Order> criteria = OrderCriteria.builder()
//                .libraryUserName("Kevin")
//                .IBAN("IE29 AG$# 3829 1312 2321 78")
//                .email("fsa@gmail.com")
//                .orderTime(LocalDateTime.of(2020, 12, 15, 17, 07, 44))
                .statusId(1L)
                .build();

        service.findAll(criteria).forEach(System.out::println);
    }

    @Test
    public void testFindById() throws EntityNotFoundException {
        System.out.println(service.findById(1L));
    }

    @Test
    public void testFind() throws EntityNotFoundException, ValidatorException {
        Criteria<Order> criteria = OrderCriteria.builder()
//                .libraryUserId(1L)
//                .orderTime(LocalDateTime.of(2020, 12, 15, 17, 07, 44))
                .statusId(1L)
                .build();

        System.out.println(service.find(criteria));
    }

    @Test
    public void testUpdate() throws EntityNotFoundException {
        try {
            service.update(new Order(6L, new User(1L, "Erica", "erica", "123", "erica@gmail.com", new Role(1L, "USER"), Arrays.asList("13414-12")), LocalDateTime.now(), new Status(1L, "IN_PROGRESS"), Arrays.asList(new Book("98-12-321-5", "The book", "Hopwaa", 12, "H&H", new Genre("FANTASY"), "One day at in one city ..."))));
        } catch (ValidatorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete() throws EntityNotFoundException, ValidatorException {
        service.delete(new Order(7L, new User(), LocalDateTime.now(), new Status(1L, "IN_PROGRESS"), Arrays.asList(new Book("98-12-321-5", "The book", "Hopwaa", 12, "H&H", new Genre(1L, "FANTASY"), "One day at in one city ..."))));
    }

    @Test
    public void testTestDelete() throws EntityNotFoundException, ValidatorException {
        service.delete(new Order(8L, new User(), LocalDateTime.now(), new Status("IN_PROGRESS"), Arrays.asList(new Book("98-12-321-5", "The book", "Hopwaa", 12, "H&H", new Genre(1L, "FANTASY"), "One day at in one city ..."))));
    }
}