package com.epam.bookshop.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class SessionCreator {

    private static final String MAIL_USER_NAME = "mail.user.name";
    private static final String MAIL_USER_PASSWORD = "mail.user.password";

    private static SessionCreator instance;

    private SessionCreator() {

    }

    public static SessionCreator getInstance() {
        if (instance == null) {
            instance = new SessionCreator();
        }

        return instance;
    }


    public Session createSession(Properties sessionProperties) {

        String userName = sessionProperties.getProperty(MAIL_USER_NAME);
        String userPassword = sessionProperties.getProperty(MAIL_USER_PASSWORD);

        return Session.getDefaultInstance(sessionProperties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, userPassword);
                    }
                });
    }
}



