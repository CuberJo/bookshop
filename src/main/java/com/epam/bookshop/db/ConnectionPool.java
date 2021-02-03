package com.epam.bookshop.db;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Pool of connections which can be used to interact with database
 */
public class ConnectionPool {

    private static ConnectionPool instance;
    private static final ReentrantLock LOCK = new ReentrantLock();
    private final ReentrantLock lock2 = new ReentrantLock();

    private ConnectionPool() {

    }

    public static ConnectionPool getInstance() {
        LOCK.lock();
        try {
            if (instance == null) {
                instance = new ConnectionPool();
            }
        } finally {
            LOCK.unlock();
        }

        return instance;
    }

    // чтобы избежать дкиких соединений
    private BlockingQueue<ConnectionProxy> availableConnections;
    private BlockingQueue<ConnectionProxy> notAvailableConnections;

    public void setLocale(String locale) {
    }

    /**
     * Makes {@code ConnectionPool} initialization with
     * created {@link Connection} instances
     */
    public void init() {
        int poolSize = DatabaseConfigurator.getInstance().getPoolSize();

        availableConnections = new LinkedBlockingQueue<>(poolSize);
        notAvailableConnections = new LinkedBlockingQueue<>(poolSize);

        for (int i = 0; i < poolSize; i++) {
            availableConnections.add(getConnection());
        }
    }


    /**
     * Creates {@link ConnectionProxy} instance and returns it
     *
     * @return {@link ConnectionProxy} instance
     */
    private ConnectionProxy getConnection() {
        ConnectionProxy conn;

        try {
            Driver driver = new Driver();
            DriverManager.registerDriver(driver);

            conn = new ConnectionProxy(DriverManager.getConnection(DatabaseConfigurator.getInstance().getDatabaseURL(),
                    DatabaseConfigurator.getInstance().getUser(),
                    DatabaseConfigurator.getInstance().getPass()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return conn;
    }


    /**
     * Gets available {@link Connection} which is actually
     * wrapper on {@link ConnectionProxy}
     *
     * @return available {@link Connection}
     */
    public Connection getAvailableConnection() {
        lock2.lock();

        ConnectionProxy connectionToReturn = null;

        try {
            try {
                connectionToReturn = availableConnections.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notAvailableConnections.add(connectionToReturn);
        } finally {
            lock2.unlock();
        }

        System.out.println(availableConnections.size());

        return connectionToReturn;
    }


    /**
     * Used to make real close of all created connections on
     * server shutdown
     */
    public void shutdown() {

        availableConnections.forEach(connection -> {
            try {
                connection.realClose();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        notAvailableConnections.forEach(connection -> {
            try {
                connection.realClose();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }



    BlockingQueue<ConnectionProxy> getAvailableConnections() {
        return availableConnections;
    }

    BlockingQueue<ConnectionProxy> getNotAvailableConnections() {
        return notAvailableConnections;
    }
}
