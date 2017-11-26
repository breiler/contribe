package com.breiler.contribe.controller;

import com.breiler.contribe.contract.CartDTO;
import com.breiler.contribe.contract.CreateItemDTO;
import com.breiler.contribe.model.Cart;
import com.breiler.contribe.service.CartService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@Api(basePath = "/api", tags = "Carts", description = "Resources for handling carts", produces = "application/json")
@RestController()
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@Log4j
public class CartController {

    private final ModelMapper modelMapper;
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.modelMapper = new ModelMapper();
        this.cartService = cartService;
    }

    @RequestMapping(value = "/api/carts", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new cart",
            notes = "Creates a new empty cart")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The cart was created successfully"),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<CartDTO> create() {
        Cart createdCart = cartService.create();
        CartDTO result = modelMapper.map(createdCart, CartDTO.class);
        return ResponseEntity.ok().body(result);
    }

    @RequestMapping(value = "/api/carts", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves all carts",
            notes = "Retrieves all carts and its content.",
            response = CartDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The carts was retrieved successfully"),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<List<CartDTO>> fetchAll() {
        List<Cart> carts = cartService.fetchAll();
        Type listType = new TypeToken<List<CartDTO>>() {}.getType();
        List<CartDTO> results = modelMapper.map(carts, listType);
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }

    @RequestMapping(value = "/api/carts/{cartId}/items", method = RequestMethod.POST)
    @ApiOperation(value = "Adds or updates a cart with an item",
            notes = "Adds or updates a cart with an item and the quantity.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The cart was updated successfully"),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<CartDTO> update(
            @ApiParam(name = "cartId", value = "The unique bookId of the cart", example = "1")
            @PathVariable(name = "cartId")
                    Long id,
            @ApiParam(name = "item", value = "The item to be updated in the cart")
            @RequestBody
                    CreateItemDTO itemDTO) {
        Cart cart = cartService.addBookToCart(id, itemDTO.getBookId(), itemDTO.getQuantity());
        CartDTO result = modelMapper.map(cart, CartDTO.class);
        return ResponseEntity.ok().body(result);
    }


    @RequestMapping(value = "/api/carts/{cartId}", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves a cart",
            notes = "Retrieves a cart and its content given its unique id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The cart was retrieved successfully"),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<CartDTO> fetch(
            @ApiParam(name = "cartId", value = "The unique id of the cart")
            @PathVariable(name = "cartId")
                    Long id) {
        Cart cart = cartService.fetch(id);
        CartDTO result = modelMapper.map(cart, CartDTO.class);
        return ResponseEntity.ok().body(result);
    }

    @RequestMapping(value = "/api/carts/{cartId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Deletes a cart",
            notes = "Deletes a cart given its unique cartId",
            response = Void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "The cart was deleted successfully", response = Void.class),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity delete(
            @ApiParam(name = "cartId", value = "The unique id of the cart")
            @PathVariable(name = "cartId")
                    Long id) {
        cartService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
