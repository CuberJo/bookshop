package com.epam.bookshop.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Prepares MIME messages
 */
public class MimeMessagePreparer {
    private static final Logger logger = LoggerFactory.getLogger(MimeMessagePreparer.class);

    private static final String CONTENT_TYPE = "text/html";

    private static MimeMessagePreparer instance;

    private MimeMessagePreparer() {}

    public static MimeMessagePreparer getInstance() {
        if (instance == null) {
            instance = new MimeMessagePreparer();
        }

        return instance;
    }


    /**
     * Prepares MIME message by filling the email content
     *
     * @param session mail session instance
     * @param accountEmail from where email is sent
     * @param subject subject of email
     * @param recipient who is going to receive this email
     * @param content actual message content
     * @return created MIME message
     */
    public MimeMessage prepare(Session session, String accountEmail, String subject, String recipient, String content) {
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(accountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setContent(content, CONTENT_TYPE);

        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
        }

        return message;
    }
}
