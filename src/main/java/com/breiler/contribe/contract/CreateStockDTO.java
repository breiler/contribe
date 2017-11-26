package com.breiler.contribe.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CreateStockDTO {

    @ApiModelProperty(notes = "The total number of items in the inventory stock", required = true, example = "1", readOnly = true)
    private Long quantity;
}
