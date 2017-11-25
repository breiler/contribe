package com.breiler.contribe.controller;

import com.breiler.contribe.contract.CartDTO;
import com.breiler.contribe.contract.CreateCartDTO;
import com.breiler.contribe.model.Cart;
import com.breiler.contribe.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @ApiResponse(code = 200, message = "The status was retreived successfully"),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<CartDTO> createCart(
            @RequestBody
                    CreateCartDTO cartDTO) {

        Cart cart = modelMapper.map(cartDTO, Cart.class);
        Cart createdCart = cartService.createCart(cart);
        return ResponseEntity.ok().body(modelMapper.map(createdCart, CartDTO.class));
    }
}
