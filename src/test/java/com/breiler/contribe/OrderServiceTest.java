package com.breiler.contribe;

import com.breiler.contribe.model.*;
import com.breiler.contribe.repository.CartRepository;
import com.breiler.contribe.repository.OrderRepository;
import com.breiler.contribe.repository.StockRepository;
import com.breiler.contribe.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private CartRepository cartRepository;

    private OrderService orderService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.orderService = new OrderService(this.cartRepository, this.orderRepository, this.stockRepository);
    }

    @Test(expected = HttpServerErrorException.class)
    public void createFromCartWhereCartDoesNotExist() {

        //Given
        when(this.cartRepository.findOne(anyLong())).thenReturn(null);

        // When
        this.orderService.createFromCart(0L);
    }

    @Test
    public void createFromCartWithOneItem() {

        //Given
        Stock stock = Stock.builder().quantity(1).build();
        Book book = Book.builder().stock(stock).build();
        Item item = Item.builder().book(book).quantity(1L).build();
        Cart cart = Cart.builder()
                .items(Collections.singletonList(item))
                .build();

        when(this.cartRepository.findOne(anyLong())).thenReturn(cart);

        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        when(this.orderRepository.save(orderArgumentCaptor.capture())).thenReturn(new Order());

        ArgumentCaptor<Stock> stockArgumentCaptor = ArgumentCaptor.forClass(Stock.class);
        when(this.stockRepository.save(stockArgumentCaptor.capture())).thenReturn(new Stock());

        // When
        this.orderService.createFromCart(0L);

        // Then
        assertEquals(1, orderArgumentCaptor.getValue().getItems().size());
        assertEquals(0, stockArgumentCaptor.getValue().getQuantity());
    }

    @Test
    public void createFromCartWithOneItemAndStillItemsInStock() {

        //Given
        Stock stock = Stock.builder().quantity(2).build();
        Book book = Book.builder().stock(stock).build();
        Item item = Item.builder().book(book).quantity(1L).build();
        Cart cart = Cart.builder()
                .items(Collections.singletonList(item))
                .build();

        when(this.cartRepository.findOne(anyLong())).thenReturn(cart);

        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        when(this.orderRepository.save(orderArgumentCaptor.capture())).thenReturn(new Order());

        ArgumentCaptor<Stock> stockArgumentCaptor = ArgumentCaptor.forClass(Stock.class);
        when(this.stockRepository.save(stockArgumentCaptor.capture())).thenReturn(new Stock());

        // When
        this.orderService.createFromCart(0L);

        // Then
        assertEquals(1, orderArgumentCaptor.getValue().getItems().size());
        assertEquals(1, stockArgumentCaptor.getValue().getQuantity());
    }
}
