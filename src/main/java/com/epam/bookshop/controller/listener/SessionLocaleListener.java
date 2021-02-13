package com.epam.bookshop.controller.listener;

import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Sets default locale when session is created
 */
@WebListener
public class SessionLocaleListener implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setAttribute(RequestConstants.LOCALE, UtilStringConstants.US);
    }
}
