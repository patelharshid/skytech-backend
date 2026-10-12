package com.skytech.repository;

import com.skytech.entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {

    @Query("SELECT i FROM ProductInventory i WHERE i.quantity <= i.lowStockThreshold")
    List<ProductInventory> findLowStockItems();
}
