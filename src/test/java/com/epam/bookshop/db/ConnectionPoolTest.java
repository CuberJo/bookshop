package com.epam.bookshop.db;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ConnectionPoolTest {

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test
    public void testGetAvailableConnection() {
        ConnectionPool.getInstance().getAvailableConnection();
    }

    @Test
    public void testShutdown() {
        ConnectionPool.getInstance().shutdown();
    }

    @Test
    public void testGetAvailableConnections() {
    }

    @Test
    public void testGetNotAvailableConnections() {
    }
}