package com.epam.bookshop.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;

/**
 * Sends an email to the given address
 */
public class MailSender {
    private static final String MAIL_USER_NAME = "mail.user.name";

    private static MailSender instance;

    private MailSender() {}

    public static MailSender getInstance() {
        if (instance == null) {
            instance = new MailSender();
        }

        return instance;
    }


    /**
     * Sends email
     *
     * @param recipient addrress of recipient
     * @param subject subject of email
     * @param response actual message to send
     * @throws MessagingException
     */
    public void send(String recipient, String subject, String response) throws MessagingException {
        Properties sessionProperties = MailPropertiesLoader.getInstance().initSessionProperties();

        Session mailSession = MailSessionCreator.getInstance().createSession(sessionProperties);

        String accountEmail = sessionProperties.getProperty(MAIL_USER_NAME);
        Message message = MimeMessagePreparer.getInstance().prepare(mailSession, accountEmail, subject, recipient, response);

        Transport.send(message);
    }
}