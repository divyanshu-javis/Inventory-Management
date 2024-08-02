package com.application.inventorymanagement.service.primaryService;

import com.application.inventorymanagement.dto.request.ReportingRequestDto;
import com.application.inventorymanagement.model.Product;
import com.application.inventorymanagement.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrimaryReportingService {

    private final ProductRepository productRepository;

    public PrimaryReportingService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> filterProductsPrimary(String category, List<String> suppliers){

        if(category == null){
            if((suppliers == null || suppliers.isEmpty())){
                return productRepository.findAll();
            }
            else {
                return productRepository.findBySupplierIn(suppliers);
            }
        }
        else {
            if((suppliers == null || suppliers.isEmpty())){
                return productRepository.findByCategory(category);
            }
            else{
                return productRepository.findBySupplierInAndCategory(suppliers, category);
            }
        }

    }


}
