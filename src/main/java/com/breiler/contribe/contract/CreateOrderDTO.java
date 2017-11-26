package com.breiler.contribe.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDTO {
    @ApiModelProperty(notes = "The items to order", required = true, readOnly = true)
    private List<CreateItemDTO> items;
}
