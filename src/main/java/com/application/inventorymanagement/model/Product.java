package com.application.inventorymanagement.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String supplier;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Inventory inventory;


}
