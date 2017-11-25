package com.breiler.contribe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String title;
    private String author;
    private BigDecimal price;
}