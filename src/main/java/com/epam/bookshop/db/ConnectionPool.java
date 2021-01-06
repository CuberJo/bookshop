package com.epam.bookshop.db;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionPool {

    private String locale = "EN";

    private static ConnectionPool instance;

    private BlockingQueue<Connection> availableConnections;
    private BlockingQueue<Connection> notAvailableConnections;

    private static AtomicBoolean isCreated = new AtomicBoolean(false);

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public static ConnectionPool getInstance() {
        if (!isCreated.get()) {
            synchronized (ConnectionPool.class){
                if (instance == null) {
                    if (!isCreated.get()) {
                        instance = new ConnectionPool();
                        isCreated.set(true);
                    }
                }
            }
        }
        return instance;
    }

    private ConnectionPool() {

    }

    public void init() {
        int poolSize = DatabaseConfigurator.getInstance().getPoolSize();

        availableConnections = new LinkedBlockingQueue<>(poolSize);
        notAvailableConnections = new LinkedBlockingQueue<>(poolSize);

        for (int i = 0; i < poolSize; i++) {
            availableConnections.add(getConnection());
        }
    }

    private Connection getConnection() {
        Connection conn;

        try {
            Driver driver = new Driver();
            DriverManager.registerDriver(driver);

            conn = new ConnectionProxy(DriverManager.getConnection(DatabaseConfigurator.getInstance().getDatabaseURL(),
                    DatabaseConfigurator.getInstance().getUser(),
                    DatabaseConfigurator.getInstance().getPass()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return conn;
    }

    public synchronized Connection getAvailableConnection() {
//        int millisecondInSleep = 0;
//        final int LIMIT_MILLISECONDS = 1000;
//
//        while (availableConnections.isEmpty() && millisecondInSleep < LIMIT_MILLISECONDS) {
//            if (millisecondInSleep >= LIMIT_MILLISECONDS) {
//                final Naming errorMessage = "Time limit of " + LIMIT_MILLISECONDS + " milliseconds reached";
//                logger.error(errorMessage);
//                throw new RuntimeException(errorMessage);
//            }
//            try {
//                TimeUnit.MILLISECONDS.sleep(100);
//                ++millisecondInSleep;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        Connection connectionToReturn = null;
        try {
            connectionToReturn = availableConnections.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notAvailableConnections.add(connectionToReturn);

        return connectionToReturn;
    }

    public void shutdown() throws SQLException {

        availableConnections.forEach(connection -> {
            try {
                ((ConnectionProxy) connection).realClose();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        notAvailableConnections.forEach(connection -> {
            try {
                ((ConnectionProxy) connection).realClose();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    /**
     * Needs to be package private access
     * @return
     */
    BlockingQueue<Connection> getAvailableConnections() {
        return availableConnections;
    }

    BlockingQueue<Connection> getNotAvailableConnections() {
        return notAvailableConnections;
    }
}
