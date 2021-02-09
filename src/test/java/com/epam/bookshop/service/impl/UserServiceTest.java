package com.epam.bookshop.service.impl;

import com.epam.bookshop.exception.DqlException;
import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.util.criteria.impl.UserCriteria;
import com.epam.bookshop.domain.impl.EntityType;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.exception.EntityNotFoundException;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.service.EntityService;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserServiceTest {

    EntityService service;

    @BeforeMethod
    public void setUp() {
        service = ServiceFactory.getInstance().create(EntityType.USER);
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test
    public void testCreate() {
        User user = new User("Solomon", "solomon", "123", "solomon@gmail.com", false);//, Arrays.asList("214-2523"));
        try {
            service.create(user);
        } catch (ValidatorException | DqlException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateWithRussianLang() {
        try {
            service.create(new User("Ирина", "ирина", "123", " irina@gmail.com", false));//, Arrays.asList("214-2523")));
        } catch (ValidatorException | DqlException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindAll() {
        service.findAll().stream()
        .forEach(System.out::println);
    }

    @Test
    public void testFindAllByCriteria() throws ValidatorException {
        Criteria<User> criteria = UserCriteria.builder()
//                .id(1L)
                .name("Erica")
//                .login("erica")
//                .email("erica@gmail.com")
                .build();

        service.findAll(criteria).stream()
                .forEach(System.out::println);
    }

    @Test
    public void testFindOneByCriteria() throws EntityNotFoundException, ValidatorException {
        Criteria<User> criteria = UserCriteria.builder()
//                .id(1L)
                .name("Samanda")
//                .login("erica")
//                .email("erica@gmail.com")
                .build();

        System.out.println(service.find(criteria));
    }

    @Test
    public void testFindById() throws EntityNotFoundException {
        System.out.println(service.findById(6));
    }


    @Test
    public void testUpdate() throws EntityNotFoundException {
        try {
            service.update(new User(2L, "Петр", "петр", "123", "petr00@gmail.com", false));//, Arrays.asList("214-2523")));
        } catch (ValidatorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteById() throws EntityNotFoundException {
        service.delete(15);
    }

    @Test
    public void testTestDeleteByEntity() throws EntityNotFoundException, ValidatorException {
        service.delete(new User(17L, "Samanda", "samanda_", "123", "samanda_@gmail.com", false));//, Arrays.asList("214-2523")));
    }
}