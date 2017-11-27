package com.breiler.contribe.service;


import com.breiler.contribe.exceptions.ItemsNotInStockException;
import com.breiler.contribe.exceptions.NotFoundException;
import com.breiler.contribe.model.Cart;
import com.breiler.contribe.model.Item;
import com.breiler.contribe.model.Order;
import com.breiler.contribe.model.Stock;
import com.breiler.contribe.repository.CartRepository;
import com.breiler.contribe.repository.OrderRepository;
import com.breiler.contribe.repository.StockRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An internal service for handling orders
 */
@Service
public class OrderService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    @Autowired
    public OrderService(CartRepository cartRepository, OrderRepository orderRepository, StockRepository stockRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.stockRepository = stockRepository;
    }

    public Order createFromCart(Long cartId) {
        Cart cart = this.cartRepository.findOne(cartId);
        if (cart == null) {
            throw new NotFoundException("Couldn't find cart with id " + cartId);
        }

        // Make sure there are enough elements in stock
        List<Item> itemsWithNotEnoughStock = getItemsWithNotEnoughStock(cart.getItems());
        if (!itemsWithNotEnoughStock.isEmpty()) {
            throw new ItemsNotInStockException(itemsWithNotEnoughStock);
        }

        List<Item> items = new ArrayList<>();
        cart.getItems()
                .forEach(cartItem -> {
                    items.add(Item.builder()
                            .book(cartItem.getBook())
                            .quantity(cartItem.getQuantity())
                            .build());

                    // Update the book stock status
                    Stock stock = cartItem.getBook().getStock();
                    stock.setQuantity(stock.getQuantity() - cartItem.getQuantity());
                    stockRepository.save(stock);
                });


        Order order = Order.builder()
                .items(items)
                .build();

        return orderRepository.save(order);
    }

    private List<Item> getItemsWithNotEnoughStock(List<Item> items) {
        return items.stream()
                .filter(cartItem -> {
                    long availableQuantity = cartItem.getBook().getStock().getQuantity();
                    return availableQuantity < cartItem.getQuantity();
                })
                .collect(Collectors.toList());
    }

    public List<Order> fetchAll() {
        return Lists.newArrayList(orderRepository.findAll());
    }

    public Order createFromItems(List<Item> items) {

        // Make sure there are enough elements in stock
        List<Item> itemsWithNotEnoughStock = getItemsWithNotEnoughStock(items);
        if (!itemsWithNotEnoughStock.isEmpty()) {
            throw new ItemsNotInStockException(itemsWithNotEnoughStock);
        }

        // Update the book stock status
        items.forEach(item -> {
            Stock stock = item.getBook().getStock();
            stock.setQuantity(stock.getQuantity() - item.getQuantity());
            stockRepository.save(stock);
        });

        Order order = Order.builder().items(items).build();
        return orderRepository.save(order);
    }
}
