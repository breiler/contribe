package com.breiler.contribe.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    @ApiModelProperty(notes = "The unique id of the order", example = "1", required = true, readOnly = true)
    private Long id;

    @ApiModelProperty(notes = "The items in the order", required = true, readOnly = true)
    private List<ItemDTO> items;

    @ApiModelProperty(notes = "The subtotal of all items in the order", required = true, example = "119.99", readOnly = true)
    private BigDecimal subtotal;

    @ApiModelProperty(notes = "The total number of items in the order", required = true, example = "1", readOnly = true)
    private long numberOfItems;
}
