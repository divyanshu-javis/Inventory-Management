package com.application.inventorymanagement.repository;

import com.application.inventorymanagement.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p WHERE p.supplier IN :suppliers")
    List<Product> findBySupplierNames(@Param("suppliers") List<String> suppliers);

    List<Product> findBySupplierIn(List<String> suppliers);

    List<Product> findByCategory(String category);

    List<Product> findBySupplierInAndCategory(List<String> suppliers, String category);

}
