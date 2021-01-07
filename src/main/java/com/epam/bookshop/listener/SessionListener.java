package com.epam.bookshop.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Locale;

@WebListener
public class SessionListener implements HttpSessionListener {

    private static final String LOCALE_ATTRIBUTE = "locale";
    private static final String US = "US";
    private static final String RU = "RU";

    @Override
//    public void sessionCreated(HttpSessionEvent se) {
//        se.getSession().setAttribute(LOCALE_ATTRIBUTE, new Locale("en", "US"));
//    }
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setAttribute(LOCALE_ATTRIBUTE, US);
    }
}
