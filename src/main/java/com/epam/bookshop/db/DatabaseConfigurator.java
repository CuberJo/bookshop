package com.epam.bookshop.db;

import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Responsible for reading database settings from properties and making it available
 * for connection pool
 */
public class DatabaseConfigurator {

    private String url;
    private String dbName;
    private String serverTimezone;
    private String user;
    private String pass;
    private int poolSize;

    private static final String DATABASE_PROPERTIES_FILEPATH = "database";
    private static final String DB_URL = "db.url";
    private static final String DB_NAME = "db.name";
    private static final String DB_USER = "db.user";
    private static final String DB_PASS = "db.password";
    private static final String DB_POOL_SIZE = "db.poolSize";
    private static final String DB_SERVER_TIMEZONE = "db.serverTimezone";
    private static final String SERVER_TIMEZONE_PARAM = "?serverTimezone=";


    private static DatabaseConfigurator instance;

    private static ReentrantLock lock = new ReentrantLock();

    public static DatabaseConfigurator getInstance() {
        lock.lock();
        try {
            if (Objects.isNull(instance)) {
                instance = new DatabaseConfigurator();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }


    public void configure() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(DATABASE_PROPERTIES_FILEPATH);
        url = resourceBundle.getString(DB_URL);
        dbName = resourceBundle.getString(DB_NAME);
        serverTimezone = resourceBundle.getString(DB_SERVER_TIMEZONE);
        user = resourceBundle.getString(DB_USER);
        pass = resourceBundle.getString(DB_PASS);
        poolSize = Integer.parseInt(resourceBundle.getString(DB_POOL_SIZE));
    }

    public String getUrl() {
        return url;
    }

    public String getDbName() {
        return dbName;
    }

    public String getServerTimezone() {
        return serverTimezone;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public String getDatabaseURL() {
        return url + dbName + SERVER_TIMEZONE_PARAM + serverTimezone;
    }
}
