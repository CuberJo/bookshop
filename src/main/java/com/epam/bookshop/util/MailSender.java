//package org.mycompany.bookshop.util;
//
//import RequestContext;
//
//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Objects;
//import java.util.Properties;
//
//public class MailSender {
//
//    private static final String CONTENT_TYPE = "text/html";
//    private static final String EMAIL = "email";
//    private static final String RESET_PASSWORD = "reset_password";
//    private static final String REGISTER_USER = "register_user";
//    private static final String PASSWORD_RESET_SUBJECT = "Password reset";
//    private static final String REGISTER_USER_SUBJECT = "Password reset";
//    private static final String REGISTER_RESPONSE = "You have successfully registered";
//
//    private static final String MAIL_USER_NAME = "mail.user.name";
//    private static final String MAIL_USER_PASSWORD = "mail.user.password";
//
//    final String mailPropertiesFile = "resources/mail.properties";
//
//    private static MailSender instance;
//
//    public static MailSender getInstance() {
//        if (instance == null) {
//            instance = new MailSender();
//        }
//
//        return instance;
//    }
//
//    public void send(RequestContext req) throws IOException, MessagingException {
//
//        Properties properties = new Properties();
//
//        properties.load(MailSender.class.getResourceAsStream(mailPropertiesFile));
//
//        Session mailSession = (new SessionCreator(properties)).createSession();
//        mailSession.setDebug(true);
//
//
//        message = new MimeMessage(mailSession);
//
//        message.setSubject(mailSubject);
//        message.setContent(mailText, CONTENT_TYPE);
//        message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendToEmail));
//
//        Transport.send(message);
//
//
//        MailThread mailOperator;
//        if (Objects.nonNull(req.getParameter(RESET_PASSWORD))) {
//            mailOperator = new MailThread(req.getParameter(EMAIL), req.getParameter(PASSWORD_RESET_SUBJECT), req.getParameter("body"), properties);
//        } else if (Objects.nonNull(req.getParameter(REGISTER_USER))) {
//            mailOperator = new MailThread(req.getParameter(EMAIL), req.getParameter(REGISTER_USER_SUBJECT), req.getParameter(REGISTER_RESPONSE), properties);
//        } else {
//            throw new RuntimeException();
//        }
//
//        sendMail("");
//    }
//
//    private static void sendMail(String recipient) {
////        final String accountEmail = "xxxxxx@xxxx.com";
////        final String pass = "xxxxx";
//
//        FileReader reader;
//        Properties properties = null;
//        try {
//            reader = new FileReader("C:\\Users\\User\\IdeaProjects\\bookshop\\src\\main\\resources\\mail.properties");
//            properties = new Properties();
//            properties.load(reader);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        final String accountEmail = properties.getProperty(MAIL_USER_NAME);
//        final String pass = properties.getProperty(MAIL_USER_PASSWORD);
//
//
//        Session session = Session.getInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(accountEmail, pass);
//            }
//        });
//
//        Message message = prepareMessage(session, accountEmail, recipient);
//
//        try {
//            Transport.send(message);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private static Message prepareMessage(Session session, String accountEmail, String recipient) {
//
//        MimeMessage mimeMessage = new MimeMessage(session);
//
//        try {
//            mimeMessage.setFrom(new InternetAddress(accountEmail));
//            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
//            mimeMessage.setSubject("Hi from second Java app");
//            String htmlContent = "<h1>Hello, User<h1>";
//            final String type = "text/html";
//            mimeMessage.setContent(htmlContent, type);
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//
//
//        return mimeMessage;
//    }
//
//}
