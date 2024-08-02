package com.application.inventorymanagement.model;

import jakarta.persistence.*;

@Entity
public class Stock {

    @Id
    private Long productId;

    private Integer quantity;

    @OneToOne
    @MapsId
    @JoinColumn(name = "productId")
    private Product product;
}
