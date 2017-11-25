package com.breiler.contribe.service;


import com.breiler.contribe.model.Cart;
import com.breiler.contribe.model.CartItem;
import com.breiler.contribe.repository.BookList;
import com.breiler.contribe.repository.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * An internal service for handling carts
 */
@Service
public class CartService {
    private final BookList bookList;
    private List<Cart> carts = new ArrayList<>();

    @Autowired
    private CartService(BookList bookList) {
        this.bookList = bookList;
    }

    public Cart createCart(Cart cart) {
        List<CartItem> items = removeItemsNotInInventory(cart.getItems());
        cart.setItems(items);

        cart.setId(UUID.randomUUID().toString());
        carts.add(cart);
        return cart;
    }

    public Optional<Cart> getCart(String cartId) {
        return carts.stream()
                .filter(c -> c.getId().equals(cartId)).findFirst();
    }

    public Cart updateCart(final Cart cart) throws HttpServerErrorException {
        Optional<Cart> existingCart = getCart(cart.getId());
        if (!existingCart.isPresent()) {
            throw new RuntimeException("Couldn't find the cart to update");
        }

        // Remove items that doesn't exist in the inventory
        List<CartItem> items = removeItemsNotInInventory(cart.getItems());
        existingCart.get().setItems(items);
        return existingCart.get();
    }

    private List<CartItem> removeItemsNotInInventory(List<CartItem> cartItems) {
        List<CartItem> items = new ArrayList<>(cartItems);
        items.removeIf(cartItem -> bookList.getStatus(cartItem.getBookId()) != StatusEnum.OK);
        return items;
    }
}
