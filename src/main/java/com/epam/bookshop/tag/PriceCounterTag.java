package com.epam.bookshop.tag;

import com.epam.bookshop.domain.impl.Book;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This tag counts total book price in cart
 */
public class PriceCounterTag extends SimpleTagSupport {

    private ArrayList<Book> cart;

    public void setCart(ArrayList<Book> cart) {
        this.cart = cart;
    }

    @Override
    public void doTag() throws IOException {
        JspWriter out = getJspContext().getOut();

        if (Objects.nonNull(cart)) {
            out.println(countTotalPrice(cart));
        }
    }


    /**
     * Counts total price
     */
    private double countTotalPrice(ArrayList<Book> cart) {
        double total = 0;

        if (Objects.nonNull(cart) && cart.stream().anyMatch(Objects::nonNull)) {
            for (int i = 0; i < cart.size(); i++) {
                total += cart.get(i).getPrice();
            }
        }

        return total;
    }
}
