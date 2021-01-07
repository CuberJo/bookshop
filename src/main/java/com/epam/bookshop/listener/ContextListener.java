package com.epam.bookshop.listener;

import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.db.DatabaseConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DatabaseConfigurator.getInstance().configure();
        ConnectionPool.getInstance().init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.getInstance().shutdown();
    }
}
