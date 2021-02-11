package com.epam.bookshop.command.impl.action;

import com.epam.bookshop.command.RequestContext;
import com.epam.bookshop.constant.RequestConstants;
import com.epam.bookshop.domain.impl.Book;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class BooksCommandTest {

    @Mock
    RequestContext requestContext;

    @Mock
    HttpSession mockHttpSession;

    Map<String,Object> attributes = new HashMap<>();
    Method method;

    @BeforeClass
    public void init() throws NoSuchMethodException {
        MockitoAnnotations.initMocks(this);

        when(requestContext.getParameter("presentGenre")).thenReturn("ROMANCE");
        when(requestContext.getParameter("notPresentGenre")).thenReturn("");


        Mockito.doAnswer((Answer<Object>) invocation -> {
            String key = (String) invocation.getArguments()[0];
            return attributes.get(key);
        }).when(mockHttpSession).getAttribute(anyString());

        when(requestContext.getSession()).thenReturn(mockHttpSession);
        when(requestContext.getSession().getAttribute(RequestConstants.LOCALE)).thenReturn("US");

        method = BooksCommand.class.getDeclaredMethod("getBooksByGenre", String.class, int.class, int.class, String.class);
        method.setAccessible(true);
    }

    @Test
    public void testExecute() throws InvocationTargetException, IllegalAccessException {
        Collection<Book> actualBooks = (Collection<Book>) method.invoke(new BooksCommand(), requestContext.getParameter("presentGenre"),
                1, 8, requestContext.getSession().getAttribute(RequestConstants.LOCALE));

        System.out.println("actualBooks = " + actualBooks);
    }

    @Test
    public void testExecuteWhenNoGenreFound() throws InvocationTargetException, IllegalAccessException {
        Collection<Book> actualBooks = (Collection<Book>) method.invoke(new BooksCommand(), requestContext.getParameter("notPresentGenre"),
                1, 8, requestContext.getSession().getAttribute(RequestConstants.LOCALE));

        System.out.println("actualBooks = " + actualBooks);
    }
}