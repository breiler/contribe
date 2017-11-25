package com.breiler.contribe.contract;

import lombok.Data;

import java.util.List;

@Data
public class CreateCartDTO {
    private List<CartItemDTO> items;
}
