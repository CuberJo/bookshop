package com.epam.bookshop.mail;

import org.testng.annotations.Test;

import javax.mail.MessagingException;

public class MailSenderTest {

    @Test
    public void testSendRussian() throws MessagingException {
        MailSender.getInstance().send("*********@gmail.com", "приветик", "русские символы");
    }
}