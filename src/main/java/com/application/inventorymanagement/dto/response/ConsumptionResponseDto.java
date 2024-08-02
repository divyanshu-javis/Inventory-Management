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
public class ConsumptionResponseDto {

    private List<String> productsNotAvailable;
    private List<String> notEnoughProducts;
    private List<InventoryDto> consumptionList;

    private String errorMessage;

    public ConsumptionResponseDto() {
        this.productsNotAvailable = new ArrayList<>();
        this.notEnoughProducts = new ArrayList<>();
        this.consumptionList = new ArrayList<>();
    }

}
