package com.epam.bookshop.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MessagePreparer {

    private static final String CONTENT_TYPE = "text/html";

    private static MessagePreparer instance;

    private MessagePreparer() {

    }

    public static MessagePreparer getInstance() {
        if (instance == null) {
            instance = new MessagePreparer();
        }

        return instance;
    }

    public MimeMessage prepare(Session session, String accountEmail, String subject, String recipient, String content) {

        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(accountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setContent(content, CONTENT_TYPE);

        } catch (MessagingException e) {
            e.printStackTrace();
        }


        return message;
    }
}
