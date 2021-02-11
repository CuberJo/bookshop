package com.epam.bookshop.controller.fliter;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class XssRequestWrapperTest {

    XssRequestWrapper wrapper;
    HttpServletRequest request;
    Method method;

    @BeforeMethod
    public void setUp() throws NoSuchMethodException {
        method = XssRequestWrapper.class.getDeclaredMethod("stripXSS", String.class);
        method.setAccessible(true);
    }

    @Test
    void stripXSSShouldReturnEmpty() throws InvocationTargetException, IllegalAccessException {
        request = mock(HttpServletRequest.class);
        wrapper = new XssRequestWrapper(request);

        String actual = (String) method.invoke(wrapper, "<script>");
        String actual2 = (String) method.invoke(wrapper, "<script>alert(‘XSS’)</script>");
        String actual3 = (String) method.invoke(wrapper, "<script>alert(document.cookie)</script>");

        System.out.println("actual = " + actual);
        System.out.println("actual2 = " + actual2);
        System.out.println("actual3 = " + actual3);
        assertEquals("", actual);
    }

    @Test
    @Parameters({"<body onload=alert(‘something’)>;",
            "<script type=”text/javascript”>var test=’../example.php?cookie_data=’+escape(document.cookie);</script>",
            "<script>destroyWebsite();</script>",})
    void stripXSSShouldReturnChanged(String input) throws InvocationTargetException, IllegalAccessException {
        request = mock(HttpServletRequest.class);
        wrapper = new XssRequestWrapper(request);

        String actual = (String) method.invoke(wrapper, input);

        System.out.println(actual);
        assertNotEquals(input, actual);
    }
}