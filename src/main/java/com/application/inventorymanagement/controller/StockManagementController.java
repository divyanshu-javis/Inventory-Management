package com.application.inventorymanagement.controller;

import com.application.inventorymanagement.dto.InventoryDto;
import com.application.inventorymanagement.repository.InventoryRepository;
import com.application.inventorymanagement.service.StockManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/inventory")
public class StockManagementController {

    private final StockManagementService stockManagementService;
    private final InventoryRepository inventoryRepository;

    public StockManagementController(StockManagementService stockManagementService, InventoryRepository inventoryRepository) {
        this.stockManagementService = stockManagementService;
        this.inventoryRepository = inventoryRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStock(@RequestBody InventoryDto inventoryDto) {
        try{
            InventoryDto response = stockManagementService.addStock(inventoryDto);

            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reduce")
    public ResponseEntity<?> reduceStock(@RequestBody InventoryDto inventoryDto) {
        try{
            InventoryDto response = stockManagementService.reduceStock(inventoryDto);
            return ResponseEntity.ok(response);
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getStocks() {
        try{
            Set<InventoryDto> inventoryReponse = stockManagementService.getStockLevels();
            return ResponseEntity.ok(inventoryReponse);
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
