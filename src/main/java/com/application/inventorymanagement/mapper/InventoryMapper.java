package com.application.inventorymanagement.mapper;

import com.application.inventorymanagement.dto.InventoryDto;
import com.application.inventorymanagement.dto.request.ConsumptionRequestDto;
import com.application.inventorymanagement.model.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InventoryMapper {

    public InventoryDto toInventoryDto(Inventory inventory) {
        if(inventory == null) return null;

        InventoryDto inventoryDto = new InventoryDto();

        inventoryDto.setName(inventory.getProduct().getName());
        inventoryDto.setQuantity(inventory.getQuantity());

        return inventoryDto;
    }

    public InventoryDto toInventoryDto(ConsumptionRequestDto consumptionRequestDto) {
        InventoryDto inventoryDto = new InventoryDto();

        inventoryDto.setName(consumptionRequestDto.getProductName());
        inventoryDto.setQuantity(consumptionRequestDto.getQuantity());

        return inventoryDto;
    }

}
