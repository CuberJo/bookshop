package com.epam.bookshop.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class MailSessionCreator {

    private static final String MAIL_USER_NAME = "mail.user.name";
    private static final String MAIL_USER_PASSWORD = "mail.user.password";

    private static MailSessionCreator instance;

    private MailSessionCreator() {

    }

    public static MailSessionCreator getInstance() {
        if (instance == null) {
            instance = new MailSessionCreator();
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



