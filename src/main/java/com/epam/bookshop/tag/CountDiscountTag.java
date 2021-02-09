package com.epam.bookshop.tag;

import com.epam.bookshop.domain.impl.Book;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This tag counts discount for book
 */
public class CountDiscountTag extends SimpleTagSupport {

    private double discount;
    private ArrayList<Book> cart;

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setCart(ArrayList<Book> cart) {
        this.cart = cart;
    }

    @Override
    public void doTag() throws IOException {
        JspWriter out = getJspContext().getOut();

        if (Objects.nonNull(discount)) {
            double totalPrice = PriceCounterTag.countTotalPrice(cart);
            out.println(new BigDecimal(String.valueOf(totalPrice)).subtract(new BigDecimal(String.valueOf(discount))));
        }
    }
}