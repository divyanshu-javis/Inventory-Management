package com.application.inventorymanagement.repository;

import com.application.inventorymanagement.dto.InventoryDto;
import com.application.inventorymanagement.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
//    @Query("select new com.application.inventorymanagement.dto.InventoryDto(i.productId, i.quantity) from Inventory i where i.productId = :productId")
//    Optional<InventoryDto> findInventoryById(Long productId);
//
//    @Query("select new com.application.inventorymanagement.dto.InventoryDto(i.productId, i.quantity) from Inventory i")
//    Set<InventoryDto> findAllStocks();
}
