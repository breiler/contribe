package com.breiler.contribe.service;


import com.breiler.contribe.model.Book;
import com.breiler.contribe.model.Cart;
import com.breiler.contribe.model.Item;
import com.breiler.contribe.repository.BookRepository;
import com.breiler.contribe.repository.CartRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An internal service for handling carts
 */
@Service
public class CartService {
    private final BookRepository bookRepository;
    private final CartRepository cartRepository;

    @Autowired
    public CartService(BookRepository bookRepository, CartRepository cartRepository) {
        this.bookRepository = bookRepository;
        this.cartRepository = cartRepository;
    }

    public Cart create() {
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        cartRepository.save(cart);
        return cart;
    }

    public Optional<Cart> fetch(final Long cartId) {
        return Optional.ofNullable(cartRepository.findOne(cartId));
    }

    public Optional<Cart> addBookToCart(final Long cartId, final Long bookId, Long quantity) {
        Cart cart = cartRepository.findOne(cartId);
        Book book = bookRepository.findOne(bookId);
        if (cart == null || book == null) {
            return Optional.empty();
        }

        if (quantity == 0) {
            cart.getItems().removeIf(i -> i.getBook().getId().equals(bookId));
        } else {
            // Find an existing item in cart
            Optional<Item> itemToBeUpdated = cart
                    .getItems()
                    .stream()
                    .filter(i -> i.getBook().getId().equals(bookId))
                    .findFirst();

            // Add one if it didn't exist
            if (!itemToBeUpdated.isPresent()) {
                Item newItem = Item.builder()
                        .book(book)
                        .build();

                cart.getItems().add(newItem);
                itemToBeUpdated = Optional.of(newItem);
            }

            // Set the quantity
            itemToBeUpdated.get().setQuantity(quantity);
        }

        cartRepository.save(cart);
        return Optional.of(cart);
    }

    public void delete(Long id) {
        cartRepository.delete(id);
    }

    public List<Cart> fetchAll() {
        return Lists.newArrayList(cartRepository.findAll());
    }
}
