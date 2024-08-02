package com.application.inventorymanagement.repository;

import com.application.inventorymanagement.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Inventory, Long> {
}
