package com.breiler.contribe.service;


import com.breiler.contribe.model.Cart;
import com.breiler.contribe.model.CartItem;
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
    private List<Cart> carts = new ArrayList<>();

    public Cart createCart(Cart cart) {
        cart.setId(UUID.randomUUID().toString());
        carts.add(cart);
        return cart;
    }

    public Optional<Cart> getCart(String cartId) {
        return carts.stream()
                .filter(c -> c.getId().equals(cartId)).findFirst();
    }

    public void updateCart(Cart cart) throws HttpServerErrorException {
        Optional<Cart> existingCart = getCart(cart.getId());
        if (!existingCart.isPresent()) {
            throw new HttpServerErrorException(HttpStatus.NOT_FOUND);
        }

        // Update the items in the cart
        List<CartItem> cartItems = existingCart.get().getItems();
        cartItems.clear();
        cartItems.addAll(cart.getItems());
    }
}
