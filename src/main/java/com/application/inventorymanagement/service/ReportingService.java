package com.application.inventorymanagement.service;

import com.application.inventorymanagement.dto.ProductDto;
import com.application.inventorymanagement.dto.request.ReportingRequestDto;
import com.application.inventorymanagement.mapper.ProductMapper;
import com.application.inventorymanagement.model.Product;
import com.application.inventorymanagement.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportingService {

    private final ProductMapper productMapper = new ProductMapper();
    private final ProductRepository productRepository;

    public ReportingService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> filterProducts(ReportingRequestDto reportingRequestDto) {

        String category = reportingRequestDto.getCategory();
        List<String> suppliers = reportingRequestDto.getSuppliers();
        Double minPrice = reportingRequestDto.getMinPrice();
        Double maxPrice = reportingRequestDto.getMaxPrice();
        Integer stockThreshold = reportingRequestDto.getStockThreshold();

        List<Product> filteredProductsPrimary;

        if(category == null){
            if((suppliers == null || suppliers.isEmpty()))
                filteredProductsPrimary =  productRepository.findAll();

            else filteredProductsPrimary =  productRepository.findBySupplierIn(suppliers);
        }
        else {
            if((suppliers == null || suppliers.isEmpty()))
                filteredProductsPrimary =  productRepository.findByCategory(category);

            else filteredProductsPrimary = productRepository.findBySupplierInAndCategory(suppliers, category);
        }

        if(stockThreshold != null){
            List<Product> filteredOnStockThreshold = filteredProductsPrimary.stream()
                    .filter(product -> product.getInventory().getQuantity() >= stockThreshold)
                    .toList();
            return secondaryFilter(filteredOnStockThreshold, minPrice, maxPrice);
        }
        else return secondaryFilter(filteredProductsPrimary, minPrice, maxPrice);

    }


    private List<ProductDto> secondaryFilter(List<Product> filteredProducts, Double minPrice, Double maxPrice) {

        List<ProductDto> reportingResponseList;

        if(minPrice != null && maxPrice != null){

            reportingResponseList = filteredProducts.stream()
                    .filter(product -> product.getPrice() >= minPrice
                            && product.getPrice() <= maxPrice)
                    .map(productMapper::toProductDto)
                    .collect(Collectors.toList());
            return reportingResponseList;
        }
        else if(minPrice != null){
            reportingResponseList = filteredProducts.stream()
                    .filter(product -> product.getPrice() >= minPrice)
                    .map(productMapper::toProductDto)
                    .collect(Collectors.toList());
            return reportingResponseList;
        }
        else if(maxPrice != null){
            reportingResponseList = filteredProducts.stream()
                    .filter(product -> product.getPrice() <= maxPrice)
                    .map(productMapper::toProductDto)
                    .collect(Collectors.toList());
            return reportingResponseList;
        }
        else {
            reportingResponseList = filteredProducts.stream()
                    .map(productMapper::toProductDto)
                    .collect(Collectors.toList());
            return reportingResponseList;
        }
    }

}
