package com.breiler.contribe.contract;

import lombok.Data;

@Data
public class CartItemDTO {
    private String bookId;
    private long quantity;
}
