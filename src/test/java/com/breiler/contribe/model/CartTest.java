package com.breiler.contribe.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CartTest {
    @Test
    public void numberOfItemsShouldBeAdded() {
        List<Item> items = new ArrayList<>();
        items.add(Item.builder().quantity(2L).build());
        items.add(Item.builder().quantity(1L).build());

        Cart cart = Cart.builder().items(items).build();
        assertEquals(3, cart.getNumberOfItems());
    }

    @Test
    public void subTotalShouldAddThePriceFromBooks() {
        Book book = Book.builder().price(BigDecimal.valueOf(1.5)).build();

        List<Item> items = new ArrayList<>();
        items.add(Item.builder().quantity(2L).book(book).build());
        items.add(Item.builder().quantity(1L).book(book).build());

        Cart cart = Cart.builder().items(items).build();
        assertEquals(BigDecimal.valueOf(4.5), cart.getSubTotal());
    }
}
