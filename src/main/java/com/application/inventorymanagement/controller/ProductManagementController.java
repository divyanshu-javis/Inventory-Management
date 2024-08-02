package com.application.inventorymanagement.controller;

import com.application.inventorymanagement.dto.ProductDto;
import com.application.inventorymanagement.service.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductManagementController {

    private final ProductManagementService productManagementService;

    @Autowired
    public ProductManagementController(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    @GetMapping("")
    public ResponseEntity<List<ProductDto>> getProducts() {
        try{
            List<ProductDto> products =  productManagementService.getAllProducts();
            return ResponseEntity.ok(products);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        try{
            ProductDto productResponse = productManagementService.addProduct(productDto);
            return ResponseEntity.ok(productResponse);
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,@RequestBody ProductDto productDto) {
        try{
            ProductDto productResponse = productManagementService.updateProduct(id,productDto);
            return ResponseEntity.ok(productResponse);
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
//        productManagementService.deleteProduct(id);
//        return ResponseEntity.ok("Product deleted successfully");

        try{
            productManagementService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully");
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
