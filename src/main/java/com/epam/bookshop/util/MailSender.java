package com.epam.bookshop.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;

public class MailSender {

    private static final String MAIL_USER_NAME = "mail.user.name";

    private static MailSender instance;

    private MailSender() {

    }

    public static MailSender getInstance() {
        if (instance == null) {
            instance = new MailSender();
        }

        return instance;
    }


    public void send(String recipient, String subject, String response) throws MessagingException {

        Properties sessionProperties = PropertiesLoader.getInstance().initSessionProperties();

        Session mailSession = SessionCreator.getInstance().createSession(sessionProperties);

        String accountEmail = sessionProperties.getProperty(MAIL_USER_NAME);
        Message message = MessagePreparer.getInstance().prepare(mailSession, accountEmail, subject, recipient, response);

        Transport.send(message);
    }
}
//        final String accountEmail = "xxxxxx@xxxx.com";
//        final String pass = "xxxxx";
