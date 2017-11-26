package com.breiler.contribe.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StockDTO {

    @ApiModelProperty(notes = "The book", required = true, readOnly = true)
    BookDTO book;

    @ApiModelProperty(notes = "The quantity of the book in the inventory stock", required = true, readOnly = true, example = "2")
    Long quantity;
}
