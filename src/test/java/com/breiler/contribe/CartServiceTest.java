package com.breiler.contribe;

import com.breiler.contribe.model.Book;
import com.breiler.contribe.model.Cart;
import com.breiler.contribe.model.Item;
import com.breiler.contribe.repository.BookRepository;
import com.breiler.contribe.repository.CartRepository;
import com.breiler.contribe.service.CartService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartRepository cartRepository;

    private CartService cartService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.cartService = new CartService(this.bookRepository, this.cartRepository);
    }

    @Test
    public void creatingCartsShouldHaveAnEmptyListOfItems() {
        // Given
        Cart cart = new Cart();
        ArgumentCaptor<Cart> argumentCaptor = ArgumentCaptor.forClass(Cart.class);
        when(cartRepository.save(argumentCaptor.capture())).thenReturn(cart);

        // When
        this.cartService.create();

        // Then
        verify(cartRepository, times(1)).save(any(Cart.class));
        assertNotNull(argumentCaptor.getValue().getItems());
        assertEquals(0, argumentCaptor.getValue().getItems().size());
    }

    @Test
    public void fetchingCartThatDoesNotExistShouldReturnEmptyOptional() {
        // Given
        when(cartRepository.findOne(anyLong())).thenReturn(null);

        // When
        Optional<Cart> optionalCart = this.cartService.fetch(0L);

        // Then
        assertFalse(optionalCart.isPresent());
    }

    @Test
    public void fetchingCartThatDoesExistShouldReturnOptional() {
        // Given
        Cart cart = new Cart();
        when(cartRepository.findOne(anyLong())).thenReturn(cart);

        // When
        Optional<Cart> optionalCart = this.cartService.fetch(0L);

        // Then
        assertTrue(optionalCart.isPresent());
    }

    @Test
    public void addBookToCartWhereCartIsMissingShouldReturnEmptyOptional() {
        // Given
        when(cartRepository.findOne(anyLong())).thenReturn(null);
        when(bookRepository.findOne(anyLong())).thenReturn(new Book());


        // When
        Optional<Cart> optionalCart = this.cartService.addBookToCart(0L, 0L, 0L);

        // Then
        assertFalse(optionalCart.isPresent());
    }

    @Test
    public void addBookToCartWhereBookIsMissingShouldReturnEmptyOptional() {
        // Given
        when(cartRepository.findOne(anyLong())).thenReturn(new Cart());
        when(bookRepository.findOne(anyLong())).thenReturn(null);

        // When
        Optional<Cart> optionalCart = this.cartService.addBookToCart(0L, 0L, 0L);

        // Then
        assertFalse(optionalCart.isPresent());
    }

    @Test
    public void addBookToCartWhereItemQuantityIsZeroShouldRemoveItemFromCart() {
        // Given
        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .quantity(1L)
                .book(Book.builder()
                        .id(0L)
                        .build())
                .build());

        Cart cart = Cart.builder().items(items).build();
        when(cartRepository.findOne(anyLong())).thenReturn(cart);
        when(bookRepository.findOne(anyLong())).thenReturn(new Book());

        ArgumentCaptor<Cart> argumentCaptor = ArgumentCaptor.forClass(Cart.class);
        when(cartRepository.save(argumentCaptor.capture())).thenReturn(cart);

        // When
        Optional<Cart> optionalCart = this.cartService.addBookToCart(0L, 0L, 0L);

        // Then
        assertTrue(optionalCart.isPresent());
        assertEquals(0, argumentCaptor.getValue().getItems().size());
    }


    @Test
    public void addBookToCartWhereItAlreadyExistShouldChangeQuantity() {
        // Given
        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .quantity(2L)
                .book(Book.builder()
                        .id(0L)
                        .build())
                .build());

        Cart cart = Cart.builder().items(items).build();
        when(cartRepository.findOne(anyLong())).thenReturn(cart);
        when(bookRepository.findOne(anyLong())).thenReturn(new Book());

        ArgumentCaptor<Cart> argumentCaptor = ArgumentCaptor.forClass(Cart.class);
        when(cartRepository.save(argumentCaptor.capture())).thenReturn(cart);

        // When
        Optional<Cart> optionalCart = this.cartService.addBookToCart(0L, 0L, 1L);

        // Then
        assertTrue(optionalCart.isPresent());
        assertEquals(1, argumentCaptor.getValue().getItems().size());
    }

    @Test
    public void addBookToCartWhereDoesNotExistShouldAddItToCart() {
        // Given
        List<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .quantity(2L)
                .book(Book.builder()
                        .id(0L)
                        .build())
                .build());

        Cart cart = Cart.builder().items(items).build();
        when(cartRepository.findOne(anyLong())).thenReturn(cart);
        when(bookRepository.findOne(anyLong())).thenReturn(new Book());

        ArgumentCaptor<Cart> argumentCaptor = ArgumentCaptor.forClass(Cart.class);
        when(cartRepository.save(argumentCaptor.capture())).thenReturn(cart);

        // When
        Optional<Cart> optionalCart = this.cartService.addBookToCart(0L, 1L, 1L);

        // Then
        assertTrue(optionalCart.isPresent());
        assertEquals(2, argumentCaptor.getValue().getItems().size());
        assertEquals(2, cart.getItems().size());

    }
}