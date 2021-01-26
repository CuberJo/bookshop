package com.epam.bookshop.listener;

import com.epam.bookshop.util.constant.UtilStrings;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Sets default locale when session is created
 */
@WebListener
public class SessionListener implements HttpSessionListener {

//    private static final String RU = "RU";

//    @Override
//    public void sessionCreated(HttpSessionEvent se) {
//        se.getSession().setAttribute(LOCALE_ATTRIBUTE, new Locale("en", "US"));
//    }
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setAttribute(UtilStrings.LOCALE, UtilStrings.US);
    }
}
