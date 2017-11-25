package com.breiler.contribe.controller;

import com.breiler.contribe.contract.CartDTO;
import com.breiler.contribe.contract.CreateCartDTO;
import com.breiler.contribe.model.Cart;
import com.breiler.contribe.service.CartService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(basePath = "/api", value = "Cart", description = "For handling carts", produces = "application/json")
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
            notes = "Creates a cart")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The cart was created successfully"),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<CartDTO> createCart(
            @ApiParam(name = "Cart", value = "The cart to be created")
            @RequestBody
                    CreateCartDTO cartDTO) {

        Cart cart = modelMapper.map(cartDTO, Cart.class);
        Cart createdCart = cartService.createCart(cart);
        return ResponseEntity.ok().body(modelMapper.map(createdCart, CartDTO.class));
    }


    @RequestMapping(value = "/api/carts/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Updates a cart",
            notes = "Updates a cart")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The cart was updated successfully"),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<CartDTO> updateCart(
            @ApiParam(name = "id", value = "The unique id of the cart")
            @PathVariable(name = "id")
                    String id,
            @ApiParam(name = "Cart", value = "The cart to be updated")
            @RequestBody
                    CreateCartDTO cartDTO) {

        Cart cart = modelMapper.map(cartDTO, Cart.class);
        cart.setId(id);

        if( !cartService.getCart(id).isPresent() ) {
            return ResponseEntity.notFound().build();
        }

        Cart updatedCart = cartService.updateCart(cart);
        return ResponseEntity.ok().body(modelMapper.map(updatedCart, CartDTO.class));
    }


    @RequestMapping(value = "/api/carts/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves a cart",
            notes = "Retrieves a cart given its unique id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The cart was retrieved successfully"),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<CartDTO> getCart(
            @ApiParam(name = "id", value = "The unique id of the cart")
            @PathVariable(name = "id")
                    String id) {
        Optional<Cart> cart = cartService.getCart(id);
        if( !cartService.getCart(id).isPresent() ) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(modelMapper.map(cart, CartDTO.class));
    }
}
