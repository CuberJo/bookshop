package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.constant.UtilStringConstants;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class AccountSettingsCommandTest {

    @Mock
    RequestContext requestContext;

    @Mock
    HttpSession mockHttpSession;

    Map<String,Object> attributes = new HashMap<>();

    @BeforeClass
    public void setUp() throws NoSuchMethodException {
        MockitoAnnotations.initMocks(this);

//        when(requestContext.getParameter("correctLogin")).thenReturn("petr");
//        when(requestContext.getParameter("incorrectLogin")).thenReturn(";'--321sdaff");
//        when(requestContext.getParameter("emptyLogin")).thenReturn("");
//        when(requestContext.getParameter("cyrillicLogin")).thenReturn("Андрей");
//
//        when(requestContext.getParameter("correctEmail")).thenReturn("pert@mail.com");
//        when(requestContext.getParameter("incorrectEmail")).thenReturn("pert@mailcom");
//        when(requestContext.getParameter("emptyEmail")).thenReturn("");
//        when(requestContext.getParameter("cyrillicEmail")).thenReturn("Андрей");
//
//        when(requestContext.getParameter("correctPass")).thenReturn("111");
//        when(requestContext.getParameter("incorrectPass")).thenReturn(";'--321sdaff");
//        when(requestContext.getParameter("emptyPass")).thenReturn("");
//
//        when(requestContext.getParameter("verifyPass")).thenReturn("111");
//        when(requestContext.getParameter("incorrectVerifyPass")).thenReturn(";'--321sdaff");
//        when(requestContext.getParameter("emptyVerifyPass")).thenReturn("");
//
//        when(requestContext.getParameter("correctCheckPass")).thenReturn("111");
//        when(requestContext.getParameter("incorrectCheckPass")).thenReturn(";'--321sdaff");
//        when(requestContext.getParameter("emptyCheckPass")).thenReturn("");

        when(requestContext.getParameter(RequestConstants.LOCALE)).thenReturn("US");

//expected, actual
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

        when(requestContext.getSession()).thenReturn(mockHttpSession);
        when(requestContext.getSession().getAttribute(RequestConstants.LOCALE)).thenReturn("US");

//        method = BooksCommand.class.getDeclaredMethod("getBooksByGenre", String.class, int.class, int.class, String.class);
//        method.setAccessible(true);
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test
    public void testExecuteWithCorrectData() {
        when(requestContext.getParameter(RequestConstants.LOGIN)).thenReturn("petr");
        when(requestContext.getParameter(RequestConstants.EMAIL)).thenReturn("pert@mail.com");
        when(requestContext.getParameter(RequestConstants.PASSWORD)).thenReturn("111");
        when(requestContext.getParameter(RequestConstants.VERIFY_PASSWORD)).thenReturn("111");
        when(requestContext.getParameter(RequestConstants.CHECK_PASSWORD)).thenReturn("111");

        new AccountSettingsCommand().execute(requestContext);
    }

    @Test
    public void testExecuteWithInCorrectLogin() {
        when(requestContext.getParameter(RequestConstants.LOGIN)).thenReturn(";'--321sdaff");
        when(requestContext.getParameter(RequestConstants.EMAIL)).thenReturn("pert@mail.com");
        when(requestContext.getParameter(RequestConstants.PASSWORD)).thenReturn("111");
        when(requestContext.getParameter(RequestConstants.VERIFY_PASSWORD)).thenReturn("111");
        when(requestContext.getParameter(RequestConstants.CHECK_PASSWORD)).thenReturn("111");

        new AccountSettingsCommand().execute(requestContext);

        System.out.println(mockHttpSession.getAttribute("error_acc_settings"));
    }

    @Test
    public void testExecuteWithInCorrectEmail() {
        when(requestContext.getParameter(RequestConstants.LOGIN)).thenReturn("petr");
        when(requestContext.getParameter(RequestConstants.EMAIL)).thenReturn("pert@mailcom");
        when(requestContext.getParameter(RequestConstants.PASSWORD)).thenReturn("111");
        when(requestContext.getParameter(RequestConstants.VERIFY_PASSWORD)).thenReturn("111");
        when(requestContext.getParameter(RequestConstants.CHECK_PASSWORD)).thenReturn("111");

        new AccountSettingsCommand().execute(requestContext);

        System.out.println(mockHttpSession.getAttribute("error_acc_settings"));
    }

    @Test
    public void testExecuteWithInCorrectCheckPass() {
        when(requestContext.getParameter(RequestConstants.LOGIN)).thenReturn("petr");
        when(requestContext.getParameter(RequestConstants.EMAIL)).thenReturn("pert@mailcom");
        when(requestContext.getParameter(RequestConstants.PASSWORD)).thenReturn("111");
        when(requestContext.getParameter(RequestConstants.VERIFY_PASSWORD)).thenReturn("111");
        when(requestContext.getParameter(RequestConstants.CHECK_PASSWORD)).thenReturn("323");

        System.out.println("Result: " + new AccountSettingsCommand().execute(requestContext).getResp());

        System.out.println(mockHttpSession.getAttribute("error_acc_settings"));
    }

    @Test
    public void testExecuteWithPassesNotEqual() {
        when(requestContext.getParameter(RequestConstants.LOGIN)).thenReturn("petr");
        when(requestContext.getParameter(RequestConstants.EMAIL)).thenReturn("pert@mailcom");
        when(requestContext.getParameter(RequestConstants.PASSWORD)).thenReturn("111");
        when(requestContext.getParameter(RequestConstants.VERIFY_PASSWORD)).thenReturn("333");
        when(requestContext.getParameter(RequestConstants.CHECK_PASSWORD)).thenReturn("111");

        new AccountSettingsCommand().execute(requestContext);

        System.out.println(mockHttpSession.getAttribute("error_acc_settings"));
    }
}