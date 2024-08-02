package com.application.inventorymanagement.controller;

import com.application.inventorymanagement.dto.request.ReportingRequestDto;
import com.application.inventorymanagement.dto.response.ReportingResponseDto;
import com.application.inventorymanagement.service.ReportingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports/custom")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @PostMapping("")
    public ResponseEntity<ReportingResponseDto> customReport(@RequestBody ReportingRequestDto reportingRequestDto){
        System.out.println(reportingRequestDto);

        ReportingResponseDto reportingResponseDto = reportingService.filterProducts(reportingRequestDto);
        return ResponseEntity.ok(reportingResponseDto);
    }

}
