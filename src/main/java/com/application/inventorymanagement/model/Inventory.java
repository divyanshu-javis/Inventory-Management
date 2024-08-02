package com.application.inventorymanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    private Long productId;

    private Integer quantity;

    @OneToOne
    @MapsId
    @JoinColumn(name = "productId", nullable = false)
    private Product product;
}
