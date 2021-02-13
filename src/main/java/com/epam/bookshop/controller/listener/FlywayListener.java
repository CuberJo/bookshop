package com.epam.bookshop.controller.listener;

import com.epam.bookshop.config.FlywayConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Configures database migration to the latest version for this application
 */
@WebListener
public class FlywayListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FlywayConfigurator.getInstance();
    }
}
