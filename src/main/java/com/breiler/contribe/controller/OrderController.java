package com.breiler.contribe.controller;

import com.breiler.contribe.contract.CreateOrderDTO;
import com.breiler.contribe.contract.OrderDTO;
import com.breiler.contribe.model.Book;
import com.breiler.contribe.model.Item;
import com.breiler.contribe.model.Order;
import com.breiler.contribe.service.BookService;
import com.breiler.contribe.service.OrderService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(basePath = "/api", tags = "Orders", description = "Resources for handling orders", produces = "application/json")
@RestController()
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@Log4j
public class OrderController {

    private final ModelMapper modelMapper;
    private final OrderService orderService;
    private BookService bookService;

    @Autowired
    public OrderController(OrderService orderService, BookService bookService) {
        this.bookService = bookService;
        this.modelMapper = new ModelMapper();
        this.orderService = orderService;
    }

    @RequestMapping(value = "/api/carts/{cartId}/order", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new order from a cart",
            notes = "Creates an order from an existing cart given its unique id. To be able to create the cart the number of items needs to exist in the stock. The number of items added in the order will be removed from the inventory stock. After a sucessful order the cart can be removed.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The order was created successfully"),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<OrderDTO> createOrderFromCart(
            @ApiParam(name = "cartId", value = "The unique id of the cart")
            @PathVariable(name = "cartId")
                    Long cartId
    ) {
        Optional<Order> order = orderService.createFromCart(cartId);
        if (!order.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(modelMapper.map(order.get(), OrderDTO.class));
    }

    @RequestMapping(value = "/api/order", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new order from a cart",
            notes = "Creates an order from an existing cart given its unique id. To be able to create the cart the number of items needs to exist in the stock. The number of items added in the order will be removed from the inventory stock. After a sucessful order the cart can be removed.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The order was created successfully"),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<OrderDTO> createOrder(
            @ApiParam(value = "The item to be updated in the cart")
            @RequestBody
                    CreateOrderDTO orderDTO
    ) {
        List<Item> items = orderDTO.getItems().stream()
                .map(item -> {
                    Book book = bookService.findById(item.getBookId());
                    return Item.builder()
                            .book(book)
                            .quantity(item.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());


        Optional<Order> order = orderService.createFromItems(items);
        if (!order.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(modelMapper.map(order.get(), OrderDTO.class));
    }


    @RequestMapping(value = "/api/orders", method = RequestMethod.GET)
    @ApiOperation(value = "Fetches all orders",
            notes = "Fetches all orders created in the system.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All orders in the system"),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<OrderDTO> fetchAll() {
        List<Order> order = orderService.fetchAll();
        Type listType = new TypeToken<List<OrderDTO>>() {
        }.getType();
        return ResponseEntity.ok().body(modelMapper.map(order, listType));
    }
}
