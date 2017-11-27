package com.breiler.contribe.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Book book;

    @Column
    private Long quantity;

    public BigDecimal getTotal() {
        return book.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
