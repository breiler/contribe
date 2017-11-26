package com.breiler.contribe.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ItemDTO {

    @ApiModelProperty(notes = "The book")
    private BookDTO book;

    @ApiModelProperty(notes = "The number of items added", example = "1", required = true, readOnly = true)
    private long quantity;
}
