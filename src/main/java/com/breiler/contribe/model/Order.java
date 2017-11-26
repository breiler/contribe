package com.breiler.contribe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "ORD") // Order is a reserved word in SQL
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
