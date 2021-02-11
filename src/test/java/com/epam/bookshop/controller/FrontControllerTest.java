package com.epam.bookshop.controller;

import com.epam.bookshop.constant.RequestConstants;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class FrontControllerTest {

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @Mock
    HttpSession mockHttpSession;

    Map<String,Object> attributes = new HashMap<>();

    @BeforeClass
    protected void setUp() {
        MockitoAnnotations.initMocks(this);

        when(request.getParameter(RequestConstants.LOGIN)).thenReturn("petr");
        when(request.getParameter(RequestConstants.EMAIL)).thenReturn("pert@mail.com");
        when(request.getParameter(RequestConstants.PASSWORD)).thenReturn("111");
        when(request.getParameter(RequestConstants.VERIFY_PASSWORD)).thenReturn("111");
        when(request.getParameter(RequestConstants.CHECK_PASSWORD)).thenReturn("111");

        Mockito.doAnswer((Answer<Object>) invocation -> {
            String key = (String) invocation.getArguments()[0];
            return attributes.get(key);
        }).when(mockHttpSession).getAttribute(anyString());

        Mockito.doAnswer((Answer<Object>) invocation -> {
            String key = (String) invocation.getArguments()[0];
            Object value = invocation.getArguments()[1];
            attributes.put(key, value);
            return null;
        }).when(mockHttpSession).setAttribute(anyString(), Mockito.any());

        when(mockHttpSession.getAttribute(RequestConstants.LOGIN)).thenReturn("petr");

        when(request.getSession()).thenReturn(mockHttpSession);
        when(request.getSession().getAttribute(RequestConstants.LOCALE)).thenReturn("US");
    }

    @Test
    public void testDoGet() throws IOException, ServletException, NoSuchMethodException {
        when(request.getParameter(RequestConstants.COMMAND)).thenReturn("account_settings");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(pw);

        Mockito.doAnswer((Answer<Object>) invocation -> {
            String key = (String) invocation.getArguments()[0];
            return attributes.get(key);
        }).when(request).getAttribute(anyString());

        new FrontController().doPost(request, response);
    }

    @Test
    public void testDoPost() {
    }
}