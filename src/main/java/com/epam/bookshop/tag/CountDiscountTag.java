package com.epam.bookshop.tag;

import com.epam.bookshop.domain.impl.Book;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

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
            out.println(countTotalPrice(cart) - discount);
        }
    }


    private double countTotalPrice(ArrayList<Book> cart) {
        double total = 0;

        for (int i = 0; i < cart.size(); i++) {
            total += cart.get(i).getPrice();
        }

        return total;
    }
}