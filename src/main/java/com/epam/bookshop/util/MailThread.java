package com.epam.bookshop.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailThread extends Thread {

    private static final String CONTENT_TYPE = "text/html";

    private MimeMessage message;
    private String sendToEmail;
    private String mailSubject;
    private String mailText;
    private Properties properties;


    public MailThread(String sendToEmail, String mailSubject, String mailText, Properties properties) {
        this.sendToEmail = sendToEmail;
        this.mailSubject = mailSubject;
        this.mailText = mailText;
        this.properties = properties;
    }


    public void run() {
        try {
            init();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            Transport.send(message);
        } catch (MessagingException e) {
//            throw new RuntimeException()
            System.err.print("Ошибка при отправлении сообщения" + e);
            // in log file
        }
    }


    private void init() throws MessagingException {

        Session mailSession = (new SessionCreator(properties)).createSession();
        mailSession.setDebug(true);


        message = new MimeMessage(mailSession);

        message.setSubject(mailSubject);
        message.setContent(mailText, CONTENT_TYPE);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendToEmail));

    }
}