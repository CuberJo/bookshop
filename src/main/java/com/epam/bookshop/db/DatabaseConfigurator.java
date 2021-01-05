package com.epam.bookshop.db;

import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class DatabaseConfigurator {

//    private boolean isConfigured;

    private String url;
    private String dbName;
    private String serverTimezone;
    private String user;
    private String pass;
    private int poolSize;

    private final String DATABASE_PROPERTIES_FILEPATH = "database";
    private final String DB_URL = "db.url";
    private final String DB_NAME = "db.name";
    private final String DB_USER = "db.user";
    private final String DB_PASS = "db.password";
    private final String DB_POOL_SIZE = "db.poolSize";
    private final String DB_SERVER_TIMEZONE = "db.serverTimezone";

    public static DatabaseConfigurator instance;

    private static AtomicBoolean isCreated = new AtomicBoolean(false);

    public static DatabaseConfigurator getInstance() {
        if (!isCreated.get()) {
            synchronized (DatabaseConfigurator.class){
                if (instance == null) {
                    if (!isCreated.get()) {
                        instance = new DatabaseConfigurator();
                        isCreated.set(true);
                    }
                }
            }
        }
        return instance;
    }

    private DatabaseConfigurator() {
//        if(!isConfigured) {
//            configure();
//        }
    }

    public void configure() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(DATABASE_PROPERTIES_FILEPATH);
        url = resourceBundle.getString(DB_URL);
        dbName = resourceBundle.getString(DB_NAME);
        serverTimezone = resourceBundle.getString(DB_SERVER_TIMEZONE);
        user = resourceBundle.getString(DB_USER);
        pass = resourceBundle.getString(DB_PASS);
        poolSize = Integer.parseInt(resourceBundle.getString(DB_POOL_SIZE));

//        isConfigured = true;
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
        final String SERVER_TIMEZONE_PARAM = "?serverTimezone=";
        return url + dbName + SERVER_TIMEZONE_PARAM + serverTimezone;
    }
}
