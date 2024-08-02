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
            List<Product> filteredOnStockThreshold = this.filterProductsOnStockThreshold(filteredProductsPrimary, stockThreshold);
            return secondaryFilter(filteredOnStockThreshold, minPrice, maxPrice);
        }
        else return secondaryFilter(filteredProductsPrimary, minPrice, maxPrice);
    }
    
    private ReportingResponseDto secondaryFilter(List<Product> primaryFilteredProducts, Double minPrice, Double maxPrice) {

        ReportingResponseDto reportingResponseDto = new ReportingResponseDto();
        
        if(minPrice != null && maxPrice != null){
            List<Product> filteredOnMinAndMaxPrice = this.filterProductsOnMinAndMaxPrice(primaryFilteredProducts, minPrice, maxPrice);
            reportingResponseDto.setProductList(
                    filteredOnMinAndMaxPrice.stream()
                            .map(productMapper::toProductDto)
                            .collect(Collectors.toList())
            );
            return reportingResponseDto;
        }
        else if(minPrice != null){
            List<Product> filteredOnMinPrice = this.filterProductsOnMinPrice(primaryFilteredProducts, minPrice);
            reportingResponseDto.setProductList(
                    filteredOnMinPrice.stream()
                            .map(productMapper::toProductDto)
                            .collect(Collectors.toList())
            );
            return reportingResponseDto;
        }
        else if(maxPrice != null){
            List<Product> filteredOnMaxPrice = this.filterProductsOnMaxPrice(primaryFilteredProducts, maxPrice);
            reportingResponseDto.setProductList(
                    filteredOnMaxPrice.stream()
                            .map(productMapper::toProductDto)
                            .collect(Collectors.toList())
            );
            return reportingResponseDto;
        }
        else {
            reportingResponseDto.setProductList(
                    primaryFilteredProducts.stream()
                            .map(productMapper::toProductDto)
                            .collect(Collectors.toList())
            );
            return reportingResponseDto;
        }
    }


    private List<Product> filterProductsOnStockThreshold(List<Product> filteredProductsPrimary, Integer stockThreshold) {

        List<Product> filteredOnStockThreshold = new ArrayList<>();

        for(Product product : filteredProductsPrimary){
            if(product.getInventory().getQuantity() >= stockThreshold){
                filteredOnStockThreshold.add(product);
            }
        }
        return filteredOnStockThreshold;
    }


    private List<Product> filterProductsOnMinAndMaxPrice(List<Product> filteredProducts, Double minPrice, Double maxPrice){
        List<Product> filteredOnMinAndMaxPrice = new ArrayList<>();
        for(Product product : filteredProducts){
            if(product.getPrice() >= minPrice && product.getPrice() <= maxPrice){
                filteredOnMinAndMaxPrice.add(product);
            }
        }
        return filteredOnMinAndMaxPrice;
    }


    private List<Product> filterProductsOnMinPrice(List<Product> filteredProducts, Double minPrice){
        List<Product> filteredOnMinPrice = new ArrayList<>();
        for(Product product : filteredProducts){
            if(product.getPrice() >= minPrice){
                filteredOnMinPrice.add(product);
            }
        }
        return filteredOnMinPrice;
    }


    private List<Product> filterProductsOnMaxPrice(List<Product> filteredProducts, Double maxPrice){
        List<Product> filteredOnMaxPrice = new ArrayList<>();
        for(Product product : filteredProducts){
            if(product.getPrice() <= maxPrice){
                filteredOnMaxPrice.add(product);
            }
        }
        return filteredOnMaxPrice;
    }


}
