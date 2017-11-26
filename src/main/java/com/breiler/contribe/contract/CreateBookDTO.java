package com.breiler.contribe.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDTO {

    @ApiModelProperty(notes = "The title of the book", example = "Do Androids Dream Of Electric Sheep?", required = true)
    private String title;

    @ApiModelProperty(notes = "The author's name", example = "Philip K Dick", required = true)
    private String author;

    @ApiModelProperty(notes = "The unit price of the book", example = "19.99", required = true)
    private BigDecimal price;
}
