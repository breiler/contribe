package com.breiler.contribe.contract;

import lombok.Data;

@Data
public class CartItemDTO {
    private BookDTO book;
    private long quantity;
}
