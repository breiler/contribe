package com.breiler.contribe.model;

import lombok.Data;

import java.util.List;

@Data
public class Cart {
    private String id;
    private List<CartItem> items;
}
