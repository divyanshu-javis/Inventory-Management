package com.application.inventorymanagement.service;

import com.application.inventorymanagement.dto.ProductDto;
import com.application.inventorymanagement.exception.InvalidInputException;
import com.application.inventorymanagement.mapper.ProductMapper;
import com.application.inventorymanagement.model.Product;
import com.application.inventorymanagement.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductManagementService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper = new ProductMapper();

    public ProductManagementService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = products.stream()
                .map(productMapper::toProductDto)
                .collect(Collectors.toList());

        return productDtos;
    }

    public ProductDto addProduct(ProductDto productDto){
        if(productDto == null) throw new InvalidInputException("Product cannot be null");

        if(productDto.getName() == null){
            throw new InvalidInputException("Product name cannot be null");
        }
        else if (productDto.getName().isEmpty()) {
            throw new InvalidInputException("Product name cannot be empty");
        }

        Optional<Product> presentProduct = productRepository.findByName(productDto.getName());
        if(presentProduct.isPresent()){
            throw new InvalidInputException("Product name already exists");
        }

        Product product = productMapper.toProductEntity(productDto);

        productRepository.save(product);
        return productMapper.toProductDto(product);

    }

    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        if(productId == null) throw new InvalidInputException("Product id cannot be null");

        Optional<Product> product = productRepository.findById(productId);
        if(product.isPresent()){
            Product updatedProduct = productMapper.toProductEntity(productDto);
            updatedProduct.setProductId(productId);
            productRepository.save(updatedProduct);
            return productMapper.toProductDto(updatedProduct);
        }
        else{
            if(productRepository.findByName(productDto.getName()).isPresent()){
                throw new InvalidInputException("Product name already exists");
            }

            Product newProduct = productMapper.toProductEntity(productDto);
            newProduct.setProductId(productId);
            productRepository.save(newProduct);
            String message = "product did not exist. New product created.";

            return productMapper.toProductDto(newProduct,message);
        }

    }

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

}
