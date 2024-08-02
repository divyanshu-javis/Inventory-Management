package com.application.inventorymanagement.controller;

import com.application.inventorymanagement.dto.response.ConsumptionResponseDto;
import com.application.inventorymanagement.dto.response.ProductionResponseDto;
import com.application.inventorymanagement.service.DataProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/inventory/import")
public class DataProcessingController {

    private final DataProcessingService dataProcessingService;

    public DataProcessingController(DataProcessingService dataProcessingService) {
        this.dataProcessingService = dataProcessingService;
    }

    @PostMapping("/consumption")
    public ResponseEntity<ConsumptionResponseDto> consumption(@RequestParam("consumptionFile") MultipartFile consumptionFile) {

        System.out.println(consumptionFile.getOriginalFilename());
        ConsumptionResponseDto consumptionResponse = dataProcessingService.processConsumptionFile(consumptionFile);
        return ResponseEntity.ok(consumptionResponse);


    }

    @PostMapping("/production")
    public ResponseEntity<ProductionResponseDto> production(@RequestParam("productionFile") MultipartFile productionFile) {

        System.out.println(productionFile.getOriginalFilename());

        ProductionResponseDto productionResponse = dataProcessingService.processProductionFile(productionFile);
        return ResponseEntity.ok(productionResponse);

    }

}
