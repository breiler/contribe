package com.breiler.contribe.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CreateOrderFromCartDTO {

    @ApiModelProperty(notes = "The unique id of the cart to create an order for", required = true, example = "1", readOnly = true)
    private Long cartId;
}
