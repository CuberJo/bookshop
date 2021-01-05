package com.epam.bookshop.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class PropertiesLoader {

    private static final String PATH_TO_MAIL_PROPERTIES_FILE = "C:\\Users\\User\\IdeaProjects\\bookshop\\src\\main\\resources\\mail.properties";

    private static PropertiesLoader instance;

    private PropertiesLoader() {

    }

    public static PropertiesLoader getInstance() {
        if (instance == null) {
            instance = new PropertiesLoader();
        }

        return instance;
    }

    public Properties initSessionProperties() {
        Properties sessionProperties = null;

        try(Reader reader = new FileReader(PATH_TO_MAIL_PROPERTIES_FILE)) {
            sessionProperties = new Properties();
            sessionProperties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sessionProperties;
    }
}
