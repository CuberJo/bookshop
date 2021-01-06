package com.epam.bookshop.listener;

import com.epam.bookshop.db.ConnectionPool;
import com.epam.bookshop.db.DatabaseConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DatabaseConfigurator.getInstance().configure();
        ConnectionPool.getInstance().init();

//        sce.getServletContext().setAttribute("genres", ServiceFactory.getInstance().create(EntityType.GENRE).findAll());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            ConnectionPool.getInstance().shutdown();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
