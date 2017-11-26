package com.breiler.contribe.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CreateItemDTO {
    @ApiModelProperty(notes = "The unique id of the book to add", example = "1", required = true)
    private Long bookId;

    @ApiModelProperty(notes = "The number of items to add or update", example = "1", required = true)
    private Long quantity;
}
