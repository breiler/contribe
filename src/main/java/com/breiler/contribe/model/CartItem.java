package com.breiler.contribe.model;

import lombok.Data;

@Data
public class CartItem {
    private String bookId;
    private long quantity;
}
