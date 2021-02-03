package com.epam.bookshop.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Reads mail properties from file
 */
public class MailPropertiesLoader {
    private static final Logger logger = LoggerFactory.getLogger(MailPropertiesLoader.class);

    private static final String PATH_TO_MAIL_PROPERTIES_FILE = "C:\\Users\\User\\IdeaProjects\\bookshop\\src\\main\\resources\\mail.properties";

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static MailPropertiesLoader instance;

    private MailPropertiesLoader() {

    }

    public static MailPropertiesLoader getInstance() {
        LOCK.lock();
        try {
            if (instance == null) {
                instance = new MailPropertiesLoader();
            }
        } finally {
            LOCK.unlock();
        }

        return instance;
    }

    public Properties initSessionProperties() {
        Properties sessionProperties = null;

        try(Reader reader = new FileReader(PATH_TO_MAIL_PROPERTIES_FILE)) {
            sessionProperties = new Properties();
            sessionProperties.load(reader);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return sessionProperties;
    }
}
