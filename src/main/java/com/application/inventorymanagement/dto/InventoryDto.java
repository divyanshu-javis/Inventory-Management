package com.application.inventorymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {
//    private Long productId;
    private String name;
    private Integer quantity;

}
