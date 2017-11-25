package com.breiler.contribe.contract;

import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    private String id;
    private List<CartItemDTO> items;
}
