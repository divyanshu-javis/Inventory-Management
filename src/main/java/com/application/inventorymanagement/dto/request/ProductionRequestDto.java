package com.application.inventorymanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductionRequestDto {
    private String productName;
    private String description;
    private String category;
    private Double price;
    private String supplier;
    private Integer quantity;
}
