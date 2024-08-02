package com.application.inventorymanagement.service.impl;

import com.application.inventorymanagement.dto.ProductDto;
import com.application.inventorymanagement.exception.InvalidInputException;
import com.application.inventorymanagement.model.Product;
import com.application.inventorymanagement.repository.ProductRepository;
import com.application.inventorymanagement.service.ProductManagementService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductManagementServiceImpl implements ProductManagementService {

    private final ProductRepository productRepository;

    public ProductManagementServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = products.stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());

        return productDtos;
    }

    @Override
    public ProductDto addProduct(ProductDto productDto){
        if(productDto == null) throw new InvalidInputException("Product cannot be null");

        Product product = this.toProductEntity(productDto);

        productRepository.save(product);
        return toProductDto(product);

    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        if(productId == null) throw new InvalidInputException("Product id cannot be null");

        Optional<Product> product = productRepository.findById(productId);
        if(product.isPresent()){
            Product updatedProduct = toProductEntity(productDto);
            updatedProduct.setProductId(productId);
            productRepository.save(updatedProduct);
            return toProductDto(updatedProduct);
        }
        else{
            if(productRepository.findByName(productDto.getName()).isPresent()){
                throw new InvalidInputException("Product name already exists");
            }

            Product newProduct = toProductEntity(productDto);
            newProduct.setProductId(productId);
            productRepository.save(newProduct);

            return toProductDto(newProduct);
        }


    }

    @Override
    public void deleteProduct(Long productId) {
        if(productId == null) throw new InvalidInputException("Product id cannot be null");

        Product product = productRepository.findById(productId).orElse(null);
        if(product != null){
            productRepository.delete(product);
        }
        else {
            throw new InvalidInputException("Product not found");
        }
    }

    public ProductDto toProductDto(Product product) {
        if(product == null) return null;

        ProductDto productDto = new ProductDto();

        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setCategory(product.getCategory());
        productDto.setSupplier(product.getSupplier());

        return productDto;
    }

    public Product toProductEntity(ProductDto productDto) {
        if(productDto == null) return null;

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setSupplier(productDto.getSupplier());

        return product;
    }

}
