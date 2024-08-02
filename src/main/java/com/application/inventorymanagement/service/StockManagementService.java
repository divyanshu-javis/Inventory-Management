package com.application.inventorymanagement.service;

import com.application.inventorymanagement.exception.ResourceNotFoundException;
import com.application.inventorymanagement.mapper.InventoryMapper;
import com.application.inventorymanagement.model.Inventory;
import com.application.inventorymanagement.model.Product;
import com.application.inventorymanagement.dto.InventoryDto;
import com.application.inventorymanagement.exception.InvalidInputException;
import com.application.inventorymanagement.repository.InventoryRepository;
import com.application.inventorymanagement.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StockManagementService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final InventoryMapper inventoryMapper = new InventoryMapper();

    public StockManagementService(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }


    public InventoryDto addStock(InventoryDto inventoryDto) {
        if(inventoryDto == null) throw new InvalidInputException("Invalid input");

//        Long productId = inventoryDto.getProductId();
        String productName = inventoryDto.getName();

//        Optional<Product> productOptional = productRepository.findById(productId);
        Optional<Product> productOptional = productRepository.findByName(productName);

        if(productOptional.isPresent()) {

            Long productId = productOptional.get().getProductId();
            Optional<Inventory> inventoryData = inventoryRepository.findById(productId);

            if(inventoryData.isPresent()) {

                int quantity = inventoryData.get().getQuantity();
                int revisedQuantity = quantity + inventoryDto.getQuantity();

                inventoryData.get().setQuantity(revisedQuantity);

                inventoryRepository.save(inventoryData.get());

                return inventoryMapper.toInventoryDto(inventoryData.get());
            }
            else {
                int quantity = inventoryDto.getQuantity();

                Inventory inventory = new Inventory();
                System.out.println(inventoryDto.getName());

                inventory.setQuantity(quantity);
                inventory.setProduct(productOptional.get());

                System.out.println(inventory);

                inventoryRepository.save(inventory);
                return inventoryMapper.toInventoryDto(inventory);
            }
        }
        //if the product does not exist in the product table
        else {
            throw new ResourceNotFoundException("Product not found");
        }

    }

    public InventoryDto reduceStock(InventoryDto inventoryDto) {
        if (inventoryDto == null) throw new InvalidInputException("Invalid input");

        String productName = inventoryDto.getName();

        Optional<Product> productOptional = productRepository.findByName(productName);

        if(productOptional.isPresent()) {

            Long productId = productOptional.get().getProductId();
            Optional<Inventory> inventoryData = inventoryRepository.findById(productId);

            if(inventoryData.isPresent()) {
                int reduceQuantity = inventoryData.get().getQuantity();
                if(reduceQuantity > inventoryDto.getQuantity()) {
                    int reducedQuantity = reduceQuantity - inventoryDto.getQuantity();

                    inventoryData.get().setQuantity(reducedQuantity);

                    inventoryRepository.save(inventoryData.get());

                    return inventoryMapper.toInventoryDto(inventoryData.get());
                }
                // product stock < reduceQuantity
                else {
                    throw new InvalidInputException("Not enough stock");
                }
            }
            // product stock == 0
            else {
                throw new InvalidInputException("Not enough stock");
            }
        }
        // if the product does not exist in the product table
        else {
            throw new ResourceNotFoundException("Product not found");
        }
    }

    public Set<InventoryDto> getStockLevels() {
        List<Inventory> inventorySet = inventoryRepository.findAll();

        Set<InventoryDto> inventoryDtoSet = new HashSet<>();
        for (Inventory inventory : inventorySet) {
            inventoryDtoSet.add(inventoryMapper.toInventoryDto(inventory));
        }

        return inventoryDtoSet;
    }


}
