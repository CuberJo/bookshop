package com.epam.bookshop.config;

import org.flywaydb.core.Flyway;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Responsible for configuring {@code Flyway} instance
 */
public class FlywayConfigurator {

    private static FlywayConfigurator instance;
    private static final ReentrantLock lock = new ReentrantLock();

    private FlywayConfigurator() {
        configure();
    }

    public static FlywayConfigurator getInstance() {
        lock.lock();
        try {
            if (Objects.isNull(instance)) {
                instance = new FlywayConfigurator();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    /**
     * Configures {@code Flyway instance with needed arguments}
     */
    private void configure() {
        DatabaseConfigurator configurator = DatabaseConfigurator.getInstance();
        Flyway flyway = Flyway.configure().dataSource(
                configurator.getDatabaseURL(),
                configurator.getUser(),
                configurator.getPass()
        ).load();
        flyway.baseline();
        flyway.migrate();
    }
}
