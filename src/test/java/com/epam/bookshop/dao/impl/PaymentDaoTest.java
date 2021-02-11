package com.epam.bookshop.dao.impl;

import com.epam.bookshop.db.ConnectionPool;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class PaymentDaoTest {

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test
    public void testCreate() {
    }

    @Test
    public void testFindAll() {
        System.out.println(new PaymentDao(ConnectionPool.getInstance().getAvailableConnection()).findAll(1, 4));
    }

    @Test
    public void testFindById() {
    }

    @Test
    public void testTestFindAll() {
    }

    @Test
    public void testFind() {
    }

    @Test
    public void testDelete() {
    }

    @Test
    public void testTestDelete() {
    }

    @Test
    public void testUpdate() {
    }

    @Test
    public void testFindAllBooksInPayment() {
    }

    @Test
    public void testCount() {
    }

    @Test
    public void testTestFindAll1() {
    }
}