package com.epam.bookshop.config;

import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Configures application with needed data fetched form properties file
 */
public class AppConfigurator {

    private String googleClientId;
    private String reCaptchaSecretKey;

    private static final String APPLICATION_PROPERTIES_FILEPATH = "application";
    private static final String APP_G_CLIENT_ID = "app.google_client_id";
    private static final String RECAPTCHA_SECRET_KEY = "app.recaptcha_secret_key";

    private static volatile AppConfigurator instance;
    private static final ReentrantLock lock = new ReentrantLock();

    private AppConfigurator() {configure();}

    public static AppConfigurator getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new AppConfigurator();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    /**
     * Loads application properties from file
     */
    private void configure() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(APPLICATION_PROPERTIES_FILEPATH);
        googleClientId = resourceBundle.getString(APP_G_CLIENT_ID);
        reCaptchaSecretKey = resourceBundle.getString(RECAPTCHA_SECRET_KEY);
    }

    public String getGoogleClientId() {
        return googleClientId;
    }

    public String getReCaptchaSecretKey() {
        return reCaptchaSecretKey;
    }
}