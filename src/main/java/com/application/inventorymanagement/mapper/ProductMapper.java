package com.application.inventorymanagement.mapper;

import com.application.inventorymanagement.dto.ProductDto;
import com.application.inventorymanagement.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductMapper {

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

    public ProductDto toProductDto(Product product, String message) {
        if(product == null) return null;

        ProductDto productDto = new ProductDto();

        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setCategory(product.getCategory());
        productDto.setSupplier(product.getSupplier());
        productDto.setMessage(message);

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
