package com.breiler.contribe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private String title;
    private String author;
    private BigDecimal price;

    public String getId() {
        String id = StringUtils.defaultString(title);
        id += StringUtils.defaultString(author);
        id += StringUtils.defaultString(author);
        id += price != null ? price.toPlainString() : "";
        return Base64.encodeBase64String(id.getBytes());
    }
}