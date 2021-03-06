package com.breiler.contribe.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "items")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * We need an extra attribute because SQLLite doesn't allow us to store empty entities
     */
    @Column
    private String name = "";

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Item> items;

    public BigDecimal getSubTotal() {
        return items.stream()
                .map(Item::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long getNumberOfItems() {
        return items.stream()
                .mapToLong(Item::getQuantity)
                .sum();
    }
}
