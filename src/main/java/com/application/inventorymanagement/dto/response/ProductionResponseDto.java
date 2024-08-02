package com.application.inventorymanagement.dto.response;

import com.application.inventorymanagement.dto.InventoryDto;
import com.application.inventorymanagement.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ProductionResponseDto {

    private List<InventoryDto> productsCreated;
    private List<InventoryDto> productsUpdated;

    private String errorMessage;

    public ProductionResponseDto() {
        this.productsCreated = new ArrayList<>();
        this.productsUpdated = new ArrayList<>();

        this.errorMessage = "";
    }

}
