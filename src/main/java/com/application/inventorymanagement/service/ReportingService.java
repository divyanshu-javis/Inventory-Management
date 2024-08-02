package com.application.inventorymanagement.service;

import com.application.inventorymanagement.dto.request.ReportingRequestDto;
import com.application.inventorymanagement.dto.response.ReportingResponseDto;
import com.application.inventorymanagement.mapper.ProductMapper;
import com.application.inventorymanagement.model.Product;
import com.application.inventorymanagement.service.primaryService.PrimaryReportingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportingService {

    private final PrimaryReportingService primaryReportingService;
    private final ProductMapper productMapper = new ProductMapper();

    public ReportingService(PrimaryReportingService primaryReportingService) {
        this.primaryReportingService = primaryReportingService;
    }

    public ReportingResponseDto filterProducts(ReportingRequestDto reportingRequestDto) {

        String category = reportingRequestDto.getCategory();
        List<String> suppliers = reportingRequestDto.getSuppliers();

        List<Product> filteredProductsPrimary = primaryReportingService.filterProductsPrimary(category, suppliers);

        Double minPrice = reportingRequestDto.getMinPrice();
        Double maxPrice = reportingRequestDto.getMaxPrice();
        Integer stockThreshold = reportingRequestDto.getStockThreshold();

        if(stockThreshold != null){
            List<Product> filteredOnStockThreshold = filteredProductsPrimary.stream()
                    .filter(product -> product.getInventory().getQuantity() >= stockThreshold)
                    .toList();
            return secondaryFilter(filteredOnStockThreshold, minPrice, maxPrice);
        }
        else return secondaryFilter(filteredProductsPrimary, minPrice, maxPrice);
    }


    private ReportingResponseDto secondaryFilter(List<Product> filteredProducts, Double minPrice, Double maxPrice) {

        ReportingResponseDto reportingResponseDto = new ReportingResponseDto();

        if(minPrice != null && maxPrice != null){
            reportingResponseDto.setProductList(
                    filteredProducts.stream()
                            .filter(product -> product.getPrice() >= minPrice
                                    && product.getPrice() <= maxPrice)
                            .map(productMapper::toProductDto)
                            .collect(Collectors.toList())
            );
            return reportingResponseDto;
        }
        else if(minPrice != null){
            reportingResponseDto.setProductList(
                    filteredProducts.stream()
                            .filter(product -> product.getPrice() >= minPrice)
                            .map(productMapper::toProductDto)
                            .collect(Collectors.toList())
            );
            return reportingResponseDto;
        }
        else if(maxPrice != null){
            reportingResponseDto.setProductList(
                    filteredProducts.stream()
                            .filter(product -> product.getPrice() <= maxPrice)
                            .map(productMapper::toProductDto)
                            .collect(Collectors.toList())
            );
            return reportingResponseDto;
        }
        else {
            reportingResponseDto.setProductList(
                    filteredProducts.stream()
                            .map(productMapper::toProductDto)
                            .collect(Collectors.toList())
            );
            return reportingResponseDto;
        }
    }

}
